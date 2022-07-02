package org.acornlang.ast

import org.acornlang.ast.util.firstOrNull
import org.acornlang.ast.util.skipUntil
import org.acornlang.syntax.SyntaxElement
import org.acornlang.syntax.SyntaxKind
import org.acornlang.syntax.SyntaxNode
import org.acornlang.syntax.SyntaxToken
import java.util.stream.Stream

sealed class AstNode(
    val syntax: SyntaxNode,
) {

    // SyntaxNode manipulation

    protected fun children() = syntax.children.stream()
    protected fun Stream<SyntaxElement>.nodes(): Stream<SyntaxNode> = filter { it is SyntaxNode }.map { it as SyntaxNode }
    protected fun Stream<SyntaxElement>.tokens(): Stream<SyntaxToken> = filter { it is SyntaxToken }.map { it as SyntaxToken }



    protected fun node(skip: Long = 0): SyntaxNode? =
        children().filter { !it.kind.isTrivia() }.nodes().skip(skip).firstOrNull()

    protected fun node(following: SyntaxKind, skip: Long = 0): SyntaxNode? =
        children().skipUntil { it.kind == following }.filter { !it.kind.isTrivia() }.nodes().skip(skip).firstOrNull()

    protected fun firstNodeOfType(type: SyntaxKind): SyntaxNode? =
        children().filter { it.kind == type }.nodes().firstOrNull()

    protected fun firstNodeOfType(type: SyntaxKind, following: SyntaxKind): SyntaxNode? =
        children().skipUntil { it.kind == following }.filter { it.kind == type }.nodes().firstOrNull()

    protected fun nodesWithType(type: SyntaxKind): Stream<SyntaxNode> =
        children().filter { it.kind == type }.nodes()


    // SyntaxToken manipulation

    protected fun token(skip: Long = 0): SyntaxToken? =
        children().filter { !it.kind.isTrivia() }.tokens().skip(skip).firstOrNull()

    protected fun token(following: SyntaxKind, skip: Long = 0): SyntaxToken? =
        children().skipUntil { it.kind == following }.filter { !it.kind.isTrivia() }.tokens().skip(skip).firstOrNull()

    protected fun firstTokenOfType(type: SyntaxKind): SyntaxToken? =
        children().filter { it.kind == type }.tokens().firstOrNull()


    // Agnostic

    protected fun <T : SyntaxElement> Stream<T>.withType(type: SyntaxKind): Stream<T> =
        filter { it.kind == type }


    // Utilities
}
