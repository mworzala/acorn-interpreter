package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class ArrayLiteralExprTest {

    @Test
    fun `empty array`() {
        checkExpr("""
            []
        """.trimIndent(), """
ARRAY_LITERAL@0..2
  LBRACKET@0..1 "["
  RBRACKET@1..2 "]"
        """.trimIndent())
    }

    @Test
    fun `single element array`() {
        checkExpr("""
            [1]
        """.trimIndent(), """
ARRAY_LITERAL@0..3
  LBRACKET@0..1 "["
  LITERAL@1..2
    NUMBER@1..2 "1"
  RBRACKET@2..3 "]"
        """.trimIndent())
    }

    @Test
    fun `multi element array`() {
        checkExpr("""
            [1, 2,]
        """.trimIndent(), """
ARRAY_LITERAL@0..7
  LBRACKET@0..1 "["
  LITERAL@1..2
    NUMBER@1..2 "1"
  COMMA@2..3 ","
  WHITESPACE@3..4 " "
  LITERAL@4..5
    NUMBER@4..5 "2"
  COMMA@5..6 ","
  RBRACKET@6..7 "]"
        """.trimIndent())
    }

}