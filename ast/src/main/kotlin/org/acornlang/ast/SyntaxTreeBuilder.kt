package org.acornlang.ast

class SyntaxTreeBuilder {
    private lateinit var root: SyntaxNode
    private var current: SyntaxNode? = null
    private val children: MutableList<SyntaxElement>
        get() = current!!.children as MutableList<SyntaxElement>

    private var cursor: Int = 0

    fun startNode(type: SyntaxKind) {
        val node = SyntaxNode(current, type, Span(cursor, -1), mutableListOf())

        if (current == null)
            root = node
        else children.add(node)
        this.current = node
    }

    fun finishNode() {
        val node = current ?: throw IllegalStateException("No node to finish")
        node.span = Span(node.span.start, cursor)
        this.current = node.parent
    }

    fun token(type: SyntaxKind, value: String) {
        children.add(SyntaxToken(current!!, type, spanOf(value), value))
    }

    fun finish(): SyntaxNode {
        return root
    }

    // Helpers

    fun spanOf(text: String): Span {
        val span = Span(cursor, cursor + text.length)
        cursor += text.length
        return span
    }

}