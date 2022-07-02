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


class TestFnDecl {

    @Test
    fun `simple fn`() {
        val decl = parseDecl<AstNamedFnDecl>("fn foo() void {}")
        val params = decl.params.assert<AstFnParamList>()
        assertEquals(0, params.params.size)

        val ret = decl.retType.assert<AstVarRef>()
        assertEquals("void", ret.name?.text)

        decl.body.assert<AstBlock>()
    }

    @Test
    fun `fn single param`() {
        val decl = parseDecl<AstNamedFnDecl>("fn foo(a: i32) void {}")
        val params = decl.params.assert<AstFnParamList>()
        assertEquals(1, params.params.size)

        val param1 = params.params[0].assert<AstFnParam>()
        assertEquals("a", param1.name?.text)
        assertEquals("i32", param1.type.assert<AstVarRef>().name?.text)

        val ret = decl.retType.assert<AstVarRef>()
        assertEquals("void", ret.name?.text)

        decl.body.assert<AstBlock>()
    }

    @Test
    fun `fn multi param`() {
        val decl = parseDecl<AstNamedFnDecl>("fn foo(self, b: i32) i32 {}")
        val params = decl.params.assert<AstFnParamList>()
        assertEquals(2, params.params.size)

        val param1 = params.params[0].assert<AstFnParam>()
        assertEquals("self", param1.name?.text)
        assertNull(param1.type)

        val param2 = params.params[1].assert<AstFnParam>()
        assertEquals("b", param2.name?.text)
        assertEquals("i32", param2.type.assert<AstVarRef>().name?.text)

        val ret = decl.retType.assert<AstVarRef>()
        assertEquals("i32", ret.name?.text)

        decl.body.assert<AstBlock>()
    }

    @Test
    fun `fn expr`() {
        val decl = parseExpr<AstFnDecl>("fn() void {}")
        val params = decl.params.assert<AstFnParamList>()
        assertEquals(0, params.params.size)

        val ret = decl.retType.assert<AstVarRef>()
        assertEquals("void", ret.name?.text)

        decl.body.assert<AstBlock>()
    }

    @Test
    fun `fn type expr`() {
        val decl = parseExpr<AstFnType>("fn() void")
        val params = decl.params.assert<AstFnParamList>()
        assertEquals(0, params.params.size)

        val ret = decl.retType.assert<AstVarRef>()
        assertEquals("void", ret.name?.text)
    }

}

class TestEnumDecl {

    @Test
    fun `empty decl`() {
        val decl = parseDecl<AstNamedEnumDecl>("enum Foo {}")
        val cases = decl.cases.assert<AstEnumCaseList>()

        assertEquals(0, cases.cases.size)
    }

    @Test
    fun `single case decl`() {
        val decl = parseDecl<AstNamedEnumDecl>("enum Foo { bar, }")
        val cases = decl.cases.assert<AstEnumCaseList>()
        assertEquals(1, cases.cases.size)

        val case1 = cases.cases[0].assert<AstEnumCase>()
        assertEquals("bar", case1.name?.text)
    }

    @Test
    fun `multi case decl`() {
        val decl = parseDecl<AstNamedEnumDecl>("enum Foo { bar, baz, }")
        val cases = decl.cases.assert<AstEnumCaseList>()
        assertEquals(2, cases.cases.size)

        val case1 = cases.cases[0].assert<AstEnumCase>()
        assertEquals("bar", case1.name?.text)

        val case2 = cases.cases[1].assert<AstEnumCase>()
        assertEquals("baz", case2.name?.text)
    }

    @Test
    fun `enum expr`() {
        val decl = parseExpr<AstEnumDecl>("enum { bar, }")
        val cases = decl.cases.assert<AstEnumCaseList>()
        assertEquals(1, cases.cases.size)

        val case1 = cases.cases[0].assert<AstEnumCase>()
        assertEquals("bar", case1.name?.text)
    }
}
