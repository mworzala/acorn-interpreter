package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class ReferenceExprTest {

    @Test
    fun `simple ref`() {
        checkExpr("""
            &a
        """.trimIndent(), """
REFERENCE@0..2
  AMP@0..1 "&"
  VAR_REF@1..2
    IDENT@1..2 "a"
        """.trimIndent())
    }

    @Test
    fun `mut ref`() {
        checkExpr("""
            &mut a
        """.trimIndent(), """
REFERENCE@0..6
  AMP@0..1 "&"
  MUT@1..4 "mut"
  WHITESPACE@4..5 " "
  VAR_REF@5..6
    IDENT@5..6 "a"
        """.trimIndent())
    }

    @Test
    fun `mut ref ref`() {
        checkExpr("""
            &mut &a
        """.trimIndent(), """
REFERENCE@0..7
  AMP@0..1 "&"
  MUT@1..4 "mut"
  WHITESPACE@4..5 " "
  REFERENCE@5..7
    AMP@5..6 "&"
    VAR_REF@6..7
      IDENT@6..7 "a"
        """.trimIndent())
    }


}