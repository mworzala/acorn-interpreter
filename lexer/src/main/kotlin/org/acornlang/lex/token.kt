package org.acornlang.lex

enum class TokenType {
    LPAREN, RPAREN,
    LBRACE, RBRACE,
    LBRACKET, RBRACKET,
    MINUS, PLUS, STAR, SLASH,
    EQ, EQEQ, BANG, BANGEQ,
    LT, LTEQ, GT, GTEQ,
    AMP, AMPAMP, PIPEPIPE,
    SEMICOLON, COLON, COMMA, DOT, AT,

    BREAK, CONST, ELSE, ENUM, FALSE,
    FN, FOREIGN, IF, LET, MUT, RETURN,
    SPEC, STRUCT, TRUE, TYPE, UNION,
    WHILE,

    NUMBER, STRING, IDENT,

    DOC_COMMENT, COMMENT,
    WHITESPACE, ERROR;

    fun isTrivia() = this in listOf(WHITESPACE, DOC_COMMENT, COMMENT)

//    fun isPrefixOp() = this in listOf(MINUS, BANG)
//    fun isInfixOp() = this in listOf(PLUS, MINUS, STAR, SLASH, EQEQ, BANGEQ, LT, LTEQ, GT, GTEQ, AMPAMP, BARBAR, DOT, EQ)
//    fun isPostfixOp() = this in listOf(LBRACKET, LPAREN)
}
