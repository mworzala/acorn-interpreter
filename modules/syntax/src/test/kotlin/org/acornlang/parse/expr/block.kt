package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class BlockTest {

    @Test
    fun `empty block`() {
        checkExpr("""
            {}
        """.trimIndent(), """
BLOCK@0..2
  LBRACE@0..1 "{"
  RBRACE@1..2 "}"
        """.trimIndent())
    }

    @Test
    fun `block with single expr`() {
        checkExpr("""
            {1;}
        """.trimIndent(), """
BLOCK@0..4
  LBRACE@0..1 "{"
  LITERAL@1..2
    NUMBER@1..2 "1"
  SEMICOLON@2..3 ";"
  RBRACE@3..4 "}"
        """.trimIndent())
    }

    @Test
    fun `block with single expr inline return`() {
        checkExpr("""
            {1}
        """.trimIndent(), """
BLOCK@0..3
  LBRACE@0..1 "{"
  IMPLICIT_RETURN@1..2
    LITERAL@1..2
      NUMBER@1..2 "1"
  RBRACE@2..3 "}"
        """.trimIndent())
    }

    @Test
    fun `block with multi expr`() {
        checkExpr("""
            {1;2}
        """.trimIndent(), """
BLOCK@0..5
  LBRACE@0..1 "{"
  LITERAL@1..2
    NUMBER@1..2 "1"
  SEMICOLON@2..3 ";"
  IMPLICIT_RETURN@3..4
    LITERAL@3..4
      NUMBER@3..4 "2"
  RBRACE@4..5 "}"
        """.trimIndent())
    }

    @Test
    fun `block with missing semicolon`() {
        checkExpr("""
            {1 2}
        """.trimIndent(), """
BLOCK@0..5
  LBRACE@0..1 "{"
  LITERAL@1..3
    NUMBER@1..2 "1"
    WHITESPACE@2..3 " "
  ERROR@3..4
    NUMBER@3..4 "2"
  RBRACE@4..5 "}"
error at 3..4: expected [SEMICOLON], found NUMBER
        """.trimIndent())
    }

}

class TestBlockRegression {
    @Test
    fun `block with let`() {
        checkExpr("""
            {let foo = 1;}
        """.trimIndent(), """
BLOCK@0..14
  LBRACE@0..1 "{"
  VAR_DECL@1..12
    LET@1..4 "let"
    WHITESPACE@4..5 " "
    IDENT@5..8 "foo"
    WHITESPACE@8..9 " "
    EQ@9..10 "="
    WHITESPACE@10..11 " "
    LITERAL@11..12
      NUMBER@11..12 "1"
  SEMICOLON@12..13 ";"
  RBRACE@13..14 "}"
        """.trimIndent())
    }

}