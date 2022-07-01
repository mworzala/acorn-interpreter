package org.acornlang.parse.impl

import org.acornlang.syntax.SyntaxKind
import org.acornlang.syntax.SyntaxTreeBuilder
import org.acornlang.lex.Lexer
import org.acornlang.lex.TokenType
import org.acornlang.parse.ParseError
import org.acornlang.parse.ParseResult

class Sink(
    private val lexer: Lexer,
    private val events: List<ParseEvent>,
) {
    private val builder = SyntaxTreeBuilder()
    private val errors = mutableListOf<ParseError>()

    init {
        lexer.reset()
    }

    fun finish(): ParseResult {
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
                is ParseEvent.Error -> errors.add(event.error)
            }

            eatTrivia()
        }

        return ParseResult(
            builder.finish(),
            errors,
        )
    }

    private fun token() {
        val (type, text, _) = lexer.next()!!
        builder.token(type.toSyntaxKind(), text)
    }

    private fun eatTrivia() {
        var token = lexer.peek()
        while (token != null) {
            if (!token.type.isTrivia())
                break

            token()
            token = lexer.peek()
        }
    }


}

private fun TokenType.toSyntaxKind(): SyntaxKind = when (this) {
    // Symbols
    TokenType.LPAREN -> SyntaxKind.LPAREN
    TokenType.RPAREN -> SyntaxKind.RPAREN
    TokenType.LBRACE -> SyntaxKind.LBRACE
    TokenType.RBRACE -> SyntaxKind.RBRACE
    TokenType.LBRACKET -> SyntaxKind.LBRACKET
    TokenType.RBRACKET -> SyntaxKind.RBRACKET
    TokenType.PLUS -> SyntaxKind.PLUS
    TokenType.MINUS -> SyntaxKind.MINUS
    TokenType.STAR -> SyntaxKind.STAR
    TokenType.SLASH -> SyntaxKind.SLASH
    TokenType.EQ -> SyntaxKind.EQ
    TokenType.EQEQ -> SyntaxKind.EQEQ
    TokenType.BANG -> SyntaxKind.BANG
    TokenType.BANGEQ -> SyntaxKind.BANGEQ
    TokenType.LT -> SyntaxKind.LT
    TokenType.LTEQ -> SyntaxKind.LTEQ
    TokenType.GT -> SyntaxKind.GT
    TokenType.GTEQ -> SyntaxKind.GTEQ
    TokenType.AMP -> SyntaxKind.AMP
    TokenType.AMPAMP -> SyntaxKind.AMPAMP
    TokenType.PIPEPIPE -> SyntaxKind.PIPEPIPE
    TokenType.SEMICOLON -> SyntaxKind.SEMICOLON
    TokenType.COLON -> SyntaxKind.COLON
    TokenType.COMMA -> SyntaxKind.COMMA
    TokenType.DOT -> SyntaxKind.DOT
    TokenType.AT -> SyntaxKind.AT

    // Keywords
    TokenType.BREAK -> SyntaxKind.BREAK
    TokenType.CONST -> SyntaxKind.CONST
    TokenType.CONTINUE -> SyntaxKind.CONTINUE
    TokenType.ELSE -> SyntaxKind.ELSE
    TokenType.ENUM -> SyntaxKind.ENUM
    TokenType.FN -> SyntaxKind.FN
    TokenType.FOREIGN -> SyntaxKind.FOREIGN
    TokenType.IF -> SyntaxKind.IF
    TokenType.LET -> SyntaxKind.LET
    TokenType.MUT -> SyntaxKind.MUT
    TokenType.RETURN -> SyntaxKind.RETURN
    TokenType.SPEC -> SyntaxKind.SPEC
    TokenType.STRUCT -> SyntaxKind.STRUCT
    TokenType.UNION -> SyntaxKind.UNION
    TokenType.WHILE -> SyntaxKind.WHILE

    // Literals
    TokenType.NUMBER -> SyntaxKind.NUMBER
    TokenType.STRING -> SyntaxKind.STRING
    TokenType.TRUE, TokenType.FALSE -> SyntaxKind.BOOL
    TokenType.IDENT -> SyntaxKind.IDENT
    TokenType.INTRINSIC_IDENT -> SyntaxKind.INTRINSIC_IDENT

    // Special
    TokenType.COMMENT -> SyntaxKind.COMMENT
    TokenType.DOC_COMMENT -> SyntaxKind.DOC_COMMENT
    TokenType.WHITESPACE -> SyntaxKind.WHITESPACE
    TokenType.ERROR -> TODO()
}