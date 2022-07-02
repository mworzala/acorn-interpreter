package org.acornlang.parse.rule

import org.acornlang.syntax.SyntaxKind
import org.acornlang.lex.TokenType
import org.acornlang.parse.impl.CompletedMarker
import org.acornlang.parse.impl.Parser
import org.acornlang.parse.stmt

internal fun Parser.expr(extended: Boolean): CompletedMarker? =
    exprBindingPower(extended, 0)

internal fun Parser.block(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.LBRACE)

    while (!at(TokenType.RBRACE) && !atEnd()) {
        val stmt = stmt(extended)
        // This can only happen if stmt finds nothing and falls through to expr, which also finds nothing.
        // In this case, expr will add an error though so we should just move on.
            ?: continue

        if (at(TokenType.SEMICOLON)) {
            bump()
        } else if (at(TokenType.RBRACE)) {
            // Add inline return here
            stmt.precede().complete(SyntaxKind.IMPLICIT_RETURN)
        } else {
            resetErrorTokens(to = TokenType.SEMICOLON)
            error()
        }
    }

    expect(TokenType.RBRACE)
    return m.complete(SyntaxKind.BLOCK)
}

private fun Parser.exprBindingPower(extended: Boolean, minBindingPower: Int): CompletedMarker? {

    var lhs = lhs(extended) ?: return null // Error was added by `lhs` already

    while (true) {
        val op = infixOp(extended) ?: break

        // Check if its a postfix operator
        val postfixBindingPower = op.postfixBindingPower()
        if (postfixBindingPower != null) {
            val (leftBindingPower, _) = postfixBindingPower
            if (leftBindingPower < minBindingPower)
                break

            lhs = when (op) {
                InfixOp.INDEX -> {
                    val m = lhs.precede()

                    bump() // [
                    expr(true)!!
                    expect(TokenType.RBRACKET)

                    m.complete(SyntaxKind.INDEX)
                }
                InfixOp.CALL -> {
                    val m = lhs.precede()

                    callArgList(extended)

                    m.complete(SyntaxKind.CALL)
                }
                InfixOp.CONSTRUCT -> {
                    val m = lhs.precede()

                    construct(true)

                    m.complete(SyntaxKind.CONSTRUCT)
                }
                else -> {
                    throw IllegalStateException("Unreachable")
                }
            }
            continue
        }

        val (leftBindingPower, rightBindingPower) = op.bindingPower()
        if (leftBindingPower < minBindingPower)
            break

        bump() // Eat the operator symbol

        val m = lhs.precede()
        val didParseRhs = exprBindingPower(extended, rightBindingPower) != null
        lhs = m.complete(when (op) {
            InfixOp.ASSIGN -> SyntaxKind.ASSIGN
            InfixOp.MEMBER_ACCESS -> SyntaxKind.MEMBER_ACCESS
            InfixOp.AMP -> SyntaxKind.TYPE_UNION
            else -> SyntaxKind.INFIX_EXPR
        })

        if (!didParseRhs)
            break
    }

    return lhs
}

private fun Parser.lhs(extended: Boolean): CompletedMarker? = when {
    at(TokenType.FN) -> fnDecl(extended)
    at(TokenType.ENUM) -> enumDecl(extended)
    at(TokenType.STRUCT) -> structDecl(extended)
    at(TokenType.UNION) -> unionDecl(extended)
    at(TokenType.SPEC) -> specDecl(extended)

    at(TokenType.LBRACE) -> block(extended)
    at(TokenType.LPAREN) -> parenOrTuple(extended)
    at(TokenType.LBRACKET) -> array(extended)

    at(TokenType.IF) -> ifExpr(extended)
    at(TokenType.WHILE) -> whileExpr(extended)

    at(TokenType.MINUS) || at(TokenType.BANG) || at(TokenType.AMP) -> prefixExpr(extended)

    at(TokenType.NUMBER) || at(TokenType.STRING) ||
            at(TokenType.TRUE) || at(TokenType.FALSE) -> literal(extended)

    at(TokenType.IDENT) -> varRef(extended)
    at(TokenType.INTRINSIC_IDENT) -> intrinsic(extended)

    else -> {
        //todo this error should simply be "expected expression, found...
        error()
        null
    }
}

private fun Parser.construct(extended: Boolean) {
    expect(TokenType.LBRACE)

    val m = start()
    while (!at(TokenType.RBRACE) && !atEnd()) {
        constructField(extended)

        if (!at(TokenType.RBRACE))
            expect(TokenType.COMMA)
    }
    m.complete(SyntaxKind.CONSTRUCT_FIELD_LIST)

    expect(TokenType.RBRACE)
}

private fun Parser.constructField(extended: Boolean): CompletedMarker {
    val m = start()

    expect(TokenType.IDENT)
    expect(TokenType.COLON)
    expr(extended)

    return m.complete(SyntaxKind.CONSTRUCT_FIELD)
}

private fun Parser.callArgList(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.LPAREN)

    while (!at(TokenType.RPAREN) && !atEnd()) {
        expr(extended)

        if (!at(TokenType.RPAREN))
            expect(TokenType.COMMA)
    }

    expect(TokenType.RPAREN)
    return m.complete(SyntaxKind.CALL_ARG_LIST)
}

private fun Parser.ifExpr(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.IF)

    // Condition
    expr(extended = false)

    // Then block
    block(true)

    // Else block
    if (match(TokenType.ELSE)) {
        // If it is an `if`, parse as another if statement
        if (at(TokenType.IF)) {
            ifExpr(true)
        } else {
            // Otherwise, parse an if block
            block(true)
        }
    }

    return m.complete(SyntaxKind.IF_EXPR)

}

private fun Parser.whileExpr(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.WHILE)

    // Condition
    expr(extended = false)

    // Body
    block(true)

    return m.complete(SyntaxKind.WHILE_EXPR)
}

private fun Parser.literal(extended: Boolean): CompletedMarker {
    if (!(at(TokenType.NUMBER) || at(TokenType.STRING) || at(TokenType.TRUE) || at(TokenType.FALSE)))
        throw IllegalStateException("Expected literal")

    val m = start()
    bump()
    return m.complete(SyntaxKind.LITERAL)
}

private fun Parser.varRef(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.IDENT)
    return m.complete(SyntaxKind.VAR_REF)
}

private fun Parser.intrinsic(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.INTRINSIC_IDENT)
    return m.complete(SyntaxKind.INTRINSIC)
}

private fun Parser.parenOrTuple(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.LPAREN)

    // Check for empty tuple
    if (match(TokenType.RPAREN))
        return m.complete(SyntaxKind.TUPLE_LITERAL)

    // Could be a paren expr or tuple
    exprBindingPower(extended, 0)

    // If there is a comma, it must be a tuple. Otherwise paren
    return if (match(TokenType.COMMA)) {
        // Parse the remaining expressions
        while (!at(TokenType.RPAREN)) {
            exprBindingPower(extended, 0)
            if (!match(TokenType.COMMA))
                break
        }

        expect(TokenType.RPAREN)
        m.complete(SyntaxKind.TUPLE_LITERAL)
    } else {
        expect(TokenType.RPAREN)
        m.complete(SyntaxKind.PAREN_EXPR)
    }
}

private fun Parser.array(extended: Boolean): CompletedMarker {
    val m = start()
    expect(TokenType.LBRACKET)

    while (!at(TokenType.RBRACKET)) {
        expr(true)
        if (!at(TokenType.RBRACKET))
            expect(TokenType.COMMA)
    }

    expect(TokenType.RBRACKET)
    return m.complete(SyntaxKind.ARRAY_LITERAL)
}

private fun Parser.prefixExpr(extended: Boolean): CompletedMarker? {
    val m = start()

    val op = when {
        at(TokenType.MINUS) -> PrefixOp.NEG
        at(TokenType.BANG) -> PrefixOp.NOT
        at(TokenType.AMP) -> PrefixOp.REF
        else -> {
            error()
            return null
        }
    }
    val (_, rightBindingPower) = op.bindingPower()

    bump() // Eat operator

    if (op == PrefixOp.REF)
        // Optional mut specifier
        match(TokenType.MUT)

    exprBindingPower(extended, rightBindingPower)

    return m.complete(when (op) {
        PrefixOp.REF -> SyntaxKind.REFERENCE
        else -> SyntaxKind.PREFIX_EXPR
    })
}


private enum class PrefixOp {
    NEG,
    NOT,
    REF,
    ;

    fun bindingPower(): Pair<Nothing?, Int> = when (this) {
        NEG, NOT, REF -> null to 50
        // Are these values right?
    }
}

private enum class InfixOp {
    // Infix
    ADD, SUB, MUL, DIV,     // arithmetic
    EQ, NE, LT, GT, LE, GE, // comparison
    AND, OR,                // logical
    AMP,                    // type union (todo bitwise operators)
    ASSIGN, MEMBER_ACCESS,

    // Postfix
    INDEX, CALL, CONSTRUCT,
    ;

    fun bindingPower(): Pair<Int, Int> = when (this) {
        ASSIGN -> 1 to 2
        AMP -> 5 to 6               // Type union
        AND, OR -> 15 to 16
        EQ, NE, LT, GT, LE, GE -> 21 to 22
        ADD, SUB -> 25 to 26
        MUL, DIV -> 31 to 32
        MEMBER_ACCESS -> 35 to 36
        else -> throw IllegalStateException("Unreachable")
    }

    fun postfixBindingPower(): Pair<Int, Nothing?>? = when (this) {
        CALL -> 33 to null
        INDEX, CONSTRUCT -> 77 to null
        else -> null
    }
}

private fun Parser.infixOp(extended: Boolean): InfixOp? = when {
    at(TokenType.PLUS) -> InfixOp.ADD
    at(TokenType.MINUS) -> InfixOp.SUB
    at(TokenType.STAR) -> InfixOp.MUL
    at(TokenType.SLASH) -> InfixOp.DIV
    at(TokenType.EQEQ) -> InfixOp.EQ
    at(TokenType.BANGEQ) -> InfixOp.NE
    at(TokenType.LT) -> InfixOp.LT
    at(TokenType.LTEQ) -> InfixOp.LE
    at(TokenType.GT) -> InfixOp.GT
    at(TokenType.GTEQ) -> InfixOp.GE
    at(TokenType.AMPAMP) -> InfixOp.AND
    at(TokenType.PIPEPIPE) -> InfixOp.OR
    at(TokenType.AMP) -> InfixOp.AMP
    // let a: i32 = 1 will parse `i32 = 1` as an assign if this is allowed in extended mode.
    // this will prevent assignment in types and conditions, which is correct.
    extended && at(TokenType.EQ) -> InfixOp.ASSIGN
    at(TokenType.DOT) -> InfixOp.MEMBER_ACCESS

    // Postfix
    at(TokenType.LBRACKET) -> InfixOp.INDEX
    at(TokenType.LPAREN) -> InfixOp.CALL
    // if a { } would parse as a construct if it was allowed in extended mode.
    extended && at(TokenType.LBRACE) -> InfixOp.CONSTRUCT

    else -> null
}

