package org.acornlang.parse.impl

import org.acornlang.parse.ParseError
import org.acornlang.syntax.SyntaxKind

sealed interface ParseEvent {

    class StartNode(val type: SyntaxKind, var fp: Int?) : ParseEvent

    object AddToken : ParseEvent

    object FinishNode : ParseEvent

    object Placeholder : ParseEvent

    class Error(val error: ParseError) : ParseEvent

}
