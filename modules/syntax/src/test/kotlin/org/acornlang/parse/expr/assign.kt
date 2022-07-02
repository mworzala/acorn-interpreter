package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class AssignExprTest {

    @Test
    fun `simple assign`() {
        checkExpr("""
            1=1
        """.trimIndent(), """
ASSIGN@0..3
  LITERAL@0..1
    NUMBER@0..1 "1"
  EQ@1..2 "="
  LITERAL@2..3
    NUMBER@2..3 "1"
        """.trimIndent())
    }

}