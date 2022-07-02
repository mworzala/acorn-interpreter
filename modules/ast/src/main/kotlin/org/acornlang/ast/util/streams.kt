package org.acornlang.ast.util

import java.util.stream.Stream

/**
 * First matching element, or null if none is found
 */
fun <T> Stream<T>.firstOrNull(): T? = findFirst().orElse(null)

fun <T> Stream<T>.collectNonNull(): List<T & Any> =
    filter { it != null }.map { it!! }.toList()

/**
 * Skip elements until the predicate passes.
 * The stream will be left with the matching element first in the stream.
 *
 * WARNING: Not usable in a parallel stream.
 */
fun <T> Stream<T>.skipUntil(predicate: (T) -> Boolean): Stream<T> =
    filter(StatefulSkippingPredicate(predicate))

private class StatefulSkippingPredicate<T>(
    private val predicate: (T) -> Boolean,
) : (T) -> Boolean {
    private var found = false

    override fun invoke(p1: T): Boolean =
        if (found) true
        else {
            found = predicate(p1)
            found
        }

}
