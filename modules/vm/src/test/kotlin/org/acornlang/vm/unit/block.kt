package org.acornlang.vm.unit

import org.acornlang.vm.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestBlockEval {

    @Test
    fun `empty block`() {
        val source = """
            {}
        """.trimIndent()

        val rawValue = evalExpr(source)
        val value = rawValue.assert<TupleValue>()
        assertEquals(0, value.length)
    }

    @Test
    fun `block with implicit return`() {
        val source = """
            {
                1
            }
        """.trimIndent()

        val rawValue = evalExpr(source)
        val value = rawValue.assert<IntValue>()
        assertEquals(1, value.value)
    }

}