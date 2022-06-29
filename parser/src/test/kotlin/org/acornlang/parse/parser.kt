package org.acornlang.parse

import org.acornlang.ast.SyntaxKind
import org.acornlang.lexer.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

fun check(input: String, expected: String, parseFn: Parser.() -> Unit) {
    val parser = Parser(input)

    val m = parser.start()
    parser.parseFn()
    with(parser) {
        m.complete(SyntaxKind.ROOT)
    }

    val actual = Sink(parser.lexemes, parser.events).finish().toDebugString()
    // Newlines make the output a but more readable
    assertEquals('\n' + expected + '\n', '\n' + actual)
}

fun checkExpr(input: String, expected: String) = check(input, expected) {
    expr()
}

class ParserTest {


    @Test
    fun `parse nothing`() {
        checkExpr("""

        """.trimIndent(), """
ROOT@0..0
        """.trimIndent())
    }

    @Test
    fun `temp test`() {
        checkExpr("""
"abc"
        """.trimIndent(), """
ROOT@0..5
  STRUCT@0..5
    STRING@0..5 ""abc""
        """.trimIndent())
    }

    @Test
    fun `temp test 2`() {
        checkExpr("""
+ // blah 
1 2
        """.trimIndent(), """
ROOT@0..14
  MINUS@0..14
    PLUS@0..13
      PLUS@0..1 "+"
      COMMENT@1..10 " // blah "
      WHITESPACE@10..11 "\n"
      NUMBER@11..12 "1"
      WHITESPACE@12..13 " "
    NUMBER@13..14 "2"
        """.trimIndent())
    }

}