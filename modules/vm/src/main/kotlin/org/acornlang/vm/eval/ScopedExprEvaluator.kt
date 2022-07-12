package org.acornlang.vm.eval

import org.acornlang.hir.*
import org.acornlang.vm.*

class ImplicitReturn(val value: Value) : Exception()

class ScopedExprEvaluator(val module: Module) : HirVisitor<Scope, Value> {
    override val default: Value
        get() = throw UnsupportedOperationException()

    private inline fun <reified V : Value, reified T : Type> visitExpectType(node: HirNode, p: Scope): Pair<V, T> {
        val value = visit(node, p).resolve()
        if (value !is V)
            throw RuntimeException("Expected ${T::class.simpleName} but got ${value::class.simpleName}")
        if (value.type !is T)
            throw RuntimeException("Expected type ${T::class.simpleName} but got ${value.type::class.simpleName}")
        return Pair(value, value.type as T)
    }


    // Expressions

    override fun visitIntLiteral(intLiteral: HirIntLiteral, p: Scope): Value {
        //todo support other int types
        return IntValue(IntType(32), intLiteral.value)
    }
    override fun visitBoolLiteral(boolLiteral: HirBoolLiteral, p: Scope) =
        BoolValue(BoolType(), boolLiteral.value)
    override fun visitStringLiteral(stringLiteral: HirStringLiteral, p: Scope) =
        StrValue(StrType(), stringLiteral.value)
    override fun visitVarRef(varRef: HirVarRef, p: Scope): Value =
        p.get(varRef.name) ?: throw RuntimeException("Undefined variable ${varRef.name}")

    private fun visitArithmetic(binary: HirBinary, p: Scope): Value {
        val (lhs, lhsType) = visitExpectType<IntValue, IntType>(binary.lhs, p)
        val (rhs, rhsType) = visitExpectType<IntValue, IntType>(binary.rhs, p)
        val targetBits = maxOf(lhsType.bits, rhsType.bits) // Use bigger type between the two

        val intValue = when (binary.operator) {
            HirBinary.Operator.ADD -> lhs.value + rhs.value
            HirBinary.Operator.SUB -> lhs.value - rhs.value
            HirBinary.Operator.MUL -> lhs.value * rhs.value
            HirBinary.Operator.DIV -> lhs.value / rhs.value
            else -> throw RuntimeException("Unreachable")
        }

        return IntValue(IntType(targetBits), intValue)
    }
    private fun visitValueComparison(binary: HirBinary, p: Scope): Value {
        val lhs = visit(binary.lhs, p)
        val rhs = visit(binary.rhs, p)

        val resultValue = when (binary.operator) {
            HirBinary.Operator.EQ -> lhs == rhs
            HirBinary.Operator.NE -> lhs != rhs
            else -> throw RuntimeException("Unreachable")
        }

        return BoolValue(BoolType(), resultValue)
    }
    private fun visitNumberComparison(binary: HirBinary, p: Scope): Value {
        val (lhs, _) = visitExpectType<IntValue, IntType>(binary.lhs, p)
        val (rhs, _) = visitExpectType<IntValue, IntType>(binary.rhs, p)

        val boolValue = when (binary.operator) {
            HirBinary.Operator.GT -> lhs.value > rhs.value
            HirBinary.Operator.GE -> lhs.value >= rhs.value
            HirBinary.Operator.LT -> lhs.value < rhs.value
            HirBinary.Operator.LE -> lhs.value <= rhs.value
            else -> throw RuntimeException("Unreachable")
        }

        return BoolValue(BoolType(), boolValue)
    }
    private fun visitLogical(binary: HirBinary, p: Scope): Value {
        val (lhs, _) = visitExpectType<BoolValue, BoolType>(binary.lhs, p)
        val (rhs, _) = visitExpectType<BoolValue, BoolType>(binary.rhs, p)

        val boolValue = when (binary.operator) {
            HirBinary.Operator.AND -> lhs.value && rhs.value
            HirBinary.Operator.OR -> lhs.value || rhs.value
            else -> throw RuntimeException("Unreachable")
        }

        return BoolValue(BoolType(), boolValue)
    }
    override fun visitBinary(binary: HirBinary, p: Scope): Value {
        return when (binary.operator) {
            HirBinary.Operator.ADD, HirBinary.Operator.SUB,
            HirBinary.Operator.MUL, HirBinary.Operator.DIV -> visitArithmetic(binary, p)
            HirBinary.Operator.EQ, HirBinary.Operator.NE -> visitValueComparison(binary, p)
            HirBinary.Operator.LT, HirBinary.Operator.LE,
            HirBinary.Operator.GT, HirBinary.Operator.GE -> visitNumberComparison(binary, p)
            HirBinary.Operator.AND, HirBinary.Operator.OR -> visitLogical(binary, p)
        }
    }
    override fun visitUnary(unary: HirUnary, p: Scope): Value {
        return when (unary.operator) {
            HirUnary.Operator.NEG -> {
                val (value, valueType) = visitExpectType<IntValue, IntType>(unary.operand, p)
                IntValue(IntType(valueType.bits), -value.value) //todo does bad things for unsigned
            }
            HirUnary.Operator.NOT -> {
                val (value, _) = visitExpectType<BoolValue, BoolType>(unary.operand, p)
                BoolValue(BoolType(), !value.value)
            }
            else -> throw RuntimeException("Unreachable")
        }
    }

    override fun visitAssign(assign: HirAssign, p: Scope): Value {
        val target = visit(assign.target, p)

        // Ensure target is mutable
        if (!target.isMut)
            throw RuntimeException("Cannot assign to immutable value")

        // Ensure the new value can be coerced to the target type.
        val newValue = visit(assign.value, p)
        //todo

        // Perform mutation
        target.assign(newValue)
        return Value.empty
    }
    override fun visitMemberAccess(memberAccess: HirMemberAccess, p: Scope): Value {
        val target = visit(memberAccess.target, p)
        if (target !is ContainerValue)
            throw RuntimeException("TypeError: ${target.type} is not a container")
        return OwnedValue(target, target.get(memberAccess.value))
    }

    override fun visitBlock(block: HirBlock, p: Scope): Value {
        val scope = ScopeImpl(p)
        for (stmt in block.stmts) {
            stmt.accept(this, scope)
        }

        return TupleValue(TupleType(listOf()), mutableListOf())
    }
    override fun visitImplicitReturn(implicitReturn: HirImplicitReturn, p: Scope): Value {
        val value = visit(implicitReturn.expr, p)
        throw ImplicitReturn(value)
    }

    override fun visitConstruct(construct: HirConstruct, p: Scope): Value {
        val target = visit(construct.target, p)
        if (target !is TypeValue)
            throw RuntimeException("TypeError: expected type, got ${target.type}")
        val type = target.value
        if (type !is StructType)
            throw RuntimeException("TypeError: expected struct, got $type")

        val values = mutableListOf<Value>()
        for (field in type.memberNames) {
            val arg = construct.args.firstOrNull { it.name == field } ?: throw RuntimeException("todo should be default, but that is not handled for now")
            val value = visit(arg.value, p)
            values.add(value)
        }

        return StructValue(type, values)
    }
    override fun visitConstructArg(constructArg: HirConstructArg, p: Scope): Value {
        throw NotImplementedError("Construct args are handled by parent")
    }

    override fun visitCall(call: HirCall, p: Scope): Value {
        var owner: Value? = null
        var target = visit(call.target, p)
        if (target is OwnedValue) {
            owner = target.owner
            target = target.value
        }
        if (target !is FnValue)
            throw RuntimeException("TypeError: expected function, got ${target.type}")

        //todo need to snapshot the scope of the function at definition time (with the exception of the base module in
        // that case) so that we can call the function in the correct scope.
        var args = call.args.map { visit(it, p) }
        if (owner != null && owner !is TypeValue) {
            // Try to add self arg
            if (target.type.paramNames[0] != "self")
                throw RuntimeException("TypeError: expected function with self arg, got ${target.type}")
            args = listOf(owner) + args
        }
        return module.call(target, args) //todo need to be careful about where the fn is called. Calling it in the current module is not necessarily correct.
    }


    // Statements

    override fun visitVarDecl(varDecl: HirVarDecl, p: Scope): Value {
        // Evaluate initializer
        var value = visit(varDecl.init, p)
        //todo check type


        // Make mutable if relevant
        if (varDecl.isMut) {
            val mutType = value.type.asMut()
                ?: throw RuntimeException("Cannot create mutable type ${value.type}")
            value = value.withType(mutType)
        }

        // Define in scope
        p.define(varDecl.name, value)
        //todo weird to return the initialized value here, but would not be if this was an expr which i am now leaning towards
        return value
    }

    // Declarations

    override fun visitFnDecl(fnDecl: HirFnDecl, p: Scope): Value {
        val paramNames = fnDecl.params.map { it.name }
        val paramTypes = mutableListOf<Type>()
        for ((i, param) in fnDecl.params.withIndex()) {
            if (i == 0 && param.name == "self") {
                if (param.type != null)
                    throw RuntimeException("Self parameter may not specify a type")
                paramTypes.add(SelfType())
            } else {
                if (param.type == null)
                    throw RuntimeException("Parameter $i has no type.")
                val typeValue = visit(param.type, p)
                if (typeValue !is TypeValue)
                    throw RuntimeException("TypeError: expected type, got ${typeValue.type}")
                paramTypes.add(typeValue.value)
            }
        }
        val retTy = visit(fnDecl.returnType, p)
        if (retTy !is TypeValue)
            throw RuntimeException("Expected type, found $retTy")
        val fnType = FnType(paramNames, paramTypes, retTy.value)

        return NativeFnValue(fnType, fnDecl.body)
    }

    override fun visitFnParam(fnParam: HirFnParam, p: Scope): Value {
        throw NotImplementedError("fn params are handled by parent")
    }

    override fun visitConstDecl(constDecl: HirConstDecl, p: Scope): Value {
        //todo check type in here somewhere
        return visit(constDecl.init, p)
    }

    override fun visitEnumDecl(enumDecl: HirEnumDecl, p: Scope): Value {
        //todo need to cache this based on source location
        // also need to cache entire comptime fn calls based on source loc & arguments
        val type = EnumType("unnamed", enumDecl.cases.map { it.name })
        return TypeValue(TypeType(), type)
    }
    override fun visitEnumCase(enumCase: HirEnumCase, p: Scope): Value {
        throw NotImplementedError("Enum cases are handled by enum decl")
    }

    override fun visitStructDecl(structDecl: HirStructDecl, p: Scope): Value {
        val names = structDecl.fields.map { it.name }
        //todo somehow need to handle cases where type definitions contain invalid things.
        // Probably a flag somewhere in here that says if we are in a "comptime" location or not.
        //todo better error if the result is not a type.
        val types = structDecl.fields.map { (visit(it.type, p) as TypeValue).value }

        val decls = mutableMapOf<String, Value>()
        for (decl in structDecl.decls) {
            if (decl !is HirConstDecl)
                TODO("Unreachable because const decl is the only decl type. Probably should commit to this or back off it")
            val value = visit(decl, p)
            decls[decl.name] = value
        }

        val type = StructType("unnamed", names, types, decls)
        return TypeValue(TypeType(), type)
    }
    override fun visitStructField(structField: HirStructField, p: Scope): Value {
        throw NotImplementedError("Struct fields are handled by decl")
    }

    override fun visitUnionDecl(unionDecl: HirUnionDecl, p: Scope): Value {
        val names = unionDecl.members.map { it.name }
        //todo somehow need to handle cases where type definitions contain invalid things.
        // Probably a flag somewhere in here that says if we are in a "comptime" location or not.
        //todo better error if the result is not a type.
        val types = unionDecl.members.map { (visit(it.type, p) as TypeValue).value }

        val type = UnionType("unnamed", names, types)
        return TypeValue(TypeType(), type)
    }
    override fun visitUnionMember(unionMember: HirUnionMember, p: Scope): Value {
        throw NotImplementedError("Struct fields are handled by decl")
    }
}