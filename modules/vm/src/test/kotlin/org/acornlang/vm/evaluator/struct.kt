package org.acornlang.vm.evaluator

import org.acornlang.vm.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TestStructEval {

    @Test
    fun `define basic struct type`() {
        val source = """
            {
                struct {
                    a: i32,
                    b: i32,
                }
            }
        """.trimIndent()

        val rawValue = evalExpr(source)
        val value = rawValue.assert<TypeValue>()
        val structType = value.value as StructType
        assertEquals(listOf("a", "b"), structType.memberNames)
    }

    @Test
    fun `construct basic struct type`() {
        val source = """
            {
                let Point = struct {
                    x: i32,
                    y: i32,
                };
                Point { x: 1, y: 2 }
            }
        """.trimIndent()

        val rawValue = evalExpr(source)
        val value = rawValue.assert<StructValue>()
        assertEquals(listOf<Long>(1, 2), value.values.map { (it as IntValue).value })
    }

    @Test
    fun `struct with type constant`() {
        val source = """
            {
                let Point = struct {
                    x: i32,
                    y: i32,
                    
                    const some_value = 1;
                };
                Point.some_value
            }
        """.trimIndent()

        val rawValue = evalExpr(source)
        val value = rawValue.assert<IntValue>()
        assertEquals(1, value.value)
    }

}