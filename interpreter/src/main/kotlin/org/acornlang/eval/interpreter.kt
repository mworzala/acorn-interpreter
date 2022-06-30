package org.acornlang.eval

import org.acornlang.Environment
import org.acornlang.Parser
import org.acornlang.syntax.*
import org.acornlang.fail
import org.acornlang.lex.TokenType
//import java.lang.foreign.Linker
//import java.lang.foreign.SegmentAllocator
import java.lang.invoke.MethodHandle
import java.nio.file.Path
import kotlin.math.max

class ModuleInterpreter(
    val env: Environment,
    val dir: Path,
    val name: String,
    val source: String,
) : Context {

    private lateinit var root: AstModule
    private val decls = mutableMapOf<String, Value?>()

    fun parse() {
        root = Parser(source).module()
    }

    fun findDecl(name: String): Value? {
        if (decls.containsKey(name))
            return decls[name]
        val decl = DeclFinder(this).visit(root, name)
        if (decl != null)
            decls[name] = decl
        return decl
    }


    // SECTION : Context Impl

    override fun call(spec: FnType, block: AstBlock, argValues: List<Value>): Value {
        if (spec.paramTypes.size != argValues.size)
            fail("Expected ${spec.paramTypes.size} arguments, but got ${argValues.size}")

        val scope = ScopeImpl(this)

        // Add arguments to scope
        for ((name, value) in spec.paramNames.zip(argValues)) {
            //todo type check me
            scope.define(name, value, false) //todo mut
        }

        val interpreter = BlockInterpreter(this)
        val returnValue = interpreter.visit(block, scope)

        //todo type coercion required here
        if (spec.ret != Type.void && returnValue.type != spec.ret)
            fail("Expected return type ${spec.ret}, got ${returnValue.type}")

        return returnValue
    }

    override fun callForeign(spec: FnType, handle: MethodHandle, args: List<Value>): Value {
//        val allocator = SegmentAllocator.implicitAllocator()
        //todo type check args
//        val foreignArgs = args.map { Value.toForeignValue(allocator, it) }
//
        // I cannot make the varargs here work for the life of me.
//        val result = when (foreignArgs.size) {
//            0 -> handle.invoke()
//            1 -> handle.invoke(foreignArgs[0])
//            2 -> handle.invoke(foreignArgs[0], foreignArgs[1])
//            3 -> handle.invoke(foreignArgs[0], foreignArgs[1], foreignArgs[2])
//            4 -> handle.invoke(foreignArgs[0], foreignArgs[1], foreignArgs[2], foreignArgs[3])
//            5 -> handle.invoke(foreignArgs[0], foreignArgs[1], foreignArgs[2], foreignArgs[3], foreignArgs[4])
//            6 -> handle.invoke(foreignArgs[0], foreignArgs[1], foreignArgs[2], foreignArgs[3], foreignArgs[4], foreignArgs[5])
//            else -> fail("Unsupported number of arguments for foreign function")
//        }
//
//        return Value.fromForeignValue(this, result, spec.ret)
        TODO("No foreign functions until i fix compilation")
    }

    override val parent: Scope get() = this

    override fun define(name: String, value: Value, mut: Boolean) {
        throw IllegalStateException("Cannot define in module context")
    }

    override fun get(name: String, mut: Boolean): Value? {
        if (mut) throw IllegalStateException("Cannot get mutable in module context")
        return when (name) {
            "i8" -> TypeValue(this, Type.i8)
            "i16" -> TypeValue(this, Type.i16)
            "i32" -> TypeValue(this, Type.i32)
            "i64" -> TypeValue(this, Type.i64)
            "bool" -> TypeValue(this, Type.bool)
            "str" -> TypeValue(this, Type.str)
            else -> findDecl(name)
        }
    }


    // SECTION : Decl finder


}

private class DeclFinder(
    val module: ModuleInterpreter,
    val container: Scope = module,
) : AstVisitor<Value?, String>() {
    override fun visitModule(node: AstModule, ctx: String) =
        node.decls.firstNotNullOfOrNull {
            it.visit(this, ctx)
        }

    override fun visitConstDecl(node: AstConstDecl, ctx: String): Value? {
        if (ctx != node.name) return null

        return node.init.visit(this, ctx)
    }

    override fun visitIntrinsicCall(node: AstIntrinsicCall, ctx: String): Value? {
        if (node.name != "import")
            throw IllegalStateException("Intrinsic '${node.name}' not supported in module context")
        if (node.args.size != 1)
            throw IllegalStateException("Intrinsic '${node.name}' takes one argument")
        val moduleName = (node.args.firstOrNull() as? AstString)?.value
            ?: fail("Expected string literal")
        val modulePath = this.module.dir.resolve(moduleName).toAbsolutePath()
        val module = this.module.env.getInterpreter(modulePath)
        return ModuleValue(this.module, module)
    }

    override fun visitNamedFnDecl(node: AstNamedFnDecl, ctx: String): Value? {
        if (ctx != node.name) return null
        return parseNamedFnDecl(node)
    }

    fun parseNamedFnDecl(node: AstNamedFnDecl): Value {
        val retTy = node.retType?.asType(container) ?: Type.void
        val paramNames = mutableListOf<String>()
        val paramTypes = node.params.mapIndexed { i, p ->
            val param = p as AstFnParam
            if (paramNames.contains(param.name))
                throw IllegalStateException("Duplicate parameter name '${param.name}' in '${node.name}'")
            paramNames.add(param.name)

            if (param.name == "self" && param.type == null)
                SelfType()
            else param.type?.asType(container)
                ?: throw IllegalStateException("Expected type for parameter '${param.name}' in '${node.name}'")
        }
        val fnType = FnType(retTy, paramNames, paramTypes)

        return if (node.foreign) {
//                val linker = Linker.nativeLinker()
//                val func = linker.defaultLookup()
//                    .lookup(node.name)
//                    .orElseThrow()
//                val descriptor = Type.getNativeDescriptor(fnType)
//                val handle = linker.downcallHandle(func, descriptor)
//                ForeignFnValue(this@ModuleInterpreter, fnType, handle)
            TODO("No foreign functions until i fix compilation")
        } else {
            NativeFnValue(this.module, fnType, node.body as AstBlock)
        }
    }

    override fun visitStructDecl(node: AstStructDecl, ctx: String): Value? {
        if (ctx != node.name) return null
        val memberNames = mutableListOf<String>()
        val memberTypes = mutableListOf<Type>()
        val memberDefaults = node.fields.map {
            if (it is AstStructField) {
                val name = it.name
                if (memberNames.contains(name))
                    throw IllegalStateException("Duplicate member name '$name' in '$ctx'")
                memberNames.add(name)

                memberTypes.add(it.type.asType(container))
                null
            } else if (it is AstNamedFnDecl) {
                val name = it.name
                if (memberNames.contains(name))
                    throw IllegalStateException("Duplicate member name '$name' in '$ctx'")
                memberNames.add(name)

                val value = parseNamedFnDecl(it)
                memberTypes.add(value.type)
                value
            } else {
                throw IllegalStateException("Unexpected node type in struct '$ctx'")
            }
        }
        val structType = StructType(node.name, memberNames, memberTypes, memberDefaults)
        return TypeValue(this.module, structType)
    }

    override fun visitEnumDecl(node: AstEnumDecl, ctx: String): Value? {
        if (ctx != node.name) return null
        val cases = node.cases.map { (it as AstEnumCase).name }
        val enumType = EnumType(cases)
        return TypeValue(this.module, enumType)
    }
}


class TypeError(message: String) : RuntimeException(message) {
    constructor(expected: Type, actual: Type) : this("Expected type $expected, found $actual")
}

class BlockInterpreter(val context: Context) : AstVisitor<Value, Scope>({ Type.empty.default(context) }) {
    override fun visitInt(node: AstInt, scope: Scope) = IntValue(context, Type.i32, node.value)
    override fun visitString(node: AstString, scope: Scope) = StrValue(context, Type.str, node.value)
    override fun visitBool(node: AstBool, scope: Scope) = BoolValue(context, Type.bool, node.value)
    override fun visitIdent(node: AstIdent, scope: Scope): Value =
        // todo mutable refs
        scope.get(node.name, false) ?: throw RuntimeException("Undefined reference ${node.name}")

    private fun evalArithmetic(node: AstBinary, scope: Scope): Value {
        val lhs = visit(node.lhs, scope)
        val rhs = visit(node.rhs, scope)

        // Ensure both are integers
        if (lhs !is IntValue)
            throw TypeError("Expected integer type, found ${lhs.type}")
        if (rhs !is IntValue)
            throw TypeError("Expected integer type, found ${rhs.type}")

        // Create a common type with the smallest number of bits to fit both
        val resultType = IntType(max(lhs.bits, rhs.bits))

        // Perform the operation
        val result = when (node.op.type) {
            TokenType.PLUS -> lhs.value + rhs.value
            TokenType.MINUS -> lhs.value - rhs.value
            TokenType.STAR -> lhs.value * rhs.value
            TokenType.SLASH -> lhs.value / rhs.value
            else -> fail("Unsupported arithmetic operator ${node.op.type}")
        }

        return IntValue(context, resultType, result)
    }
    private fun evalComparison(node: AstBinary, scope: Scope): Value {
        val lhs = visit(node.lhs, scope)
        val rhs = visit(node.rhs, scope)

        // Perform the operation
        val result = when (node.op.type) {
            TokenType.EQEQ -> lhs == rhs
            TokenType.BANGEQ -> lhs != rhs
            else -> {
                // Ensure both are integers
                if (lhs !is IntValue)
                    throw TypeError("Expected integer type, found ${lhs.type}")
                if (rhs !is IntValue)
                    throw TypeError("Expected integer type, found ${rhs.type}")

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

        return BoolValue(context, Type.bool, result)
    }
    private fun evalLogical(node: AstBinary, scope: Scope): Value {
        val lhs = visit(node.lhs, scope)
        val rhs = visit(node.rhs, scope)

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

        return BoolValue(context, Type.bool, result)
    }
    override fun visitBinary(node: AstBinary, scope: Scope): Value {
        return when (node.op.type) {
            TokenType.PLUS, TokenType.MINUS,
            TokenType.STAR, TokenType.SLASH -> evalArithmetic(node, scope)
            TokenType.EQEQ, TokenType.BANGEQ,
            TokenType.LT, TokenType.LTEQ,
            TokenType.GT, TokenType.GTEQ -> evalComparison(node, scope)
            TokenType.AMPAMP, TokenType.BARBAR -> evalLogical(node, scope)
            else -> fail("unsupported operator ${node.op.type}")
        }
    }

    override fun visitMakeRef(node: AstMakeRef, ctx: Scope): Value {
        val value = visit(node.target, ctx)
        return RefValue(context, RefType(value.type), value)
    }

    override fun visitType(node: AstType, ctx: Scope): Value {
        return TypeValue(context, node.asType(ctx))
    }

    override fun visitArrayType(node: AstArrayType, ctx: Scope): Value {
        val inner = visit(node.inner, ctx)
        if (inner !is TypeValue)
            throw TypeError("Expected type, found ${inner.type}")
        return TypeValue(context, ArrayType(inner.value))
    }

    override fun visitIf(node: AstIf, scope: Scope): Value {
        val cond = visit(node.condition, scope)
        if (cond !is BoolValue)
            throw TypeError("Expected bool, found ${cond.type}")

        return if (cond.value)
            visit(node.thenBlock, scope)
        else if (node.elseBlock != null)
            visit(node.elseBlock, scope)
        else default
        //todo if without else should not be allowed as an expression
        //todo maybe some "is unused" flag in context?
    }

    override fun visitReturn(node: AstReturn, scope: Scope): Value {
        if (node.expr == null)
            return default

        val result = visit(node.expr, scope)

//        if (result.type == ctx.returnType)
//            return result

        //todo check return type??
        //todo returns need to set some flag saying no more instructions should be evaluated (eg it must be the final inst in a block)
//        return result.coerceType(to=ctx.returnType)
        return result
    }

    override fun visitCall(node: AstCall, scope: Scope): Value {
        val (self, target) = if (node.target is AstAccess)
            visitAccessWithTarget(node.target, scope)
        else null to visit(node.target, scope)
        if (target !is FnValue)
            throw TypeError("Expected function, found ${target.type}")

        var args = node.args.map { visit(it, scope).clone() }
        if (target.type.paramNames.getOrNull(0) == "self")
            args = listOf(self!!) + args
        return target.invoke(args)
    }

    override fun visitConstruct(node: AstConstruct, scope: Scope): Value {
        val typeValue = visit(node.target, scope)
        if (typeValue !is TypeValue)
            throw TypeError("Expected type, found ${typeValue.type}")
        val structType = typeValue.value
        if (structType !is StructType)
            throw TypeError("Expected struct type, found ${typeValue.value}")
        val value = StructValue(context, structType)
        for ((name, init) in node.fields)
            value.set(name, visit(init, scope))
        return value
    }

    override fun visitAccess(node: AstAccess, scope: Scope): Value {
        return visitAccessWithTarget(node, scope).second
    }

    private fun visitAccessWithTarget(node: AstAccess, scope: Scope): Pair<Value?, Value> {
        var target = visit(node.target!!, scope)
        if (target is RefValue)
            target = target.value
        if (target is ContainerValue)
            return Pair(target, target.get(node.field))
        if (target !is TypeValue)
            throw TypeError("Expected struct, enum, found ${target.type}")
        val type = target.value
        if (type !is EnumType)
            throw TypeError("Expected enum, found ${target.value}")
        val value = type.caseNames.indexOf(node.field)
        if (value == -1)
            throw TypeError("Unknown enum case '${node.field}' in $type")
        return Pair(null, EnumValue(context, type, value))
    }

    override fun visitAssign(node: AstAssign, ctx: Scope): Value {
        val target = visit(node.target, ctx)
        val value = visit(node.value, ctx)

        //todo check for mut
        target.assign(to=value)
        return value
    }

    override fun visitIndex(node: AstIndex, ctx: Scope): Value {
        var target = visit(node.target, ctx)
        if (target is RefValue)
            target = target.value
        if (target !is IndexableValue)
            throw TypeError("Expected indexable, found ${target.type}")
        val index = visit(node.index, ctx)
        return target.get(index)
    }

    override fun visitBlock(node: AstBlock, scope: Scope): Value {
        // New scope for block
        val blockScope = ScopeImpl(scope)

        // Eval block
        var result: Value = default
        for (stmt in node.stmts) {
            result = stmt.visit(this, blockScope)
        }

        // Return final result
        //todo this is not really valid. Should only return the last statement if it does not have a semicolon or is a return.
        return result
    }

    override fun visitLet(node: AstLet, scope: Scope): Value {
        var init = visit(node.init, scope)

        if (node.type != null) {
            val type = visit(node.type, scope)
            if (type !is TypeValue)
                throw TypeError("Expected type, found ${type.type}")
            if (type.value != init.type)
                //todo type coercion rules
                TODO("Implicit type coercion from ${init.type} to ${type.value}")
//                init = init.coerceType(to=type.inner)
        }

        if (node.mut) {
            val mutType = init.type.asMut()
                ?: throw TypeError("Cannot have mutable ${init.type}")
            init = init.withType(mutType)
        }

        scope.define(node.name, init, false)
        return default
    }

    override fun visitIntrinsicCall(node: AstIntrinsicCall, ctx: Scope): Value {
        return when (node.name) {
            // Print a value to stdout
            "dbg" -> {
                print("dbg :: ")
                for (arg in node.args) {
                    print(visit(arg, ctx).toString())
                    print(" ")
                }
                println()
                TupleValue(context, Type.empty)
            }
            // Create an array and return a slice to the whole thing
            "slice" -> {
                if (node.args.size < 1)
                    throw TypeError("Expected at least one argument to @slice")
                val items = node.args.map { visit(it, ctx) }
                val type = items.first().type
                if (items.any { it.type != type })
                    throw TypeError("All arguments to @slice must be of the same type")

                val arrayType = ArrayType(type)
                val array = ArrayValue(context, arrayType, items)
                SliceValue(context, arrayType, array, 0, array.length)
            }
            "slice_append" -> {
                if (node.args.size < 2)
                    throw TypeError("Expected at least two arguments to @slice_append")
                val existing = visit(node.args[0], ctx) as? SliceValue
                    ?: throw TypeError("Expected slice as first argument to @slice_append")
                val type = existing.type.type

                val items = node.args.subList(1, node.args.size).map { visit(it, ctx) }
                if (items.any { it.type != type })
                    throw TypeError("All arguments to @slice_append must be of the same type as the slice")

                val newArray = ArrayValue(context, existing.type, existing.contentsCloned() + items)
                SliceValue(context, existing.type, newArray, 0, newArray.length)
            }
            "slice_range" -> {
                if (node.args.size < 3)
                    throw TypeError("Expected 3 arguments to @slice_range")
                val existing = visit(node.args[0], ctx) as? SliceValue
                    ?: throw TypeError("Expected slice as first argument to @slice_append")
                val start = visit(node.args[1], ctx) as? IntValue
                    ?: throw TypeError("Expected int as second argument to @slice_append")
                val end = visit(node.args[2], ctx) as? IntValue
                    ?: throw TypeError("Expected int as third argument to @slice_append")

                if (start.value < 0 || end.value < 0)
                    throw TypeError("Slice indices must be non-negative")
                if (start.value > end.value)
                    throw TypeError("Start index must be less than end index")
                if (start.value >= existing.length || end.value >= existing.length)
                    throw TypeError("Slice indices must be within the bounds of the slice")

                val newStart = existing.start + start.value.toInt()
                val newLen = end.value.toInt() - start.value.toInt()
                SliceValue(context, existing.type, existing.array, newStart, newLen)
            }
            "slice_len" -> {
                if (node.args.size < 1)
                    throw TypeError("Expected 1 argument to @slice_len")
                val existing = visit(node.args[0], ctx) as? IndexableValue
                    ?: throw TypeError("Expected indexable as first argument to @slice_len")
                IntValue(context, Type.i32, existing.length.toLong())
            }
            "assert" -> {
                if (node.args.isEmpty())
                    throw TypeError("Expected 1 argument to @assert")
                val cond = visit(node.args[0], ctx) as? BoolValue
                    ?: throw TypeError("Expected bool as first argument to @assert")
                if (!cond.value)
                    throw AssertionError("Assertion failed on line ${node.line}")
                TupleValue(context, Type.empty)
            }
            else -> TODO("Unknown intrinsic named " + node.name)
        }
    }
}