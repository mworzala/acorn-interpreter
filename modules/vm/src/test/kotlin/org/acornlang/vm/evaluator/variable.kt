package org.acornlang.vm.evaluator

import org.acornlang.vm.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TestVariableEval {

    @Test
    fun `basic define reference`() {
        val source = """
            {
                let x = 1;
                x
            }
        """.trimIndent()

        val rawValue = evalExpr(source)
        val value = rawValue.assert<IntValue>()
        assertEquals(1, value.value)
    }

    @Test
    fun `basic mutable value`() {
        val source = """
            {
                let mut x = 1;
                x = 2;
                x
            }
        """.trimIndent()

        val rawValue = evalExpr(source)
        val value = rawValue.assert<IntValue>()
        assertEquals(2, value.value)
    }

    @Test
    fun `mutable immutable value`() {
        val source = """
            {
                let x = 1;
                x = 2;
                x
            }
        """.trimIndent()

        //todo better errors :(
        assertThrows<RuntimeException> {
            evalExpr(source)
        }
    }

}