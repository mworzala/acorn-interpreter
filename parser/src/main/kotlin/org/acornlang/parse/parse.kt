package org.acornlang.parse

import org.acornlang.parse.impl.CompletedMarker
import org.acornlang.parse.impl.Parser
import org.acornlang.parse.impl.Sink
import org.acornlang.parse.rule.module
import org.acornlang.syntax.SyntaxNode
import org.acornlang.syntax.SyntaxKind

interface ParseError

class ParseResult(val node: SyntaxNode, val errors: List<ParseError>)

/**
 * Parses a string of Acorn source code into a [SyntaxNode], with any errors encountered.
 * The returned [SyntaxNode] is _always_ a [SyntaxKind.ROOT].
 *
 * The source code should be an Acorn Module, aka full file contents as opposed to a single expression/stmt/etc.
 */
fun parse(input: String): ParseResult = parse(input, Parser::module)

/**
 * Parses a string of Acorn source code given the provided parse rule. Used above for public parsing api, and in tests
 * to parse small snippets in isolation.
 */
internal fun parse(input: String, rule: Parser.() -> CompletedMarker?): ParseResult {
    val parser = Parser(input)

    val m = parser.start()
    parser.rule()
    m.complete(SyntaxKind.ROOT)

    val sink = Sink(parser.lexer, parser.events)
    return sink.finish()
}
