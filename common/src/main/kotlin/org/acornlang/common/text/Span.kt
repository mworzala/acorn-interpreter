package org.acornlang.common.text

class Span(
    val start: Int,
    val end: Int,
) {
    override fun toString(): String {
        return "${start}..${end}"
    }
}
