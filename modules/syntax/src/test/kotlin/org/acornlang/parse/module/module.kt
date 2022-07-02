package org.acornlang.parse.module

import org.acornlang.parse.checkModule
import org.junit.jupiter.api.Test

class TestModule {

    @Test
    fun `empty module`() {
        checkModule("""

        """.trimIndent(), """
MODULE@0..0
        """.trimIndent())
    }

    @Test
    fun `module single decl`() {
        checkModule("""
const foo = 1;
        """.trimIndent(), """
MODULE@0..14
  CONST_DECL@0..14
    CONST@0..5 "const"
    WHITESPACE@5..6 " "
    IDENT@6..9 "foo"
    WHITESPACE@9..10 " "
    EQ@10..11 "="
    WHITESPACE@11..12 " "
    LITERAL@12..13
      NUMBER@12..13 "1"
    SEMICOLON@13..14 ";"
        """.trimIndent())
    }

    @Test
    fun `module multi decl`() {
        checkModule("""
const foo = 1;
const bar = 2;
        """.trimIndent(), """
MODULE@0..29
  CONST_DECL@0..15
    CONST@0..5 "const"
    WHITESPACE@5..6 " "
    IDENT@6..9 "foo"
    WHITESPACE@9..10 " "
    EQ@10..11 "="
    WHITESPACE@11..12 " "
    LITERAL@12..13
      NUMBER@12..13 "1"
    SEMICOLON@13..14 ";"
    WHITESPACE@14..15 "\n"
  CONST_DECL@15..29
    CONST@15..20 "const"
    WHITESPACE@20..21 " "
    IDENT@21..24 "bar"
    WHITESPACE@24..25 " "
    EQ@25..26 "="
    WHITESPACE@26..27 " "
    LITERAL@27..28
      NUMBER@27..28 "2"
    SEMICOLON@28..29 ";"
        """.trimIndent())
    }

}