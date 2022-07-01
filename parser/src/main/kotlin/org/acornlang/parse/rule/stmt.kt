package org.acornlang.parse

import org.acornlang.syntax.SyntaxKind
import org.acornlang.lex.TokenType
import org.acornlang.parse.impl.CompletedMarker
import org.acornlang.parse.impl.Parser
import org.acornlang.parse.rule.expr

internal fun Parser.stmt(extended: Boolean): CompletedMarker? = when {
    at(TokenType.LET) -> varDecl(extended)
    at(TokenType.RETURN) -> `return`(extended)
    at(TokenType.BREAK) -> `break`(extended)
    at(TokenType.CONTINUE) -> `continue`(extended)
    else -> expr(extended)
}

private fun Parser.varDecl(extended: Boolean): CompletedMarker {
    val m = start()

    expect(TokenType.LET)

    // Optional mutability specifier
    match(TokenType.MUT)

    expect(TokenType.IDENT)

    // Type expression
    if (match(TokenType.COLON))
        expr(false)

    // Initializer
    expect(TokenType.EQ)
    expr(extended)

    return m.complete(SyntaxKind.VAR_DECL)
}

// For now return (and break) is a statement, not sure if it will need to become an expression
private fun Parser.`return`(extended: Boolean): CompletedMarker {
    val m = start()

    expect(TokenType.RETURN)

    // Return value
    if (!at(TokenType.SEMICOLON) && !at(TokenType.RBRACE) && !atEnd())
        expr(extended)

    return m.complete(SyntaxKind.RETURN_STMT)
}

private fun Parser.`break`(extended: Boolean): CompletedMarker {
    val m = start()

    expect(TokenType.BREAK)

    // Break with value? and to label?

    return m.complete(SyntaxKind.BREAK_STMT)
}

private fun Parser.`continue`(extended: Boolean): CompletedMarker {
    val m = start()

    expect(TokenType.CONTINUE)

    // continue to label?

    return m.complete(SyntaxKind.CONTINUE_STMT)
}