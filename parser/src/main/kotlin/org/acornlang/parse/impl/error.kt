package org.acornlang.parse.impl

import org.acornlang.common.text.Span
import org.acornlang.lex.TokenType
import org.acornlang.parse.ParseError

class GenericParseError(
    val expected: List<TokenType>,
    val found: TokenType?,
    val range: Span,
) : ParseError {

    override fun toString(): String {
        val sb = StringBuilder()

        sb.append("error at $range: expected $expected, found $found")

        return sb.toString()
    }
}
