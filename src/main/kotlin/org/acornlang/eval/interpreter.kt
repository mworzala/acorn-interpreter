package org.acornlang

import org.acornlang.ast.*
import org.acornlang.eval.*
import org.acornlang.lexer.TokenType
import kotlin.math.max


// One interpreter exists per module in the program
class Interpreter(
    val env: Environment,
    val name: String,
    val source: String,
) : Context {
    val NULL = NullValue(this)

    private lateinit var root: AstModule

    private val decls = mutableMapOf<String, Value>()

    fun parse() {
        root = Parser(source).module()
    }

    fun findDeclByName(name: String): Value? {
        if (decls.containsKey(name))
            return decls[name]

        val visitor = object : AstVisitor<Value?, String>(null) {
            override fun visitModule(node: AstModule, param: String) =
                node.decls.firstNotNullOfOrNull {
                    it.visit(this, param)
                }

            override fun visitConstDecl(node: AstConstDecl, param: String): Value? {
                TODO("not implemented")
            }

            override fun visitNamedFnDecl(node: AstNamedFnDecl, param: String): Value? {
                if (node.name != param)
                    return null
                return FunctionValue.fromAst(this@Interpreter, node)
            }

            override fun visitStructDecl(node: AstStructDecl, param: String): Value? {
                if (node.name != param)
                    return null
                val structType = StructType.from(this@Interpreter, node)
                return TypeValue(this@Interpreter, structType)
            }

            override fun visitEnumDecl(node: AstEnumDecl, param: String): Value? {
                if (node.name != param)
                    return null
                val enumType = EnumType.from(this@Interpreter, node)
                return TypeValue(this@Interpreter, enumType)
            }
        }

        val decl = visitor.visit(root, name)
        if (decl != null)
            decls[name] = decl
        return decl
    }



    // Internal

    fun evaluateBlockInScope(block: AstBlock, context: Context): Value {
        println(block.stringify())
        val evalutor = BlockEvaluator(this)
        return evalutor.visitBlock(block, context)
    }

    // Context impl

    override val owner: Interpreter get() = this
    override val returnType: Type
        get() = TODO("Not yet implemented")

    override fun define(name: String, value: Value) {
        TODO("not allowed")
    }

    override fun get(name: String): Value? {
        return when (name) {
            "i8" -> TypeValue(this, Type.i8)
            "i16" -> TypeValue(this, Type.i16)
            "i32" -> TypeValue(this, Type.i32)
            "i64" -> TypeValue(this, Type.i64)
            "bool" -> TypeValue(this, Type.bool)
            else -> findDeclByName(name)
        }
    }

    fun getType(ast: AstNode): Type {
        return when (ast) {
            is AstType -> getType(ast)
            is AstPtrType -> getType(ast)
            else -> fail("Expected type")
        }
    }

    fun getType(ast: AstPtrType): Type = PtrType(getType(ast.inner))

    fun getType(ast: AstType): Type {
        val type = get(ast.name) as? TypeValue
        return type?.inner ?: fail("Type ${ast.name} not found")
    }


}

private class BlockEvaluator(owner: Interpreter) : AstVisitor<Value, Context>(owner.NULL) {
    override fun visitInt(node: AstInt, ctx: Context) = IntValue(ctx.owner, node.value, Type.from(node.value.toLong()) as IntType)
    override fun visitString(node: AstString, ctx: Context) = StringValue(ctx.owner, node.value)
    override fun visitBool(node: AstBool, ctx: Context) = BoolValue(ctx.owner, node.value)
    override fun visitRef(node: AstRef, ctx: Context): Value =
        ctx.get(node.name) ?: throw RuntimeException("Undefined reference ${node.name}")

    private fun evalArithmetic(node: AstBinary, ctx: Context): Value {
        val lhs = visit(node.lhs, ctx)
        val lhsType = lhs.type
        val rhs = visit(node.rhs, ctx)
        val rhsType = rhs.type

        // Ensure both are integers
        if (lhs !is IntValue || lhsType !is IntType)
            throw TypeError("Expected integer type, found $lhsType")
        if (rhs !is IntValue || rhsType !is IntType)
            throw TypeError("Expected integer type, found $rhsType")

        // Create a common type with the smallest number of bits to fit both
        val resultType = IntType(max(lhsType.bits, rhsType.bits))

        // Perform the operation
        val result = when (node.op.type) {
            TokenType.PLUS -> lhs.value + rhs.value
            TokenType.MINUS -> lhs.value - rhs.value
            TokenType.STAR -> lhs.value * rhs.value
            TokenType.SLASH -> lhs.value / rhs.value
            else -> fail("Unsupported arithmetic operator ${node.op.type}")
        }

        return IntValue(ctx.owner, result, resultType)
    }
    private fun evalComparison(node: AstBinary, ctx: Context): Value {
        val lhs = visit(node.lhs, ctx)
        val rhs = visit(node.rhs, ctx)

        // Perform the operation
        val result = when (node.op.type) {
            TokenType.EQEQ -> lhs == rhs
            TokenType.BANGEQ -> lhs != rhs
            else -> {
                val lhsType = lhs.type
                val rhsType = rhs.type

                // Ensure both are integers
                if (lhs !is IntValue || lhsType !is IntType)
                    throw TypeError("Expected integer type, found $lhsType")
                if (rhs !is IntValue || rhsType !is IntType)
                    throw TypeError("Expected integer type, found $rhsType")

                // Perform the operation
                when (node.op.type) {
                    TokenType.LT -> lhs.value < rhs.value
                    TokenType.LTEQ -> lhs.value <= rhs.value
                    TokenType.GT -> lhs.value > rhs.value
                    TokenType.GTEQ -> lhs.value >= rhs.value
                    else -> fail("Unsupported comparison operator ${node.op.type}")
                }
            }
        }

        return BoolValue(ctx.owner, result)
    }
    private fun evalLogical(node: AstBinary, ctx: Context): Value {
        val lhs = visit(node.lhs, ctx)
        val rhs = visit(node.rhs, ctx)

        // Ensure both are booleans
        if (lhs !is BoolValue)
            throw TypeError("Expected bool, found ${lhs.type}")
        if (rhs !is BoolValue)
            throw TypeError("Expected bool, found ${rhs.type}")

        // Perform the operation
        val result = when (node.op.type) {
            TokenType.AMPAMP -> lhs.value && rhs.value
            TokenType.BARBAR -> lhs.value || rhs.value
            else -> fail("Unsupported logical operator ${node.op.type}")
        }

        return BoolValue(ctx.owner, result)
    }
    override fun visitBinary(node: AstBinary, ctx: Context): Value {
        return when (node.op.type) {
            TokenType.PLUS, TokenType.MINUS,
            TokenType.STAR, TokenType.SLASH -> evalArithmetic(node, ctx)
            TokenType.EQEQ, TokenType.BANGEQ,
            TokenType.LT, TokenType.LTEQ,
            TokenType.GT, TokenType.GTEQ -> evalComparison(node, ctx)
            TokenType.AMPAMP, TokenType.BARBAR -> evalLogical(node, ctx)
            else -> fail("unsupported operator ${node.op.type}")
        }
    }

    override fun visitIf(node: AstIf, ctx: Context): Value {
        val cond = visit(node.condition, ctx)
        if (cond !is BoolValue)
            throw TypeError("Expected bool, found ${cond.type}")

        return if (cond.value)
            visit(node.thenBlock, ctx)
        else if (node.elseBlock != null)
            visit(node.elseBlock, ctx)
        else default
        //todo if without else should not be allowed as an expression
        //todo maybe some "is unused" flag in context?
    }

    override fun visitReturn(node: AstReturn, ctx: Context): Value {
        if (node.expr == null)
            return default

        val result = visit(node.expr, ctx)

        if (result.type == ctx.returnType)
            return result

        return result.coerceType(to=ctx.returnType)
    }

    override fun visitCall(node: AstCall, ctx: Context): Value {
        val target = visit(node.target, ctx)
        if (target !is FunctionValue)
            throw TypeError("Expected function, found ${target.type}")

        val args = node.args.map { visit(it, ctx) }
        val result = target.invoke(args)
        return result
    }

    override fun visitConstruct(node: AstConstruct, ctx: Context): Value {
        val typeValue = visit(node.target, ctx)
        if (typeValue !is TypeValue)
            throw TypeError("Expected type, found ${typeValue.type}")
        val structType = typeValue.inner
        if (structType !is StructType)
            throw TypeError("Expected struct type, found ${typeValue.inner}")
        val value = StructValue(ctx.owner, structType)
        for ((name, init) in node.fields)
            value.set(name, visit(init, ctx))
        return value
    }

    override fun visitAccess(node: AstAccess, ctx: Context): Value {
        val target = visit(node.target!!, ctx)
        if (target is StructValue)
            return target.get(node.field)
        if (target !is TypeValue)
            throw TypeError("Expected struct, enum, found ${target.type}")
        val type = target.inner
        if (type !is EnumType)
            throw TypeError("Expected enum, found ${target.inner}")
        val value = type.cases.indexOf(node.field)
        if (value == -1)
            throw TypeError("Unknown enum case '${node.field}' in ${type.name}")
        return EnumValue(ctx.owner, type, value)
    }

    override fun visitBlock(node: AstBlock, ctx: Context): Value {
        var result: Value = default
        for (stmt in node.stmts) {
            result = stmt.visit(this, ctx)
        }
        return result
    }

    override fun visitLet(node: AstLet, ctx: Context): Value {
        var init = visit(node.init, ctx)

        if (node.type != null) {
            val type = visit(node.type, ctx)
            if (type !is TypeValue)
                throw TypeError("Expected type, found ${type.type}")
            if (type != init.type)
                init = init.coerceType(to=type.inner)
        }

        ctx.define(node.name, init)
        return default
    }

    override fun visitPtrType(node: AstPtrType, ctx: Context): Value {
        val inner = visit(node.inner, ctx)
        if (inner !is TypeValue)
            throw TypeError("Expected type, found ${inner.type}")
        return TypeValue(ctx.owner, PtrType(inner.inner))
    }

    override fun visitType(node: AstType, ctx: Context): Value {
        val type = ctx.get(node.name)
        if (type !is TypeValue)
            throw TypeError("Expected type, found ${type?.type}")
        return type
    }
}