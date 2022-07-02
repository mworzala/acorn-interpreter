package org.acornlang.hir

/**
 * High level representation of Acorn.
 *
 * Represents semantically valid input, so input must be validated prior to lowering.
 *
 * Not a 1:1 mapping from source code, sugar is removed.
 */
abstract class HirNode {

    abstract fun <P, R> accept(visitor: HirVisitor<P, R>, p: P): R

}
