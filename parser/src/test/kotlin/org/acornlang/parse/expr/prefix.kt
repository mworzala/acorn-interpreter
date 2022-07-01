package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class PrefixExprTest {

    @Test
    fun `simple minus`() {
        checkExpr("""
            -1
        """.trimIndent(), """
PREFIX_EXPR@0..2
  MINUS@0..1 "-"
  LITERAL@1..2
    NUMBER@1..2 "1"
        """.trimIndent())
    }

    @Test
    fun `nested minus`() {
        checkExpr("""
            --1
        """.trimIndent(), """
PREFIX_EXPR@0..3
  MINUS@0..1 "-"
  PREFIX_EXPR@1..3
    MINUS@1..2 "-"
    LITERAL@2..3
      NUMBER@2..3 "1"
        """.trimIndent())
    }

    @Test
    fun `simple not`() {
        checkExpr("""
            !1
        """.trimIndent(), """
PREFIX_EXPR@0..2
  BANG@0..1 "!"
  LITERAL@1..2
    NUMBER@1..2 "1"
        """.trimIndent())
    }

    @Test
    fun `not precedence`() {
        checkExpr("""
            3 < !1 + 2
        """.trimIndent(), """
INFIX_EXPR@0..10
  LITERAL@0..2
    NUMBER@0..1 "3"
    WHITESPACE@1..2 " "
  LT@2..3 "<"
  WHITESPACE@3..4 " "
  INFIX_EXPR@4..10
    PREFIX_EXPR@4..7
      BANG@4..5 "!"
      LITERAL@5..7
        NUMBER@5..6 "1"
        WHITESPACE@6..7 " "
    PLUS@7..8 "+"
    WHITESPACE@8..9 " "
    LITERAL@9..10
      NUMBER@9..10 "2"
        """.trimIndent())
    }

}