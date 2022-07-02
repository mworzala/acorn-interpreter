package org.acornlang.parse

import org.acornlang.common.text.Diff
import org.acornlang.parse.impl.CompletedMarker
import org.acornlang.parse.impl.Parser
import org.acornlang.parse.rule.containerItem
import org.acornlang.parse.rule.expr
import org.acornlang.parse.rule.module
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse

internal fun check(input: String, expected: String, parseFn: Parser.(Boolean) -> CompletedMarker?) {
    try {
        val result = parse(input, parseFn)

        val actualStr = result.node.toDebugString()
        val errorStr = result.errors.joinToString("\n") { it.toString() } + '\n'

        val expected = '\n' + expected + '\n'
        val actual = '\n' + actualStr + (if (result.errors.isNotEmpty()) errorStr else "")

        if (expected != actual) {
            Diff.emitInlineDiff(expected, actual)
        }

        assertEquals(expected, actual)
    } catch (e: Throwable) {
        e.printStackTrace()
        assertFalse(true)
    }
}

fun checkExpr(@Language("acorn", prefix = "fn a() {", suffix = "}") input: String, expected: String) = check(input, expected, Parser::expr)
fun checkStmt(input: String, expected: String) = check(input, expected, Parser::stmt)
fun checkContainerItem(@Language("acorn") input: String, expected: String) = check(input, expected, Parser::containerItem)
fun checkModule(@Language("acorn") input: String, expected: String) = check(input, expected, Parser::module)

class ParserTest {


    //todo needs parseModule
//    @Test
//    fun `parse nothing`() {
//        checkStmt("""
//
//        """.trimIndent(), """
//ROOT@0..0
//        """.trimIndent())
//    }

}