package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class ExprRegressionTests {

    @Test
    fun `too many errors emitted when rhs fails`() {
        checkExpr("""
            (1+
        """.trimIndent(), """
PAREN_EXPR@0..3
  LPAREN@0..1 "("
  INFIX_EXPR@1..3
    LITERAL@1..2
      NUMBER@1..2 "1"
    PLUS@2..3 "+"
error at 3..3: expected [FN, ENUM, STRUCT, UNION, SPEC, LBRACE, LPAREN, LBRACKET, IF, WHILE, MINUS, BANG, AMP, NUMBER, STRING, TRUE, FALSE, IDENT, INTRINSIC_IDENT], found null
error at 3..3: expected [COMMA, RPAREN], found null
        """.trimIndent())
    }

}