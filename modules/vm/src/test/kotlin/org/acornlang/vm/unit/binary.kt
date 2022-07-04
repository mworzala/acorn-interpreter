package org.acornlang.vm.unit

import org.acornlang.vm.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestBinaryEval {

    @Test
    fun testBinaryArithmetic() {
        val source = """
            1 + 2 * 3 / 3 - 1
        """.trimIndent()

        val value = evalExpr(source)
        val intValue = value.assert<IntValue>()
        assertEquals(2, intValue.value)
    }

    @Test
    fun `binary value comparison`() {
        val source = """
            1 == "test"
        """.trimIndent()

        val value = evalExpr(source)
        val intValue = value.assert<BoolValue>()
        assertEquals(false, intValue.value)
    }

    @Test
    fun `binary value comparison true`() {
        val source = """
            "test" == "test"
        """.trimIndent()

        val value = evalExpr(source)
        val intValue = value.assert<BoolValue>()
        assertEquals(true, intValue.value)
    }

    @Test
    fun `binary int comparison`() {
        val source = """
            1 < 2
        """.trimIndent()

        val value = evalExpr(source)
        val intValue = value.assert<BoolValue>()
        assertEquals(true, intValue.value)
    }

    @Test
    fun `binary logical`() {
        val source = """
            true || false && true
        """.trimIndent()

        val value = evalExpr(source)
        val intValue = value.assert<BoolValue>()
        assertEquals(true, intValue.value)
    }

}