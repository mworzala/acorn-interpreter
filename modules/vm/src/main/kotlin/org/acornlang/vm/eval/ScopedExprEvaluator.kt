package org.acornlang.vm.eval

import org.acornlang.hir.*
import org.acornlang.vm.*

class ImplicitReturn(val value: Value) : Exception()

class ScopedExprEvaluator(val module: Module) : HirVisitor<Scope, Value> {
    override val default: Value
        get() = throw UnsupportedOperationException()


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
        return target.get(memberAccess.value)
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
        val target = visit(call.target, p)
        if (target !is FnValue)
            throw RuntimeException("TypeError: expected function, got ${target.type}")

        //todo need to snapshot the scope of the function at definition time (with the exception of the base module in
        // that case) so that we can call the function in the correct scope.
        val args = call.args.map { visit(it, p) }
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
        val paramTypes = fnDecl.params.map { visit(it.type, p) }.map { (it as TypeValue).value } //todo better errors "expected type, found blah"
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
}