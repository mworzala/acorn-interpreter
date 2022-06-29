package org.acornlang.lexer

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

    // Syntax nodes to be moved
    ROOT,

    DOC_COMMENT, COMMENT,
    WHITESPACE, ERROR;

    fun isTrivia() = this in listOf(WHITESPACE, DOC_COMMENT, COMMENT)

//    fun isPrefixOp() = this in listOf(MINUS, BANG)
//    fun isInfixOp() = this in listOf(PLUS, MINUS, STAR, SLASH, EQEQ, BANGEQ, LT, LTEQ, GT, GTEQ, AMPAMP, BARBAR, DOT, EQ)
//    fun isPostfixOp() = this in listOf(LBRACKET, LPAREN)
}
