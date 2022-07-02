package org.acornlang.parse.container

import org.acornlang.parse.checkContainerItem
import org.junit.jupiter.api.Test

class TestConstDecl {

    @Test
    fun `const decl constant`() {
        checkContainerItem("""
            const foo = 42;
        """.trimIndent(), """
CONST_DECL@0..15
  CONST@0..5 "const"
  WHITESPACE@5..6 " "
  IDENT@6..9 "foo"
  WHITESPACE@9..10 " "
  EQ@10..11 "="
  WHITESPACE@11..12 " "
  LITERAL@12..14
    NUMBER@12..14 "42"
  SEMICOLON@14..15 ";"
        """.trimIndent())
    }

    @Test
    fun `const decl with type expr`() {
        checkContainerItem("""
            const foo: i32 = 42;
        """.trimIndent(), """
CONST_DECL@0..20
  CONST@0..5 "const"
  WHITESPACE@5..6 " "
  IDENT@6..9 "foo"
  COLON@9..10 ":"
  WHITESPACE@10..11 " "
  VAR_REF@11..15
    IDENT@11..14 "i32"
    WHITESPACE@14..15 " "
  EQ@15..16 "="
  WHITESPACE@16..17 " "
  LITERAL@17..19
    NUMBER@17..19 "42"
  SEMICOLON@19..20 ";"
        """.trimIndent())
    }

    @Test
    fun `const decl no init or type expr`() {
        checkContainerItem("""
            const foo;
        """.trimIndent(), """
CONST_DECL@0..10
  CONST@0..5 "const"
  WHITESPACE@5..6 " "
  IDENT@6..9 "foo"
  SEMICOLON@9..10 ";"
        """.trimIndent())
    }

    @Test
    fun `const decl missing semicolon`() {
        checkContainerItem("""
            const foo
        """.trimIndent(), """
CONST_DECL@0..9
  CONST@0..5 "const"
  WHITESPACE@5..6 " "
  IDENT@6..9 "foo"
error at 9..9: expected [COLON, EQ, SEMICOLON], found null
        """.trimIndent())
        //todo i would like this error to simply be "missing semicolon"
    }

}