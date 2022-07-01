package org.acornlang.parse.stmt

import org.acornlang.parse.checkStmt
import org.junit.jupiter.api.Test

class LetTest {

    @Test
    fun `basic let to do error handling`() {
        checkStmt("""
            let foo = bar
        """.trimIndent(), """
VAR_DECL@0..13
  LET@0..3 "let"
  WHITESPACE@3..4 " "
  IDENT@4..7 "foo"
  WHITESPACE@7..8 " "
  EQ@8..9 "="
  WHITESPACE@9..10 " "
  VAR_REF@10..13
    IDENT@10..13 "bar"
        """.trimIndent())
    }

    @Test
    fun `let mut`() {
        checkStmt("""
            let mut foo = bar
        """.trimIndent(), """
VAR_DECL@0..17
  LET@0..3 "let"
  WHITESPACE@3..4 " "
  MUT@4..7 "mut"
  WHITESPACE@7..8 " "
  IDENT@8..11 "foo"
  WHITESPACE@11..12 " "
  EQ@12..13 "="
  WHITESPACE@13..14 " "
  VAR_REF@14..17
    IDENT@14..17 "bar"
        """.trimIndent())
    }

    @Test
    fun `let with type expression`() {
        checkStmt("""
            let foo: i32 = bar
        """.trimIndent(), """
VAR_DECL@0..18
  LET@0..3 "let"
  WHITESPACE@3..4 " "
  IDENT@4..7 "foo"
  COLON@7..8 ":"
  WHITESPACE@8..9 " "
  VAR_REF@9..13
    IDENT@9..12 "i32"
    WHITESPACE@12..13 " "
  EQ@13..14 "="
  WHITESPACE@14..15 " "
  VAR_REF@15..18
    IDENT@15..18 "bar"
        """.trimIndent())
    }

    @Test
    fun `let with colon missing type expression`() {
        checkStmt("""
            let foo: = bar
        """.trimIndent(), """
VAR_DECL@0..14
  LET@0..3 "let"
  WHITESPACE@3..4 " "
  IDENT@4..7 "foo"
  COLON@7..8 ":"
  WHITESPACE@8..9 " "
  EQ@9..10 "="
  WHITESPACE@10..11 " "
  VAR_REF@11..14
    IDENT@11..14 "bar"
error at 9..10: expected [FN, ENUM, STRUCT, UNION, SPEC, LBRACE, LPAREN, LBRACKET, IF, WHILE, MINUS, BANG, AMP, NUMBER, STRING, TRUE, FALSE, IDENT, INTRINSIC_IDENT], found EQ
        """.trimIndent())
    }

//    @Test
//    fun `let error recovery case`() {
//        //todo will fail until we have blocks
//        checkStmt("""
//            let foo =
//            let bar = baz
//        """.trimIndent(), """
//ROOT@0..23
//  VAR_DECL@0..10
//    LET@0..3 "let"
//    WHITESPACE@3..4 " "
//    IDENT@4..7 "foo"
//    WHITESPACE@7..8 " "
//    EQ@8..9 "="
//    WHITESPACE@9..10 "\n"
//  VAR_DECL@10..23
//    LET@10..13 "let"
//    WHITESPACE@13..14 " "
//    IDENT@14..17 "bar"
//    WHITESPACE@17..18 " "
//    EQ@18..19 "="
//    WHITESPACE@19..20 " "
//    VAR_REF@20..23
//      IDENT@20..23 "baz"
//error at 10..13: expected [NUMBER, STRING, TRUE, FALSE, IDENT, LPAREN, MINUS], found LET
//        """.trimIndent())
//    }

}