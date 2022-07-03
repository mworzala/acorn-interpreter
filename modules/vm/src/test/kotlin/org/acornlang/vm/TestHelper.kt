package org.acornlang.vm

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import java.nio.file.Paths

fun createModuleWithSource(source: String): Module {
    return Module("test.acorn", Paths.get("."), source)
}

fun evalExpr(source: String): Value {
    val module = createModuleWithSource("const a = $source;")
    return module.getDecl("a")
}

inline fun <reified T : Value?> Value?.assert(): T {
    assertNotNull(this)
    assertEquals(T::class.java, this!!.javaClass)
    return this as T
}