package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class InfixExprTest {

    @Test
    fun `simple add`() {
        checkExpr("""
            1+1
        """.trimIndent(), """
ROOT@0..3
  INFIX_EXPR@0..3
    LITERAL@0..1
      NUMBER@0..1 "1"
    PLUS@1..2 "+"
    LITERAL@2..3
      NUMBER@2..3 "1"
        """.trimIndent())
    }

    @Test
    fun `left associativity`() {
        checkExpr("""
            1+2+3+4
        """.trimIndent(), """
ROOT@0..7
  INFIX_EXPR@0..7
    INFIX_EXPR@0..5
      INFIX_EXPR@0..3
        LITERAL@0..1
          NUMBER@0..1 "1"
        PLUS@1..2 "+"
        LITERAL@2..3
          NUMBER@2..3 "2"
      PLUS@3..4 "+"
      LITERAL@4..5
        NUMBER@4..5 "3"
    PLUS@5..6 "+"
    LITERAL@6..7
      NUMBER@6..7 "4"
        """.trimIndent())
    }

    @Test
    fun `arithmetic precedence`() {
        checkExpr("""
            1+2*3
        """.trimIndent(), """
ROOT@0..5
  INFIX_EXPR@0..5
    LITERAL@0..1
      NUMBER@0..1 "1"
    PLUS@1..2 "+"
    INFIX_EXPR@2..5
      LITERAL@2..3
        NUMBER@2..3 "2"
      STAR@3..4 "*"
      LITERAL@4..5
        NUMBER@4..5 "3"
        """.trimIndent())
    }

    @Test
    fun `missing rhs`() {
        checkExpr("""
            1+
        """.trimIndent(), """
ROOT@0..2
  INFIX_EXPR@0..2
    LITERAL@0..1
      NUMBER@0..1 "1"
    PLUS@1..2 "+"
error at 2..2: expected [NUMBER, STRING, TRUE, FALSE, IDENT, LPAREN, MINUS], found null
        """.trimIndent())
    }

}