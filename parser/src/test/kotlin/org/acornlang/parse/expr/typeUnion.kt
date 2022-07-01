package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class TypeUnionExprTest {

    @Test
    fun `simple type union`() {
        checkExpr("""
            1&1
        """.trimIndent(), """
TYPE_UNION@0..3
  LITERAL@0..1
    NUMBER@0..1 "1"
  AMP@1..2 "&"
  LITERAL@2..3
    NUMBER@2..3 "1"
        """.trimIndent())
    }

    @Test
    fun `type union precedence with member access`() {
        checkExpr("""
            A.B & C
        """.trimIndent(), """
TYPE_UNION@0..7
  MEMBER_ACCESS@0..4
    VAR_REF@0..1
      IDENT@0..1 "A"
    DOT@1..2 "."
    VAR_REF@2..4
      IDENT@2..3 "B"
      WHITESPACE@3..4 " "
  AMP@4..5 "&"
  WHITESPACE@5..6 " "
  VAR_REF@6..7
    IDENT@6..7 "C"
        """.trimIndent())
    }

}