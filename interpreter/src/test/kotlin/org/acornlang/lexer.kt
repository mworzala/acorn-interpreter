package org.acornlang

import org.acornlang.lexer.Lexer
import org.acornlang.lexer.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LexerTest {
    @Test
    fun `Individual symbols`() {
        fun check(input: String, expected: TokenType) {
            val lexer = Lexer(input);

            val token = lexer.next();
            assertEquals(expected, token.type)

            val eof = lexer.next();
            assertEquals(TokenType.EOF, eof.type)
        }

        // Symbols
        check("(", TokenType.LPAREN);
        check(")", TokenType.RPAREN);
        check("{", TokenType.LBRACE);
        check("}", TokenType.RBRACE);
        check("[", TokenType.LBRACKET);
        check("]", TokenType.RBRACKET);
        check("-", TokenType.MINUS);
        check("+", TokenType.PLUS);
        check("*", TokenType.STAR);
        check("/", TokenType.SLASH);
        check("=", TokenType.EQ);
        check("==", TokenType.EQEQ);
        check("!", TokenType.BANG)
        check("!=", TokenType.BANGEQ);
        check("<", TokenType.LT);
        check("<=", TokenType.LTEQ);
        check(">", TokenType.GT);
        check(">=", TokenType.GTEQ);
        check("&&", TokenType.AMPAMP);
        check("||", TokenType.BARBAR);
        check(";", TokenType.SEMICOLON);
        check(":", TokenType.COLON);

        // Keywords
        check("const", TokenType.CONST);
        check("else", TokenType.ELSE);
        check("enum", TokenType.ENUM);
        check("fn", TokenType.FN);
        check("foreign", TokenType.FOREIGN);
        check("if", TokenType.IF);
        check("let", TokenType.LET);
        check("return", TokenType.RETURN);
        check("struct", TokenType.STRUCT);
        check("while", TokenType.WHILE);
        check("true", TokenType.TRUE);
        check("false", TokenType.FALSE);

        // Number
        check("123", TokenType.NUMBER);

        // String
        check("\"hello\"", TokenType.STRING);

        // Ident
        check("hello", TokenType.IDENT);
    }
}

