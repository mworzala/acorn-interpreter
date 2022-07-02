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

class TestStructDecl {

    @Test
    fun `empty struct`() {
        val decl = parseDecl<AstNamedStructDecl>("struct Foo {}")
        val fields = decl.fields.assert<AstStructFieldList>()

        assertEquals(0, fields.fields.size)
    }

    @Test
    fun `single field struct`() {
        val decl = parseDecl<AstNamedStructDecl>("struct Foo { bar: i32, }")
        val fields = decl.fields.assert<AstStructFieldList>()
        assertEquals(1, fields.fields.size)

        val field1 = fields.fields[0].assert<AstStructField>()
        assertEquals("bar", field1.name?.text)
        val type1 = field1.type.assert<AstVarRef>()
        assertEquals("i32", type1.name?.text)
    }

    @Test
    fun `multi field struct`() {
        val decl = parseDecl<AstNamedStructDecl>("struct Foo { bar: i32, baz: i32, }")
        val fields = decl.fields.assert<AstStructFieldList>()
        assertEquals(2, fields.fields.size)

        val field1 = fields.fields[0].assert<AstStructField>()
        assertEquals("bar", field1.name?.text)
        val type1 = field1.type.assert<AstVarRef>()
        assertEquals("i32", type1.name?.text)

        val field2 = fields.fields[1].assert<AstStructField>()
        assertEquals("baz", field2.name?.text)
        val type2 = field2.type.assert<AstVarRef>()
        assertEquals("i32", type2.name?.text)
    }

    @Test
    fun `struct expr`() {
        val decl = parseExpr<AstStructDecl>("struct { foo: i32, }")
        val fields = decl.fields.assert<AstStructFieldList>()
        assertEquals(1, fields.fields.size)

        val field1 = fields.fields[0].assert<AstStructField>()
        assertEquals("foo", field1.name?.text)
        val type1 = field1.type.assert<AstVarRef>()
        assertEquals("i32", type1.name?.text)
    }

    @Test
    fun `struct container members before and after field list`() {
        val decl = parseDecl<AstNamedStructDecl>("""
            struct Foo { 
                const before = 1;
                
                bar: i32,
                 
                const after = 2;
            }
        """.trimIndent())

        val fields = decl.fields.assert<AstStructFieldList>()
        assertEquals(1, fields.fields.size)
        val field1 = fields.fields[0].assert<AstStructField>()
        assertEquals("bar", field1.name?.text)
        val type1 = field1.type.assert<AstVarRef>()
        assertEquals("i32", type1.name?.text)

        val members = decl.decls
        assertEquals(2, members.size)
        val member1 = members[0].assert<AstConstDecl>()
        assertEquals("before", member1.name?.text)
        val member2 = members[1].assert<AstConstDecl>()
        assertEquals("after", member2.name?.text)
    }
}

class TestUnionDecl {

    @Test
    fun `empty union`() {
        val decl = parseDecl<AstNamedUnionDecl>("union Foo {}")
        val members = decl.members.assert<AstUnionMemberList>()

        assertEquals(0, members.members.size)
    }

    @Test
    fun `single member union`() {
        val decl = parseDecl<AstNamedUnionDecl>("union Foo { Bar: i32, }")
        val members = decl.members.assert<AstUnionMemberList>()
        assertEquals(1, members.members.size)

        val member1 = members.members[0].assert<AstUnionMember>()
        assertEquals("Bar", member1.name?.text)
        val type1 = member1.type.assert<AstVarRef>()
        assertEquals("i32", type1.name?.text)
    }

    @Test
    fun `multi member union`() {
        val decl = parseDecl<AstNamedUnionDecl>("union Foo { Bar: i32, Baz: f32, }")
        val members = decl.members.assert<AstUnionMemberList>()
        assertEquals(2, members.members.size)

        val member1 = members.members[0].assert<AstUnionMember>()
        assertEquals("Bar", member1.name?.text)
        val type1 = member1.type.assert<AstVarRef>()
        assertEquals("i32", type1.name?.text)

        val member2 = members.members[1].assert<AstUnionMember>()
        assertEquals("Baz", member2.name?.text)
        val type2 = member2.type.assert<AstVarRef>()
        assertEquals("f32", type2.name?.text)
    }

    @Test
    fun `Union expr`() {
        val decl = parseExpr<AstUnionDecl>("union { Foo: i32, }")
        val members = decl.members.assert<AstUnionMemberList>()
        assertEquals(1, members.members.size)

        val member1 = members.members[0].assert<AstUnionMember>()
        assertEquals("Foo", member1.name?.text)
        val type1 = member1.type.assert<AstVarRef>()
        assertEquals("i32", type1.name?.text)
    }

    @Test
    fun `Union container members before and after Member list`() {
        val decl = parseDecl<AstNamedUnionDecl>("""
            union Foo { 
                const before = 1;
                
                Bar: i32,
                 
                const after = 2;
            }
        """.trimIndent())

        val members = decl.members.assert<AstUnionMemberList>()
        assertEquals(1, members.members.size)
        val member1 = members.members[0].assert<AstUnionMember>()
        assertEquals("Bar", member1.name?.text)
        val type1 = member1.type.assert<AstVarRef>()
        assertEquals("i32", type1.name?.text)

        val decls = decl.decls
        assertEquals(2, decls.size)
        val decl1 = decls[0].assert<AstConstDecl>()
        assertEquals("before", decl1.name?.text)
        val decl2 = decls[1].assert<AstConstDecl>()
        assertEquals("after", decl2.name?.text)
    }
}

class TestSpecDecl {

    @Test
    fun `empty spec`() {
        val decl = parseDecl<AstNamedSpecDecl>("spec Foo {}")
        val members = decl.members

        assertEquals(0, members.size)
    }

    @Test
    fun `single member spec`() {
        val decl = parseDecl<AstNamedSpecDecl>("""
            spec Foo {
                fn bar() void;
            }
        """.trimIndent())
        val members = decl.members
        assertEquals(1, members.size)

        val member1 = members[0].assert<AstNamedFnDecl>()
        assertEquals("bar", member1.name?.text)
    }

    @Test
    fun `spec expr`() {
        val decl = parseExpr<AstSpecDecl>("""
            spec {
                fn bar() void;
            }
        """.trimIndent())
        val members = decl.members
        assertEquals(1, members.size)

        val member1 = members[0].assert<AstNamedFnDecl>()
        assertEquals("bar", member1.name?.text)
    }
}
