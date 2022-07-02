package org.acornlang.ast

import org.acornlang.parse.ParseError
import org.acornlang.parse.parse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

fun parseDeclRaw(source: String): AstDecl {
    val result = parse(source)
    assertEquals(listOf<ParseError>(), result.errors)
    val module = AstModule(result.node)
    return module.decls.first()
}

private inline fun <reified T : AstDecl> parseDecl(source: String): T {
    val decl = parseDeclRaw(source)
    assertEquals(T::class, decl::class)
    return decl as T
}

inline fun <reified T : AstNode> AstNode?.assert(): T {
    assertNotNull(this)
    assertEquals(T::class, T::class)
    return this as T
}

class TestConstDecl {

    @Test
    fun `full decl`() {
        val decl = parseDecl<AstConstDecl>("const a: i32 = 1;")

        assertEquals("a", decl.name?.text)
        assertEquals("i32", decl.type.assert<AstVarRef>().name?.text)
        assertEquals("1", decl.init.assert<AstLiteral>().value?.text)
    }

    @Test
    fun `missing type expr`() {
        val decl = parseDecl<AstConstDecl>("const a = 1;")

        assertEquals("a", decl.name?.text)
        assertNull(decl.type)
        assertEquals("1", decl.init.assert<AstLiteral>().value?.text)
    }

    @Test
    fun `missing init expr`() {
        val decl = parseDecl<AstConstDecl>("const a: i32;")

        assertEquals("a", decl.name?.text)
        assertEquals("i32", decl.type.assert<AstVarRef>().name?.text)
        assertNull(decl.init)
    }

}
