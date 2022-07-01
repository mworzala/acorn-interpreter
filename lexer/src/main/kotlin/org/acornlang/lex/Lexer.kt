package org.acornlang.lex

import org.acornlang.common.text.Span

data class Token(val type: TokenType, val value: String, val span: Span)

class Lexer(
    val source: String,
) {
    private var start: Int = 0
    private var cursor: Int = 0

    fun reset() {
        start = 0
        cursor = 0
    }

    fun next(): Token? {
        this.start = this.cursor

        if (atEnd()) return null

        val ws = whitespaceOrNull()
        if (ws != null)
            return ws

        val c = this.advance()
        if (isAlpha(c))
            return ident()
        if (isDigit(c))
            return number()
        if (c == '"')
            return string()

        return symbol(c)
    }

    fun peek(): Token? {
        val result = next()
        this.cursor = this.start
        return result
    }

    // Lex functions

    private fun whitespaceOrNull(): Token? {
        while (true) {
            when (this.peek0()) {
                ' ' -> this.advance()
                '\t' -> this.advance()
                '\r' -> this.advance()
                '\n' -> this.advance()
                '/' -> {
                    // If there are two slashes is a comment, ignore until end of line
                    if (this.peek1() == '/') {
                        do {
                            this.advance()
                        } while (!this.atEnd() && this.peek0() != '\n')
                        //todo differentiate doc comments
                        return token(TokenType.COMMENT)
                    }
                    break
                }
                else -> break
            }
        }

        if (this.start == this.cursor) return null
        return token(TokenType.WHITESPACE)
    }

    private fun ident(): Token {
        while (isAlpha(peek0()) || isDigit(peek0()))
            advance()

        // Could be keyword
        val result = identOrKeyword()
        if (result != TokenType.IDENT)
            return token(result)

        // Could be an intrinsic identifier.
        if (peek0() == '!') {
            advance()
            return token(TokenType.INTRINSIC_IDENT)
        }

        // Regular identifier
        return token(TokenType.IDENT)
    }

    private fun number(): Token {
        while (isDigit(peek0()))
            advance()

        //todo floating point

        return token(TokenType.NUMBER)
    }

    private fun string(): Token {
        var next = peek0()
        while (next != '"' && !atEnd()) {
            advance()
            next = peek0()
        }

        // todo this allows for multiline strings, that should not be allowed.
        if (atEnd())
            return token(TokenType.ERROR)

        // Eat closing quote
        advance()
        return token(TokenType.STRING)
    }

    private fun symbol(c: Char): Token {
        when (c) {
            // @formatter:off
            '(' -> return token(TokenType.LPAREN)
            ')' -> return token(TokenType.RPAREN)
            '{' -> return token(TokenType.LBRACE)
            '}' -> return token(TokenType.RBRACE)
            '[' -> return token(TokenType.LBRACKET)
            ']' -> return token(TokenType.RBRACKET)
            '-' -> return token(TokenType.MINUS)
            '+' -> return token(TokenType.PLUS)
            '*' -> return token(TokenType.STAR)
            '/' -> return token(TokenType.SLASH)
            '=' -> return token(if (this.match('=')) TokenType.EQEQ else TokenType.EQ)
            '!' -> return token(if (this.match('=')) TokenType.BANGEQ else TokenType.BANG)
            '<' -> return token(if (this.match('=')) TokenType.LTEQ else TokenType.LT)
            '>' -> return token(if (this.match('=')) TokenType.GTEQ else TokenType.GT)
            '&' -> return token(if (this.match('&')) TokenType.AMPAMP else TokenType.AMP)
            '|' -> return token(if (this.match('|')) TokenType.PIPEPIPE else TokenType.ERROR)
            ';' -> return token(TokenType.SEMICOLON)
            ':' -> return token(TokenType.COLON)
            ',' -> return token(TokenType.COMMA)
            '.' -> return token(TokenType.DOT)
            '@' -> return token(TokenType.AT)
            else -> return token(TokenType.ERROR)
            // @formatter:on
        }
    }

    private fun identOrKeyword(): TokenType {
        fun checkKeyword(start: Int, length: Int, rest: String, type: TokenType): TokenType {
            if (this.cursor - this.start == start + length &&
                this.source.substring(this.start + start, this.cursor) == rest)
                return type
            return TokenType.IDENT
        }

        when (this.source[this.start]) {
            'b' -> return checkKeyword(1, 4, "reak", TokenType.BREAK)
            'c' -> {
                if (this.cursor - this.start > 1) {
                    when(this.source[this.start + 1]) {
                        'o' -> {
                            if (this.cursor - this.start > 2) {
                                when(this.source[this.start + 2]) {
                                    'n' -> {
                                        if (this.cursor - this.start > 3) {
                                            when(this.source[this.start + 3]) {
                                                's' -> return checkKeyword(4, 1, "t", TokenType.CONST)
                                                't' -> return checkKeyword(4, 4, "inue", TokenType.CONTINUE)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            'e' -> {
                if (this.cursor - this.start > 1) {
                    when(this.source[this.start + 1]) {
                        'l' -> return checkKeyword(2, 2, "se", TokenType.ELSE)
                        'n' -> return checkKeyword(2, 2, "um", TokenType.ENUM)
                    }
                }
            }
            'f' -> {
                if (this.cursor - this.start > 1) {
                    when (this.source[this.start + 1]) {
                        'a' -> return checkKeyword(2, 3, "lse", TokenType.FALSE)
                        'n' -> return checkKeyword(2, 0, "", TokenType.FN)
                        'o' -> return checkKeyword(2, 5, "reign", TokenType.FOREIGN)
                    }
                }
            }
            'i' -> return checkKeyword(1, 1, "f", TokenType.IF)
            'l' -> return checkKeyword(1, 2, "et", TokenType.LET)
            'm' -> return checkKeyword(1, 2, "ut", TokenType.MUT)
            'r' -> return checkKeyword(1, 5, "eturn", TokenType.RETURN)
            's' -> {
                if (this.cursor - this.start > 1) {
                    when (this.source[this.start + 1]) {
                        'p' -> return checkKeyword(2, 2, "ec", TokenType.SPEC)
                        't' -> return checkKeyword(2, 4, "ruct", TokenType.STRUCT)
                    }
                }
            }
            't' -> {
                if (this.cursor - this.start > 1) {
                    when (this.source[this.start + 1]) {
                        'r' -> return checkKeyword(2, 2, "ue", TokenType.TRUE)
                    }
                }
            }
            'u' -> return checkKeyword(1, 4, "nion", TokenType.UNION)
            'w' -> return checkKeyword(1, 4, "hile", TokenType.WHILE)
        }

        return TokenType.IDENT
    }


    // Utilities

    private fun text(): String {
        return source.substring(start, cursor)
    }

    private fun atEnd(): Boolean {
        return this.cursor >= this.source.length
    }

    private fun peek0(): Char {
        if (this.atEnd())
            return '\u0000'
        return this.source[this.cursor]
    }

    private fun peek1(): Char {
        if (this.cursor >= this.source.length - 1)
            return '\u0000'
        return this.source[this.cursor + 1]
    }

    private fun advance(): Char {
        if (atEnd()) return '\u0000'
        cursor++
        return source[cursor - 1]
    }

    private fun match(c: Char): Boolean {
        if (this.atEnd()) return false
        if (this.peek0() != c) return false
        this.advance()
        return true
    }

    private fun isAlpha(c: Char): Boolean {
        return c in 'a'..'z' || c in 'A'..'Z' || c == '_'
    }

    private fun isDigit(c: Char): Boolean {
        return c in '0'..'9'
    }

    private fun token(type: TokenType) = Token(type, text(), Span(start, cursor))
}