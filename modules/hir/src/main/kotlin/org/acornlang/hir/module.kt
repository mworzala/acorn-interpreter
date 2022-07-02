package org.acornlang.hir

class HirModule(
    val decls: List<HirDecl>
) : HirNode() {

    override fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R = visitor.visitModule(this, p)
}