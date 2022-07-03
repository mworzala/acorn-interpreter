package org.acornlang.vm.evaluator

import org.acornlang.vm.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestEnumEval {

    @Test
    fun `empty block`() {
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

}