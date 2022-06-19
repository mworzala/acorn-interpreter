package org.acornlang.eval

import org.acornlang.ast.*
import org.acornlang.fail
import java.lang.foreign.FunctionDescriptor
import java.lang.foreign.MemoryLayout
import java.lang.foreign.ValueLayout
import kotlin.math.abs

class TypeError(message: String) : RuntimeException(message) {
    constructor(expected: Type, actual: Type) : this("Expected type $expected, found $actual")
}

class TypeCoercionError(from: Type, to: Type) :
    RuntimeException("Cannot coerce $from to $to")

interface Type {
    companion object {
        val void: Type = VoidType()

        val i8: Type = IntegerType(8)
        val i16: Type = IntegerType(16)
        val i32: Type = IntegerType(32)
        val i64: Type = IntegerType(64)
        val bool: Type = BooleanType()

        fun from(node: AstNode): Type = when (node) {
            is AstPtrType -> PtrType(from(node.inner))
            is AstType -> when (node.name) {
                "i8" -> i8
                "i16" -> i16
                "i32" -> i32
                "i64" -> i64
                "bool" -> bool
                else -> fail("Unknown type: ${node.name}")
            }

            else -> fail("unexpected type node: ${node::class.simpleName}")
        }

        fun from(i: Long): Type = when {
            abs(i) < Byte.MAX_VALUE -> i8
            abs(i) < Short.MAX_VALUE -> i16
            abs(i) < Int.MAX_VALUE -> i32
            abs(i) < Long.MAX_VALUE -> i64
            else -> fail("Integer literal too large")
        }
    }

    val isPointer: Boolean get() = false
    val name: String

    fun coerce(to: Type): Type = throw TypeCoercionError(this, to)
}

class IntegerType(
    val bits: Int
) : Type {
    override val name: String = "i$bits"

    override fun coerce(to: Type): Type {
        if (to is IntegerType && bits < to.bits)
            return to
        return super.coerce(to)
    }

    override fun equals(other: Any?): Boolean {
        return other is IntegerType && other.bits == bits
    }

    override fun hashCode(): Int = bits

    override fun toString(): String = name
}

private class BooleanType : Type {

    override val name: String = "bool"

    override fun equals(other: Any?): Boolean {
        return other is BooleanType
    }

    override fun hashCode(): Int = "bool".hashCode()

    override fun toString(): String = name
}

private class VoidType : Type {

    override val name: String = "void"

    override fun equals(other: Any?): Boolean {
        return other is VoidType
    }

    override fun hashCode(): Int = "void".hashCode()

    override fun toString(): String = name
}

private class PtrType(
    val inner: Type,
) : Type {
    override val isPointer: Boolean get() = true

    override val name: String
        get() = "*${inner.name}"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PtrType

        if (inner != other.inner) return false

        return true
    }

    override fun hashCode(): Int {
        return inner.hashCode()
    }

    override fun toString(): String = name
}


//todo perhaps should be a pointer type?
class FnType(
    val returnType: Type,
    val params: List<Type>,
) : Type {
    companion object {
        fun from(node: AstNamedFnDecl): FnType {
            return FnType(
                node.retType?.let(Type::from) ?: Type.void,
                node.params.map { (it as AstFnParam).type }.map(Type::from)
            )
        }
    }

    val arity: Int get() = params.size

    fun toFunctionDescriptor(): FunctionDescriptor {
        val params = params.map { it.toMemoryLayout() }
        return if (returnType == Type.void) {
            FunctionDescriptor.ofVoid(*params.toTypedArray())
        } else FunctionDescriptor.of(
            returnType.toMemoryLayout(),
            *params.toTypedArray(),
        )
    }

    override val name: String
        get() = "fn(${params.joinToString(", ")}) $returnType"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FnType

        if (returnType != other.returnType) return false
        if (params != other.params) return false

        return true
    }

    override fun hashCode(): Int {
        var result = returnType.hashCode()
        result = 31 * result + params.hashCode()
        return result
    }

    override fun toString(): String = name
}



private fun Type.toMemoryLayout(): MemoryLayout {
    return when {
        this is IntegerType -> when (bits) {
            8 -> ValueLayout.JAVA_BYTE
            16 -> ValueLayout.JAVA_SHORT
            32 -> ValueLayout.JAVA_INT
            64 -> ValueLayout.JAVA_LONG
            else -> fail("Unsupported integer size in foreign function: $bits")
        }

        this is BooleanType -> ValueLayout.JAVA_BOOLEAN
        this.isPointer -> ValueLayout.ADDRESS
        else -> fail("unexpected type: $this")
    }
}
