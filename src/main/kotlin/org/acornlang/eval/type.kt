package org.acornlang.eval

import org.acornlang.Interpreter
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
        val type: Type = TypeType()

        val i8: IntType = IntType(8)
        val i16: IntType = IntType(16)
        val i32: IntType = IntType(32)
        val i64: IntType = IntType(64)
        val bool: Type = BoolType()

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

    fun defaultValue(owner: Interpreter): Value
}

class TypeType : Type {
    override val name: String
        get() = "type"

    override fun defaultValue(owner: Interpreter): Value {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        return other is BoolType
    }

    override fun hashCode(): Int = "type".hashCode()

    override fun toString(): String = name

}

class IntType(
    val bits: Int
) : Type {
    override val name: String = "i$bits"

    override fun coerce(to: Type): Type {
        if (to is IntType && bits < to.bits)
            return to
        return super.coerce(to)
    }

    override fun defaultValue(owner: Interpreter) = IntValue(owner, 0, Type.i8)

    override fun equals(other: Any?): Boolean {
        return other is IntType && other.bits == bits
    }

    override fun hashCode(): Int = bits

    override fun toString(): String = name
}

private class BoolType : Type {

    override val name: String = "bool"

    override fun defaultValue(owner: Interpreter) = BoolValue(owner, false)

    override fun equals(other: Any?): Boolean {
        return other is BoolType
    }

    override fun hashCode(): Int = "bool".hashCode()

    override fun toString(): String = name
}

private class VoidType : Type {

    override val name: String = "void"

    override fun defaultValue(owner: Interpreter) =
        throw IllegalStateException("Void type does not have a value.")

    override fun equals(other: Any?): Boolean {
        return other is VoidType
    }

    override fun hashCode(): Int = "void".hashCode()

    override fun toString(): String = name
}

class PtrType(
    val inner: Type,
) : Type {
    override val isPointer: Boolean get() = true

    override val name: String
        get() = "*${inner.name}"

    override fun defaultValue(owner: Interpreter): Value {
        TODO("Not yet implemented")
    }

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
        fun from(owner: Interpreter, node: AstNamedFnDecl): FnType {
            return FnType(
                node.retType?.let(owner::getType) ?: Type.void,
                node.params.map { (it as AstFnParam).type }.map(owner::getType)
            )
        }
    }

    override fun defaultValue(owner: Interpreter): Value {
        TODO("No default for function type, maybe it should be a nullptr? Probably just an error though")
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

class StructType(
    val structName: String,
    val fieldNames: List<String>,
    val fieldTypes: List<Type>,
) : Type {
    companion object {
        fun from(owner: Interpreter, node: AstStructDecl): StructType {
            return StructType(
                node.name,
                node.fields.map { (it as AstStructField).name },
                node.fields.map { (it as AstStructField).type }.map(owner::getType)
            )
        }
    }

    override val name: String
        get() = "struct $structName(${fieldNames.mapIndexed { i, name -> "${name}: ${fieldTypes[i]}" }.joinToString(", ")})"

    override fun defaultValue(owner: Interpreter) =
        throw IllegalStateException("TODO: This should be valid, and should fill all values with their default. The problem, is that we need the AST node. Probably StructType needs to keep track of it's AST node.")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StructType

        if (structName != other.structName) return false
        if (fieldNames != other.fieldNames) return false
        if (fieldTypes != other.fieldTypes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = structName.hashCode()
        result = 31 * result + fieldNames.hashCode()
        result = 31 * result + fieldTypes.hashCode()
        return result
    }

    override fun toString(): String = name
}

class EnumType(
    val enumName: String,
    val cases: List<String>,
) : Type {
    companion object {
        fun from(owner: Interpreter, node: AstEnumDecl): EnumType {
            return EnumType(
                node.name,
                node.cases.map { (it as AstEnumCase).name }
            )
        }
    }

    override val name: String
        get() = "enum $enumName(${cases.joinToString(", ")})"

    override fun defaultValue(owner: Interpreter): Value {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EnumType

        if (enumName != other.enumName) return false
        if (cases != other.cases) return false

        return true
    }
    override fun hashCode(): Int {
        var result = enumName.hashCode()
        result = 31 * result + cases.hashCode()
        return result
    }

    override fun toString(): String = name
}


private fun Type.toMemoryLayout(): MemoryLayout {
    return when {
        this is IntType -> when (bits) {
            8 -> ValueLayout.JAVA_BYTE
            16 -> ValueLayout.JAVA_SHORT
            32 -> ValueLayout.JAVA_INT
            64 -> ValueLayout.JAVA_LONG
            else -> fail("Unsupported integer size in foreign function: $bits")
        }

        this is BoolType -> ValueLayout.JAVA_BOOLEAN
        this.isPointer -> ValueLayout.ADDRESS
        else -> fail("unexpected type: $this")
    }
}
