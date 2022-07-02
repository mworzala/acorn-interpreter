package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class TupleLiteralExprTest {

    @Test
    fun `empty tuple`() {
        checkExpr("""
            ()
        """.trimIndent(), """
TUPLE_LITERAL@0..2
  LPAREN@0..1 "("
  RPAREN@1..2 ")"
        """.trimIndent())
    }

    @Test
    fun `single element tuple`() {
        checkExpr("""
            (1,)
        """.trimIndent(), """
TUPLE_LITERAL@0..4
  LPAREN@0..1 "("
  LITERAL@1..2
    NUMBER@1..2 "1"
  COMMA@2..3 ","
  RPAREN@3..4 ")"
        """.trimIndent())
    }

    @Test
    fun `multi element tuple`() {
        checkExpr("""
            (1, 2)
        """.trimIndent(), """
TUPLE_LITERAL@0..6
  LPAREN@0..1 "("
  LITERAL@1..2
    NUMBER@1..2 "1"
  COMMA@2..3 ","
  WHITESPACE@3..4 " "
  LITERAL@4..5
    NUMBER@4..5 "2"
  RPAREN@5..6 ")"
        """.trimIndent())
    }

}