package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class ExprRegressionTests {

    @Test
    fun `too many errors emitted when rhs fails`() {
        checkExpr("""
            (1+
        """.trimIndent(), """
ROOT@0..3
  PAREN_EXPR@0..3
    LPAREN@0..1 "("
    INFIX_EXPR@1..3
      LITERAL@1..2
        NUMBER@1..2 "1"
      PLUS@2..3 "+"
error at 3..3: expected [NUMBER, STRING, TRUE, FALSE, IDENT, LPAREN, MINUS], found null
error at 3..3: expected [RPAREN], found null
        """.trimIndent())
    }

}