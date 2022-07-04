package org.acornlang.vm.integration

import org.acornlang.vm.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TestGenericTypes {

    @Test
    fun `test instantiate generic type`() {
        val source = """
            fn Point(T: type) type {
                struct {
                    x: T,
                    y: T,
                }
            }
            
            fn main() type {
                let PointI32 = Point(i32);
                PointI32
            }
        """.trimIndent()

        val value = evalModuleMain(source)
        val typeValue = value.assert<TypeValue>()
        val type = typeValue.value
        assertTrue(type is StructType)
        val structType = type as StructType
        assertEquals(2, structType.memberTypes.size)
        assertTrue(structType.memberTypes[0] is IntType)
        assertTrue(structType.memberTypes[1] is IntType)
    }



}