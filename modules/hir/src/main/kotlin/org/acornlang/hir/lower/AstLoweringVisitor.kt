package org.acornlang.hir.lower

import org.acornlang.ast.*
import org.acornlang.hir.*

class AstLoweringVisitor : AstVisitor<Unit, HirNode> {
    override val default: HirNode get() = HirNode.None

    // SECTION: Module
    // ===============

    override fun visitModule(module: AstModule, p: Unit): HirNode {
        val decls = module.decls.map { visit(it, p) as HirDecl }
        return HirModule(decls)
    }


    // SECTION: Declaration
    // ====================

    override fun visitConstDecl(constDecl: AstConstDecl, p: Unit): HirNode {
        val name = constDecl.name!!.text

        var type: HirExpr? = null
        val constType = constDecl.type
        if (constType != null)
            type = visit(constType, p) as HirExpr

        val init = visit(constDecl.init, p) as HirExpr

        return HirConstDecl(name, type, init)
    }

    override fun visitNamedFnDecl(namedFnDecl: AstNamedFnDecl, p: Unit): HirNode {
        val paramList = namedFnDecl.params!!
        val params = paramList.params.map { visit(it, p) as HirFnParam }
        val retType = visit(namedFnDecl.retType, p) as HirExpr
        val body = visit(namedFnDecl.body, p) as HirBlock
        val fnDecl = HirFnDecl(params, retType, body)

        // Wrap in const
        val name = namedFnDecl.name!!.text
        return HirConstDecl(name, null, fnDecl)
    }

    override fun visitFnDecl(fnDecl: AstFnDecl, p: Unit): HirNode {
        val paramList = fnDecl.params!!
        val params = paramList.params.map { visit(it, p) as HirFnParam }
        val retType = visit(fnDecl.retType, p) as HirExpr
        val body = visit(fnDecl.body, p) as HirBlock

        return HirFnDecl(params, retType, body)
    }

    override fun visitFnType(fnType: AstFnType, p: Unit): HirNode {
        val paramList = fnType.params!!
        val params = paramList.params.map { visit(it, p) as HirFnParam }

        val retType = visit(fnType.retType, p) as HirExpr

        return HirFnType(params, retType)
    }

    override fun visitFnParam(fnParam: AstFnParam, p: Unit): HirNode {
        val name = fnParam.name!!.text
        val type = visit(fnParam.type, p) as? HirExpr

        return HirFnParam(name, type)
    }

    override fun visitNamedEnumDecl(namedEnumDecl: AstNamedEnumDecl, p: Unit): HirNode {
        val caseList = namedEnumDecl.cases!!
        val cases = caseList.cases.map { visit(it, p) as HirEnumCase }
        val enumDecl = HirEnumDecl(cases)

        // Wrap in const
        val name = namedEnumDecl.name!!.text
        return HirConstDecl(name, null, enumDecl)
    }

    override fun visitEnumDecl(enumDecl: AstEnumDecl, p: Unit): HirNode {
        val caseList = enumDecl.cases!!
        val cases = caseList.cases.map { visit(it, p) as HirEnumCase }

        return HirEnumDecl(cases)
    }

    override fun visitEnumCase(enumCase: AstEnumCase, p: Unit): HirNode {
        val name = enumCase.name!!.text

        return HirEnumCase(name)
    }

    override fun visitNamedStructDecl(namedStructDecl: AstNamedStructDecl, p: Unit): HirNode {
        val fieldList = namedStructDecl.fields!!
        val fields = fieldList.fields.map { visit(it, p) as HirStructField }
        val decls = namedStructDecl.decls.map { visit(it, p) as HirDecl }
        val structDecl = HirStructDecl(fields, decls)

        // Wrap in const
        val name = namedStructDecl.name!!.text
        return HirConstDecl(name, null, structDecl)
    }

    override fun visitStructDecl(structDecl: AstStructDecl, p: Unit): HirNode {
        val fieldList = structDecl.fields!!
        val fields = fieldList.fields.map { visit(it, p) as HirStructField }
        val decls = structDecl.decls.map { visit(it, p) as HirDecl }

        return HirStructDecl(fields, decls)
    }

    override fun visitStructField(structField: AstStructField, p: Unit): HirNode {
        val name = structField.name!!.text
        val type = visit(structField.type, p) as HirExpr

        return HirStructField(name, type)
    }

    override fun visitNamedUnionDecl(namedUnionDecl: AstNamedUnionDecl, p: Unit): HirNode {
        val memberList = namedUnionDecl.members!!
        val members = memberList.members.map { visit(it, p) as HirUnionMember }
        val decls = namedUnionDecl.decls.map { visit(it, p) as HirDecl }
        val unionDecl = HirUnionDecl(members, decls)

        // Wrap in const
        val name = namedUnionDecl.name!!.text
        return HirConstDecl(name, null, unionDecl)
    }

    override fun visitUnionDecl(unionDecl: AstUnionDecl, p: Unit): HirNode {
        val memberList = unionDecl.members!!
        val members = memberList.members.map { visit(it, p) as HirUnionMember }
        val decls = unionDecl.decls.map { visit(it, p) as HirDecl }

        return HirUnionDecl(members, decls)
    }

    override fun visitUnionMember(unionMember: AstUnionMember, p: Unit): HirNode {
        val name = unionMember.name!!.text
        val type = visit(unionMember.type, p) as HirExpr

        return HirUnionMember(name, type)
    }

    override fun visitNamedSpecDecl(namedSpecDecl: AstNamedSpecDecl, p: Unit): HirNode {
        val members = namedSpecDecl.members.map { visit(it, p) as HirDecl }
        val specDecl = HirSpecDecl(members)

        // Wrap in const
        val name = namedSpecDecl.name!!.text
        return HirConstDecl(name, null, specDecl)
    }

    override fun visitSpecDecl(specDecl: AstSpecDecl, p: Unit): HirNode {
        val members = specDecl.members.map { visit(it, p) as HirDecl }

        return HirSpecDecl(members)
    }


    // SECTION: Statement
    // ==================

    override fun visitExprStmt(exprStmt: AstExprStmt, p: Unit): HirNode {
        return HirExprStmt(visit(exprStmt.expr, p) as HirExpr)
    }

    override fun visitVarDecl(varDecl: AstVarDecl, p: Unit): HirNode {
        val name = varDecl.name!!.text
        val type = visit(varDecl.type, p) as? HirExpr
        val init = visit(varDecl.init, p) as HirExpr

        return HirVarDecl(name, varDecl.mut, type, init)
    }

    override fun visitReturn(returnStmt: AstReturn, p: Unit): HirNode {
        val expr = visit(returnStmt.expr, p) as? HirExpr

        return HirReturn(expr)
    }

    override fun visitBreak(breakStmt: AstBreak, p: Unit): HirNode {
        return HirBreak()
    }

    override fun visitContinue(continueStmt: AstContinue, p: Unit): HirNode {
        return HirContinue()
    }


    // SECTION: Expression
    // ===================

    override fun visitLiteral(literal: AstLiteral, p: Unit): HirNode {
        return when (literal.type) {
            AstLiteral.Type.NUMBER -> {
                val strValue = literal.value!!.text
                val longValue = strValue.toLong() //todo handle overflow and switch to bigint
                return HirIntLiteral(longValue)
            }
            AstLiteral.Type.BOOL -> HirBoolLiteral(literal.value!!.text == "true")
            AstLiteral.Type.STRING -> {
                val value = literal.value!!.text
                HirStringLiteral(value.substring(1, value.length - 1))
            }
        }
    }

    override fun visitVarRef(varRef: AstVarRef, p: Unit): HirNode {
        return HirVarRef(varRef.name!!.text)
    }

    override fun visitIntrinsic(intrinsic: AstIntrinsic, p: Unit): HirNode {
        val name = intrinsic.name!!.text
        // Cut off the `!` suffix on the name, we know it is intrinsic at this point.
        return HirIntrinsicRef(name.substring(0, name.length - 1))
    }

    override fun visitReference(reference: AstReference, p: Unit): HirNode {
        val expr = visit(reference.expr, p) as HirExpr

        return HirReference(reference.mut, expr)
    }

    override fun visitUnary(unary: AstUnary, p: Unit): HirNode {
        val operator = when (val operatorStr = unary.operator!!.text) {
            "-" -> HirUnary.Operator.NEG
            "!" -> HirUnary.Operator.NOT
            else -> throw IllegalArgumentException("Unknown unary operator: $operatorStr")
        }

        val operand = visit(unary.operand, p) as HirExpr

        return HirUnary(operator, operand)
    }

    override fun visitBinary(binary: AstBinary, p: Unit): HirNode {
        val operator = when (val operatorStr = binary.operator!!.text) {
            "+" -> HirBinary.Operator.ADD
            "-" -> HirBinary.Operator.SUB
            "*" -> HirBinary.Operator.MUL
            "/" -> HirBinary.Operator.DIV

            "==" -> HirBinary.Operator.EQ
            "!=" -> HirBinary.Operator.NE
            "<" -> HirBinary.Operator.LT
            "<=" -> HirBinary.Operator.LE
            ">" -> HirBinary.Operator.GT
            ">=" -> HirBinary.Operator.GE

            "&&" -> HirBinary.Operator.AND
            "||" -> HirBinary.Operator.OR

            else -> throw IllegalArgumentException("Unknown binary operator: $operatorStr")
        }

        val lhs = visit(binary.lhs, p) as HirExpr
        val rhs = visit(binary.rhs, p) as HirExpr

        return HirBinary(operator, lhs, rhs)
    }

    override fun visitParenGroup(parenGroup: AstParenGroup, p: Unit): HirNode {
        return visit(parenGroup.expr, p) as HirExpr
    }

    override fun visitAssign(assign: AstAssign, p: Unit): HirNode {
        val target = visit(assign.target, p) as HirExpr
        val value = visit(assign.expr, p) as HirExpr

        return HirAssign(target, value)
    }

    override fun visitMemberAccess(memberAccess: AstMemberAccess, p: Unit): HirNode {
        val target = visit(memberAccess.target, p) as HirExpr

        val memberAst = memberAccess.member as AstVarRef
        // It must be a var ref, only strings are allowed.
        val member = memberAst.name!!.text

        return HirMemberAccess(target, member)
    }

    override fun visitTypeUnion(typeUnion: AstTypeUnion, p: Unit): HirNode {
        val lhs = visit(typeUnion.lhs, p) as HirExpr
        val rhs = visit(typeUnion.rhs, p) as HirExpr

        return HirTypeUnion(lhs, rhs)
    }

    override fun visitIndex(index: AstIndex, p: Unit): HirNode {
        val target = visit(index.target, p) as HirExpr
        val value = visit(index.expr, p) as HirExpr

        return HirIndex(target, value)
    }

    override fun visitArrayLiteral(arrayLiteral: AstArrayLiteral, p: Unit): HirNode {
        val values = arrayLiteral.exprs.map { visit(it, p) as HirExpr }

        return HirArrayLiteral(values)
    }

    override fun visitTupleLiteral(tupleLiteral: AstTupleLiteral, p: Unit): HirNode {
        val values = tupleLiteral.exprs.map { visit(it, p) as HirExpr }

        return HirTupleLiteral(values)
    }

    override fun visitBlock(block: AstBlock, p: Unit): HirNode {
        val stmts = block.stmts.map { visit(it, p) as HirStmt }

        return HirBlock(stmts)
    }

    override fun visitImplicitReturn(implicitReturn: AstImplicitReturn, p: Unit): HirNode {
        val expr = visit(implicitReturn.expr, p) as HirExpr

        return HirImplicitReturn(expr)
    }

    override fun visitConstruct(construct: AstConstruct, p: Unit): HirNode {
        val target = visit(construct.target, p) as HirExpr

        val fields = construct.fields!!
        val args = fields.fields.map { visit(it, p) as HirConstructArg }

        return HirConstruct(target, args)
    }

    override fun visitConstructField(constructField: AstConstructField, p: Unit): HirNode {
        val name = constructField.name!!.text
        val value = visit(constructField.expr, p) as HirExpr

        return HirConstructArg(name, value)
    }

    override fun visitCall(call: AstCall, p: Unit): HirNode {
        val target = visit(call.target, p) as HirExpr

        val argList = call.args!!
        val args = argList.args.map { visit(it, p) as HirExpr }

        return HirCall(target, args)
    }

    override fun visitIf(ifStmt: AstIf, p: Unit): HirNode {
        val cond = visit(ifStmt.cond, p) as HirExpr
        val thenBranch = visit(ifStmt.thenBranch, p) as HirBlock
        val elseBranch = visit(ifStmt.elseBranch, p) as? HirBlock

        return HirIf(cond, thenBranch, elseBranch)
    }

    override fun visitWhile(whileStmt: AstWhile, p: Unit): HirNode {
        val cond = visit(whileStmt.cond, p) as HirExpr
        val body = visit(whileStmt.body, p) as HirBlock

        return HirWhile(cond, body)
    }
}