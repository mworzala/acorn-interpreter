package org.acornlang.syntax

import org.acornlang.common.text.Span

enum class SyntaxKind {
    ROOT, ERROR,

    // SECTION: Statements
    // ===================

    // keyword / decl
    LET, VAR_DECL,


    // SECTION: Expressions
    // ====================

    // Literals
    LITERAL, NUMBER, STRING, BOOL, IDENT,
    VAR_REF,

    // Operators
    PREFIX_EXPR, INFIX_EXPR, POSTFIX_EXPR,
    PLUS, MINUS, STAR, SLASH,           // arithmetic
    EQEQ, BANGEQ, LT, LTEQ, GT, GTEQ,   // comparison
    AMPAMP, PIPEPIPE,                   // logical
    BANG, AMP,                          // misc

    // Misc
    PAREN_EXPR,


    // SECTION: Remaining symbols
    // ==========================

    // Misc symbols
    LPAREN, RPAREN,
    LBRACE, RBRACE,
    LBRACKET, RBRACKET,
    EQ,
    SEMICOLON, COLON, COMMA,
    DOT, AT,

    // Keywords todo move to nearby their usage
    BREAK, CONST, ELSE, ENUM,
    FN, FOREIGN, IF, MUT, RETURN,
    SPEC, STRUCT, TYPE, UNION, WHILE,

    // Special
    COMMENT, DOC_COMMENT,
    WHITESPACE
}

interface SyntaxElement {
    val parent: SyntaxNode?
    val type: SyntaxKind
    val span: Span

    fun toDebugString(indent: Int = 0): String
}

class SyntaxNode(
    override val parent: SyntaxNode?,
    override val type: SyntaxKind,
    span: Span,
    val children: List<SyntaxElement>,
) : SyntaxElement {

    override var span: Span = span
        internal set

    override fun toDebugString(indent: Int): String {
        val sb = StringBuilder()
        sb.append(" ".repeat(indent))
        sb.append("$type@$span\n")
        children.forEach {
            sb.append(it.toDebugString(indent + 2))
        }
        return sb.toString()
    }
}

class SyntaxToken(
    override val parent: SyntaxNode?,
    override val type: SyntaxKind,
    override val span: Span,
    val text: String,
) : SyntaxElement {

    override fun toDebugString(indent: Int): String {
        return "${" ".repeat(indent)}$type@$span \"${text.replace("\n", "\\n")}\"\n"
    }
}
