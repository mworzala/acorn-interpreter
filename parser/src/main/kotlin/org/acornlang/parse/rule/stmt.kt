package org.acornlang.parse

import org.acornlang.syntax.SyntaxKind
import org.acornlang.lex.TokenType
import org.acornlang.parse.impl.CompletedMarker
import org.acornlang.parse.impl.Parser
import org.acornlang.parse.rule.expr

internal fun Parser.stmt(): CompletedMarker? {
    if (at(TokenType.LET))
        return varDecl()
    return expr()
}

private fun Parser.varDecl(): CompletedMarker {
    val m = start()

    expect(TokenType.LET)
    expect(TokenType.IDENT)

    // Type expression
    if (match(TokenType.COLON))
        expr()

    // Initializer
    expect(TokenType.EQ)
    expr()

    return m.complete(SyntaxKind.VAR_DECL)
}