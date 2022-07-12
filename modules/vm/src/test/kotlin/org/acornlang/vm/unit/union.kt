package org.acornlang.vm.unit

import org.acornlang.vm.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestUnionEval {

    @Test
    fun `define basic union type`() {
        val source = """
            {
                union {
                    a: i32,
                    b: i32,
                }
            }
        """.trimIndent()

        val rawValue = evalExpr(source)
        val value = rawValue.assert<TypeValue>()
        val unionType = value.value as UnionType
        assertEquals(listOf("a", "b"), unionType.memberNames)
    }

}