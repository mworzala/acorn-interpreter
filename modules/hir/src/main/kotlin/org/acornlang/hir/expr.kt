package org.acornlang.hir

abstract class HirExpr : HirNode()


class HirIntLiteral(
    val value: Long
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitIntLiteral(this, p)
}
//todo HirBigInt

class HirBoolLiteral(
    val value: Boolean
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitBoolLiteral(this, p)
}

class HirStringLiteral(
    val value: String
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitStringLiteral(this, p)
}

class HirVarRef(
    val name: String
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitVarRef(this, p)
}

class HirIntrinsicRef(
    val name: String
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitIntrinsicRef(this, p)
}

class HirReference(
    val isMut: Boolean,
    val expr: HirExpr,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitReference(this, p)
}


class HirBinary(
    val operator: Operator,
    val lhs: HirExpr,
    val rhs: HirExpr
) : HirExpr() {

    enum class Operator {
        ADD, SUB, MUL, DIV,         // Arithmetic
        EQ, NEQ, LT, LE, GT, GE,    // Comparison
        AND, OR,                    // Logical
        //todo bitwise?
    }

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitBinary(this, p)
}

class HirUnary(
    val operator: Operator,
    val operand: HirExpr,
) : HirExpr() {

    enum class Operator {
        NEG, NOT,
    }

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitUnary(this, p)
}


class HirTypeUnion(
    val lhs: HirExpr,
    val rhs: HirExpr,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitTypeUnion(this, p)
}


class HirAssign(
    val target: HirExpr,
    val value: HirExpr,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitAssign(this, p)
}

class HirMemberAccess(
    val target: HirExpr,
    val value: String, //todo is there any case where this is not just an ident?
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitMemberAccess(this, p)
}

class HirIndex(
    val target: HirExpr,
    val value: HirExpr,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitIndex(this, p)
}

class HirArrayLiteral(
    val values: List<HirExpr>
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitArrayLiteral(this, p)
}

class HirTupleLiteral(
    val values: List<HirExpr>
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitTupleLiteral(this, p)
}


class HirBlock(
    val stmts: List<HirStmt>,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitBlock(this, p)
}

class HirImplicitReturn(
    val expr: HirExpr
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitImplicitReturn(this, p)
}


class HirConstruct(
    val target: HirExpr,
    val args: List<HirConstructArg>,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitConstruct(this, p)
}

class HirConstructArg(
    val name: String,
    val value: HirExpr,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitConstructArg(this, p)
}


class HirCall(
    val target: HirExpr,
    val args: List<HirExpr>,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitCall(this, p)
}


class HirIf(
    val cond: HirExpr,
    val thenBranch: HirExpr,
    val elseBranch: HirExpr?
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitIf(this, p)
}

class HirWhile(
    val cond: HirExpr,
    val body: HirExpr,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitWhile(this, p)
}


// Function

class HirFnDecl(
    val params: List<HirFnParam>,
    val returnType: HirExpr,
    val body: HirBlock,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitFnDecl(this, p)
}

class HirFnType(
    val params: List<HirFnParam>,
    val returnType: HirExpr,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitFnType(this, p)
}

class HirFnParam(
    val name: String,
    val type: HirExpr?,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitFnParam(this, p)
}


// Enum

class HirEnumDecl(
    val cases: List<HirEnumCase>,
    //todo container items
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitEnumDecl(this, p)
}

class HirEnumCase(
    val name: String,
) : HirNode() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitEnumCase(this, p)
}


// Struct

class HirStructDecl(
    val fields: List<HirStructField>,
    val decls: List<HirDecl>,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitStructDecl(this, p)
}

class HirStructField(
    val name: String,
    val type: HirExpr,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitStructField(this, p)
}


// Union

class HirUnionDecl(
    val members: List<HirUnionMember>,
    val decls: List<HirDecl>,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitUnionDecl(this, p)
}

class HirUnionMember(
    val name: String,
    val type: HirExpr,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitUnionMember(this, p)
}


// Spec

class HirSpecDecl(
    val members: List<HirDecl>,
) : HirExpr() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitSpecDecl(this, p)
}

