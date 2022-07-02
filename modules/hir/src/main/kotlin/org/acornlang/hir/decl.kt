package org.acornlang.hir

abstract class HirDecl : HirNode()


class HirConstDecl(
    val name: String,
    val type: HirExpr?,
    val init: HirExpr,
) : HirDecl() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R =
        visitor.visitConstDecl(this, p)
}
