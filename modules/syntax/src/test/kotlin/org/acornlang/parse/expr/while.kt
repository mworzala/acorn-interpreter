package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class WhileExprTest {

    @Test
    fun `empty while`() {
        checkExpr("""
            while true {}
        """.trimIndent(), """
WHILE_EXPR@0..13
  WHILE@0..5 "while"
  WHITESPACE@5..6 " "
  LITERAL@6..11
    BOOL@6..10 "true"
    WHITESPACE@10..11 " "
  BLOCK@11..13
    LBRACE@11..12 "{"
    RBRACE@12..13 "}"
        """.trimIndent())
    }

    @Test
    fun `while with complex condition and body`() {
        checkExpr("""
            while 1 < 2 {
                3 + 4
            }
        """.trimIndent(), """
WHILE_EXPR@0..25
  WHILE@0..5 "while"
  WHITESPACE@5..6 " "
  INFIX_EXPR@6..12
    LITERAL@6..8
      NUMBER@6..7 "1"
      WHITESPACE@7..8 " "
    LT@8..9 "<"
    WHITESPACE@9..10 " "
    LITERAL@10..12
      NUMBER@10..11 "2"
      WHITESPACE@11..12 " "
  BLOCK@12..25
    LBRACE@12..13 "{"
    WHITESPACE@13..18 "\n    "
    RET_INLINE@18..24
      INFIX_EXPR@18..24
        LITERAL@18..20
          NUMBER@18..19 "3"
          WHITESPACE@19..20 " "
        PLUS@20..21 "+"
        WHITESPACE@21..22 " "
        LITERAL@22..24
          NUMBER@22..23 "4"
          WHITESPACE@23..24 "\n"
    RBRACE@24..25 "}"
        """.trimIndent())
    }

}