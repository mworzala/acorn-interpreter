package org.acornlang.parse

import org.acornlang.Parser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private fun check(source: String, expected: String) {
    val parser = Parser(source)
    val node = parser.expr()

    assertEquals(expected + "\n", node.stringify())
}

class ExprTest {
    @Test
    fun `all literals`() {
        check("""
            1 + ref + "string" + true + false
        """.trimIndent(), """
            BINARY PLUS
              BINARY PLUS
                BINARY PLUS
                  BINARY PLUS
                    INT "1"
                    REF "ref"
                  STR "string"
                BOOL "true"
              BOOL "false"
        """.trimIndent())
    }

    @Test
    fun `basic binary expression`() {
        check("""
            1 + 2 + 3
        """.trimIndent(), """
            BINARY PLUS
              BINARY PLUS
                INT "1"
                INT "2"
              INT "3"
        """.trimIndent())
    }

    @Test
    fun `precedence test`() {
        check("""
            1 + 2 * 3
        """.trimIndent(), """
            BINARY PLUS
              INT "1"
              BINARY STAR
                INT "2"
                INT "3"
        """.trimIndent())
    }

    @Test
    fun `precedence test all`() {
        check("""
            1 + 2 < 3 && 4
        """.trimIndent(), """
            BINARY PLUS
              INT "1"
              BINARY LT
                INT "2"
                BINARY AMPAMP
                  INT "3"
                  INT "4"
        """.trimIndent())
    }

    @Test
    fun `precedence test all 2`() {
        check("""
            4 && 3 < 2 + 1
        """.trimIndent(), """
            BINARY PLUS
              BINARY LT
                BINARY AMPAMP
                  INT "4"
                  INT "3"
                INT "2"
              INT "1"
        """.trimIndent())
    }

    @Test
    fun `prefix operator`() {
        check("""
            -1
        """.trimIndent(), """
            UNARY MINUS
              INT "1"
        """.trimIndent())
    }

    @Test
    fun `unary - precedence with +`() {
        check("""
            --1 + 2
        """.trimIndent(), """
            BINARY PLUS
              UNARY MINUS
                UNARY MINUS
                  INT "1"
              INT "2"
        """.trimIndent())
    }

    @Test
    fun `nested parens`() {
        check("""
            (((1)))
        """.trimIndent(), """
            INT "1"
        """.trimIndent())
    }

    @Test
    fun `empty block`() {
        check("""
            {}
        """.trimIndent(), """
            BLOCK
        """.trimIndent())
    }

    @Test
    fun `block with stmts`() {
        check("""
            {
                1;
                2;
            }
        """.trimIndent(), """
            BLOCK
              INT "1"
              INT "2"
        """.trimIndent())
    }

    @Test
    fun `return no expr`() {
        check("""
            return
        """.trimIndent(), """
            RETURN
        """.trimIndent())
    }

    @Test
    fun `return with expr`() {
        check("""
            return 1 + 2
        """.trimIndent(), """
            RETURN
              BINARY PLUS
                INT "1"
                INT "2"
        """.trimIndent())
    }

    @Test
    fun `index operator`() {
        check("""
            1[2]
        """.trimIndent(), """
            INDEX
              INT "1"
              INT "2"
        """.trimIndent())
    }

    @Test
    fun `index operator 2`() {
        check("""
            (1 + 2)[1 + 2]
        """.trimIndent(), """
            INDEX
              BINARY PLUS
                INT "1"
                INT "2"
              BINARY PLUS
                INT "1"
                INT "2"
        """.trimIndent())
    }

    @Test
    fun `empty if`() {
        check("""
            if 1 {}
        """.trimIndent(), """
            IF
              INT "1"
              BLOCK
        """.trimIndent())
    }

    @Test
    fun `empty if else`() {
        check("""
            if 1 {} else {}
        """.trimIndent(), """
            IF
              INT "1"
              BLOCK
              BLOCK
        """.trimIndent())
    }

    @Test
    fun `empty if else if`() {
        check("""
            if 1 {} else if 2 {}
        """.trimIndent(), """
            IF
              INT "1"
              BLOCK
              IF
                INT "2"
                BLOCK
        """.trimIndent())
    }

    @Test
    fun `if else with content`() {
        check("""
            if 1 {
                2;
            } else {
                3;
            }
        """.trimIndent(), """
            IF
              INT "1"
              BLOCK
                INT "2"
              BLOCK
                INT "3"
        """.trimIndent())
    }

    @Test
    fun `empty while`() {
        check("""
            while 1 {}
        """.trimIndent(), """
            WHILE
              INT "1"
              BLOCK
        """.trimIndent())
    }

    @Test
    fun `while with content`() {
        check("""
            while 1 < 2 {
                3;
            }
        """.trimIndent(), """
            WHILE
              BINARY LT
                INT "1"
                INT "2"
              BLOCK
                INT "3"
        """.trimIndent())
    }

    @Test
    fun `simple call`() {
        check("""
            a()
        """.trimIndent(), """
            CALL
              REF "a"
        """.trimIndent())
    }

    @Test
    fun `call with args`() {
        check("""
            a(1, 2 + 3)
        """.trimIndent(), """
            CALL
              REF "a"
              INT "1"
              BINARY PLUS
                INT "2"
                INT "3"
        """.trimIndent())
    }
}