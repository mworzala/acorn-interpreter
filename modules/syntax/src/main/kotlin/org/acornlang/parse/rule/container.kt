package org.acornlang.parse.rule

import org.acornlang.lex.TokenType
import org.acornlang.parse.impl.CompletedMarker
import org.acornlang.parse.impl.Marker
import org.acornlang.parse.impl.Parser
import org.acornlang.syntax.SyntaxKind

internal fun Parser.containerItem(extended: Boolean): CompletedMarker? {
    val m = start()

    while (at(TokenType.DOC_COMMENT))
        bump()

    return when {
        at(TokenType.CONST) -> constDecl(m, extended)
        at(TokenType.FN) -> namedFnDecl(m, extended)
        at(TokenType.ENUM) -> namedEnumDecl(m, extended)
        at(TokenType.STRUCT) -> namedStructDecl(m, extended)
        at(TokenType.UNION) -> namedUnionDecl(m, extended)
        at(TokenType.SPEC) -> namedSpecDecl(m, extended)
        else -> {
            error()
            return null
        }
    }
}

// SECTION: Const Decl
// ===================

private fun Parser.constDecl(m: Marker, extended: Boolean): CompletedMarker {
    expect(TokenType.CONST)
    expect(TokenType.IDENT)

    // Type expression
    if (match(TokenType.COLON))
        expr(false)

    // Initializer
    // This is actually required semantically, but we can complain about this later.
    if (match(TokenType.EQ))
        expr(extended)

    expect(TokenType.SEMICOLON)
    return m.complete(SyntaxKind.CONST_DECL)
}


// SECTION: Function Decl
// ======================

internal fun Parser.namedFnDecl(m: Marker, extended: Boolean): CompletedMarker {
    expect(TokenType.FN)
    expect(TokenType.IDENT)

    fnParamList(extended)

    // Return type
    expr(false)

    // Semicolon or body
    if (at(TokenType.SEMICOLON))
        bump()
    else block(extended)

    return m.complete(SyntaxKind.NAMED_FN_DECL)
}

/**
 * Anonymous function. Handles parsing both FN_DECL and FN_PROTO.
 */
internal fun Parser.fnDecl(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.FN)

    fnParamList(extended)

    // Return type
    expr(false)

    // Type (no body) or decl (body
    return if (!at(TokenType.LBRACE)) {
        m.complete(SyntaxKind.FN_TYPE)
    } else {
        block(extended)
        m.complete(SyntaxKind.FN_DECL)
    }
}

private fun Parser.fnParamList(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.LPAREN)

    while (!at(TokenType.RPAREN) && !atEnd()) {
        fnParam(extended)

        if (!at(TokenType.RPAREN))
            expect(TokenType.COMMA)
    }

    expect(TokenType.RPAREN)
    return m.complete(SyntaxKind.FN_PARAM_LIST)
}

private fun Parser.fnParam(extended: Boolean): CompletedMarker {
    val m = start()

    expect(TokenType.IDENT)
    if (match(TokenType.COLON))
        expr(extended)

    return m.complete(SyntaxKind.FN_PARAM)
}


// SECTION: Enum Decl
// ==================

internal fun Parser.namedEnumDecl(m: Marker, extended: Boolean): CompletedMarker {
    expect(TokenType.ENUM)
    expect(TokenType.IDENT)

    enumBody(extended)

    return m.complete(SyntaxKind.NAMED_ENUM_DECL)
}

internal fun Parser.enumDecl(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.ENUM)

    enumBody(extended)

    return m.complete(SyntaxKind.ENUM_DECL)
}

private fun Parser.enumBody(extended: Boolean) {
    pushRecoveryToken(TokenType.RBRACE)
    expect(TokenType.LBRACE)

    // Parse case list
    val m = start()
    while (at(TokenType.IDENT)) {
        enumCase(extended)
    }
    m.complete(SyntaxKind.ENUM_CASE_LIST)

    // Parse container items after case list
    while (!at(TokenType.RBRACE) && !atEnd()) {
        containerItem(extended)
    }

    expect(TokenType.RBRACE)
    popRecoveryToken(TokenType.RBRACE)
}

private fun Parser.enumCase(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.IDENT)
    expect(TokenType.COMMA)
    return m.complete(SyntaxKind.ENUM_CASE)
}


// SECTION: Struct Decl
// ====================

internal fun Parser.namedStructDecl(m: Marker, extended: Boolean): CompletedMarker {
    expect(TokenType.STRUCT)
    expect(TokenType.IDENT)

    structBody(extended)

    return m.complete(SyntaxKind.NAMED_STRUCT_DECL)
}

internal fun Parser.structDecl(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.STRUCT)

    structBody(extended)

    return m.complete(SyntaxKind.STRUCT_DECL)
}

private fun Parser.structBody(extended: Boolean) {
    expect(TokenType.LBRACE)

    // Parse container items before field list
    while (!at(TokenType.IDENT) && !at(TokenType.RBRACE)) {
        containerItem(extended)
    }

    // Parse case list
    val m = start()
    while (at(TokenType.IDENT)) {
        structField(extended)
    }
    m.complete(SyntaxKind.STRUCT_FIELD_LIST)

    // Parse container items after field list
    while (!at(TokenType.RBRACE)) {
        containerItem(extended)
    }

    expect(TokenType.RBRACE)
}

private fun Parser.structField(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.IDENT)

    // Type expression
    if (match(TokenType.COLON))
        expr(false)

    expect(TokenType.COMMA)
    return m.complete(SyntaxKind.STRUCT_FIELD)
}


// SECTION: Union Decl
// ====================

internal fun Parser.namedUnionDecl(m: Marker, extended: Boolean): CompletedMarker {
    expect(TokenType.UNION)
    expect(TokenType.IDENT)

    unionBody(extended)

    return m.complete(SyntaxKind.NAMED_UNION_DECL)
}

internal fun Parser.unionDecl(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.UNION)

    unionBody(extended)

    return m.complete(SyntaxKind.UNION_DECL)
}

private fun Parser.unionBody(extended: Boolean) {
    expect(TokenType.LBRACE)

    // Parse container items before member list
    while (!at(TokenType.IDENT) && !at(TokenType.RBRACE)) {
        containerItem(extended)
    }

    // Parse case list
    val m = start()
    while (at(TokenType.IDENT)) {
        unionMember(extended)
    }
    m.complete(SyntaxKind.UNION_MEMBER_LIST)

    // Parse container items after member list
    while (!at(TokenType.RBRACE)) {
        containerItem(extended)
    }

    expect(TokenType.RBRACE)
}

private fun Parser.unionMember(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.IDENT)

    // Type expression
    if (match(TokenType.COLON))
        expr(false)

    expect(TokenType.COMMA)
    return m.complete(SyntaxKind.UNION_MEMBER)
}


// SECTION: Spec Decl
// ==================

internal fun Parser.namedSpecDecl(m: Marker, extended: Boolean): CompletedMarker {
    expect(TokenType.SPEC)
    expect(TokenType.IDENT)

    specBody(extended)

    return m.complete(SyntaxKind.NAMED_SPEC_DECL)
}

internal fun Parser.specDecl(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.SPEC)

    specBody(extended)

    return m.complete(SyntaxKind.SPEC_DECL)
}

private fun Parser.specBody(extended: Boolean) {
    expect(TokenType.LBRACE)

    // Parse fn list
    while (!at(TokenType.RBRACE)) {
        namedFnDecl(start(), extended)
    }

    expect(TokenType.RBRACE)
}
