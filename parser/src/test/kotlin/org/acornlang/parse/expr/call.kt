package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class CallExprTest {

    @Test
    fun `simple call`() {
        checkExpr("""
            a()
        """.trimIndent(), """
CALL@0..3
  VAR_REF@0..1
    IDENT@0..1 "a"
  CALL_ARG_LIST@1..3
    LPAREN@1..2 "("
    RPAREN@2..3 ")"
        """.trimIndent())
    }

    @Test
    fun `call single arg`() {
        checkExpr("""
            a(1)
        """.trimIndent(), """
CALL@0..4
  VAR_REF@0..1
    IDENT@0..1 "a"
  CALL_ARG_LIST@1..4
    LPAREN@1..2 "("
    LITERAL@2..3
      NUMBER@2..3 "1"
    RPAREN@3..4 ")"
        """.trimIndent())
    }

    @Test
    fun `call multi arg`() {
        checkExpr("""
            a(1, 2)
        """.trimIndent(), """
CALL@0..7
  VAR_REF@0..1
    IDENT@0..1 "a"
  CALL_ARG_LIST@1..7
    LPAREN@1..2 "("
    LITERAL@2..3
      NUMBER@2..3 "1"
    COMMA@3..4 ","
    WHITESPACE@4..5 " "
    LITERAL@5..6
      NUMBER@5..6 "2"
    RPAREN@6..7 ")"
        """.trimIndent())
    }

    @Test
    fun `call member access`() {
        checkExpr("""
            foo.bar()
        """.trimIndent(), """
CALL@0..9
  MEMBER_ACCESS@0..7
    VAR_REF@0..3
      IDENT@0..3 "foo"
    DOT@3..4 "."
    VAR_REF@4..7
      IDENT@4..7 "bar"
  CALL_ARG_LIST@7..9
    LPAREN@7..8 "("
    RPAREN@8..9 ")"
        """.trimIndent())
    }

    @Test
    fun `call precedence`() {
        checkExpr("""
            foo.bar() = baz.qux()
        """.trimIndent(), """
ASSIGN@0..21
  CALL@0..10
    MEMBER_ACCESS@0..7
      VAR_REF@0..3
        IDENT@0..3 "foo"
      DOT@3..4 "."
      VAR_REF@4..7
        IDENT@4..7 "bar"
    CALL_ARG_LIST@7..10
      LPAREN@7..8 "("
      RPAREN@8..9 ")"
      WHITESPACE@9..10 " "
  EQ@10..11 "="
  WHITESPACE@11..12 " "
  CALL@12..21
    MEMBER_ACCESS@12..19
      VAR_REF@12..15
        IDENT@12..15 "baz"
      DOT@15..16 "."
      VAR_REF@16..19
        IDENT@16..19 "qux"
    CALL_ARG_LIST@19..21
      LPAREN@19..20 "("
      RPAREN@20..21 ")"
        """.trimIndent())
    }

    @Test
    fun `call precedence 2`() {
        checkExpr("""
            foo() & bar()
        """.trimIndent(), """
TYPE_UNION@0..13
  CALL@0..6
    VAR_REF@0..3
      IDENT@0..3 "foo"
    CALL_ARG_LIST@3..6
      LPAREN@3..4 "("
      RPAREN@4..5 ")"
      WHITESPACE@5..6 " "
  AMP@6..7 "&"
  WHITESPACE@7..8 " "
  CALL@8..13
    VAR_REF@8..11
      IDENT@8..11 "bar"
    CALL_ARG_LIST@11..13
      LPAREN@11..12 "("
      RPAREN@12..13 ")"
        """.trimIndent())
    }

    @Test
    fun `call precedence 3`() {
        checkExpr("""
            foo() + bar()
        """.trimIndent(), """
INFIX_EXPR@0..13
  CALL@0..6
    VAR_REF@0..3
      IDENT@0..3 "foo"
    CALL_ARG_LIST@3..6
      LPAREN@3..4 "("
      RPAREN@4..5 ")"
      WHITESPACE@5..6 " "
  PLUS@6..7 "+"
  WHITESPACE@7..8 " "
  CALL@8..13
    VAR_REF@8..11
      IDENT@8..11 "bar"
    CALL_ARG_LIST@11..13
      LPAREN@11..12 "("
      RPAREN@12..13 ")"
        """.trimIndent())
    }

    @Test
    fun `call index`() {
        checkExpr("""
            a[0]()
        """.trimIndent(), """
CALL@0..6
  INDEX@0..4
    VAR_REF@0..1
      IDENT@0..1 "a"
    LBRACKET@1..2 "["
    LITERAL@2..3
      NUMBER@2..3 "0"
    RBRACKET@3..4 "]"
  CALL_ARG_LIST@4..6
    LPAREN@4..5 "("
    RPAREN@5..6 ")"
        """.trimIndent())
    }

}