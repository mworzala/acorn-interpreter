package org.acornlang.ast

import org.acornlang.ast.util.collectNonNull
import org.acornlang.syntax.SyntaxKind
import org.acornlang.syntax.SyntaxNode
import org.acornlang.syntax.SyntaxToken

abstract class AstDecl(syntax: SyntaxNode) : AstNode(syntax) {

    companion object {
        fun castOrNull(node: SyntaxNode): AstDecl? = when (node.kind) {
            SyntaxKind.CONST_DECL -> AstConstDecl(node)
            SyntaxKind.NAMED_FN_DECL -> AstNamedFnDecl(node)
            SyntaxKind.NAMED_ENUM_DECL -> AstNamedEnumDecl(node)
            SyntaxKind.NAMED_STRUCT_DECL -> AstNamedStructDecl(node)
            SyntaxKind.NAMED_UNION_DECL -> AstNamedUnionDecl(node)
            SyntaxKind.NAMED_SPEC_DECL -> AstNamedSpecDecl(node)
            else -> null
        }
    }

    val docComments: List<SyntaxToken>
        get() = children().tokens().withType(SyntaxKind.DOC_COMMENT).toList()

}

// SECTION: Const decl
// ===================

class AstConstDecl(syntax: SyntaxNode) : AstDecl(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitConstDecl(this, p)

    val name: SyntaxToken?
        get() = firstTokenOfType(SyntaxKind.IDENT)

    val type: AstExpr?
        get() = node(following = SyntaxKind.COLON)?.let(AstExpr::castOrNull)

    val init: AstExpr?
        get() = node(following = SyntaxKind.EQ)?.let(AstExpr::castOrNull)

}


// SECTION: Fn decl
// ================

// NAMED_FN_DECL, FN_DECL, FN_TYPE, FN_PARAM_LIST, FN_PARAM

class AstNamedFnDecl(syntax: SyntaxNode) : AstDecl(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitNamedFnDecl(this, p)

    val name: SyntaxToken?
        get() = firstTokenOfType(SyntaxKind.IDENT)

    val params: AstFnParamList?
        get() = node()?.let(::AstFnParamList)

    val retType: AstExpr?
        // Skip over the param list
        get() = node(skip = 1)?.let(AstExpr::castOrNull)

    val body: AstExpr?
        // Skip param list, ret type. ret ty could be block, so cannot use firstOfType
        get() = node(skip = 2)?.let(AstExpr::castOrNull)
}

class AstFnDecl(syntax: SyntaxNode) : AstExpr(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitFnDecl(this, p)

    val params: AstFnParamList?
        get() = node()?.let(::AstFnParamList)

    val retType: AstExpr?
        // Skip over the param list
        get() = node(skip = 1)?.let(AstExpr::castOrNull)

    val body: AstExpr?
        // Skip param list, ret type. ret ty could be block, so cannot use firstOfType
        get() = node(skip = 2)?.let(AstExpr::castOrNull)
}

class AstFnType(syntax: SyntaxNode) : AstExpr(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitFnType(this, p)

    val params: AstFnParamList?
        get() = node()?.let(::AstFnParamList)

    val retType: AstExpr?
        // Skip over the param list.
        get() = node(skip = 1)?.let(AstExpr::castOrNull)
}

class AstFnParamList(syntax: SyntaxNode) : AstNode(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitFnParamList(this, p)

    val params: List<AstFnParam>
        get() = children().nodes().withType(SyntaxKind.FN_PARAM).map(::AstFnParam).collectNonNull()
}

class AstFnParam(syntax: SyntaxNode) : AstNode(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitFnParam(this, p)

    val name: SyntaxToken?
        get() = firstTokenOfType(SyntaxKind.IDENT)

    val type: AstExpr?
        get() = node(following = SyntaxKind.COLON)?.let(AstExpr::castOrNull)
}


// SECTION: Enum decl
// ==================

class AstNamedEnumDecl(syntax: SyntaxNode) : AstDecl(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitNamedEnumDecl(this, p)

    val name: SyntaxToken?
        get() = firstTokenOfType(SyntaxKind.IDENT)

    val cases: AstEnumCaseList?
        get() = firstNodeOfType(SyntaxKind.ENUM_CASE_LIST)?.let(::AstEnumCaseList)

    //todo container items
}

class AstEnumDecl(syntax: SyntaxNode) : AstExpr(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitEnumDecl(this, p)

    val cases: AstEnumCaseList?
        get() = firstNodeOfType(SyntaxKind.ENUM_CASE_LIST)?.let(::AstEnumCaseList)
}

class AstEnumCaseList(syntax: SyntaxNode) : AstNode(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitEnumCaseList(this, p)

    val cases: List<AstEnumCase>
        get() = children().nodes().withType(SyntaxKind.ENUM_CASE).map(::AstEnumCase).collectNonNull()
}

class AstEnumCase(syntax: SyntaxNode) : AstNode(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitEnumCase(this, p)

    val name: SyntaxToken?
        get() = firstTokenOfType(SyntaxKind.IDENT)
}


// SECTION: Struct decl
// ====================

class AstNamedStructDecl(syntax: SyntaxNode) : AstDecl(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitNamedStructDecl(this, p)

    val name: SyntaxToken?
        get() = firstTokenOfType(SyntaxKind.IDENT)

    val fields: AstStructFieldList?
        get() = firstNodeOfType(SyntaxKind.STRUCT_FIELD_LIST)?.let(::AstStructFieldList)

    val decls: List<AstDecl>
        get() = children().nodes().filter { it.kind != SyntaxKind.STRUCT_FIELD_LIST }.map(AstDecl::castOrNull).collectNonNull()
}

class AstStructDecl(syntax: SyntaxNode) : AstExpr(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitStructDecl(this, p)

    val fields: AstStructFieldList?
        get() = firstNodeOfType(SyntaxKind.STRUCT_FIELD_LIST)?.let(::AstStructFieldList)

    val decls: List<AstDecl>
        get() = children().nodes().filter { it.kind != SyntaxKind.STRUCT_FIELD_LIST }.map(AstDecl::castOrNull).collectNonNull()
}

class AstStructFieldList(syntax: SyntaxNode) : AstNode(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitStructFieldList(this, p)

    val fields: List<AstStructField>
        get() = children().nodes().withType(SyntaxKind.STRUCT_FIELD).map(::AstStructField).collectNonNull()
}

class AstStructField(syntax: SyntaxNode) : AstNode(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitStructField(this, p)

    val name: SyntaxToken?
        get() = firstTokenOfType(SyntaxKind.IDENT)

    val type: AstExpr?
        get() = node(following = SyntaxKind.COLON)?.let(AstExpr::castOrNull)
}


// SECTION: Union decl
// ===================

class AstNamedUnionDecl(syntax: SyntaxNode) : AstDecl(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitNamedUnionDecl(this, p)

    val name: SyntaxToken?
        get() = firstTokenOfType(SyntaxKind.IDENT)

    val members: AstUnionMemberList?
        get() = firstNodeOfType(SyntaxKind.UNION_MEMBER_LIST)?.let(::AstUnionMemberList)

    val decls: List<AstDecl>
        get() = children().nodes().filter { it.kind != SyntaxKind.UNION_MEMBER_LIST }.map(AstDecl::castOrNull).collectNonNull()
}

class AstUnionDecl(syntax: SyntaxNode) : AstExpr(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitUnionDecl(this, p)

    val members: AstUnionMemberList?
        get() = firstNodeOfType(SyntaxKind.UNION_MEMBER_LIST)?.let(::AstUnionMemberList)

    val decls: List<AstDecl>
        get() = children().nodes().filter { it.kind != SyntaxKind.UNION_MEMBER_LIST }.map(AstDecl::castOrNull).collectNonNull()
}

class AstUnionMemberList(syntax: SyntaxNode) : AstNode(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitUnionMemberList(this, p)

    val members: List<AstUnionMember>
        get() = children().nodes().withType(SyntaxKind.UNION_MEMBER).map(::AstUnionMember).collectNonNull()
}

class AstUnionMember(syntax: SyntaxNode) : AstNode(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitUnionMember(this, p)

    val name: SyntaxToken?
        get() = firstTokenOfType(SyntaxKind.IDENT)

    val type: AstExpr?
        get() = node(following = SyntaxKind.COLON)?.let(AstExpr::castOrNull)
}


// SECTION: Spec decl
// ==================

class AstNamedSpecDecl(syntax: SyntaxNode) : AstDecl(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitNamedSpecDecl(this, p)

    val name: SyntaxToken?
        get() = firstTokenOfType(SyntaxKind.IDENT)

    val members: List<AstNamedFnDecl>
        get() = children().nodes().withType(SyntaxKind.NAMED_FN_DECL).map(::AstNamedFnDecl).collectNonNull()

    //todo should allow extra container items in here.
}

class AstSpecDecl(syntax: SyntaxNode) : AstExpr(syntax) {

    override fun <P, R> visit(visitor: AstVisitor<P, R>, p: P): R = visitor.visitSpecDecl(this, p)

    val members: List<AstNamedFnDecl>
        get() = children().nodes().withType(SyntaxKind.NAMED_FN_DECL).map(::AstNamedFnDecl).collectNonNull()
}
