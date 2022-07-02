package org.acornlang.ast

import org.acornlang.ast.util.collectNonNull
import org.acornlang.syntax.SyntaxKind
import org.acornlang.syntax.SyntaxNode
import org.acornlang.syntax.SyntaxToken

open class AstExpr(syntax: SyntaxNode) : AstNode(syntax) {

    companion object {
        fun castOrNull(node: SyntaxNode): AstExpr? = when (node.kind) {
            SyntaxKind.LITERAL -> AstLiteral(node)
            SyntaxKind.VAR_REF -> AstVarRef(node)
            SyntaxKind.INTRINSIC -> AstIntrinsic(node)

            SyntaxKind.INFIX_EXPR -> AstBinary(node)
            SyntaxKind.PREFIX_EXPR, SyntaxKind.POSTFIX_EXPR -> AstUnary(node)

            SyntaxKind.PAREN_EXPR -> AstParenGroup(node)
            SyntaxKind.ASSIGN -> AstAssign(node)
            SyntaxKind.MEMBER_ACCESS -> AstMemberAccess(node)
            SyntaxKind.TYPE_UNION -> AstTypeUnion(node)
            SyntaxKind.INDEX -> AstIndex(node)
            SyntaxKind.ARRAY_LITERAL -> AstArrayLiteral(node)
            SyntaxKind.TUPLE_LITERAL -> AstTupleLiteral(node)

            SyntaxKind.BLOCK -> AstBlock(node)
            SyntaxKind.IMPLICIT_RETURN -> AstImplicitReturn(node)

            else -> null
        }
    }

}

class AstLiteral(syntax: SyntaxNode) : AstExpr(syntax) {

    enum class Type {
        NUMBER, STRING, BOOL,
    }

    val type: Type get() = when (token()?.kind) {
        SyntaxKind.NUMBER -> Type.NUMBER
        SyntaxKind.STRING -> Type.STRING
        SyntaxKind.BOOL -> Type.BOOL
        else -> throw IllegalStateException("Unreachable")
    }

    val value: SyntaxToken? get() = token()

}

class AstVarRef(syntax: SyntaxNode) : AstExpr(syntax) {
    val name: SyntaxToken? get() = token()
}

class AstIntrinsic(syntax: SyntaxNode) : AstExpr(syntax) {
    val name: SyntaxToken? get() = token()
}

class AstUnary(syntax: SyntaxNode) : AstExpr(syntax) {
    // Valid to simply get the first token, since it is the only token
    val operator: SyntaxToken? get() = token()

    val operand: AstExpr? get() = node()?.let(AstExpr::castOrNull)
}

class AstBinary(syntax: SyntaxNode) : AstExpr(syntax) {
    val operator: SyntaxToken? get() = token()

    val lhs: AstExpr? get() =
        node()?.let(AstExpr::castOrNull)

    val rhs: AstExpr? get() =
        node(skip = 1)?.let(AstExpr::castOrNull)
}

class AstParenGroup(syntax: SyntaxNode) : AstExpr(syntax) {
    val expr: AstExpr?
        get() = node()?.let(AstExpr::castOrNull)
}

class AstAssign(syntax: SyntaxNode) : AstExpr(syntax) {
    val target: AstExpr? get() =
        node()?.let(AstExpr::castOrNull)

    val expr: AstExpr? get() =
        node(skip = 1)?.let(AstExpr::castOrNull)
}

class AstMemberAccess(syntax: SyntaxNode) : AstExpr(syntax) {
    val target: AstExpr? get() =
        node()?.let(AstExpr::castOrNull)

    val member: AstExpr? get() =
        node(skip = 1)?.let(AstExpr::castOrNull)
}

class AstTypeUnion(syntax: SyntaxNode) : AstExpr(syntax) {
    val lhs: AstExpr? get() =
        node()?.let(AstExpr::castOrNull)

    val rhs: AstExpr? get() =
        node(skip = 1)?.let(AstExpr::castOrNull)
}

class AstIndex(syntax: SyntaxNode) : AstExpr(syntax) {
    val target: AstExpr? get() =
        node()?.let(AstExpr::castOrNull)

    val expr: AstExpr? get() =
        node(skip = 1)?.let(AstExpr::castOrNull)
}

class AstArrayLiteral(syntax: SyntaxNode) : AstExpr(syntax) {
    val exprs: List<AstExpr> get() =
        children().nodes().map(AstExpr::castOrNull).collectNonNull()
}

class AstTupleLiteral(syntax: SyntaxNode) : AstExpr(syntax) {
    val exprs: List<AstExpr> get() =
        children().nodes().map(AstExpr::castOrNull).collectNonNull()
}

class AstBlock(syntax: SyntaxNode) : AstExpr(syntax) {
    val stmts: List<AstStmt>
        get() = children().nodes().map(AstStmt::castOrNull).collectNonNull()
}

class AstImplicitReturn(syntax: SyntaxNode) : AstExpr(syntax) {
    val expr: AstExpr?
        get() = node()?.let(AstExpr::castOrNull)
}
