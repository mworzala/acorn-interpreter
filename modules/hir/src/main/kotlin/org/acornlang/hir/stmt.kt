package org.acornlang.hir

abstract class HirStmt : HirNode()

class HirExprStmt(val expr: HirExpr) : HirStmt() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitExprStmt(this, p)
}

class HirVarDecl(
    val name: String,
    val isMut: Boolean,
    val type: HirExpr?,
    val init: HirExpr, //todo for now this is required, will not always be the case
) : HirStmt() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitVarDecl(this, p)
}

class HirReturn(
    val expr: HirExpr?
) : HirStmt() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitReturn(this, p)
}

class HirBreak : HirStmt() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitBreak(this, p)
}

class HirContinue : HirStmt() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitContinue(this, p)
}
