package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class LiteralExprTest {

    @Test
    fun `single number`() {
        checkExpr("""
            123
        """.trimIndent(), """
LITERAL@0..3
  NUMBER@0..3 "123"
        """.trimIndent())
    }

    @Test
    fun `single string`() {
        checkExpr("""
            "Hello, World!"
        """.trimIndent(), """
LITERAL@0..15
  STRING@0..15 ""Hello, World!""
        """.trimIndent())
    }

    @Test
    fun `single true`() {
        checkExpr("""
            true
        """.trimIndent(), """
LITERAL@0..4
  BOOL@0..4 "true"
        """.trimIndent())
    }

    @Test
    fun `single false`() {
        checkExpr("""
            false
        """.trimIndent(), """
LITERAL@0..5
  BOOL@0..5 "false"
        """.trimIndent())
    }

    @Test
    fun `intrinsic identifier`() {
        checkExpr("""
            import!
        """.trimIndent(), """
INTRINSIC@0..7
  INTRINSIC_IDENT@0..7 "import!"
        """.trimIndent())
    }

}