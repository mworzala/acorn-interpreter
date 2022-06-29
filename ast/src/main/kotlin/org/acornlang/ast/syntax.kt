package org.acornlang.ast

enum class SyntaxKind {
    ROOT,

    NUMBER, STRING, PLUS, MINUS, STRUCT, COMMENT, WHITESPACE
}

class Span(
    val start: Int,
    val end: Int,
) {
    override fun toString(): String {
        return "${start}..${end}"
    }
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
