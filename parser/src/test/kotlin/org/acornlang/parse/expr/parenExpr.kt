package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class ParenExprTest {

    @Test
    fun `single paren`() {
        checkExpr("""
            (1)
        """.trimIndent(), """
PAREN_EXPR@0..3
  LPAREN@0..1 "("
  LITERAL@1..2
    NUMBER@1..2 "1"
  RPAREN@2..3 ")"
        """.trimIndent())
    }

    @Test
    fun `nested parens`() {
        checkExpr("""
            ((((1))))
        """.trimIndent(), """
PAREN_EXPR@0..9
  LPAREN@0..1 "("
  PAREN_EXPR@1..8
    LPAREN@1..2 "("
    PAREN_EXPR@2..7
      LPAREN@2..3 "("
      PAREN_EXPR@3..6
        LPAREN@3..4 "("
        LITERAL@4..5
          NUMBER@4..5 "1"
        RPAREN@5..6 ")"
      RPAREN@6..7 ")"
    RPAREN@7..8 ")"
  RPAREN@8..9 ")"
        """.trimIndent())
    }

    @Test
    fun `paren resets precedence`() {
        checkExpr("""
            (1+2)*3
        """.trimIndent(), """
INFIX_EXPR@0..7
  PAREN_EXPR@0..5
    LPAREN@0..1 "("
    INFIX_EXPR@1..4
      LITERAL@1..2
        NUMBER@1..2 "1"
      PLUS@2..3 "+"
      LITERAL@3..4
        NUMBER@3..4 "2"
    RPAREN@4..5 ")"
  STAR@5..6 "*"
  LITERAL@6..7
    NUMBER@6..7 "3"
        """.trimIndent())
    }

    @Test
    fun `unclosed parens`() {
        checkExpr("""
            (1+2
        """.trimIndent(), """
PAREN_EXPR@0..4
  LPAREN@0..1 "("
  INFIX_EXPR@1..4
    LITERAL@1..2
      NUMBER@1..2 "1"
    PLUS@2..3 "+"
    LITERAL@3..4
      NUMBER@3..4 "2"
error at 4..4: expected [PLUS, MINUS, STAR, SLASH, EQEQ, BANGEQ, LT, LTEQ, GT, GTEQ, AMPAMP, PIPEPIPE, AMP, EQ, DOT, LBRACKET, LPAREN, LBRACE, COMMA, RPAREN], found null
        """.trimIndent())
    }

}