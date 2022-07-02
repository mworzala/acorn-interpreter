package org.acornlang.ast

import org.acornlang.parse.ParseError
import org.acornlang.parse.parse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

fun parseStmtRaw(source: String): AstStmt {
    val result = parse("const a = {$source;};")
    assertEquals(listOf<ParseError>(), result.errors)
    val module = AstModule(result.node)
    val const = module.decls.first() as AstConstDecl
    val expr = const.init as AstBlock
    return expr.stmts.first()
}

inline fun <reified T : AstStmt> parseStmt(source: String): T {
    val decl = parseStmtRaw(source)
    assertEquals(T::class, decl::class)
    return decl as T
}

class TestVarDecl {

    @Test
    fun `full decl`() {
        val decl = parseStmt<AstVarDecl>("let a: i32 = 1")

        assertEquals("a", decl.name?.text)
        assertFalse(decl.mut)
        assertEquals("i32", decl.type.assert<AstVarRef>().name?.text)
        assertEquals("1", decl.init.assert<AstLiteral>().value?.text)
    }

    @Test
    fun `full decl mut`() {
        val decl = parseStmt<AstVarDecl>("let mut a: i32 = 1")

        assertEquals("a", decl.name?.text)
        assertTrue(decl.mut)
        assertEquals("i32", decl.type.assert<AstVarRef>().name?.text)
        assertEquals("1", decl.init.assert<AstLiteral>().value?.text)
    }

    @Test
    fun `missing type expr`() {
        val decl = parseStmt<AstVarDecl>("let a = 1")

        assertEquals("a", decl.name?.text)
        assertNull(decl.type)
        assertEquals("1", decl.init.assert<AstLiteral>().value?.text)
    }

    @Test
    fun `missing init expr`() {
        val decl = parseStmt<AstVarDecl>("let a: i32")

        assertEquals("a", decl.name?.text)
        assertEquals("i32", decl.type.assert<AstVarRef>().name?.text)
        assertNull(decl.init)
    }

}

class TestReturn {

    @Test
    fun `return no expr`() {
        val decl = parseStmt<AstReturn>("return")
        assertNull(decl.expr)
    }

    @Test
    fun `return expr`() {
        val decl = parseStmt<AstReturn>("return 1")
        val inner = decl.expr.assert<AstLiteral>()

        assertEquals("1", inner.value?.text)
    }

}

class TestBreak {

    @Test
    fun `break`() {
        parseStmt<AstBreak>("break")
    }

}

class TestContinue {

    @Test
    fun `break`() {
        parseStmt<AstContinue>("continue")
    }

}
