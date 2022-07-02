package org.acornlang.parse.container

import org.acornlang.parse.checkContainerItem
import org.junit.jupiter.api.Test

class TestDocComment {

    @Test
    fun `doc comment before const decl`() {
        checkContainerItem(
            """
            /// Test 123
            const foo = 1;
        """.trimIndent(), """
CONST_DECL@0..27
  DOC_COMMENT@0..12 "/// Test 123"
  WHITESPACE@12..13 "\n"
  CONST@13..18 "const"
  WHITESPACE@18..19 " "
  IDENT@19..22 "foo"
  WHITESPACE@22..23 " "
  EQ@23..24 "="
  WHITESPACE@24..25 " "
  LITERAL@25..26
    NUMBER@25..26 "1"
  SEMICOLON@26..27 ";"
        """.trimIndent()
        )
    }

    @Test
    fun `doc comment before fn decl`() {
        checkContainerItem(
            """
            /// First doc comment
            /// Another doc comment!
            /// Third comment
            fn foo() void {}
        """.trimIndent(), """
NAMED_FN_DECL@0..81
  DOC_COMMENT@0..21 "/// First doc comment"
  DOC_COMMENT@21..46 "\n/// Another doc comment!"
  DOC_COMMENT@46..64 "\n/// Third comment"
  WHITESPACE@64..65 "\n"
  FN@65..67 "fn"
  WHITESPACE@67..68 " "
  IDENT@68..71 "foo"
  FN_PARAM_LIST@71..74
    LPAREN@71..72 "("
    RPAREN@72..73 ")"
    WHITESPACE@73..74 " "
  VAR_REF@74..79
    IDENT@74..78 "void"
    WHITESPACE@78..79 " "
  BLOCK@79..81
    LBRACE@79..80 "{"
    RBRACE@80..81 "}"
        """.trimIndent()
        )
    }

}