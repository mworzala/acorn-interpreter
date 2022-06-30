package org.acornlang.parse.impl

import org.acornlang.syntax.SyntaxKind
import org.acornlang.common.text.Span
import org.acornlang.lex.Lexer
import org.acornlang.lex.Token
import org.acornlang.lex.TokenType
import org.acornlang.parse.ParseError

// Convenience definitions
internal typealias Marker = Parser.CompletedMarker
internal typealias CompletedMarker = Parser.CompletedMarker

internal class Parser(
    input: String,
    val lexer: Lexer = Lexer(input),
) {
    companion object {
        private val RECOVERY_SET = listOf(TokenType.LET)
    }

    val expectedKinds = mutableListOf<TokenType>()
    val events = mutableListOf<ParseEvent>()

    init {
        lexer.reset()
    }

    // SECTION: Parsing utilities

    /**
     * @return true if the next token is of the given type.
     */
    fun at(type: TokenType): Boolean {
        expectedKinds.add(type)
        return peek() == type
    }

    /**
     * Consume the next symbol no matter the type.
     */
    fun bump() {
        eatTrivia()

        expectedKinds.clear()
        lexer.next()!! //todo better error here i guess
        events.add(ParseEvent.AddToken)
    }

    /**
     * Consumes the next token if it matches the given type.
     *
     * @return true if the next token is of the given type.
     */
    fun match(type: TokenType): Boolean {
        expectedKinds.add(type)
        val result = peek() == type
        if (result) bump()
        return result
    }

    /**
     * Consumes the next token if it is of the given type, otherwise emits an error.
     */
    fun expect(type: TokenType) {
        if (!match(type))
            error()
    }

    /**
     * Emits an error at the current location
     *
     * todo add a way to customize the error emitted. In some cases we can emit more specific error messages.
     */
    fun error() {
        val found = peekToken()
        val parseError = if (found != null) {
            val (type, _, span) = found
            GenericParseError(
                expectedKinds.toMutableList(),
                type, span,
            )
        } else {
            GenericParseError(expectedKinds.toMutableList(), null, Span(lexer.source.length, lexer.source.length))
        }
        events.add(ParseEvent.Error(parseError))
        expectedKinds.clear()

        if (!atSet(RECOVERY_SET) && !atEnd()) {
            val m = start()
            bump()
            m.complete(SyntaxKind.ERROR)
        }
    }

    // SECTION: Markers

    fun start(): Marker {
        val pos = events.size
        events.add(ParseEvent.Placeholder)
        return Marker(pos)
    }

    inner class Marker(
        val pos: Int,
    ) {
        fun complete(type: SyntaxKind): CompletedMarker {
            val eventAtPos = events[pos]
            if (eventAtPos !is ParseEvent.Placeholder)
                throw IllegalStateException("Placeholder expected, found $eventAtPos")

            events[pos] = ParseEvent.StartNode(type, null)
            events.add(ParseEvent.FinishNode)

            return CompletedMarker(pos)
        }
    }

    inner class CompletedMarker(
        val pos: Int,
    ) {
        fun precede(): Marker {
            val newMarker = start()

            val startEvent = events[pos] as ParseEvent.StartNode // Error otherwise todo
            startEvent.fp = newMarker.pos - pos

            return newMarker
        }
    }

    // SECTION: Helpers

    private fun peekToken(): Token? {
        eatTrivia()
        return lexer.peek()
    }

    private fun peek(): TokenType? {
        eatTrivia()
        return lexer.peek()?.type
    }

    private fun atSet(set: List<TokenType>): Boolean = set.contains(peek())

    private fun atEnd() = peek() == null

    private fun eatTrivia() {
        while (lexer.peek()?.type?.isTrivia() == true) {
            lexer.next()
        }
    }

}