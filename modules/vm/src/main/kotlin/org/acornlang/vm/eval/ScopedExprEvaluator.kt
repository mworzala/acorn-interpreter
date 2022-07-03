package org.acornlang.vm.eval

import org.acornlang.hir.*
import org.acornlang.vm.*

class ImplicitReturn(val value: Value) : Exception()

class ScopedExprEvaluator : HirVisitor<Scope, Value> {
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

    override fun visitEnumDecl(enumDecl: HirEnumDecl, p: Scope): Value {
        //todo need to cache this based on source location
        // also need to cache entire comptime fn calls based on source loc & arguments
        val type = EnumType("unnamed", enumDecl.cases.map { it.name })
        return TypeValue(TypeType(), type)
    }
    override fun visitEnumCase(enumCase: HirEnumCase, p: Scope): Value {
        throw NotImplementedError("Enum cases are handled by enum decl")
    }
}