package org.acornlang.parse

import org.acornlang.ast.SyntaxKind
import org.acornlang.lexer.TokenType

internal fun Parser.expr() {
    if (peek() == TokenType.NUMBER || peek() == TokenType.IDENT)
        bump()
    if (peek() == TokenType.STRING) {
        val m = start()
        bump()
        m.complete(SyntaxKind.STRUCT)
    }
    if (peek() == TokenType.PLUS) {
        val m = start()
        bump() // Eat +
        expr()
        val cm = m.complete(SyntaxKind.PLUS)
        val m2 = cm.precede()
        bump()
        m2.complete(SyntaxKind.MINUS)
    }

    //todo generally a parser should return a CompletedMarker so that the parent can call precede on it
}