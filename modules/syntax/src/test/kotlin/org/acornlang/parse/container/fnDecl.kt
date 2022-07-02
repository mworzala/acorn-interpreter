package org.acornlang.parse.container

import org.acornlang.parse.checkContainerItem
import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class TestNamedFnDecl {

    @Test
    fun `empty named fn`() {
        checkContainerItem("""
            fn foo() void;
        """.trimIndent(), """
NAMED_FN_DECL@0..14
  FN@0..2 "fn"
  WHITESPACE@2..3 " "
  IDENT@3..6 "foo"
  FN_PARAM_LIST@6..9
    LPAREN@6..7 "("
    RPAREN@7..8 ")"
    WHITESPACE@8..9 " "
  VAR_REF@9..13
    IDENT@9..13 "void"
  SEMICOLON@13..14 ";"
        """.trimIndent())
    }

    @Test
    fun `empty named fn with body`() {
        checkContainerItem("""
            fn foo() void {}
        """.trimIndent(), """
NAMED_FN_DECL@0..16
  FN@0..2 "fn"
  WHITESPACE@2..3 " "
  IDENT@3..6 "foo"
  FN_PARAM_LIST@6..9
    LPAREN@6..7 "("
    RPAREN@7..8 ")"
    WHITESPACE@8..9 " "
  VAR_REF@9..14
    IDENT@9..13 "void"
    WHITESPACE@13..14 " "
  BLOCK@14..16
    LBRACE@14..15 "{"
    RBRACE@15..16 "}"
        """.trimIndent())
    }

    @Test
    fun `named fn with single param`() {
        checkContainerItem("""
            fn foo(a: i32) void {}
        """.trimIndent(), """
NAMED_FN_DECL@0..22
  FN@0..2 "fn"
  WHITESPACE@2..3 " "
  IDENT@3..6 "foo"
  FN_PARAM_LIST@6..15
    LPAREN@6..7 "("
    FN_PARAM@7..13
      IDENT@7..8 "a"
      COLON@8..9 ":"
      WHITESPACE@9..10 " "
      VAR_REF@10..13
        IDENT@10..13 "i32"
    RPAREN@13..14 ")"
    WHITESPACE@14..15 " "
  VAR_REF@15..20
    IDENT@15..19 "void"
    WHITESPACE@19..20 " "
  BLOCK@20..22
    LBRACE@20..21 "{"
    RBRACE@21..22 "}"
        """.trimIndent())
    }

    @Test
    fun `named fn with multiple params`() {
        checkContainerItem("""
            fn foo(a: i32, b: i32) void {}
        """.trimIndent(), """
NAMED_FN_DECL@0..30
  FN@0..2 "fn"
  WHITESPACE@2..3 " "
  IDENT@3..6 "foo"
  FN_PARAM_LIST@6..23
    LPAREN@6..7 "("
    FN_PARAM@7..13
      IDENT@7..8 "a"
      COLON@8..9 ":"
      WHITESPACE@9..10 " "
      VAR_REF@10..13
        IDENT@10..13 "i32"
    COMMA@13..14 ","
    WHITESPACE@14..15 " "
    FN_PARAM@15..21
      IDENT@15..16 "b"
      COLON@16..17 ":"
      WHITESPACE@17..18 " "
      VAR_REF@18..21
        IDENT@18..21 "i32"
    RPAREN@21..22 ")"
    WHITESPACE@22..23 " "
  VAR_REF@23..28
    IDENT@23..27 "void"
    WHITESPACE@27..28 " "
  BLOCK@28..30
    LBRACE@28..29 "{"
    RBRACE@29..30 "}"
        """.trimIndent())
    }

    @Test
    fun `named fn with ret type`() {
        checkContainerItem("""
            fn foo() i32 {}
        """.trimIndent(), """
NAMED_FN_DECL@0..15
  FN@0..2 "fn"
  WHITESPACE@2..3 " "
  IDENT@3..6 "foo"
  FN_PARAM_LIST@6..9
    LPAREN@6..7 "("
    RPAREN@7..8 ")"
    WHITESPACE@8..9 " "
  VAR_REF@9..13
    IDENT@9..12 "i32"
    WHITESPACE@12..13 " "
  BLOCK@13..15
    LBRACE@13..14 "{"
    RBRACE@14..15 "}"
        """.trimIndent())
    }

    @Test
    fun `full fn decl`() {
        checkContainerItem("""
            fn add(a: i32, b: i32) i32 {
                a + b
            }
        """.trimIndent(), """
NAMED_FN_DECL@0..40
  FN@0..2 "fn"
  WHITESPACE@2..3 " "
  IDENT@3..6 "add"
  FN_PARAM_LIST@6..23
    LPAREN@6..7 "("
    FN_PARAM@7..13
      IDENT@7..8 "a"
      COLON@8..9 ":"
      WHITESPACE@9..10 " "
      VAR_REF@10..13
        IDENT@10..13 "i32"
    COMMA@13..14 ","
    WHITESPACE@14..15 " "
    FN_PARAM@15..21
      IDENT@15..16 "b"
      COLON@16..17 ":"
      WHITESPACE@17..18 " "
      VAR_REF@18..21
        IDENT@18..21 "i32"
    RPAREN@21..22 ")"
    WHITESPACE@22..23 " "
  VAR_REF@23..27
    IDENT@23..26 "i32"
    WHITESPACE@26..27 " "
  BLOCK@27..40
    LBRACE@27..28 "{"
    WHITESPACE@28..33 "\n    "
    IMPLICIT_RETURN@33..39
      INFIX_EXPR@33..39
        VAR_REF@33..35
          IDENT@33..34 "a"
          WHITESPACE@34..35 " "
        PLUS@35..36 "+"
        WHITESPACE@36..37 " "
        VAR_REF@37..39
          IDENT@37..38 "b"
          WHITESPACE@38..39 "\n"
    RBRACE@39..40 "}"
        """.trimIndent())
    }

}

class TestFnDecl {

    @Test
    fun `fn type decl`() {
        checkExpr("""
            fn() i32
        """.trimIndent(), """
FN_TYPE@0..8
  FN@0..2 "fn"
  FN_PARAM_LIST@2..5
    LPAREN@2..3 "("
    RPAREN@3..4 ")"
    WHITESPACE@4..5 " "
  VAR_REF@5..8
    IDENT@5..8 "i32"
        """.trimIndent())
    }

    @Test
    fun `fn type decl named params`() {
        checkExpr("""
            fn(a: i32, b: i32) i32
        """.trimIndent(), """
FN_TYPE@0..22
  FN@0..2 "fn"
  FN_PARAM_LIST@2..19
    LPAREN@2..3 "("
    FN_PARAM@3..9
      IDENT@3..4 "a"
      COLON@4..5 ":"
      WHITESPACE@5..6 " "
      VAR_REF@6..9
        IDENT@6..9 "i32"
    COMMA@9..10 ","
    WHITESPACE@10..11 " "
    FN_PARAM@11..17
      IDENT@11..12 "b"
      COLON@12..13 ":"
      WHITESPACE@13..14 " "
      VAR_REF@14..17
        IDENT@14..17 "i32"
    RPAREN@17..18 ")"
    WHITESPACE@18..19 " "
  VAR_REF@19..22
    IDENT@19..22 "i32"
        """.trimIndent())
    }

    @Test
    fun `fn as const`() {
        checkContainerItem(
            """
            const add = fn(a: i32, b: i32) i32 {
                a + b
            };
        """.trimIndent(), """
CONST_DECL@0..49
  CONST@0..5 "const"
  WHITESPACE@5..6 " "
  IDENT@6..9 "add"
  WHITESPACE@9..10 " "
  EQ@10..11 "="
  WHITESPACE@11..12 " "
  FN_DECL@12..48
    FN@12..14 "fn"
    FN_PARAM_LIST@14..31
      LPAREN@14..15 "("
      FN_PARAM@15..21
        IDENT@15..16 "a"
        COLON@16..17 ":"
        WHITESPACE@17..18 " "
        VAR_REF@18..21
          IDENT@18..21 "i32"
      COMMA@21..22 ","
      WHITESPACE@22..23 " "
      FN_PARAM@23..29
        IDENT@23..24 "b"
        COLON@24..25 ":"
        WHITESPACE@25..26 " "
        VAR_REF@26..29
          IDENT@26..29 "i32"
      RPAREN@29..30 ")"
      WHITESPACE@30..31 " "
    VAR_REF@31..35
      IDENT@31..34 "i32"
      WHITESPACE@34..35 " "
    BLOCK@35..48
      LBRACE@35..36 "{"
      WHITESPACE@36..41 "\n    "
      IMPLICIT_RETURN@41..47
        INFIX_EXPR@41..47
          VAR_REF@41..43
            IDENT@41..42 "a"
            WHITESPACE@42..43 " "
          PLUS@43..44 "+"
          WHITESPACE@44..45 " "
          VAR_REF@45..47
            IDENT@45..46 "b"
            WHITESPACE@46..47 "\n"
      RBRACE@47..48 "}"
  SEMICOLON@48..49 ";"
        """.trimIndent()
        )
    }
}