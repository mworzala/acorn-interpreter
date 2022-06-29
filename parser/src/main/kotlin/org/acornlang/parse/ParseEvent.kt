package org.acornlang.parse

import org.acornlang.ast.SyntaxKind

sealed interface ParseEvent {

    class StartNode(val type: SyntaxKind, var fp: Int?) : ParseEvent

    object AddToken : ParseEvent

    object FinishNode : ParseEvent

    object Placeholder : ParseEvent

}
