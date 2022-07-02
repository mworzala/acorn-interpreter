package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class MemberAccessExprTest {

    @Test
    fun `simple access`() {
        checkExpr("""
            a.b
        """.trimIndent(), """
MEMBER_ACCESS@0..3
  VAR_REF@0..1
    IDENT@0..1 "a"
  DOT@1..2 "."
  VAR_REF@2..3
    IDENT@2..3 "b"
        """.trimIndent())
    }

    @Test
    fun `multi access`() {
        checkExpr("""
            a.b.c
        """.trimIndent(), """
MEMBER_ACCESS@0..5
  MEMBER_ACCESS@0..3
    VAR_REF@0..1
      IDENT@0..1 "a"
    DOT@1..2 "."
    VAR_REF@2..3
      IDENT@2..3 "b"
  DOT@3..4 "."
  VAR_REF@4..5
    IDENT@4..5 "c"
        """.trimIndent())
    }

    @Test
    fun `access assign`() {
        checkExpr("""
            a.b = c
        """.trimIndent(), """
ASSIGN@0..7
  MEMBER_ACCESS@0..4
    VAR_REF@0..1
      IDENT@0..1 "a"
    DOT@1..2 "."
    VAR_REF@2..4
      IDENT@2..3 "b"
      WHITESPACE@3..4 " "
  EQ@4..5 "="
  WHITESPACE@5..6 " "
  VAR_REF@6..7
    IDENT@6..7 "c"
        """.trimIndent())
    }

    @Test
    fun `access precedence`() {
        checkExpr("""
            a.b + d.e
        """.trimIndent(), """
INFIX_EXPR@0..9
  MEMBER_ACCESS@0..4
    VAR_REF@0..1
      IDENT@0..1 "a"
    DOT@1..2 "."
    VAR_REF@2..4
      IDENT@2..3 "b"
      WHITESPACE@3..4 " "
  PLUS@4..5 "+"
  WHITESPACE@5..6 " "
  MEMBER_ACCESS@6..9
    VAR_REF@6..7
      IDENT@6..7 "d"
    DOT@7..8 "."
    VAR_REF@8..9
      IDENT@8..9 "e"
        """.trimIndent())
    }

}