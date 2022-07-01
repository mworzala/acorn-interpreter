package org.acornlang.parse.container

import org.acornlang.parse.checkContainerItem
import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class TestNamedUnionDecl {

    @Test
    fun `empty named union`() {
        checkContainerItem("""
            union Foo {}
        """.trimIndent(), """
NAMED_UNION_DECL@0..12
  UNION@0..5 "union"
  WHITESPACE@5..6 " "
  IDENT@6..9 "Foo"
  WHITESPACE@9..10 " "
  LBRACE@10..11 "{"
  UNION_MEMBER_LIST@11..11
  RBRACE@11..12 "}"
        """.trimIndent())
    }

    @Test
    fun `union single member`() {
        checkContainerItem("""
            union Foo {
                bar: i32,
            }
        """.trimIndent(), """
NAMED_UNION_DECL@0..27
  UNION@0..5 "union"
  WHITESPACE@5..6 " "
  IDENT@6..9 "Foo"
  WHITESPACE@9..10 " "
  LBRACE@10..11 "{"
  WHITESPACE@11..16 "\n    "
  UNION_MEMBER_LIST@16..26
    UNION_MEMBER@16..26
      IDENT@16..19 "bar"
      COLON@19..20 ":"
      WHITESPACE@20..21 " "
      VAR_REF@21..24
        IDENT@21..24 "i32"
      COMMA@24..25 ","
      WHITESPACE@25..26 "\n"
  RBRACE@26..27 "}"
        """.trimIndent())
    }

    @Test
    fun `union multi member`() {
        checkContainerItem("""
            union Foo {
                bar: i32,
                baz: i32,
            }
        """.trimIndent(), """
NAMED_UNION_DECL@0..41
  UNION@0..5 "union"
  WHITESPACE@5..6 " "
  IDENT@6..9 "Foo"
  WHITESPACE@9..10 " "
  LBRACE@10..11 "{"
  WHITESPACE@11..16 "\n    "
  UNION_MEMBER_LIST@16..40
    UNION_MEMBER@16..30
      IDENT@16..19 "bar"
      COLON@19..20 ":"
      WHITESPACE@20..21 " "
      VAR_REF@21..24
        IDENT@21..24 "i32"
      COMMA@24..25 ","
      WHITESPACE@25..30 "\n    "
    UNION_MEMBER@30..40
      IDENT@30..33 "baz"
      COLON@33..34 ":"
      WHITESPACE@34..35 " "
      VAR_REF@35..38
        IDENT@35..38 "i32"
      COMMA@38..39 ","
      WHITESPACE@39..40 "\n"
  RBRACE@40..41 "}"
        """.trimIndent())
    }

    @Test
    fun `union with container member before and after`() {
        checkContainerItem("""
            union Foo {
                const foo = 1;
                
                bar: i32,
                
                const bar = 2;
            }
        """.trimIndent(), """
NAMED_UNION_DECL@0..75
  UNION@0..5 "union"
  WHITESPACE@5..6 " "
  IDENT@6..9 "Foo"
  WHITESPACE@9..10 " "
  LBRACE@10..11 "{"
  WHITESPACE@11..16 "\n    "
  CONST_DECL@16..40
    CONST@16..21 "const"
    WHITESPACE@21..22 " "
    IDENT@22..25 "foo"
    WHITESPACE@25..26 " "
    EQ@26..27 "="
    WHITESPACE@27..28 " "
    LITERAL@28..29
      NUMBER@28..29 "1"
    SEMICOLON@29..30 ";"
    WHITESPACE@30..40 "\n    \n    "
  UNION_MEMBER_LIST@40..59
    UNION_MEMBER@40..59
      IDENT@40..43 "bar"
      COLON@43..44 ":"
      WHITESPACE@44..45 " "
      VAR_REF@45..48
        IDENT@45..48 "i32"
      COMMA@48..49 ","
      WHITESPACE@49..59 "\n    \n    "
  CONST_DECL@59..74
    CONST@59..64 "const"
    WHITESPACE@64..65 " "
    IDENT@65..68 "bar"
    WHITESPACE@68..69 " "
    EQ@69..70 "="
    WHITESPACE@70..71 " "
    LITERAL@71..72
      NUMBER@71..72 "2"
    SEMICOLON@72..73 ";"
    WHITESPACE@73..74 "\n"
  RBRACE@74..75 "}"
        """.trimIndent())
    }

}

class TestUnionDecl {

    @Test
    fun `struct as expr`() {
        checkContainerItem("""
            const Foo = union {
                bar: i32,
            };
        """.trimIndent(), """
CONST_DECL@0..36
  CONST@0..5 "const"
  WHITESPACE@5..6 " "
  IDENT@6..9 "Foo"
  WHITESPACE@9..10 " "
  EQ@10..11 "="
  WHITESPACE@11..12 " "
  UNION_DECL@12..35
    UNION@12..17 "union"
    WHITESPACE@17..18 " "
    LBRACE@18..19 "{"
    WHITESPACE@19..24 "\n    "
    UNION_MEMBER_LIST@24..34
      UNION_MEMBER@24..34
        IDENT@24..27 "bar"
        COLON@27..28 ":"
        WHITESPACE@28..29 " "
        VAR_REF@29..32
          IDENT@29..32 "i32"
        COMMA@32..33 ","
        WHITESPACE@33..34 "\n"
    RBRACE@34..35 "}"
  SEMICOLON@35..36 ";"
        """.trimIndent())
    }
}
