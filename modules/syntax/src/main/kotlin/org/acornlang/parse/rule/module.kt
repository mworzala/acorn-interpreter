package org.acornlang.parse.rule

import org.acornlang.parse.impl.CompletedMarker
import org.acornlang.parse.impl.Parser
import org.acornlang.syntax.SyntaxKind

internal fun Parser.module(extended: Boolean): CompletedMarker? {
    val m = start()

    while (!atEnd()) {
        containerItem(true)
    }

    return m.complete(SyntaxKind.MODULE)
}
