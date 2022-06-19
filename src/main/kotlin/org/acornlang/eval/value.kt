package org.acornlang.eval

import com.sun.jdi.IntegerValue
import org.acornlang.Interpreter
import org.acornlang.ast.AstBlock
import org.acornlang.ast.AstFnParam
import org.acornlang.ast.AstNamedFnDecl
import org.acornlang.fail
import java.lang.foreign.Linker
import java.lang.foreign.SegmentAllocator
import java.lang.invoke.MethodHandle

abstract class Value(
    val owner: Interpreter,
) {
    companion object {
        fun fromAddressable(owner: Interpreter, type: Type, value: Any): Value {
            return when (type) {
                is IntegerType -> {
                    IntValue(
                        owner, when (type.bits) {
                            8 -> (value as Byte).toLong()
                            16 -> (value as Short).toLong()
                            32 -> (value as Int).toLong()
                            64 -> (value as Long)
                            else -> fail("unsupported integer size")
                        }, type
                    )
                }

                else -> fail("Unsupported type: $type")
            }
        }
    }

    abstract val type: Type
    open fun coerceType(to: Type): Value = throw TypeCoercionError(type, to)

    // Convert the object to a native addressable value
    open fun toAddressable(alloc: SegmentAllocator): Any = throw UnsupportedOperationException()

    override fun toString(): String = "($type) ${stringify()}"

    open fun stringify(): String = javaClass.simpleName
}

class NullValue(
    owner: Interpreter,
) : Value(owner) {
    override val type: Type get() = TODO("Typeof null")
}

class IntValue(
    owner: Interpreter,
    val value: Long,
    override val type: IntegerType,
) : Value(owner) {

    override fun coerceType(to: Type): Value {
        val newType = type.coerce(to)
        return IntValue(owner, value, newType as IntegerType)
    }

    override fun toAddressable(alloc: SegmentAllocator): Any {
        return when (type.bits) {
            8 -> value.toByte()
            16 -> value.toShort()
            32 -> value.toInt()
            64 -> value.toLong()
            else -> throw UnsupportedOperationException()
        }
    }

    override fun stringify() = "$value"
}

class StringValue(
    owner: Interpreter,
    val value: String,
) : Value(owner) {
    override val type: Type
        get() = TODO("Not yet implemented")

    override fun toAddressable(alloc: SegmentAllocator): Any {
        return alloc.allocateUtf8String(value)
    }

    override fun stringify() = "\"$value\""
}

class BoolValue(
    owner: Interpreter,
    val value: Boolean,
) : Value(owner) {
    override val type: Type get() = Type.bool

    override fun toAddressable(alloc: SegmentAllocator) = value

    override fun stringify() = "$value"
}

abstract class FunctionValue(
    owner: Interpreter,
) : Value(owner) {

    companion object {
        fun fromAst(owner: Interpreter, ast: AstNamedFnDecl): FunctionValue {
            if (ast.foreign)
                return ForeignFunctionValueImpl(owner, ast)
            return FunctionValueImpl(owner, ast)
        }
    }

    operator fun invoke(vararg args: Value) = invoke(args.toList())
    abstract operator fun invoke(args: List<Value>): Value
}

private class FunctionValueImpl(
    owner: Interpreter,
    val ast: AstNamedFnDecl,
) : FunctionValue(owner) {

    override val type: Type = FnType.from(ast)

    override fun invoke(args: List<Value>): Value {
        val retTy: Type = ast.retType?.let(Type::from) ?: Type.void

        val ctx = ContextImpl(owner, owner, retTy)
        // Add arguments to scope
        ast.params.forEachIndexed { i, param ->
            if (param !is AstFnParam)
                fail("Expected parameter to be a function parameter")
            ctx.define(param.name, args[i])
        }

        return owner.evaluateBlockInScope(ast.body as AstBlock, ctx)
    }
}

private class ForeignFunctionValueImpl(
    owner: Interpreter,
    val ast: AstNamedFnDecl,
) : FunctionValue(owner) {
    override val type: FnType = FnType.from(ast)

    private val handle: MethodHandle

    init {
        val linker = Linker.nativeLinker()
        val func = linker.defaultLookup()
            .lookup(ast.name).orElse(null)
            ?: fail("Could not find function $ast")
        val descriptor = (type as FnType).toFunctionDescriptor()

        handle = linker.downcallHandle(func, descriptor)
    }


    override fun invoke(args: List<Value>): Value {
        val allocator = SegmentAllocator.implicitAllocator()

        val nativeArgs = args.map { it.toAddressable(allocator) }
        // I cannot make the varargs here work for the life of me.
        val result = when (nativeArgs.size) {
            0 -> handle.invoke()
            1 -> handle.invoke(nativeArgs[0])
            2 -> handle.invoke(nativeArgs[0], nativeArgs[1])
            3 -> handle.invoke(nativeArgs[0], nativeArgs[1], nativeArgs[2])
            4 -> handle.invoke(nativeArgs[0], nativeArgs[1], nativeArgs[2], nativeArgs[3])
            5 -> handle.invoke(nativeArgs[0], nativeArgs[1], nativeArgs[2], nativeArgs[3], nativeArgs[4])
            6 -> handle.invoke(nativeArgs[0], nativeArgs[1], nativeArgs[2], nativeArgs[3], nativeArgs[4], nativeArgs[5])
            else -> fail("Unsupported number of arguments for foreign function")
        }

        return fromAddressable(owner, type.returnType, result)
    }
}
