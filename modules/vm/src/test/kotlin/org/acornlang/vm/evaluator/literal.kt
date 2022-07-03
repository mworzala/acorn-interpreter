package org.acornlang.vm.evaluator

import org.acornlang.vm.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestLiteralEval {

    @Test
    fun testLiteralInt() {
        val source = """
            1
        """.trimIndent()

        val value = evalExpr(source)
        val intValue = value.assert<IntValue>()
        assertEquals(1, intValue.value)
    }

    @Test
    fun testLiteralBoolTrue() {
        val source = """
            true
        """.trimIndent()

        val value = evalExpr(source)
        val bool = value.assert<BoolValue>()
        assertEquals(true, bool.value)
    }

    @Test
    fun testLiteralBoolFalse() {
        val source = """
            false
        """.trimIndent()

        val value = evalExpr(source)
        val bool = value.assert<BoolValue>()
        assertEquals(false, bool.value)
    }

    @Test
    fun testLiteralString() {
        val source = """
            "Hello, world!"
        """.trimIndent()

        val value = evalExpr(source)
        val str = value.assert<StrValue>()
        assertEquals("Hello, world!", str.value)
    }

}