package org.acornlang.vm.unit

import org.acornlang.vm.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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

    @Test
    fun `struct with inner fn decl`() {
        val source = """
            {
                let Vec2 = struct {
                    x: i32,
                    y: i32,
                    
                    fn length_squared(self) i32 {
                        self.x * self.x + self.y * self.y
                    }
                };
                let v = Vec2 { x: 3, y: 5 };
                v.length_squared
            }
        """.trimIndent()

        val rawValue = evalExpr(source)
        val value = rawValue.assert<NativeFnValue>()
        assertEquals(listOf("self"), value.type.paramNames)
    }

    @Test
    fun `struct with inner fn call`() {
        val source = """
            {
                let Vec2 = struct {
                    x: i32,
                    y: i32,
                    
                    fn length_squared(self) i32 {
                        self.x * self.x + self.y * self.y
                    }
                };
                let v = Vec2 { x: 3, y: 4 };
                v.length_squared()
            }
        """.trimIndent()

        val rawValue = evalExpr(source)
        val value = rawValue.assert<IntValue>()
        assertEquals(25, value.value)
    }

}