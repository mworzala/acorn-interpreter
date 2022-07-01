package org.acornlang.parse.expr

import org.acornlang.parse.checkExpr
import org.junit.jupiter.api.Test

class IndexExprTest {

    @Test
    fun `simple index`() {
        checkExpr("""
            a[1]
        """.trimIndent(), """
INDEX@0..4
  VAR_REF@0..1
    IDENT@0..1 "a"
  LBRACKET@1..2 "["
  LITERAL@2..3
    NUMBER@2..3 "1"
  RBRACKET@3..4 "]"
        """.trimIndent())
    }

    @Test
    fun `nested index`() {
        checkExpr("""
            a[b[1]]
        """.trimIndent(), """
INDEX@0..7
  VAR_REF@0..1
    IDENT@0..1 "a"
  LBRACKET@1..2 "["
  INDEX@2..6
    VAR_REF@2..3
      IDENT@2..3 "b"
    LBRACKET@3..4 "["
    LITERAL@4..5
      NUMBER@4..5 "1"
    RBRACKET@5..6 "]"
  RBRACKET@6..7 "]"
        """.trimIndent())
    }

    @Test
    fun `assign to index`() {
        checkExpr("""
            a[1] = b[2]
        """.trimIndent(), """
ASSIGN@0..11
  INDEX@0..5
    VAR_REF@0..1
      IDENT@0..1 "a"
    LBRACKET@1..2 "["
    LITERAL@2..3
      NUMBER@2..3 "1"
    RBRACKET@3..4 "]"
    WHITESPACE@4..5 " "
  EQ@5..6 "="
  WHITESPACE@6..7 " "
  INDEX@7..11
    VAR_REF@7..8
      IDENT@7..8 "b"
    LBRACKET@8..9 "["
    LITERAL@9..10
      NUMBER@9..10 "2"
    RBRACKET@10..11 "]"
        """.trimIndent())
    }

}