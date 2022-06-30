package org.acornlang.parse.rule

import org.acornlang.syntax.SyntaxKind
import org.acornlang.lex.TokenType
import org.acornlang.parse.impl.CompletedMarker
import org.acornlang.parse.impl.Parser

internal fun Parser.expr(): CompletedMarker? {
    exprBindingPower(0)

//    error()
    return null
}

private fun Parser.exprBindingPower(minBindingPower: Int): CompletedMarker? {

    var lhs = lhs() ?: return null //todo errors later

    while (true) {
        val op = when {
            at(TokenType.PLUS) -> InfixOp.ADD
            at(TokenType.MINUS) -> InfixOp.SUB
            at(TokenType.STAR) -> InfixOp.MUL
            at(TokenType.SLASH) -> InfixOp.DIV
            else -> break
        }

        val (leftBindingPower, rightBindingPower) = op.bindingPower()
        if (leftBindingPower < minBindingPower)
            break

        bump() // Eat the operator symbol

        val m = lhs.precede()
        val didParseRhs = exprBindingPower(rightBindingPower) != null
        lhs = m.complete(SyntaxKind.INFIX_EXPR)

        if (!didParseRhs)
            break
    }

    return lhs
}

private fun Parser.lhs(): CompletedMarker? = when {
    at(TokenType.NUMBER) || at(TokenType.STRING) ||
            at(TokenType.TRUE) || at(TokenType.FALSE) -> literal()
    at(TokenType.IDENT) -> varRef()
    at(TokenType.LPAREN) -> parenExpr()
    at(TokenType.MINUS) -> prefixExpr()
    else -> {
        error()
        null
    }
}

private fun Parser.literal(): CompletedMarker {
    if (!(at(TokenType.NUMBER) || at(TokenType.STRING) || at(TokenType.TRUE) || at(TokenType.FALSE)))
        throw IllegalStateException("Expected literal")

    val m = start()
    bump()
    return m.complete(SyntaxKind.LITERAL)
}

private fun Parser.varRef(): CompletedMarker {
    val m = start()
    expect(TokenType.IDENT)
    return m.complete(SyntaxKind.VAR_REF)
}

private fun Parser.parenExpr(): CompletedMarker {
    val m = start()
    expect(TokenType.LPAREN)

    exprBindingPower(0)

    expect(TokenType.RPAREN)
    return m.complete(SyntaxKind.PAREN_EXPR)
}

private fun Parser.prefixExpr(): CompletedMarker {
    val m = start()

    val op = PrefixOp.NEG
    val (_, rightBindingPower) = op.bindingPower()

    expect(TokenType.MINUS)

    exprBindingPower(rightBindingPower)
    return m.complete(SyntaxKind.PREFIX_EXPR)
}


private enum class PrefixOp {
    NEG;
//    NOT;

    fun bindingPower(): Pair<Nothing?, Int> = when (this) {
        NEG -> null to 5
    }
}

private enum class InfixOp {
    ADD, SUB,
    MUL, DIV;

    fun bindingPower(): Pair<Int, Int> = when (this) {
        ADD, SUB -> 1 to 2
        MUL, DIV -> 2 to 3
    }
}

