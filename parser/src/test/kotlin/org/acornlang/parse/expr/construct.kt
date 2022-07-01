package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class ConstructExprTest {

    @Test
    fun `simple construct`() {
        checkExpr("""
            Foo {}
        """.trimIndent(), """
CONSTRUCT@0..6
  VAR_REF@0..4
    IDENT@0..3 "Foo"
    WHITESPACE@3..4 " "
  LBRACE@4..5 "{"
  CONSTRUCT_FIELD_LIST@5..5
  RBRACE@5..6 "}"
        """.trimIndent())
    }

    @Test
    fun `single arg construct`() {
        checkExpr("""
            Foo {
                a: 1
            }
        """.trimIndent(), """
CONSTRUCT@0..16
  VAR_REF@0..4
    IDENT@0..3 "Foo"
    WHITESPACE@3..4 " "
  LBRACE@4..5 "{"
  WHITESPACE@5..10 "\n    "
  CONSTRUCT_FIELD_LIST@10..15
    CONSTRUCT_FIELD@10..15
      IDENT@10..11 "a"
      COLON@11..12 ":"
      WHITESPACE@12..13 " "
      LITERAL@13..15
        NUMBER@13..14 "1"
        WHITESPACE@14..15 "\n"
  RBRACE@15..16 "}"
        """.trimIndent())
    }

    @Test
    fun `multi arg construct`() {
        checkExpr("""
            Foo {
                a: 1,
                b: 2,
            }
        """.trimIndent(), """
CONSTRUCT@0..27
  VAR_REF@0..4
    IDENT@0..3 "Foo"
    WHITESPACE@3..4 " "
  LBRACE@4..5 "{"
  WHITESPACE@5..10 "\n    "
  CONSTRUCT_FIELD_LIST@10..26
    CONSTRUCT_FIELD@10..14
      IDENT@10..11 "a"
      COLON@11..12 ":"
      WHITESPACE@12..13 " "
      LITERAL@13..14
        NUMBER@13..14 "1"
    COMMA@14..15 ","
    WHITESPACE@15..20 "\n    "
    CONSTRUCT_FIELD@20..24
      IDENT@20..21 "b"
      COLON@21..22 ":"
      WHITESPACE@22..23 " "
      LITERAL@23..24
        NUMBER@23..24 "2"
    COMMA@24..25 ","
    WHITESPACE@25..26 "\n"
  RBRACE@26..27 "}"
        """.trimIndent())
    }

}