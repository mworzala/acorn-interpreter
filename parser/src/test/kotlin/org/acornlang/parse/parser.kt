package org.acornlang.parse

import org.acornlang.parse.impl.CompletedMarker
import org.acornlang.parse.impl.Parser
import org.acornlang.parse.rule.expr
import org.acornlang.parse.rule.module
import org.junit.jupiter.api.Assertions.assertEquals

internal fun check(input: String, expected: String, parseFn: Parser.() -> CompletedMarker?) {
    val result = parse(input, parseFn)

    val actual = result.node.toDebugString()
    val errorStr = result.errors.joinToString("\n") { it.toString() } + '\n'

    // Newlines make the output a but more readable
    assertEquals('\n' + expected + '\n', '\n' + actual + (if (result.errors.isNotEmpty()) errorStr else ""))
}

fun checkExpr(input: String, expected: String) = check(input, expected, Parser::expr)
fun checkStmt(input: String, expected: String) = check(input, expected, Parser::stmt)
fun checkModule(input: String, expected: String) = check(input, expected, Parser::module)

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