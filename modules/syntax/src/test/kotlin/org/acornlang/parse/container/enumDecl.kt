package org.acornlang.parse.container

import org.acornlang.parse.checkContainerItem
import org.junit.jupiter.api.Test

class TestNamedEnumDecl {

    @Test
    fun `empty named enum`() {
        checkContainerItem("""
            enum Foo {}
        """.trimIndent(), """
NAMED_ENUM_DECL@0..11
  ENUM@0..4 "enum"
  WHITESPACE@4..5 " "
  IDENT@5..8 "Foo"
  WHITESPACE@8..9 " "
  LBRACE@9..10 "{"
  ENUM_CASE_LIST@10..10
  RBRACE@10..11 "}"
        """.trimIndent())
    }

    @Test
    fun `enum single case`() {
        checkContainerItem("""
            enum Color {
                red,
            }
        """.trimIndent(), """
NAMED_ENUM_DECL@0..23
  ENUM@0..4 "enum"
  WHITESPACE@4..5 " "
  IDENT@5..10 "Color"
  WHITESPACE@10..11 " "
  LBRACE@11..12 "{"
  WHITESPACE@12..17 "\n    "
  ENUM_CASE_LIST@17..22
    ENUM_CASE@17..22
      IDENT@17..20 "red"
      COMMA@20..21 ","
      WHITESPACE@21..22 "\n"
  RBRACE@22..23 "}"
        """.trimIndent())
    }

    @Test
    fun `enum multi case`() {
        checkContainerItem("""
            enum Color {
                red,
                green,
                blue,
            }
        """.trimIndent(), """
NAMED_ENUM_DECL@0..44
  ENUM@0..4 "enum"
  WHITESPACE@4..5 " "
  IDENT@5..10 "Color"
  WHITESPACE@10..11 " "
  LBRACE@11..12 "{"
  WHITESPACE@12..17 "\n    "
  ENUM_CASE_LIST@17..43
    ENUM_CASE@17..26
      IDENT@17..20 "red"
      COMMA@20..21 ","
      WHITESPACE@21..26 "\n    "
    ENUM_CASE@26..37
      IDENT@26..31 "green"
      COMMA@31..32 ","
      WHITESPACE@32..37 "\n    "
    ENUM_CASE@37..43
      IDENT@37..41 "blue"
      COMMA@41..42 ","
      WHITESPACE@42..43 "\n"
  RBRACE@43..44 "}"
        """.trimIndent())
    }

    @Test
    fun `enum with container member`() {
        checkContainerItem("""
            enum Color {
                red,
                
                const foo = 1;
            }
        """.trimIndent(), """
NAMED_ENUM_DECL@0..47
  ENUM@0..4 "enum"
  WHITESPACE@4..5 " "
  IDENT@5..10 "Color"
  WHITESPACE@10..11 " "
  LBRACE@11..12 "{"
  WHITESPACE@12..17 "\n    "
  ENUM_CASE_LIST@17..31
    ENUM_CASE@17..31
      IDENT@17..20 "red"
      COMMA@20..21 ","
      WHITESPACE@21..31 "\n    \n    "
  CONST_DECL@31..46
    CONST@31..36 "const"
    WHITESPACE@36..37 " "
    IDENT@37..40 "foo"
    WHITESPACE@40..41 " "
    EQ@41..42 "="
    WHITESPACE@42..43 " "
    LITERAL@43..44
      NUMBER@43..44 "1"
    SEMICOLON@44..45 ";"
    WHITESPACE@45..46 "\n"
  RBRACE@46..47 "}"
        """.trimIndent())
    }

}

class TestEnumDecl {

    @Test
    fun `enum as expr`() {
        checkContainerItem("""
            const Color = enum {
                red,
            };
        """.trimIndent(), """
CONST_DECL@0..32
  CONST@0..5 "const"
  WHITESPACE@5..6 " "
  IDENT@6..11 "Color"
  WHITESPACE@11..12 " "
  EQ@12..13 "="
  WHITESPACE@13..14 " "
  ENUM_DECL@14..31
    ENUM@14..18 "enum"
    WHITESPACE@18..19 " "
    LBRACE@19..20 "{"
    WHITESPACE@20..25 "\n    "
    ENUM_CASE_LIST@25..30
      ENUM_CASE@25..30
        IDENT@25..28 "red"
        COMMA@28..29 ","
        WHITESPACE@29..30 "\n"
    RBRACE@30..31 "}"
  SEMICOLON@31..32 ";"
        """.trimIndent())
    }
}
