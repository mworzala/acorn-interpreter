package org.acornlang.vm.unit

import org.acornlang.vm.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TestEnumEval {

    @Test
    fun `access enum case`() {
        //todo should not be allowed to put a type into a `let`, should be `const`, but that isnt supported as a stmt right now.
        val source = """
            {
                let Num = enum {
                    one, two, three,
                };
                Num.two
            }
        """.trimIndent()

        val rawValue = evalExpr(source)
        val value = rawValue.assert<EnumValue>()
        assertEquals(1, value.value)
    }

    @Test
    fun `unknown enum value`() {
        val source = """
            {
                let Num = enum {
                    one, two, three,
                };
                Num.four
            }
        """.trimIndent()

        val exception = assertThrows<RuntimeException> { evalExpr(source) }
        assertEquals("No such case 'four' in enum unnamed { one, two, three }", exception.message)
    }

}