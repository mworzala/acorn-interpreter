package org.acornlang.parse.container

import org.acornlang.parse.checkContainerItem
import org.junit.jupiter.api.Test

class TestNamedStructDecl {

    @Test
    fun `empty named struct`() {
        checkContainerItem("""
            struct Foo {}
        """.trimIndent(), """
NAMED_STRUCT_DECL@0..13
  STRUCT@0..6 "struct"
  WHITESPACE@6..7 " "
  IDENT@7..10 "Foo"
  WHITESPACE@10..11 " "
  LBRACE@11..12 "{"
  STRUCT_FIELD_LIST@12..12
  RBRACE@12..13 "}"
        """.trimIndent())
    }

    @Test
    fun `struct single field`() {
        checkContainerItem("""
            struct Foo {
                bar: i32,
            }
        """.trimIndent(), """
NAMED_STRUCT_DECL@0..28
  STRUCT@0..6 "struct"
  WHITESPACE@6..7 " "
  IDENT@7..10 "Foo"
  WHITESPACE@10..11 " "
  LBRACE@11..12 "{"
  WHITESPACE@12..17 "\n    "
  STRUCT_FIELD_LIST@17..27
    STRUCT_FIELD@17..27
      IDENT@17..20 "bar"
      COLON@20..21 ":"
      WHITESPACE@21..22 " "
      VAR_REF@22..25
        IDENT@22..25 "i32"
      COMMA@25..26 ","
      WHITESPACE@26..27 "\n"
  RBRACE@27..28 "}"
        """.trimIndent())
    }

    @Test
    fun `struct multi field`() {
        checkContainerItem("""
            struct Point {
                x: i32,
                y: i32,
            }
        """.trimIndent(), """
NAMED_STRUCT_DECL@0..40
  STRUCT@0..6 "struct"
  WHITESPACE@6..7 " "
  IDENT@7..12 "Point"
  WHITESPACE@12..13 " "
  LBRACE@13..14 "{"
  WHITESPACE@14..19 "\n    "
  STRUCT_FIELD_LIST@19..39
    STRUCT_FIELD@19..31
      IDENT@19..20 "x"
      COLON@20..21 ":"
      WHITESPACE@21..22 " "
      VAR_REF@22..25
        IDENT@22..25 "i32"
      COMMA@25..26 ","
      WHITESPACE@26..31 "\n    "
    STRUCT_FIELD@31..39
      IDENT@31..32 "y"
      COLON@32..33 ":"
      WHITESPACE@33..34 " "
      VAR_REF@34..37
        IDENT@34..37 "i32"
      COMMA@37..38 ","
      WHITESPACE@38..39 "\n"
  RBRACE@39..40 "}"
        """.trimIndent())
    }

    @Test
    fun `struct with container member before and after`() {
        checkContainerItem("""
            struct Color {
                const foo = 1;
                
                blah: i32,
                
                const bar = 2;
            }
        """.trimIndent(), """
NAMED_STRUCT_DECL@0..79
  STRUCT@0..6 "struct"
  WHITESPACE@6..7 " "
  IDENT@7..12 "Color"
  WHITESPACE@12..13 " "
  LBRACE@13..14 "{"
  WHITESPACE@14..19 "\n    "
  CONST_DECL@19..43
    CONST@19..24 "const"
    WHITESPACE@24..25 " "
    IDENT@25..28 "foo"
    WHITESPACE@28..29 " "
    EQ@29..30 "="
    WHITESPACE@30..31 " "
    LITERAL@31..32
      NUMBER@31..32 "1"
    SEMICOLON@32..33 ";"
    WHITESPACE@33..43 "\n    \n    "
  STRUCT_FIELD_LIST@43..63
    STRUCT_FIELD@43..63
      IDENT@43..47 "blah"
      COLON@47..48 ":"
      WHITESPACE@48..49 " "
      VAR_REF@49..52
        IDENT@49..52 "i32"
      COMMA@52..53 ","
      WHITESPACE@53..63 "\n    \n    "
  CONST_DECL@63..78
    CONST@63..68 "const"
    WHITESPACE@68..69 " "
    IDENT@69..72 "bar"
    WHITESPACE@72..73 " "
    EQ@73..74 "="
    WHITESPACE@74..75 " "
    LITERAL@75..76
      NUMBER@75..76 "2"
    SEMICOLON@76..77 ";"
    WHITESPACE@77..78 "\n"
  RBRACE@78..79 "}"
        """.trimIndent())
    }

}

class TestStructDecl {

    @Test
    fun `struct as expr`() {
        checkContainerItem("""
            const Foo = struct {
                bar: i32,
            };
        """.trimIndent(), """
CONST_DECL@0..37
  CONST@0..5 "const"
  WHITESPACE@5..6 " "
  IDENT@6..9 "Foo"
  WHITESPACE@9..10 " "
  EQ@10..11 "="
  WHITESPACE@11..12 " "
  STRUCT_DECL@12..36
    STRUCT@12..18 "struct"
    WHITESPACE@18..19 " "
    LBRACE@19..20 "{"
    WHITESPACE@20..25 "\n    "
    STRUCT_FIELD_LIST@25..35
      STRUCT_FIELD@25..35
        IDENT@25..28 "bar"
        COLON@28..29 ":"
        WHITESPACE@29..30 " "
        VAR_REF@30..33
          IDENT@30..33 "i32"
        COMMA@33..34 ","
        WHITESPACE@34..35 "\n"
    RBRACE@35..36 "}"
  SEMICOLON@36..37 ";"
        """.trimIndent())
    }
}
