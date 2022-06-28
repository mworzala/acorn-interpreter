package org.acornlang.parse

import org.acornlang.Parser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private fun check(source: String, expected: String) {
    val parser = Parser(source)
    val node = parser.decl()

    assertEquals(expected + "\n", node.stringify())
}

class DeclTest {
    @Test
    fun `basic const`() {
        check("""
            const a: i32 = 1;
        """.trimIndent(), """
            CONST "a"
              TYPE "i32"
              INT "1"
        """.trimIndent())
    }

    @Test
    fun `const ptr and expr`() {
        check("""
            const a: *i32 = 1 + 1;
        """.trimIndent(), """
            CONST "a"
              PTR_TYPE
                TYPE "i32"
              BINARY PLUS
                INT "1"
                INT "1"
        """.trimIndent())
    }

    @Test
    fun `basic named fn`() {
        check("""
            fn foo() {}
        """.trimIndent(), """
            NAMED_FN "foo"
              TYPE "void"
              BLOCK
        """.trimIndent())
    }

    @Test
    fun `named fn with params and body`() {
        check("""
            fn add(a: i32, b: i32) i32 {
                return a + b;
            }
        """.trimIndent(), """
            NAMED_FN "add"
              TYPE "i32"
              PARAM "a"
                TYPE "i32"
              PARAM "b"
                TYPE "i32"
              BLOCK
                RETURN
                  BINARY PLUS
                    REF "a"
                    REF "b"
        """.trimIndent())
    }

    @Test
    fun `foreign named fn`() {
        check("""
            foreign fn puts(s: *i8) i32;
        """.trimIndent(), """
            NAMED_FN "puts" .foreign
              TYPE "i32"
              PARAM "s"
                PTR_TYPE
                  TYPE "i8"
        """.trimIndent())
    }

    @Test
    fun `empty struct`() {
        check("""
            struct Foo {}
        """.trimIndent(), """
            STRUCT "Foo"
        """.trimIndent())
    }

    @Test
    fun `struct with fields`() {
        check("""
            struct Foo {
                a: i32,
                b: i32,
            }
        """.trimIndent(), """
            STRUCT "Foo"
              FIELD "a"
                TYPE "i32"
              FIELD "b"
                TYPE "i32"
        """.trimIndent())
    }

    @Test
    fun `empty enum`() {
        check("""
            enum Foo {}
        """.trimIndent(), """
            ENUM "Foo"
        """.trimIndent())
    }

    @Test
    fun `enum with cases`() {
        check("""
            enum Foo {
                one,
                two,
            }
        """.trimIndent(), """
            ENUM "Foo"
              CASE "one"
              CASE "two"
        """.trimIndent())
    }

    @Test
    fun `empty union`() {
        check("""
            union Foo {
            }
        """.trimIndent(), """
            UNION "Foo"
        """.trimIndent())
    }

    @Test
    fun `union with untyped case`() {
        check("""
            union Foo {
                None,
            }
        """.trimIndent(), """
            UNION "Foo"
              CASE "None"
        """.trimIndent())
    }

    @Test
    fun `union with typed case`() {
        check("""
            union Foo {
                Int: i32,
            }
        """.trimIndent(), """
            UNION "Foo"
              CASE "Int"
                TYPE "i32"
        """.trimIndent())
    }

//    @Test
//    fun `union with multiple types and cases`() {
//        check("""
//            union Foo {
//                Int: i32,
//                Tuple: (i32, i32),
//                Struct: struct {
//                    a: i32,
//                    b: i32,
//                },
//                None,
//            }
//        """.trimIndent(), """
//            UNION "Foo"
//              CASE "Int"
//              CASE "Tuple"
//              CASE "STRUCT"
//        """.trimIndent())
//    }

}