package org.acornlang.parse

import org.acornlang.Parser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

private fun check(source: String, expected: String) {
    val parser = Parser(source)
    val node = parser.typeExpr()

    Assertions.assertEquals(expected + "\n", node.stringify())
}

class TypeExprTest {
    @Test
    fun `all literals`() {
        check("""
            a
        """.trimIndent(), """
            b
        """.trimIndent()
        )
    }
}