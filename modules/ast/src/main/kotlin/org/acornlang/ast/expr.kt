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
            SyntaxKind.REFERENCE -> AstReference(node)

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

            SyntaxKind.CONSTRUCT -> AstConstruct(node)
            SyntaxKind.CONSTRUCT_FIELD_LIST -> AstConstructFieldList(node)
            SyntaxKind.CONSTRUCT_FIELD -> AstConstructField(node)

            SyntaxKind.CALL -> AstCall(node)
            SyntaxKind.CALL_ARG_LIST -> AstCallArgList(node)

            SyntaxKind.IF_EXPR -> AstIf(node)
            SyntaxKind.WHILE_EXPR -> AstWhile(node)

            SyntaxKind.FN_DECL -> AstFnDecl(node)
            SyntaxKind.FN_TYPE -> AstFnType(node)
            SyntaxKind.ENUM_DECL -> AstEnumDecl(node)

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

class AstReference(syntax: SyntaxNode) : AstExpr(syntax) {
    val mut: Boolean get() = firstTokenOfType(SyntaxKind.MUT) != null

    val expr: AstExpr? get() =
        node()?.let(AstExpr::castOrNull)
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

class AstConstruct(syntax: SyntaxNode) : AstExpr(syntax) {
    val target: AstExpr? get() =
        node()?.let(AstExpr::castOrNull)

    val fields: AstConstructFieldList? get() =
        node(skip = 1)?.let(::AstConstructFieldList)
}

class AstConstructFieldList(syntax: SyntaxNode) : AstExpr(syntax) {
    val fields: List<AstConstructField> get() =
        children().nodes().map(::AstConstructField).collectNonNull()
}

class AstConstructField(syntax: SyntaxNode) : AstExpr(syntax) {
    val name: SyntaxToken? get() = token()

    val expr: AstExpr? get() =
        node()?.let(AstExpr::castOrNull)
}

class AstCall(syntax: SyntaxNode) : AstExpr(syntax) {
    val target: AstExpr? get() =
        node()?.let(AstExpr::castOrNull)

    val args: AstCallArgList? get() =
        node(skip = 1)?.let(::AstCallArgList)
}

class AstCallArgList(syntax: SyntaxNode) : AstExpr(syntax) {
    val args: List<AstExpr> get() =
        children().nodes().map(AstExpr::castOrNull).collectNonNull()
}

class AstIf(syntax: SyntaxNode) : AstExpr(syntax) {
    val cond: AstExpr? get() =
        node()?.let(AstExpr::castOrNull)

    val thenBranch: AstExpr? get() =
        node(skip = 1)?.let(AstExpr::castOrNull)

    val elseBranch: AstExpr? get() =
        node(skip = 2)?.let(AstExpr::castOrNull)
}

class AstWhile(syntax: SyntaxNode) : AstExpr(syntax) {
    val cond: AstExpr? get() =
        node()?.let(AstExpr::castOrNull)

    val body: AstExpr? get() =
        node(skip = 1)?.let(AstExpr::castOrNull)
}
