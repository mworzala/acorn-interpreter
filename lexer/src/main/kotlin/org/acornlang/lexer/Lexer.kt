package org.acornlang.lexer

typealias Token = Pair<TokenType, String>

class Lexer(
    private val source: String,
) {
    private var start: Int = 0
    private var cursor: Int = 0

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
                        return Token(TokenType.COMMENT, text())
                    }
                    break
                }
                else -> break
            }
        }

        if (this.start == this.cursor) return null
        return Token(TokenType.WHITESPACE, text())
    }

    private fun ident(): Token {
        while (isAlpha(peek0()) || isDigit(peek0()))
            advance()

        return Token(identOrKeyword(), text())
    }

    private fun number(): Token {
        while (isDigit(peek0()))
            advance()

        //todo floating point

        return Token(TokenType.NUMBER, text())
    }

    private fun string(): Token {
        var next = peek0()
        while (next != '"' && !atEnd()) {
            advance()
            next = peek0()
        }

        // todo this allows for multiline strings, that should not be allowed.
        if (atEnd())
            return Token(TokenType.ERROR, text())

        // Eat closing quote
        advance()
        return Token(TokenType.STRING, text())
    }

    private fun symbol(c: Char): Token {
        when (c) {
            // @formatter:off
            '(' -> return Token(TokenType.LPAREN, text())
            ')' -> return Token(TokenType.RPAREN, text())
            '{' -> return Token(TokenType.LBRACE, text())
            '}' -> return Token(TokenType.RBRACE, text())
            '[' -> return Token(TokenType.LBRACKET, text())
            ']' -> return Token(TokenType.RBRACKET, text())
            '-' -> return Token(TokenType.MINUS, text())
            '+' -> return Token(TokenType.PLUS, text())
            '*' -> return Token(TokenType.STAR, text())
            '/' -> return Token(TokenType.SLASH, text())
            '=' -> return Token(if (this.match('=')) TokenType.EQEQ else TokenType.EQ, text())
            '!' -> return Token(if (this.match('=')) TokenType.BANGEQ else TokenType.BANG, text())
            '<' -> return Token(if (this.match('=')) TokenType.LTEQ else TokenType.LT, text())
            '>' -> return Token(if (this.match('=')) TokenType.GTEQ else TokenType.GT, text())
            '&' -> return Token(if (this.match('&')) TokenType.AMPAMP else TokenType.AMP, text())
            '|' -> return Token(if (this.match('|')) TokenType.BARBAR else TokenType.ERROR, text())
            ';' -> return Token(TokenType.SEMICOLON, text())
            ':' -> return Token(TokenType.COLON, text())
            ',' -> return Token(TokenType.COMMA, text())
            '.' -> return Token(TokenType.DOT, text())
            '@' -> return Token(TokenType.AT, text())
            else -> return Token(TokenType.ERROR, text())
            // @formatter:on
        }
    }

    private fun identOrKeyword(): TokenType {
        fun checkKeyword(start: Int, length: Int, rest: String, type: TokenType): TokenType {
            if (this.cursor - this.start == start + length &&
                this.source.substring(this.start + start, this.cursor) == rest)
                return type;
            return TokenType.IDENT;
        }

        when (this.source[this.start]) {
            'c' -> return checkKeyword(1, 4, "onst", TokenType.CONST)
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
            's' -> return checkKeyword(1, 5, "truct", TokenType.STRUCT)
            't' -> {
                if (this.cursor - this.start > 1) {
                    when (this.source[this.start + 1]) {
                        'r' -> return checkKeyword(2, 2, "ue", TokenType.TRUE)
                        'y' -> return checkKeyword(2, 2, "pe", TokenType.TYPE)
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
        return this.cursor >= this.source.length;
    }

    private fun peek0(): Char {
        if (this.atEnd())
            return '\u0000'
        return this.source[this.cursor];
    }

    private fun peek1(): Char {
        if (this.cursor >= this.source.length - 1)
            return '\u0000'
        return this.source[this.cursor + 1];
    }

    private fun advance(): Char {
        if (atEnd()) return '\u0000'
        cursor++
        return source[cursor - 1]
    }

    private fun match(c: Char): Boolean {
        if (this.atEnd()) return false;
        if (this.peek0() != c) return false;
        this.advance();
        return true;
    }

    private fun isAlpha(c: Char): Boolean {
        return c in 'a'..'z' || c in 'A'..'Z' || c == '_';
    }

    private fun isDigit(c: Char): Boolean {
        return c in '0'..'9';
    }
}