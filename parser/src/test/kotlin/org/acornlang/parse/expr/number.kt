package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class ExprTest {

    @Test
    fun `single number`() {
        checkExpr("""
            123
        """.trimIndent(), """
ROOT@0..3
  NUMBER@0..3 "123"
        """.trimIndent())
    }

}