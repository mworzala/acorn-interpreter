package org.acornlang.ast

import org.acornlang.ast.util.collectNonNull
import org.acornlang.syntax.SyntaxNode

class AstModule(syntax: SyntaxNode) : AstNode(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitModule(this, p)

    val decls: List<AstDecl>
        get() = children().nodes().map(AstDecl::castOrNull).collectNonNull()

}