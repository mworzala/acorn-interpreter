package org.acornlang.vm

import java.util.*

/**
 * Types themselves are immutable, but describe whether a holding value is mutable or not.
 */
interface Type {
    companion object

    val isMut: Boolean get() = false
    /** Attempts to convert this to a mutable type. Returns null if that operation is not possible. */
    fun asMut(): Type? = null
}


class IntType(
    val bits: Int,
    override val isMut: Boolean = false,
) : Type {
    override fun asMut() = IntType(bits, true)

    override fun toString() = mutToString("i$bits")
    override fun equals(other: Any?) = this === other || (other is IntType && bits == other.bits)
    override fun hashCode() = Objects.hash(javaClass, bits)
}

class BoolType(
    override val isMut: Boolean = false,
) : Type {
    override fun asMut() = BoolType(true)

    override fun toString() = mutToString("bool")
    override fun equals(other: Any?) = this === other || other is BoolType
    override fun hashCode() = javaClass.hashCode()
}

class StrType(
    override val isMut: Boolean = false,
) : Type {
    override fun asMut() = StrType(true)

    override fun toString() = mutToString("str")
    override fun equals(other: Any?) = this === other || other is StrType
    override fun hashCode() = javaClass.hashCode()
}

class RefType(
    val type: Type,
    override val isMut: Boolean = false,
) : Type {
    override fun asMut() = RefType(type, true)

    override fun toString() = "&$type"
    override fun equals(other: Any?) =
        this === other || (other is RefType && type == other.type)
    override fun hashCode() = Objects.hash(javaClass, type)
}


class ArrayType(
    val type: Type,
    override val isMut: Boolean = false,
) : Type {
    override fun asMut() = ArrayType(type, true)

    override fun toString() = "[$type]"
    override fun equals(other: Any?) =
        this === other || (other is ArrayType && type == other.type)
    override fun hashCode() = Objects.hash(javaClass, type)
}

class TupleType(
    val types: List<Type>,
    override val isMut: Boolean = false,
) : Type {
    override fun asMut() = TupleType(types, true)

    override fun toString() = "(${types.joinToString(", ")})"
    override fun equals(other: Any?) = this === other || (other is TupleType && types == other.types)
    override fun hashCode() = types.hashCode()
}


class FnType(
    val params: List<Type>,
    val ret: Type,
) : Type {

    override fun toString() = "fn(${params.joinToString(", ") { it.toString() }}) $ret"
    override fun equals(other: Any?) =
        this === other || (other is FnType && params == other.params && ret == other.ret)
    override fun hashCode() = Objects.hash(params, ret)
}


class EnumType(
    val name: String,
    val cases: List<String>,
    override val isMut: Boolean = false,
) : Type {
    fun withName(name: String) = EnumType(name, cases, isMut)
    override fun asMut() = EnumType(name, cases, true)

    override fun toString() = "enum $name { ${cases.joinToString(", ")} }"
    override fun equals(other: Any?) = this === other || (other is EnumType && cases == other.cases)
    override fun hashCode() = Objects.hash(javaClass, cases)
}

class StructType(
    val name: String,
    val memberNames: List<String>,
    val memberTypes: List<Type>,
    //todo inner container items
    override val isMut: Boolean = false,
) : Type {
    fun withName(name: String) = StructType(name, memberNames, memberTypes, isMut)
    override fun asMut() = StructType(name, memberNames, memberTypes, true)

    override fun toString() = "struct $name { ${memberNames.mapIndexed { i, name -> "${name}: ${memberTypes[i]}" }.joinToString(", ")} }"
    override fun equals(other: Any?) = this === other ||
            (other is StructType && name == other.name && memberNames == other.memberTypes && memberTypes == other.memberTypes)
    override fun hashCode() = Objects.hash(name, memberNames, memberTypes)
}

class UnionType(
    val name: String,
    val memberNames: List<String>,
    val memberTypes: List<Type>,
    //todo inner container items
    override val isMut: Boolean = false,
) : Type {
    fun withName(name: String) = StructType(name, memberNames, memberTypes, isMut)
    override fun asMut() = StructType(name, memberNames, memberTypes, true)

    override fun toString() = "union $name { ${memberNames.mapIndexed { i, name -> "${name}: ${memberTypes[i]}" }.joinToString(", ")} }"
    override fun equals(other: Any?) = this === other ||
            (other is UnionType && name == other.name && memberNames == other.memberTypes && memberTypes == other.memberTypes)
    override fun hashCode() = Objects.hash(name, memberNames, memberTypes)
}

//todo spec type
//class SpecType(
//
//) : Type {
//
//}


class TypeType(
    override val isMut: Boolean = false,
) : Type {
    override fun asMut() = TypeType(true)

    override fun toString() = mutToString("type")
    override fun equals(other: Any?) = this === other || (other is TypeType)
    override fun hashCode() = Objects.hash(javaClass)
}


// Helpers

private fun Type.mutToString(value: String): String = if (isMut) "mut $value" else value
