package org.acornlang.ast

interface AstVisitor<P, R> {
    val default: R

    fun visit(node: AstNode?, p: P): R = node?.visit(this, p) ?: default

    // SECTION: Module
    // ===============

    fun visitModule(module: AstModule, p: P): R {
        for (decl in module.decls)
            visit(decl, p)
        return default
    }

    // SECTION: Decl
    // =============

    fun visitConstDecl(constDecl: AstConstDecl, p: P): R {
        visit(constDecl.type, p)
        visit(constDecl.init, p)
        return default
    }

    fun visitNamedFnDecl(namedFnDecl: AstNamedFnDecl, p: P): R {
        visit(namedFnDecl.params, p)
        visit(namedFnDecl.retType, p)
        visit(namedFnDecl.body, p)
        return default
    }
    fun visitFnDecl(fnDecl: AstFnDecl, p: P): R {
        visit(fnDecl.params, p)
        visit(fnDecl.retType, p)
        visit(fnDecl.body, p)
        return default
    }
    fun visitFnType(fnType: AstFnType, p: P): R {
        visit(fnType.params, p)
        visit(fnType.retType, p)
        return default
    }
    fun visitFnParamList(fnParamList: AstFnParamList, p: P): R {
        for (param in fnParamList.params)
            visit(param, p)
        return default
    }
    fun visitFnParam(fnParam: AstFnParam, p: P): R {
        return default
    }

    fun visitNamedEnumDecl(namedEnumDecl: AstNamedEnumDecl, p: P): R {
        visit(namedEnumDecl.cases, p)
        return default
    }
    fun visitEnumDecl(enumDecl: AstEnumDecl, p: P): R {
        visit(enumDecl.cases, p)
        return default
    }
    fun visitEnumCaseList(enumCaseList: AstEnumCaseList, p: P): R {
        for (case in enumCaseList.cases)
            visit(case, p)
        return default
    }
    fun visitEnumCase(enumCase: AstEnumCase, p: P): R {
        return default
    }

    fun visitNamedStructDecl(namedStructDecl: AstNamedStructDecl, p: P): R {
        visit(namedStructDecl.fields, p)
        for (decl in namedStructDecl.decls)
            visit(decl, p)
        return default
    }
    fun visitStructDecl(structDecl: AstStructDecl, p: P): R {
        visit(structDecl.fields, p)
        for (decl in structDecl.decls)
            visit(decl, p)
        return default
    }
    fun visitStructFieldList(structFieldList: AstStructFieldList, p: P): R {
        for (field in structFieldList.fields)
            visit(field, p)
        return default
    }
    fun visitStructField(structField: AstStructField, p: P): R {
        visit(structField.type, p)
        return default
    }

    fun visitNamedUnionDecl(namedUnionDecl: AstNamedUnionDecl, p: P): R {
        visit(namedUnionDecl.members, p)
        for (decl in namedUnionDecl.decls)
            visit(decl, p)
        return default
    }
    fun visitUnionDecl(unionDecl: AstUnionDecl, p: P): R {
        visit(unionDecl.members, p)
        for (decl in unionDecl.decls)
            visit(decl, p)
        return default
    }
    fun visitUnionMemberList(unionMemberList: AstUnionMemberList, p: P): R {
        for (member in unionMemberList.members)
            visit(member, p)
        return default
    }
    fun visitUnionMember(unionMember: AstUnionMember, p: P): R {
        visit(unionMember.type, p)
        return default
    }

    fun visitNamedSpecDecl(namedSpecDecl: AstNamedSpecDecl, p: P): R {
        for (member in namedSpecDecl.members)
            visit(member, p)
        return default
    }
    fun visitSpecDecl(specDecl: AstSpecDecl, p: P): R {
        for (member in specDecl.members)
            visit(member, p)
        return default
    }

    // SECTION: Statement
    // ==================

    fun visitExprStmt(exprStmt: AstExprStmt, p: P): R {
        return visit(exprStmt.expr, p)
    }

    fun visitVarDecl(varDecl: AstVarDecl, p: P): R {
        visit(varDecl.type, p)
        visit(varDecl.init, p)
        return default
    }

    fun visitReturn(returnStmt: AstReturn, p: P): R {
        visit(returnStmt.expr, p)
        return default
    }
    fun visitBreak(breakStmt: AstBreak, p: P): R {
        return default
    }
    fun visitContinue(continueStmt: AstContinue, p: P): R {
        return default
    }

    // SECTION: Expression
    // ===================

    fun visitLiteral(literal: AstLiteral, p: P): R {
        return default
    }
    fun visitVarRef(varRef: AstVarRef, p: P): R {
        return default
    }
    fun visitIntrinsic(intrinsic: AstIntrinsic, p: P): R {
        return default
    }
    fun visitReference(reference: AstReference, p: P): R {
        visit(reference.expr, p)
        return default
    }

    fun visitUnary(unary: AstUnary, p: P): R {
        visit(unary.operand, p)
        return default
    }
    fun visitBinary(binary: AstBinary, p: P): R {
        visit(binary.lhs, p)
        visit(binary.rhs, p)
        return default
    }
    fun visitParenGroup(parenGroup: AstParenGroup, p: P): R {
        visit(parenGroup.expr, p)
        return default
    }

    fun visitAssign(assign: AstAssign, p: P): R {
        visit(assign.target, p)
        visit(assign.expr, p)
        return default
    }
    fun visitMemberAccess(memberAccess: AstMemberAccess, p: P): R {
        visit(memberAccess.target, p)
        visit(memberAccess.member, p)
        return default
    }

    fun visitTypeUnion(typeUnion: AstTypeUnion, p: P): R {
        visit(typeUnion.lhs, p)
        visit(typeUnion.rhs, p)
        return default
    }

    fun visitIndex(index: AstIndex, p: P): R {
        visit(index.target, p)
        visit(index.expr, p)
        return default
    }
    fun visitArrayLiteral(arrayLiteral: AstArrayLiteral, p: P): R {
        for (expr in arrayLiteral.exprs)
            visit(expr, p)
        return default
    }
    fun visitTupleLiteral(tupleLiteral: AstTupleLiteral, p: P): R {
        for (expr in tupleLiteral.exprs)
            visit(expr, p)
        return default
    }

    fun visitBlock(block: AstBlock, p: P): R {
        for (stmt in block.stmts)
            visit(stmt, p)
        return default
    }
    fun visitImplicitReturn(implicitReturn: AstImplicitReturn, p: P): R {
        visit(implicitReturn.expr, p)
        return default
    }

    fun visitConstruct(construct: AstConstruct, p: P): R {
        visit(construct.target, p)
        visit(construct.fields, p)
        return default
    }
    fun visitConstructFieldList(constructFieldList: AstConstructFieldList, p: P): R {
        for (field in constructFieldList.fields)
            visit(field, p)
        return default
    }
    fun visitConstructField(constructField: AstConstructField, p: P): R {
        visit(constructField.expr, p)
        return default
    }

    fun visitCall(call: AstCall, p: P): R {
        visit(call.target, p)
        visit(call.args, p)
        return default
    }
    fun visitCallArgList(callArgList: AstCallArgList, p: P): R {
        for (arg in callArgList.args)
            visit(arg, p)
        return default
    }

    fun visitIf(ifStmt: AstIf, p: P): R {
        visit(ifStmt.cond, p)
        visit(ifStmt.thenBranch, p)
        visit(ifStmt.elseBranch, p)
        return default
    }
    fun visitWhile(whileStmt: AstWhile, p: P): R {
        visit(whileStmt.cond, p)
        visit(whileStmt.body, p)
        return default
    }

}