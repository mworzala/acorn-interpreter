package org.acornlang.parse

import org.acornlang.Parser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private fun check(source: String, expected: String) {
    val parser = Parser(source)
    val node = parser.stmt()

    assertEquals(expected + "\n", node.stringify())
}

class Stmt {
    @Test
    fun `basic let`() {
        check("""
            let a: i32 = 1
        """.trimIndent(), """
            LET "a"
              TYPE "i32"
              INT "1"
        """.trimIndent())
    }
    @Test
    fun `let ptr and expr`() {
        check("""
            let a: *i32 = 1 + 1
        """.trimIndent(), """
            LET "a"
              PTR_TYPE
                TYPE "i32"
              BINARY PLUS
                INT "1"
                INT "1"
        """.trimIndent())
    }

}