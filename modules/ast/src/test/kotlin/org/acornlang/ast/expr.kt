package org.acornlang.ast

import org.acornlang.parse.ParseError
import org.acornlang.parse.parse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

fun parseExprRaw(source: String): AstExpr {
    val result = parse("const a = $source;")
    assertEquals(listOf<ParseError>(), result.errors)
    val module = AstModule(result.node)
    val const = module.decls.first() as AstConstDecl
    val expr = const.init
    assertNotNull(expr)
    return expr!!
}

inline fun <reified T : AstExpr> parseExpr(source: String): T {
    val decl = parseExprRaw(source)
    assertEquals(T::class, decl::class)
    return decl as T
}

class TestLiteral {

    @Test
    fun `type = number`() {
        val decl = parseExpr<AstLiteral>("1")

        assertEquals(AstLiteral.Type.NUMBER, decl.type)
        assertEquals("1", decl.value?.text)
    }

    @Test
    fun `type = string`() {
        val decl = parseExpr<AstLiteral>("\"foo\"")

        assertEquals(AstLiteral.Type.STRING, decl.type)
        assertEquals("\"foo\"", decl.value?.text)
    }

    @Test
    fun `type = bool`() {
        val decl = parseExpr<AstLiteral>("true")

        assertEquals(AstLiteral.Type.BOOL, decl.type)
        assertEquals("true", decl.value?.text)
    }

}

class TestVarRef {

    @Test
    fun `basic var ref`() {
        val decl = parseExpr<AstVarRef>("foo")

        assertEquals("foo", decl.name?.text)
    }

}

class TestIntrinsic {

    @Test
    fun `intrinsic ref`() {
        val decl = parseExpr<AstIntrinsic>("import!")

        assertEquals("import!", decl.name?.text)
    }

}

class TestBinaryExpr {

    @Test
    fun `simple case`() {
        val decl = parseExpr<AstBinary>("1+2")

        val lhs = decl.lhs.assert<AstLiteral>()
        assertEquals("1", lhs.value?.text)

        assertEquals("+", decl.operator?.text)

        val rhs = decl.rhs.assert<AstLiteral>()
        assertEquals("2", rhs.value?.text)
    }

}

class TestUnaryExpr {

    @Test
    fun `simple case`() {
        val decl = parseExpr<AstUnary>("-1")

        assertEquals("-", decl.operator?.text)

        val operand = decl.operand.assert<AstLiteral>()
        assertEquals("1", operand.value?.text)
    }

}

class TestParenExpr {

    @Test
    fun `intrinsic ref`() {
        val decl = parseExpr<AstParenGroup>("(1)")
        val inner = decl.expr.assert<AstLiteral>()

        assertEquals("1", inner.value?.text)
    }

}

class TestAssignExpr {

    @Test
    fun `assign to var`() {
        val decl = parseExpr<AstAssign>("a = 1")

        val target = decl.target.assert<AstVarRef>()
        assertEquals("a", target.name?.text)

        val expr = decl.expr.assert<AstLiteral>()
        assertEquals("1", expr.value?.text)
    }

}

class TestMemberAccess {

    @Test
    fun `single member`() {
        val decl = parseExpr<AstMemberAccess>("foo.bar")

        val target = decl.target.assert<AstVarRef>()
        assertEquals("foo", target.name?.text)

        val member = decl.member.assert<AstVarRef>()
        assertEquals("bar", member.name?.text)
    }

}

class TestTypeUnion {

    @Test
    fun `single union`() {
        val decl = parseExpr<AstTypeUnion>("Foo & Bar")

        val lhs = decl.lhs.assert<AstVarRef>()
        assertEquals("Foo", lhs.name?.text)

        val rhs = decl.rhs.assert<AstVarRef>()
        assertEquals("Bar", rhs.name?.text)
    }

}

class TestIndex {

    @Test
    fun `index single expr`() {
        val decl = parseExpr<AstIndex>("foo[1]")

        val target = decl.target.assert<AstVarRef>()
        assertEquals("foo", target.name?.text)

        val expr = decl.expr.assert<AstLiteral>()
        assertEquals("1", expr.value?.text)
    }

}

class TestArrayLiteral {

    @Test
    fun `empty array`() {
        val decl = parseExpr<AstArrayLiteral>("[]")

        assertEquals(0, decl.exprs.size)
    }

    @Test
    fun `single element array`() {
        val decl = parseExpr<AstArrayLiteral>("[1]")
        assertEquals(1, decl.exprs.size)

        val expr1 = decl.exprs[0].assert<AstLiteral>()
        assertEquals("1", expr1.value?.text)
    }

    @Test
    fun `multi element array`() {
        val decl = parseExpr<AstArrayLiteral>("[1, 2]")
        assertEquals(2, decl.exprs.size)

        val expr1 = decl.exprs[0].assert<AstLiteral>()
        assertEquals("1", expr1.value?.text)

        val expr2 = decl.exprs[1].assert<AstLiteral>()
        assertEquals("2", expr2.value?.text)
    }

}

class TestTupleLiteral {

    @Test
    fun `empty tuple`() {
        val decl = parseExpr<AstTupleLiteral>("()")

        assertEquals(0, decl.exprs.size)
    }

    @Test
    fun `single element tuple`() {
        val decl = parseExpr<AstTupleLiteral>("(1,)")
        assertEquals(1, decl.exprs.size)

        val expr1 = decl.exprs[0].assert<AstLiteral>()
        assertEquals("1", expr1.value?.text)
    }

    @Test
    fun `multi element tuple`() {
        val decl = parseExpr<AstTupleLiteral>("(1, 2,)")
        assertEquals(2, decl.exprs.size)

        val expr1 = decl.exprs[0].assert<AstLiteral>()
        assertEquals("1", expr1.value?.text)

        val expr2 = decl.exprs[1].assert<AstLiteral>()
        assertEquals("2", expr2.value?.text)
    }

}

class TestBlockExpr {

    @Test
    fun `empty block`() {
        val decl = parseExpr<AstBlock>("{}")

        assertEquals(0, decl.stmts.size)
    }

    @Test
    fun `single stmt block`() {
        val decl = parseExpr<AstBlock>("{1;}")

        assertEquals(1, decl.stmts.size)
    }

    @Test
    fun `multi stmt block`() {
        val decl = parseExpr<AstBlock>("{1;2;}")

        assertEquals(2, decl.stmts.size)
    }

    @Test
    fun `block detail & iret`() {
        val decl = parseExpr<AstBlock>("{1;2}")
        val stmts = decl.stmts

        val stmt1 = stmts[0].assert<AstExprStmt>()
        val expr1 = stmt1.expr.assert<AstLiteral>()
        assertEquals("1", expr1.value?.text)

        val stmt2 = stmts[1].assert<AstExprStmt>()
        val expr2 = stmt2.expr.assert<AstImplicitReturn>()
        val expr2expr = expr2.expr.assert<AstLiteral>()
        assertEquals("2", expr2expr.value?.text)
    }

}
