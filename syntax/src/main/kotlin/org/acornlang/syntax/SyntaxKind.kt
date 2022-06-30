package org.acornlang.syntax

enum class SyntaxKind {
    ROOT, ERROR,

    // SECTION: Statements
    // ===================

    // keyword / decl
    LET, VAR_DECL,


    // SECTION: Expressions
    // ====================

    // Literals
    LITERAL, NUMBER, STRING, BOOL, IDENT,
    VAR_REF,

    // Operators
    PREFIX_EXPR, INFIX_EXPR, POSTFIX_EXPR,
    PLUS, MINUS, STAR, SLASH,           // arithmetic
    EQEQ, BANGEQ, LT, LTEQ, GT, GTEQ,   // comparison
    AMPAMP, PIPEPIPE,                   // logical
    BANG, AMP,                          // misc

    // Misc
    PAREN_EXPR,


    // SECTION: Remaining symbols
    // ==========================

    // Misc symbols
    LPAREN, RPAREN,
    LBRACE, RBRACE,
    LBRACKET, RBRACKET,
    EQ,
    SEMICOLON, COLON, COMMA,
    DOT, AT,

    // Keywords todo move to nearby their usage
    BREAK, CONST, ELSE, ENUM,
    FN, FOREIGN, IF, MUT, RETURN,
    SPEC, STRUCT, TYPE, UNION, WHILE,

    // Special
    COMMENT, DOC_COMMENT,
    WHITESPACE
}
