package org.acornlang.lexer

import org.acornlang.fail

enum class TokenType {
    LPAREN, RPAREN,
    LBRACE, RBRACE,
    LBRACKET, RBRACKET,
    MINUS, PLUS, STAR, SLASH,
    EQ, EQEQ, BANG, BANGEQ,
    LT, LTEQ, GT, GTEQ,
    AMP, AMPAMP, BARBAR,
    SEMICOLON, COLON, COMMA, DOT, AT,

    CONST, ELSE, ENUM, FN,
    FOREIGN, IF, LET, MUT, RETURN,
    STRUCT, WHILE, TRUE, FALSE,
    UNION, TYPE,

    NUMBER, STRING, IDENT,

    ERROR, EOF;

    fun isPrefixOp() = this in listOf(MINUS, BANG)
    fun isInfixOp() = this in listOf(PLUS, MINUS, STAR, SLASH, EQEQ, BANGEQ, LT, LTEQ, GT, GTEQ, AMPAMP, BARBAR, DOT, EQ)
    fun isPostfixOp() = this in listOf(LBRACKET, LPAREN)
}

data class Span(val start: Int, val end: Int)

data class Token(val type: TokenType, val span: Span)

class Lexer(
    private val source: String,
) {
    private var start: Int = 0
    private var cursor: Int = 0

    fun next(): Token {
        this.skip_trivia();
        this.start = this.cursor;

        if (this.atEnd()) {
            return this.newToken(TokenType.EOF);
        }

        val c = this.advance();
        if (isAlpha(c))
            return this.ident();
        if (isDigit(c))
            return this.number();
        if (c == '"')
            return this.string();

        return this.symbol(c);
    }

    // Utilities

    private fun newToken(type: TokenType): Token {
        return Token(
            type,
            Span(
                this.start,
                this.cursor,
            )
        )
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
        this.cursor++;
        return this.source[this.cursor - 1];
    }

    private fun match(c: Char): Boolean {
        if (this.atEnd()) return false;
        if (this.peek0() != c) return false;
        this.advance();
        return true;
    }

    // Lex functions

    private fun skip_trivia() {
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
                        continue
                    } else return
                }
                else -> return
            }
        }
    }

    private fun ident(): Token {
        while (isAlpha(this.peek0()) || isDigit(this.peek0())) {
            this.advance();
        }
        return this.newToken(this.identOrKeyword());
    }

    private fun number(): Token {
        while (isDigit(this.peek0())) {
            this.advance();
        }

        //todo floating point

        return this.newToken(TokenType.NUMBER);
    }

    private fun string(): Token {
        var next = this.peek0()
        while (next != '"' && !this.atEnd()) {
            if (next == '\n') fail("Newline in string");
            this.advance();
            next = this.peek0();
        }

        if (this.atEnd())
            fail("Unterminated string");

        // Eat closing quote
        this.advance();
        return this.newToken(TokenType.STRING)
    }

    private fun symbol(c: Char): Token {
        when (c) {
            // @formatter:off
            '(' -> return this.newToken(TokenType.LPAREN)
            ')' -> return this.newToken(TokenType.RPAREN)
            '{' -> return this.newToken(TokenType.LBRACE)
            '}' -> return this.newToken(TokenType.RBRACE)
            '[' -> return this.newToken(TokenType.LBRACKET)
            ']' -> return this.newToken(TokenType.RBRACKET)
            '-' -> return this.newToken(TokenType.MINUS)
            '+' -> return this.newToken(TokenType.PLUS)
            '*' -> return this.newToken(TokenType.STAR)
            '/' -> return this.newToken(TokenType.SLASH)
            '=' -> return this.newToken(if (this.match('=')) TokenType.EQEQ else TokenType.EQ)
            '!' -> return this.newToken(if (this.match('=')) TokenType.BANGEQ else TokenType.BANG)
            '<' -> return this.newToken(if (this.match('=')) TokenType.LTEQ else TokenType.LT)
            '>' -> return this.newToken(if (this.match('=')) TokenType.GTEQ else TokenType.GT)
            '&' -> return this.newToken(if (this.match('&')) TokenType.AMPAMP else TokenType.AMP)
            '|' -> return this.newToken(if (this.match('|')) TokenType.BARBAR else TokenType.ERROR)
            ';' -> return this.newToken(TokenType.SEMICOLON)
            ':' -> return this.newToken(TokenType.COLON)
            ',' -> return this.newToken(TokenType.COMMA)
            '.' -> return this.newToken(TokenType.DOT)
            '@' -> return this.newToken(TokenType.AT)
            else -> fail("Unexpected character: ${c.toInt()} '${c}' '${source.substring(cursor)}'")
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

    private fun isAlpha(c: Char): Boolean {
        return c in 'a'..'z' || c in 'A'..'Z' || c == '_';
    }

    private fun isDigit(c: Char): Boolean {
        return c in '0'..'9';
    }
}