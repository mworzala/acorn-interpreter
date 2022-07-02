package org.acornlang.ast

import org.acornlang.syntax.SyntaxKind
import org.acornlang.syntax.SyntaxNode
import org.acornlang.syntax.SyntaxToken

open class AstDecl(syntax: SyntaxNode) : AstNode(syntax) {

    companion object {
        fun castOrNull(node: SyntaxNode): AstDecl? = when (node.kind) {
            SyntaxKind.CONST_DECL -> AstConstDecl(node)
            else -> null
        }
    }

    val docComments: List<SyntaxToken>
        get() = children().tokens().withType(SyntaxKind.DOC_COMMENT).toList()

}

class AstConstDecl(syntax: SyntaxNode) : AstDecl(syntax) {

    val name: SyntaxToken?
        get() = firstTokenOfType(SyntaxKind.IDENT)

    val type: AstExpr?
        get() = node(following = SyntaxKind.COLON)?.let(AstExpr::castOrNull)

    val init: AstExpr?
        get() = node(following = SyntaxKind.EQ)?.let(AstExpr::castOrNull)

}
