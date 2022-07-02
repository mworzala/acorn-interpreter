package org.acornlang.hir

interface HirVisitor<P, R> {
    val default: R

    fun visit(node: HirNode?, p: P): R = node?.accept(this, p) ?: default


    // SECTION: Module
    // ===============

    fun visitModule(module: HirModule, p: P): R {
        for (decl in module.decls)
            visit(decl, p)
        return default
    }


    // SECTION: Declaration
    // ====================

    fun visitConstDecl(constDecl: HirConstDecl, p: P): R {
        visit(constDecl.type, p)
        visit(constDecl.init, p)
        return default
    }


    // SECTION: Statement
    // ==================

    fun visitVarDecl(varDecl: HirVarDecl, p: P): R {
        visit(varDecl.type, p)
        visit(varDecl.init, p)
        return default
    }

    fun visitReturn(returnStmt: HirReturn, p: P): R {
        visit(returnStmt.expr, p)
        return default
    }
    fun visitBreak(breakStmt: HirBreak, p: P): R {
        return default
    }
    fun visitContinue(continueStmt: HirContinue, p: P): R {
        return default
    }


    // SECTION: Expression
    // ===================

    fun visitIntLiteral(intLiteral: HirIntLiteral, p: P): R {
        return default
    }
    fun visitBoolLiteral(boolLiteral: HirBoolLiteral, p: P): R {
        return default
    }
    fun visitStringLiteral(stringLiteral: HirStringLiteral, p: P): R {
        return default
    }
    fun visitVarRef(varRef: HirVarRef, p: P): R {
        return default
    }
    fun visitIntrinsicRef(intrinsicRef: HirIntrinsicRef, p: P): R {
        return default
    }
    fun visitReference(reference: HirReference, p: P): R {
        visit(reference.expr, p)
        return default
    }

    fun visitBinary(binary: HirBinary, p: P): R {
        visit(binary.lhs, p)
        visit(binary.rhs, p)
        return default
    }
    fun visitUnary(unary: HirUnary, p: P): R {
        visit(unary.operand, p)
        return default
    }

    fun visitTypeUnion(typeUnion: HirTypeUnion, p: P): R {
        visit(typeUnion.lhs, p)
        visit(typeUnion.rhs, p)
        return default
    }

    fun visitAssign(assign: HirAssign, p: P): R {
        visit(assign.target, p)
        visit(assign.value, p)
        return default
    }
    fun visitMemberAccess(memberAccess: HirMemberAccess, p: P): R {
        visit(memberAccess.target, p)
        return default
    }
    fun visitIndex(index: HirIndex, p: P): R {
        visit(index.target, p)
        visit(index.value, p)
        return default
    }
    fun visitArrayLiteral(arrayLiteral: HirArrayLiteral, p: P): R {
        for (expr in arrayLiteral.values)
            visit(expr, p)
        return default
    }
    fun visitTupleLiteral(tupleLiteral: HirTupleLiteral, p: P): R {
        for (expr in tupleLiteral.values)
            visit(expr, p)
        return default
    }

    fun visitBlock(block: HirBlock, p: P): R {
        for (stmt in block.stmts)
            visit(stmt, p)
        return default
    }
    fun visitImplicitReturn(implicitReturn: HirImplicitReturn, p: P): R {
        visit(implicitReturn.expr, p)
        return default
    }

    fun visitConstruct(construct: HirConstruct, p: P): R {
        visit(construct.target, p)
        for (arg in construct.args)
            visit(arg, p)
        return default
    }
    fun visitConstructArg(constructArg: HirConstructArg, p: P): R {
        visit(constructArg.value, p)
        return default
    }

    fun visitCall(call: HirCall, p: P): R {
        visit(call.target, p)
        for (arg in call.args)
            visit(arg, p)
        return default
    }

    fun visitIf(ifStmt: HirIf, p: P): R {
        visit(ifStmt.cond, p)
        visit(ifStmt.thenBranch, p)
        visit(ifStmt.elseBranch, p)
        return default
    }
    fun visitWhile(whileStmt: HirWhile, p: P): R {
        visit(whileStmt.cond, p)
        visit(whileStmt.body, p)
        return default
    }

    fun visitFnDecl(fnDecl: HirFnDecl, p: P): R {
        for (param in fnDecl.params)
            visit(param, p)
        visit(fnDecl.returnType, p)
        visit(fnDecl.body, p)
        return default
    }
    fun visitFnType(fnType: HirFnType, p: P): R {
        for (param in fnType.params)
            visit(param, p)
        visit(fnType.returnType, p)
        return default
    }
    fun visitFnParam(fnParam: HirFnParam, p: P): R {
        visit(fnParam.type, p)
        return default
    }

    fun visitEnumDecl(enumDecl: HirEnumDecl, p: P): R {
        for (case in enumDecl.cases)
            visit(case, p)
        return default
    }
    fun visitEnumCase(enumCase: HirEnumCase, p: P): R {
        return default
    }

    fun visitStructDecl(structDecl: HirStructDecl, p: P): R {
        for (field in structDecl.fields)
            visit(field, p)
        for (decl in structDecl.decls)
            visit(decl, p)
        return default
    }
    fun visitStructField(structField: HirStructField, p: P): R {
        visit(structField.type, p)
        return default
    }

    fun visitUnionDecl(unionDecl: HirUnionDecl, p: P): R {
        for (field in unionDecl.members)
            visit(field, p)
        for (decl in unionDecl.decls)
            visit(decl, p)
        return default
    }
    fun visitUnionMember(unionMember: HirUnionMember, p: P): R {
        visit(unionMember.type, p)
        return default
    }

    fun visitSpecDecl(specDecl: HirSpecDecl, p: P): R {
        for (decl in specDecl.members)
            visit(decl, p)
        return default
    }

}