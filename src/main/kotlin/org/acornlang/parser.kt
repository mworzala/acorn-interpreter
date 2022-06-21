package org.acornlang

import org.acornlang.ast.*
import org.acornlang.lexer.Lexer
import org.acornlang.lexer.Token
import org.acornlang.lexer.TokenType

class Parser(
    private val source: String,
) {
    private val tokens = mutableListOf<Token>()
    private var cursor = 0

    init {
        val lexer = Lexer(source)
        var tok = lexer.next()
        while (tok.type != TokenType.EOF) {
            tokens.add(tok)
            tok = lexer.next()
        }
        // Add EOF token
        tokens.add(tok)
    }

    // Public parsers

    fun expr(): AstNode {
        if (peek(TokenType.LBRACE))
            return exprBlock()
        if (peek(TokenType.RETURN))
            return exprReturn()
        if (peek(TokenType.IF))
            return exprIf()
        if (peek(TokenType.WHILE))
            return exprWhile()
        return expr(0)
    }

    fun stmt(): AstNode {
        if (peek(TokenType.LET))
            return stmtLet()
        return expr()
    }

    fun decl(): AstNode {
        if (peek(TokenType.CONST))
            return declConst()
        if (peek(TokenType.FN, TokenType.FOREIGN))
            return declNamedFn()
        if (peek(TokenType.STRUCT))
            return declStruct()
        if (peek(TokenType.ENUM))
            return declEnum()
        fail("Expected const, foreign, fn, struct, enum. found ${peek().type}")
    }

    fun module(): AstModule {
        val decls = mutableListOf<AstNode>()
        while (!peek(TokenType.EOF)) {
            decls.add(decl())
        }
        return AstModule(decls)
    }

    // Parse helpers

    // SECTION: Expressions
    // Expression parsing & associated helpers

    private fun expr(minPower: Int): AstNode {
        val next = next()
        var lhs: AstNode = when {
            next.type == TokenType.NUMBER -> AstInt(next.text.toLong())
            next.type == TokenType.IDENT -> exprRefOrConstruct(AstRef(next.text))
            next.type == TokenType.DOT -> AstAccess(null, expect(TokenType.IDENT).text)
            next.type == TokenType.STRING -> AstString(next.text.substring(1, next.text.length - 1))
            next.type == TokenType.TRUE -> AstBool(true)
            next.type == TokenType.FALSE -> AstBool(false)
            next.type == TokenType.LPAREN -> {
                val lhs = expr(0)
                expect(TokenType.RPAREN)
                lhs
            }
            next.type.isPrefixOp() -> {
                val (_, rbp) = prefixPower(next.type)
                val rhs = expr(rbp)
                AstUnary(next, rhs)
            }
            else -> fail("Expected number, ident, string, true, false, '-'. found ${next.type}")
        }

        while (true) {
            val opToken = peek()
            val op = if (opToken.type.isInfixOp() || opToken.type.isPostfixOp())
                opToken.type
            else break

            val postfix = postfixPower(op)
            if (postfix != null) {
                val (lbp, _) = postfix
                if (lbp < minPower)
                    break
                next() // Eat the operator

                lhs = if (op == TokenType.LBRACKET) {
                    val inner = expr(0)
                    expect(TokenType.RBRACKET)
                    AstIndex(lhs, inner)
                } else if (op == TokenType.LPAREN) {
                    exprCall(lhs)
                } else {
                    // There are not currently any postfix operators not being handled specifically
                    AstUnary(opToken, lhs)
                }
                continue
            }

            val (lbp, rbp) = infixPower(op)
            if (lbp < minPower)
                break

            next() // Eat the operator token

            if (op == TokenType.DOT) {
                lhs = AstAccess(lhs, expect(TokenType.IDENT).text)
                continue
            }

            val rhs = expr(rbp)

            lhs = AstBinary(opToken, lhs, rhs)
        }

        return lhs
    }

    private fun prefixPower(op: TokenType): Pair<Int, Int> {
        return when (op) {
            TokenType.MINUS, TokenType.BANG -> Pair(-1, 5)
            else -> fail("Unknown prefix operator $op")
        }
    }

    private fun infixPower(op: TokenType): Pair<Int, Int> {
        return when (op) {
            TokenType.PLUS, TokenType.MINUS -> Pair(1, 2)
            TokenType.STAR, TokenType.SLASH -> Pair(3, 4)
            TokenType.EQEQ, TokenType.BANGEQ,
            TokenType.LT, TokenType.LTEQ,
            TokenType.GT, TokenType.GTEQ -> Pair(5, 6)
            TokenType.AMPAMP, TokenType.BARBAR -> Pair(7, 8)
            TokenType.DOT -> Pair(9, 10)
            else -> fail("Unknown operator $op")
        }
    }

    private fun postfixPower(op: TokenType): Pair<Int, Int>? {
        return when (op) {
            TokenType.LBRACKET -> Pair(11, -1)
            TokenType.LPAREN -> Pair(11, -1)
            else -> null
        }
    }

    private fun exprBlock(): AstNode {
        expect(TokenType.LBRACE)
        val body = mutableListOf<AstNode>()
        while (!peek(TokenType.RBRACE)) {
            body.add(stmt())
            if (!peek(TokenType.RBRACE))
                expect(TokenType.SEMICOLON)
        }
        expect(TokenType.RBRACE)
        return AstBlock(body)
    }

    private fun exprReturn(): AstNode {
        expect(TokenType.RETURN)

        var expr: AstNode? = null

        val next = peek().type
        if (next !in listOf(TokenType.SEMICOLON, TokenType.RBRACE, TokenType.EOF)) {
            expr = expr()
        }

        return AstReturn(expr)
    }

    private fun exprIf(): AstNode {
        expect(TokenType.IF)

        // Condition
        val condition = expr()

        // Then
        val thenBlock = exprBlock()

        // Else
        var elseBlock: AstNode? = null
        if (peek(TokenType.ELSE)) {
            next() // Eat the else
            elseBlock = if (peek(TokenType.IF))
                exprIf()
            else exprBlock()
        }

        return AstIf(condition, thenBlock, elseBlock)
    }

    private fun exprWhile(): AstNode {
        expect(TokenType.WHILE)

        val condition = expr()
        val thenBlock = exprBlock()

        return AstWhile(condition, thenBlock)
    }

    private fun exprCall(target: AstNode): AstNode {
        // Parse arguments
        val args = mutableListOf<AstNode>()

        while (peek().type != TokenType.RPAREN) {
            args.add(expr(0))
            if (peek().type == TokenType.RPAREN)
                break
            expect(TokenType.COMMA)
        }

        expect(TokenType.RPAREN)

        return AstCall(target, args)
    }

    private fun exprRefOrConstruct(ident: AstNode): AstNode {
        if (!match(TokenType.LBRACE))
            return ident

        // We are now in a construct block

        val fields = mutableListOf<Pair<String, AstNode>>()
        while (peek().type != TokenType.RBRACE) {
            val nameToken = expect(TokenType.IDENT)
            expect(TokenType.COLON)
            fields.add(nameToken.text to expr(0))
            if (peek().type == TokenType.RBRACE)
                break
            expect(TokenType.COMMA)
        }

        expect(TokenType.RBRACE)

        return AstConstruct(ident, fields)
    }

    private fun typeExpr(): AstNode {
        return if (peek(TokenType.STAR)) {
            next() // Eat the star
            AstPtrType(typeExpr())
        } else if (peek(TokenType.IDENT)) {
            AstType(next().text)
        } else {
            fail("Expected *, ident, found ${peek().type}")
        }
    }


    // SECTION: Statements
    // Statement parsing and associated helpers

    private fun stmtLet(): AstNode {
        expect(TokenType.LET)

        val name = expect(TokenType.IDENT).text

        // Type expression
        var type: AstNode? = null
        if (match(TokenType.COLON))
            type = typeExpr()

        // Init expr
        expect(TokenType.EQ)
        val init = expr()

        return AstLet(name, type, init)
    }


    // SECTION: Declarations
    // Top level declarations within a module

    private fun declConst(): AstNode {
        expect(TokenType.CONST)

        val name = expect(TokenType.IDENT).text

        // Type expression
        expect(TokenType.COLON)
        val type = typeExpr()

        // Init expr
        expect(TokenType.EQ)
        val init = expr()

        expect(TokenType.SEMICOLON)

        return AstConstDecl(name, type, init)
    }

    private fun declNamedFn(): AstNode {
        // Parse a single function parameter
        fun fnParam(): AstNode {
            val name = expect(TokenType.IDENT).text

            expect(TokenType.COLON)
            val type = typeExpr()

            return AstFnParam(name, type)
        }

        val foreign = match(TokenType.FOREIGN)

        expect(TokenType.FN)

        val name = expect(TokenType.IDENT).text

        // Parameters
        expect(TokenType.LPAREN)
        val params = mutableListOf<AstNode>()
        while (!peek(TokenType.RPAREN)) {
            params.add(fnParam())
            if (!peek(TokenType.RPAREN))
                expect(TokenType.COMMA)
        }
        expect(TokenType.RPAREN)

        // Return type
        var retTy: AstNode? = null
        if (!peek(TokenType.SEMICOLON, TokenType.LBRACE))
            retTy = typeExpr()

        // Body
        var body: AstNode? = null
        if (!foreign)
            body = exprBlock()
        else {
            expect(TokenType.SEMICOLON)
        }

        return AstNamedFnDecl(name, foreign, retTy, params, body)
    }

    private fun declStruct(): AstNode {
        // Parse a single struct field
        fun structField(): AstNode {
            val name = expect(TokenType.IDENT).text

            expect(TokenType.COLON)
            val type = typeExpr()

            return AstStructField(name, type)
        }

        expect(TokenType.STRUCT)
        val name = expect(TokenType.IDENT).text

        // Fields
        expect(TokenType.LBRACE)
        val fields = mutableListOf<AstNode>()
        while (!peek(TokenType.RBRACE)) {
            fields.add(structField())
            expect(TokenType.COMMA)
        }
        expect(TokenType.RBRACE)

        return AstStructDecl(name, fields)
    }

    private fun declEnum(): AstNode {
        // Parse a single enum case
        fun enumCase(): AstNode {
            val name = expect(TokenType.IDENT).text

            return AstEnumCase(name)
        }

        expect(TokenType.ENUM)
        val name = expect(TokenType.IDENT).text

        // Fields
        expect(TokenType.LBRACE)
        val cases = mutableListOf<AstNode>()
        while (!peek(TokenType.RBRACE)) {
            cases.add(enumCase())
            expect(TokenType.COMMA)
        }
        expect(TokenType.RBRACE)

        return AstEnumDecl(name, cases)
    }

    // Helper functions

    private fun next(): Token {
        if (cursor >= tokens.size) {
            throw IllegalStateException("Unexpected end of input")
        }
        return tokens[cursor++]
    }

    private fun peek(): Token {
        if (cursor >= tokens.size) {
            throw IllegalStateException("Unexpected end of input")
        }
        return tokens[cursor]
    }

    private fun peek(vararg types: TokenType): Boolean {
        if (cursor >= tokens.size)
            return false
        return tokens[cursor].type in types
    }

    private fun match(vararg types: TokenType): Boolean {
        if (cursor >= tokens.size)
            return false
        if (tokens[cursor].type in types) {
            next()
            return true
        }
        return false
    }

    private fun expect(type: TokenType): Token {
        val tok = next()
        if (tok.type != type) {
            fail("Expected $type. Found ${tok.type}")
        }
        return tok
    }

    private val Token.text get() = source.substring(span.start, span.end)

}