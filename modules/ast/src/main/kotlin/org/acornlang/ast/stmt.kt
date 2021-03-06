package org.acornlang.ast

import org.acornlang.syntax.SyntaxKind
import org.acornlang.syntax.SyntaxNode
import org.acornlang.syntax.SyntaxToken


abstract class AstStmt(syntax: SyntaxNode) : AstNode(syntax) {
    companion object {
        fun castOrNull(node: SyntaxNode): AstStmt? = when (node.kind) {
            SyntaxKind.VAR_DECL -> AstVarDecl(node)
            SyntaxKind.RETURN_STMT -> AstReturn(node)
            SyntaxKind.BREAK_STMT -> AstBreak(node)
            SyntaxKind.CONTINUE_STMT -> AstContinue(node)
            // If it is an expr node, wrap in AstExprStmt
            else -> AstExpr.castOrNull(node)?.let(::AstExprStmt)
        }
    }
}

class AstExprStmt(val expr: AstExpr) : AstStmt(expr.syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitExprStmt(this, p)

}

class AstVarDecl(syntax: SyntaxNode) : AstStmt(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitVarDecl(this, p)

    val name: SyntaxToken?
        get() = firstTokenOfType(SyntaxKind.IDENT)

    val mut: Boolean
        get() = firstTokenOfType(SyntaxKind.MUT) != null

    val type: AstExpr?
        get() = node(following = SyntaxKind.COLON)?.let(AstExpr::castOrNull)

    val init: AstExpr?
        get() = node(following = SyntaxKind.EQ)?.let(AstExpr::castOrNull)

}

class AstReturn(syntax: SyntaxNode) : AstStmt(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitReturn(this, p)

    val expr: AstExpr?
        get() = node()?.let(AstExpr::castOrNull)
}

class AstBreak(syntax: SyntaxNode) : AstStmt(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitBreak(this, p)

}

class AstContinue(syntax: SyntaxNode) : AstStmt(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitContinue(this, p)

}
