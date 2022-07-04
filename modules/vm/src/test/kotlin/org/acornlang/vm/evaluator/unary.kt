package org.acornlang.vm.evaluator

import org.acornlang.vm.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestUnaryEval {


    @Test
    fun `unary negate`() {
        val source = """
            -1
        """.trimIndent()

        val value = evalExpr(source)
        val intValue = value.assert<IntValue>()
        assertEquals(-1, intValue.value)
    }

    @Test
    fun `unary not`() {
        val source = """
            !false
        """.trimIndent()

        val value = evalExpr(source)
        val intValue = value.assert<BoolValue>()
        assertEquals(true, intValue.value)
    }

}