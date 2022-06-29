package org.acornlang.parse

import org.acornlang.ast.SyntaxKind
import org.acornlang.ast.SyntaxNode
import org.acornlang.ast.SyntaxTreeBuilder
import org.acornlang.lexer.Token
import org.acornlang.lexer.TokenType

class Sink(
    private val lexemes: List<Token>,
    private val events: List<ParseEvent>,
) {
    private var cursor: Int = 0
    private val builder = SyntaxTreeBuilder()

    fun finish(): SyntaxNode {
        val events = events.toMutableList()

        for ((idx, event) in events.withIndex()) {

            when (event) {
                is ParseEvent.StartNode -> {
                    val types = mutableListOf(event.type)

                    var i = idx
                    var fp = event.fp

                    while (fp != null) {
                        i += fp

                        val next = events[i]
                        fp = if (next is ParseEvent.StartNode) {
                            events[i] = ParseEvent.Placeholder // Replace consumed events with placeholder
                            types.add(next.type)
                            next.fp
                        } else throw IllegalStateException("Unreachable")
                    }

                    for (type in types.reversed()) {
                        builder.startNode(type)
                    }
                }
                is ParseEvent.AddToken -> token()
                is ParseEvent.FinishNode -> builder.finishNode()
                is ParseEvent.Placeholder -> {}
            }

            eatWhitespace()
        }

        return builder.finish()
    }

    private fun token() {
        val (type, text) = lexemes[cursor]
        builder.token(type.toSyntaxKind(), text)
        cursor += 1
    }

    private fun eatWhitespace() {
        var lexeme = lexemes.getOrNull(cursor)
        while (lexeme != null) {
            if (!lexeme.first.isTrivia())
                break

            token()
            lexeme = lexemes.getOrNull(cursor)
        }
    }


}

private fun TokenType.toSyntaxKind(): SyntaxKind = when (this) {
    TokenType.ROOT -> SyntaxKind.ROOT
    TokenType.NUMBER -> SyntaxKind.NUMBER
    TokenType.STRING -> SyntaxKind.STRING
    TokenType.PLUS -> SyntaxKind.PLUS
    TokenType.MINUS -> SyntaxKind.MINUS
    TokenType.STRUCT -> SyntaxKind.STRUCT
    TokenType.COMMENT -> SyntaxKind.COMMENT
    TokenType.WHITESPACE -> SyntaxKind.WHITESPACE
    else ->TODO()
}