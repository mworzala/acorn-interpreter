package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class InfixExprTest {

    @Test
    fun `simple add`() {
        checkExpr("""
            1+1
        """.trimIndent(), """
INFIX_EXPR@0..3
  LITERAL@0..1
    NUMBER@0..1 "1"
  PLUS@1..2 "+"
  LITERAL@2..3
    NUMBER@2..3 "1"
        """.trimIndent())
    }

    @Test
    fun `left associativity`() {
        checkExpr("""
            1+2+3+4
        """.trimIndent(), """
INFIX_EXPR@0..7
  INFIX_EXPR@0..5
    INFIX_EXPR@0..3
      LITERAL@0..1
        NUMBER@0..1 "1"
      PLUS@1..2 "+"
      LITERAL@2..3
        NUMBER@2..3 "2"
    PLUS@3..4 "+"
    LITERAL@4..5
      NUMBER@4..5 "3"
  PLUS@5..6 "+"
  LITERAL@6..7
    NUMBER@6..7 "4"
        """.trimIndent())
    }

    @Test
    fun `arithmetic precedence`() {
        checkExpr("""
            1+2*3
        """.trimIndent(), """
INFIX_EXPR@0..5
  LITERAL@0..1
    NUMBER@0..1 "1"
  PLUS@1..2 "+"
  INFIX_EXPR@2..5
    LITERAL@2..3
      NUMBER@2..3 "2"
    STAR@3..4 "*"
    LITERAL@4..5
      NUMBER@4..5 "3"
        """.trimIndent())
    }

    @Test
    fun `comparison operators`() {
        checkExpr("""
            1 == 2 != 3 < 4 <= 5 > 6 >= 7
        """.trimIndent(), """
INFIX_EXPR@0..29
  INFIX_EXPR@0..25
    INFIX_EXPR@0..21
      INFIX_EXPR@0..16
        INFIX_EXPR@0..12
          INFIX_EXPR@0..7
            LITERAL@0..2
              NUMBER@0..1 "1"
              WHITESPACE@1..2 " "
            EQEQ@2..4 "=="
            WHITESPACE@4..5 " "
            LITERAL@5..7
              NUMBER@5..6 "2"
              WHITESPACE@6..7 " "
          BANGEQ@7..9 "!="
          WHITESPACE@9..10 " "
          LITERAL@10..12
            NUMBER@10..11 "3"
            WHITESPACE@11..12 " "
        LT@12..13 "<"
        WHITESPACE@13..14 " "
        LITERAL@14..16
          NUMBER@14..15 "4"
          WHITESPACE@15..16 " "
      LTEQ@16..18 "<="
      WHITESPACE@18..19 " "
      LITERAL@19..21
        NUMBER@19..20 "5"
        WHITESPACE@20..21 " "
    GT@21..22 ">"
    WHITESPACE@22..23 " "
    LITERAL@23..25
      NUMBER@23..24 "6"
      WHITESPACE@24..25 " "
  GTEQ@25..27 ">="
  WHITESPACE@27..28 " "
  LITERAL@28..29
    NUMBER@28..29 "7"
        """.trimIndent())
    }

    @Test
    fun `logical operators`() {
        checkExpr("""
            1 && 2 || 3
        """.trimIndent(), """
INFIX_EXPR@0..11
  INFIX_EXPR@0..7
    LITERAL@0..2
      NUMBER@0..1 "1"
      WHITESPACE@1..2 " "
    AMPAMP@2..4 "&&"
    WHITESPACE@4..5 " "
    LITERAL@5..7
      NUMBER@5..6 "2"
      WHITESPACE@6..7 " "
  PIPEPIPE@7..9 "||"
  WHITESPACE@9..10 " "
  LITERAL@10..11
    NUMBER@10..11 "3"
        """.trimIndent())
    }

    @Test
    fun `infix precedence`() {
        checkExpr("""
            1 + 2 < 3 - 4 || 5 > 6
        """.trimIndent(), """
INFIX_EXPR@0..22
  INFIX_EXPR@0..14
    INFIX_EXPR@0..6
      LITERAL@0..2
        NUMBER@0..1 "1"
        WHITESPACE@1..2 " "
      PLUS@2..3 "+"
      WHITESPACE@3..4 " "
      LITERAL@4..6
        NUMBER@4..5 "2"
        WHITESPACE@5..6 " "
    LT@6..7 "<"
    WHITESPACE@7..8 " "
    INFIX_EXPR@8..14
      LITERAL@8..10
        NUMBER@8..9 "3"
        WHITESPACE@9..10 " "
      MINUS@10..11 "-"
      WHITESPACE@11..12 " "
      LITERAL@12..14
        NUMBER@12..13 "4"
        WHITESPACE@13..14 " "
  PIPEPIPE@14..16 "||"
  WHITESPACE@16..17 " "
  INFIX_EXPR@17..22
    LITERAL@17..19
      NUMBER@17..18 "5"
      WHITESPACE@18..19 " "
    GT@19..20 ">"
    WHITESPACE@20..21 " "
    LITERAL@21..22
      NUMBER@21..22 "6"
        """.trimIndent())
    }

    @Test
    fun `missing rhs`() {
        checkExpr("""
            1+
        """.trimIndent(), """
INFIX_EXPR@0..2
  LITERAL@0..1
    NUMBER@0..1 "1"
  PLUS@1..2 "+"
error at 2..2: expected [FN, ENUM, STRUCT, UNION, SPEC, LBRACE, LPAREN, LBRACKET, IF, WHILE, MINUS, BANG, AMP, NUMBER, STRING, TRUE, FALSE, IDENT, INTRINSIC_IDENT], found null
        """.trimIndent())
    }

}