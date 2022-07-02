package org.acornlang.syntax

enum class SyntaxKind {

    MODULE,

    // SECTION: Container Items
    // ========================

    CONST_DECL, CONST,

    NAMED_FN_DECL, FN_DECL, FN_TYPE, FN,
    FN_PARAM_LIST, FN_PARAM,

    NAMED_ENUM_DECL, ENUM_DECL, ENUM,
    ENUM_CASE_LIST, ENUM_CASE,

    NAMED_STRUCT_DECL, STRUCT_DECL, STRUCT,
    STRUCT_FIELD_LIST, STRUCT_FIELD,

    NAMED_UNION_DECL, UNION_DECL, UNION,
    UNION_MEMBER_LIST, UNION_MEMBER,

    NAMED_SPEC_DECL, SPEC_DECL, SPEC,

    // SECTION: Statements
    // ===================

    // keyword / decl
    VAR_DECL, LET, MUT,

    RETURN_STMT, RETURN,
    BREAK_STMT, BREAK,
    CONTINUE_STMT, CONTINUE,


    // SECTION: Expressions
    // ====================

    // Literals
    LITERAL, NUMBER, STRING, BOOL,
    VAR_REF, IDENT,
    INTRINSIC, INTRINSIC_IDENT,

    // Operators
    PREFIX_EXPR, INFIX_EXPR, POSTFIX_EXPR,
    PLUS, MINUS, STAR, SLASH,           // arithmetic
    EQEQ, BANGEQ, LT, LTEQ, GT, GTEQ,   // comparison
    AMPAMP, PIPEPIPE,                   // logical
    BANG, AMP,                          // misc

    // Misc
    PAREN_EXPR,
    BLOCK, IMPLICIT_RETURN,
    ASSIGN,
    MEMBER_ACCESS,
    TYPE_UNION,
    TUPLE_LITERAL,
    ARRAY_LITERAL,
    INDEX,
    REFERENCE,

    CONSTRUCT, CONSTRUCT_FIELD_LIST, CONSTRUCT_FIELD,

    CALL, CALL_ARG_LIST,

    IF_EXPR, IF, ELSE,
    WHILE_EXPR, WHILE,


    // SECTION: Remaining symbols
    // ==========================

    // Misc symbols
    LPAREN, RPAREN,
    LBRACE, RBRACE,
    LBRACKET, RBRACKET,
    SEMICOLON, COLON, COMMA,
    EQ, DOT, AT,

    // Keywords todo move to nearby their usage
    FOREIGN,

    // Special
    COMMENT, DOC_COMMENT,
    WHITESPACE,
    ERROR,
    ;

    fun isTrivia() = this in listOf(WHITESPACE, COMMENT)
}
