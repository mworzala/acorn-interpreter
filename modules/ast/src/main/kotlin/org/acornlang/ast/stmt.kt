package org.acornlang.ast

import org.acornlang.syntax.SyntaxKind
import org.acornlang.syntax.SyntaxNode
import org.acornlang.syntax.SyntaxToken



open class AstStmt(syntax: SyntaxNode) : AstNode(syntax) {
    companion object {
        fun castOrNull(node: SyntaxNode): AstStmt? = when (node.kind) {
            SyntaxKind.VAR_DECL -> AstVarDecl(node)
            else -> null
        }
    }
}

class AstVarDecl(syntax: SyntaxNode) : AstStmt(syntax) {

    val name: SyntaxToken?
        get() = firstTokenOfType(SyntaxKind.IDENT)

    val type: AstExpr?
        get() = firstNode(following = SyntaxKind.COLON)?.let(AstExpr::castOrNull)

    val init: AstExpr?
        get() = TODO()

}