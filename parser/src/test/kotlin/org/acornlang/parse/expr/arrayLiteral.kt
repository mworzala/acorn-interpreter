package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class ArrayLiteralExprTest {

    @Test
    fun `simple array`() {
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

}