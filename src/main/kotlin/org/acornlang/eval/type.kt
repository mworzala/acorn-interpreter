package org.acornlang.eval

import org.acornlang.fail
import java.util.*
import kotlin.math.abs

val Type.Companion.i8   get() = IntType(8)
val Type.Companion.i16  get() = IntType(16)
val Type.Companion.i32  get() = IntType(32)
val Type.Companion.i64  get() = IntType(64)
val Type.Companion.bool get() = BoolType()
val Type.Companion.str  get() = StrType()

val Type.Companion.type get() = TypeType()
val Type.Companion.void get() = VoidType()
val Type.Companion.empty: Type get() = TupleType(listOf())

abstract class Type {
    companion object;

    open val isMut: Boolean get() = false
    open fun asMut(): Type? = null

    abstract fun default(context: Context): Value

    abstract override fun toString(): String
}

class VoidType : Type() {

    override fun default(context: Context) = throw RuntimeException("void has no default value")

    override fun toString() = "void"
    override fun equals(other: Any?) = this === other || other is VoidType
    override fun hashCode() = javaClass.hashCode()
}


class IntType(
    val bits: Int,
    override val isMut: Boolean = false,
) : Type() {

    companion object {
        fun smallestViable(i: Long): IntType = when {
            abs(i) < Byte.MAX_VALUE -> i8
            abs(i) < Short.MAX_VALUE -> i16
            abs(i) < Int.MAX_VALUE -> i32
            abs(i) < Long.MAX_VALUE -> i64
            else -> fail("Integer literal too large")
        }
    }

    override fun asMut() = IntType(bits, true)

    override fun default(context: Context) = IntValue(context, this, 0)

    override fun toString() = "i$bits"
    override fun equals(other: Any?) = this === other || (other is IntType && bits == other.bits)
    override fun hashCode() = Objects.hash(javaClass, bits)
}

class BoolType(
    override val isMut: Boolean = false,
) : Type() {

    override fun asMut() = BoolType(true)

    override fun default(context: Context) = BoolValue(context, bool, false)

    override fun toString() = "bool"
    override fun equals(other: Any?) = this === other || other is BoolType
    override fun hashCode() = javaClass.hashCode()
}

class StrType(
    override val isMut: Boolean = false,
) : Type() {

    override fun asMut() = StrType(true)

    override fun default(context: Context) = StrValue(context, str, "")

    override fun toString() = "str"
    override fun equals(other: Any?) = this === other || other is StrType
    override fun hashCode() = javaClass.hashCode()
}

class RefType(
    val type: Type
) : Type() {

    override fun default(context: Context) = RefValue(context, this, type.default(context))

    override fun toString() = "&$type"
    override fun equals(other: Any?) =
        this === other || (other is RefType && type == other.type)
    override fun hashCode() = Objects.hash(javaClass, type)
}

class TypeType : Type() {

    override fun default(context: Context) = TypeValue(context, this)

    override fun toString() = "type"
    override fun equals(other: Any?) = this === other || (other is TypeType)
    override fun hashCode() = Objects.hash(javaClass)
}

class FnType(
    val ret: Type,
    val paramNames: List<String>,
    val paramTypes: List<Type>
) : Type() {

    override fun default(context: Context) = throw RuntimeException("fn has no default value")

    override fun toString() = "fn(${paramNames.mapIndexed { i, name -> "${name}: ${paramTypes[i]}" }.joinToString(", ")}) $ret"
    override fun equals(other: Any?) =
        this === other || (other is FnType && ret == other.ret && paramNames == other.paramNames && paramTypes == other.paramTypes)
    override fun hashCode() = Objects.hash(ret, paramNames, paramTypes)
}

class TupleType(
    val types: List<Type>,
) : Type() {

    override fun default(context: Context) = TupleValue(context, this)

    override fun toString() = "(${types.joinToString(", ")})"
    override fun equals(other: Any?) = this === other || (other is TupleType && types == other.types)
    override fun hashCode() = types.hashCode()
}

class StructType(
    val fieldNames: List<String>,
    val fieldTypes: List<Type>,
    override val isMut: Boolean = false,
) : Type() {

    override fun asMut() = StructType(fieldNames, fieldTypes, true)

    override fun default(context: Context) = StructValue(context, this)

    override fun toString() = "struct { ${fieldNames.mapIndexed { i, name -> "${name}: ${fieldTypes[i]}" }.joinToString(", ")} }"
    override fun equals(other: Any?) =
        this === other || (other is StructType && fieldNames == other.fieldTypes && fieldTypes == other.fieldTypes)
    override fun hashCode() = Objects.hash(fieldNames, fieldTypes)
}

class EnumType(
    val caseNames: List<String>,
    override val isMut: Boolean = false,
) : Type() {

    override fun asMut() = EnumType(caseNames, true)

    override fun default(context: Context) = EnumValue(context, this, 0)

    override fun toString() = "enum { ${caseNames.joinToString(", ")} }"
    override fun equals(other: Any?) = this === other || (other is EnumType && caseNames == other.caseNames)
    override fun hashCode() = caseNames.hashCode()
}
