package org.acornlang.vm

import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import java.nio.file.Paths

fun createModuleWithSource(source: String): Module {
    return Module("test.acorn", Paths.get("."), source)
}

fun evalExpr(@Language("acorn", prefix = "const a = ", suffix = ";") source: String): Value {
    val module = createModuleWithSource("const a = $source;")
    return module.getDecl("a")
}

fun evalModuleMain(@Language("acorn") source: String): Value {
    val module = createModuleWithSource(source)
    val main = module.getDecl("main").assert<NativeFnValue>()
    return module.call(main, listOf())
}

inline fun <reified T : Value?> Value?.assert(): T {
    assertNotNull(this)
    assertEquals(T::class.java, this!!.javaClass)
    return this as T
}