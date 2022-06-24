package org.acornlang

fun fail(message: String): Nothing {
    throw RuntimeException(message)
}