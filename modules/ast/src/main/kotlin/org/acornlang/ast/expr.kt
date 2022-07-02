package org.acornlang.ast

import org.acornlang.syntax.SyntaxNode

open class AstExpr(syntax: SyntaxNode) : AstNode(syntax) {

    companion object {
        fun castOrNull(node: SyntaxNode): AstExpr? = when (node.kind) {

            else -> null
        }
    }

}