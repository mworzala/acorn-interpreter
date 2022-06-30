package org.acornlang

import org.acornlang.syntax.AstBinary
import org.acornlang.syntax.AstInt
import org.acornlang.lex.Span
import org.acornlang.lex.Token
import org.acornlang.lex.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AstTest {

    @Test
    fun `Basic stringify`() {
        val root = AstBinary(
            Token(TokenType.PLUS, Span(0, 0)),
            AstInt(1),
            AstBinary(
                Token(TokenType.PLUS, Span(0, 0)),
                AstInt(2),
                AstInt(3)
            )
        )

        val expected = """
            BINARY PLUS
              INT "1"
              BINARY PLUS
                INT "2"
                INT "3"
        """.trimIndent()
        assertEquals(expected + '\n', root.stringify())
    }

}