package org.acornlang.ast

import org.acornlang.ast.util.firstOrNull
import org.acornlang.ast.util.skipUntil
import org.acornlang.syntax.SyntaxKind
import org.acornlang.syntax.SyntaxNode
import org.acornlang.syntax.SyntaxToken
import java.util.stream.Stream

sealed class AstNode(
    val syntax: SyntaxNode,
) {

    // SyntaxNode manipulation

    protected fun nodes(): Stream<SyntaxNode> = syntax.children.stream()
        .filter { it is SyntaxNode }
        .map { it as SyntaxNode }

    // First non trivia node

    protected fun firstNode(): SyntaxNode? =
        nodes().filter { !it.kind.isTrivia() }.firstOrNull()

    protected fun firstNode(following: SyntaxKind): SyntaxNode? =
        nodes().skipUntil { it.kind == following }.filter { !it.kind.isTrivia() }.firstOrNull()

    // First node of type

    protected fun firstNodeOfType(type: SyntaxKind): SyntaxNode? =
        nodes().filter { it.kind == type }.firstOrNull()

    protected fun firstNodeOfType(type: SyntaxKind, following: SyntaxKind): SyntaxNode? =
        nodes().skipUntil { it.kind == following }.filter { it.kind == type }.firstOrNull()


    // SyntaxToken manipulation

    protected fun tokens(): Stream<SyntaxToken> = syntax.children.stream()
        .filter { it is SyntaxToken }
        .map { it as SyntaxToken }

    protected fun firstTokenOfType(type: SyntaxKind): SyntaxToken? =
        tokens().filter { it.kind == type }.firstOrNull()


    // Utilities
}
