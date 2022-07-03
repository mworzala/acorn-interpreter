package org.acornlang.parse

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class RegressionTest {

    //todo not sure the best solution here. it reaches the = which is in the error set, but then it cannot pass the `=`. I think
    //     i need to introduce a recovery set stack where `=` can be pushed on when parsing a let stmt, then removed afterwards.
    @Test
    fun `infinite loop with wrong word`() {
        checkExpr("""
            {
                val Num = enum {
                    one, two, three,
                };
            }
        """.trimIndent(), """
BLOCK@0..56
  LBRACE@0..1 "{"
  WHITESPACE@1..6 "\n    "
  VAR_REF@6..10
    IDENT@6..9 "val"
    WHITESPACE@9..10 " "
  ERROR@10..14
    IDENT@10..13 "Num"
    WHITESPACE@13..14 " "
  ERROR@14..16
    EQ@14..15 "="
    WHITESPACE@15..16 " "
  ENUM_DECL@16..53
    ENUM@16..20 "enum"
    WHITESPACE@20..21 " "
    LBRACE@21..22 "{"
    WHITESPACE@22..31 "\n        "
    ENUM_CASE_LIST@31..52
      ENUM_CASE@31..36
        IDENT@31..34 "one"
        COMMA@34..35 ","
        WHITESPACE@35..36 " "
      ENUM_CASE@36..41
        IDENT@36..39 "two"
        COMMA@39..40 ","
        WHITESPACE@40..41 " "
      ENUM_CASE@41..52
        IDENT@41..46 "three"
        COMMA@46..47 ","
        WHITESPACE@47..52 "\n    "
    RBRACE@52..53 "}"
  SEMICOLON@53..54 ";"
  WHITESPACE@54..55 "\n"
  RBRACE@55..56 "}"
error at 10..13: expected [SEMICOLON], found IDENT
error at 14..15: expected [RBRACE, LET, RETURN, BREAK, CONTINUE, FN, ENUM, STRUCT, UNION, SPEC, LBRACE, LPAREN, LBRACKET, IF, WHILE, MINUS, BANG, AMP, NUMBER, STRING, TRUE, FALSE, IDENT, INTRINSIC_IDENT], found EQ
        """.trimIndent())
    }

    @Test
    fun `missing comma in enum decl`() {
        checkExpr("""
            {
                let Num = enum {
                    one, two, three
                };
                Num.two
            }
        """.trimIndent(), """
BLOCK@0..67
  LBRACE@0..1 "{"
  WHITESPACE@1..6 "\n    "
  VAR_DECL@6..52
    LET@6..9 "let"
    WHITESPACE@9..10 " "
    IDENT@10..13 "Num"
    WHITESPACE@13..14 " "
    EQ@14..15 "="
    WHITESPACE@15..16 " "
    ENUM_DECL@16..52
      ENUM@16..20 "enum"
      WHITESPACE@20..21 " "
      LBRACE@21..22 "{"
      WHITESPACE@22..31 "\n        "
      ENUM_CASE_LIST@31..51
        ENUM_CASE@31..36
          IDENT@31..34 "one"
          COMMA@34..35 ","
          WHITESPACE@35..36 " "
        ENUM_CASE@36..41
          IDENT@36..39 "two"
          COMMA@39..40 ","
          WHITESPACE@40..41 " "
        ENUM_CASE@41..51
          IDENT@41..46 "three"
          WHITESPACE@46..51 "\n    "
      RBRACE@51..52 "}"
  SEMICOLON@52..53 ";"
  WHITESPACE@53..58 "\n    "
  IMPLICIT_RETURN@58..66
    MEMBER_ACCESS@58..66
      VAR_REF@58..61
        IDENT@58..61 "Num"
      DOT@61..62 "."
      VAR_REF@62..66
        IDENT@62..65 "two"
        WHITESPACE@65..66 "\n"
  RBRACE@66..67 "}"
error at 51..52: expected [COMMA], found RBRACE
        """.trimIndent())
    }

}