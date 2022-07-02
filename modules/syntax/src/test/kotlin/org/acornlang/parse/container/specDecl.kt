package org.acornlang.parse.container

import org.acornlang.parse.checkContainerItem
import org.junit.jupiter.api.Test

class TestNamedSpecDecl {

    @Test
    fun `empty named spec`() {
        checkContainerItem("""
            spec Foo {}
        """.trimIndent(), """
NAMED_SPEC_DECL@0..11
  SPEC@0..4 "spec"
  WHITESPACE@4..5 " "
  IDENT@5..8 "Foo"
  WHITESPACE@8..9 " "
  LBRACE@9..10 "{"
  RBRACE@10..11 "}"
        """.trimIndent())
    }

    @Test
    fun `spec single fn`() {
        checkContainerItem("""
            spec Foo {
                fn bar() void;
            }
        """.trimIndent(), """
NAMED_SPEC_DECL@0..31
  SPEC@0..4 "spec"
  WHITESPACE@4..5 " "
  IDENT@5..8 "Foo"
  WHITESPACE@8..9 " "
  LBRACE@9..10 "{"
  WHITESPACE@10..15 "\n    "
  NAMED_FN_DECL@15..30
    FN@15..17 "fn"
    WHITESPACE@17..18 " "
    IDENT@18..21 "bar"
    FN_PARAM_LIST@21..24
      LPAREN@21..22 "("
      RPAREN@22..23 ")"
      WHITESPACE@23..24 " "
    VAR_REF@24..28
      IDENT@24..28 "void"
    SEMICOLON@28..29 ";"
    WHITESPACE@29..30 "\n"
  RBRACE@30..31 "}"
        """.trimIndent())
    }

    @Test
    fun `spec multi fn`() {
        checkContainerItem("""
            spec Foo {
                fn bar() void;
                fn baz() void;
            }
        """.trimIndent(), """
NAMED_SPEC_DECL@0..50
  SPEC@0..4 "spec"
  WHITESPACE@4..5 " "
  IDENT@5..8 "Foo"
  WHITESPACE@8..9 " "
  LBRACE@9..10 "{"
  WHITESPACE@10..15 "\n    "
  NAMED_FN_DECL@15..34
    FN@15..17 "fn"
    WHITESPACE@17..18 " "
    IDENT@18..21 "bar"
    FN_PARAM_LIST@21..24
      LPAREN@21..22 "("
      RPAREN@22..23 ")"
      WHITESPACE@23..24 " "
    VAR_REF@24..28
      IDENT@24..28 "void"
    SEMICOLON@28..29 ";"
    WHITESPACE@29..34 "\n    "
  NAMED_FN_DECL@34..49
    FN@34..36 "fn"
    WHITESPACE@36..37 " "
    IDENT@37..40 "baz"
    FN_PARAM_LIST@40..43
      LPAREN@40..41 "("
      RPAREN@41..42 ")"
      WHITESPACE@42..43 " "
    VAR_REF@43..47
      IDENT@43..47 "void"
    SEMICOLON@47..48 ";"
    WHITESPACE@48..49 "\n"
  RBRACE@49..50 "}"
        """.trimIndent())
    }

}

class TestSpecDecl {

    @Test
    fun `spec as expr`() {
        checkContainerItem("""
            const Foo = spec {
                fn add(a: i32, b: i32) i32;
            };
        """.trimIndent(), """
CONST_DECL@0..53
  CONST@0..5 "const"
  WHITESPACE@5..6 " "
  IDENT@6..9 "Foo"
  WHITESPACE@9..10 " "
  EQ@10..11 "="
  WHITESPACE@11..12 " "
  SPEC_DECL@12..52
    SPEC@12..16 "spec"
    WHITESPACE@16..17 " "
    LBRACE@17..18 "{"
    WHITESPACE@18..23 "\n    "
    NAMED_FN_DECL@23..51
      FN@23..25 "fn"
      WHITESPACE@25..26 " "
      IDENT@26..29 "add"
      FN_PARAM_LIST@29..46
        LPAREN@29..30 "("
        FN_PARAM@30..36
          IDENT@30..31 "a"
          COLON@31..32 ":"
          WHITESPACE@32..33 " "
          VAR_REF@33..36
            IDENT@33..36 "i32"
        COMMA@36..37 ","
        WHITESPACE@37..38 " "
        FN_PARAM@38..44
          IDENT@38..39 "b"
          COLON@39..40 ":"
          WHITESPACE@40..41 " "
          VAR_REF@41..44
            IDENT@41..44 "i32"
        RPAREN@44..45 ")"
        WHITESPACE@45..46 " "
      VAR_REF@46..49
        IDENT@46..49 "i32"
      SEMICOLON@49..50 ";"
      WHITESPACE@50..51 "\n"
    RBRACE@51..52 "}"
  SEMICOLON@52..53 ";"
        """.trimIndent())
    }
}
