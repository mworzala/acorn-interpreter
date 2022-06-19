package org.acornlang.parse

import org.acornlang.Parser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private fun check(source: String, expected: String) {
    val parser = Parser(source)
    val node = parser.module()

    assertEquals(expected + "\n", node.stringify())
}

class Module {
    @Test
    fun `complex module`() {
        check("""
            const string: *i8 = "Hello, world!";
            
            foreign fn puts(s: *i8) i32;
            
            fn main() i32 {
                puts(string);
                let twenty_one: i32 = 21;
                return twenty_one * 2;
            }
        """.trimIndent(), """
            MODULE
              CONST "string"
                PTR_TYPE
                  TYPE "i8"
                STR "Hello, world!"
              NAMED_FN "puts" .foreign
                TYPE "i32"
                PARAM "s"
                  PTR_TYPE
                    TYPE "i8"
              NAMED_FN "main"
                TYPE "i32"
                BLOCK
                  CALL
                    REF "puts"
                    REF "string"
                  LET "twenty_one"
                    TYPE "i32"
                    INT "21"
                  RETURN
                    BINARY STAR
                      REF "twenty_one"
                      INT "2"
        """.trimIndent())
    }

}