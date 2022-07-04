package org.acornlang.vm.unit

import org.acornlang.vm.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestFunctionEval {

    @Test
    fun `fn decl no args`() {
        val source = """
            {
                let oneFn = fn() i32 { 1 };
                oneFn
            }
        """.trimIndent()

        val rawValue = evalExpr(source)
        rawValue.assert<NativeFnValue>()
    }

    @Test
    fun `fn call no args`() {
        val source = """
            {
                let oneFn = fn() i32 { 1 };
                oneFn()
            }
        """.trimIndent()

        val rawValue = evalExpr(source)
        val value = rawValue.assert<IntValue>()
        assertEquals(1, value.value)
    }

//    @Test
//    fun `fn call with args`() {
//        val source = """
//            {
//                let oneFn = fn() i32 { 1 };
//                oneFn()
//            }
//        """.trimIndent()
//
//        val rawValue = evalExpr(source)
//        val value = rawValue.assert<IntValue>()
//        assertEquals(1, value.value)
//    }

}