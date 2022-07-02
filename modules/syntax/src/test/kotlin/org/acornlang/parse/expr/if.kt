package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class IfExprTest {

    @Test
    fun `empty if`() {
        checkExpr("""
            if true {}
        """.trimIndent(), """
IF_EXPR@0..10
  IF@0..2 "if"
  WHITESPACE@2..3 " "
  LITERAL@3..8
    BOOL@3..7 "true"
    WHITESPACE@7..8 " "
  BLOCK@8..10
    LBRACE@8..9 "{"
    RBRACE@9..10 "}"
        """.trimIndent())
    }

    @Test
    fun `if else block`() {
        checkExpr("""
            if true {} else {}
        """.trimIndent(), """
IF_EXPR@0..18
  IF@0..2 "if"
  WHITESPACE@2..3 " "
  LITERAL@3..8
    BOOL@3..7 "true"
    WHITESPACE@7..8 " "
  BLOCK@8..11
    LBRACE@8..9 "{"
    RBRACE@9..10 "}"
    WHITESPACE@10..11 " "
  ELSE@11..15 "else"
  WHITESPACE@15..16 " "
  BLOCK@16..18
    LBRACE@16..17 "{"
    RBRACE@17..18 "}"
        """.trimIndent())
    }

    @Test
    fun `if else if`() {
        checkExpr("""
            if true {} else if false {}
        """.trimIndent(), """
IF_EXPR@0..27
  IF@0..2 "if"
  WHITESPACE@2..3 " "
  LITERAL@3..8
    BOOL@3..7 "true"
    WHITESPACE@7..8 " "
  BLOCK@8..11
    LBRACE@8..9 "{"
    RBRACE@9..10 "}"
    WHITESPACE@10..11 " "
  ELSE@11..15 "else"
  WHITESPACE@15..16 " "
  IF_EXPR@16..27
    IF@16..18 "if"
    WHITESPACE@18..19 " "
    LITERAL@19..25
      BOOL@19..24 "false"
      WHITESPACE@24..25 " "
    BLOCK@25..27
      LBRACE@25..26 "{"
      RBRACE@26..27 "}"
        """.trimIndent())
    }

}