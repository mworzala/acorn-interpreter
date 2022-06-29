package org.acornlang.parse

import org.acornlang.ast.SyntaxKind
import org.acornlang.ast.SyntaxNode
import org.acornlang.lexer.Lexer
import org.acornlang.lexer.Token
import org.acornlang.lexer.TokenType

class Parse(
    val node: SyntaxNode,
) {
    fun toDebugTree(): String {
        return node.toDebugString()
    }
}

class Marker(
    val pos: Int,
)

class CompletedMarker(
    val pos: Int,
)

class Parser(
    input: String,
) {
    internal val lexemes: List<Token> //todo should not actually keep this as a list, should just be inside the lexer api with next, peek, eat whitespace, etc.
    private var cursor: Int = 0

    internal val events = mutableListOf<ParseEvent>()

    init {
        val lexemes = mutableListOf<Token>()
        this.lexemes = lexemes

        val lexer = Lexer(input)
        var lexeme = lexer.next()
        while (lexeme != null) {
            lexemes.add(lexeme)
            lexeme = lexer.next()
        }
    }

    fun parse(): Parse {
        val m = start()
        expr()
        m.complete(SyntaxKind.ROOT)

        return Parse(
            Sink(lexemes, events).finish(),
        )
    }

    internal fun at(type: TokenType): Boolean = peek() == type

    internal fun peek(): TokenType? {
        eatWhitespace()
        return peekRaw()
    }

    internal fun bump() {
        eatWhitespace()

        lexemes.getOrNull(cursor)!! //todo better error here
        events.add(ParseEvent.AddToken)
        cursor += 1
    }

    internal fun start(): Marker {
        val pos = events.size
        events.add(ParseEvent.Placeholder)
        return Marker(pos)
    }

    internal fun Marker.complete(type: SyntaxKind): CompletedMarker {
        val eventAtPos = events[pos]
        if (eventAtPos !is ParseEvent.Placeholder)
            throw IllegalStateException("Placeholder expected, found $eventAtPos")

        events[pos] = ParseEvent.StartNode(type, null)
        events.add(ParseEvent.FinishNode)

        return CompletedMarker(pos)
    }

    internal fun CompletedMarker.precede(): Marker {
        val newMarker = start()

        val startEvent = events[pos] as ParseEvent.StartNode // Error otherwise todo
        startEvent.fp = newMarker.pos - pos

        return newMarker
    }

    // Helpers

    private fun eatWhitespace() {
        while (peekRaw()?.isTrivia() == true) {
            cursor += 1
        }
    }

    private fun peekRaw(): TokenType? {
        return lexemes.getOrNull(cursor)?.first
    }

}