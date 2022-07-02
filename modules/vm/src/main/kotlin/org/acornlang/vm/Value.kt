package org.acornlang.vm

import org.acornlang.hir.HirBlock
import java.lang.invoke.MethodHandle
import java.util.*

//todo could add a type parameter for the type of the type, then some casts would not be necessary here, but i dont think its worth the added typing bloat
sealed interface Value {
    companion object

    val type: Type
    val isMut: Boolean get() = type.isMut

    /** Shallow copy to change the type of the value. */
    fun withType(type: Type): Value
    /** Copy of the value, if `deep == false`, it implements value semantics */
    fun clone(deep: Boolean = false): Value
}


class IntValue(
    override val type: IntType,
    var value: Long,
) : Value {
    override fun withType(type: Type) = IntValue(type as IntType, value)
    override fun clone(deep: Boolean) = IntValue(type, value)

    override fun toString() = value.toString()
    override fun equals(other: Any?) = this === other || (other is IntValue && value == other.value)
    override fun hashCode() = Objects.hash(javaClass, value)
}

class BoolValue(
    override val type: BoolType,
    var value: Boolean,
) : Value {
    override fun withType(type: Type) = BoolValue(type as BoolType, value)
    override fun clone(deep: Boolean) = BoolValue(type, value)

    override fun toString() = value.toString()
    override fun equals(other: Any?) = this === other || (other is BoolValue && value == other.value)
    override fun hashCode() = Objects.hash(javaClass, value)
}

class StrValue(
    override val type: StrType,
    var value: String,
) : Value {
    override fun withType(type: Type) = StrValue(type as StrType, value)
    override fun clone(deep: Boolean) = StrValue(type, value)

    override fun toString() = "\"$value\""
    override fun equals(other: Any?) = this === other || (other is StrValue && value == other.value)
    override fun hashCode() = Objects.hash(javaClass, value)
}

class RefValue(
    override val type: RefType,
    var value: Value,
) : Value {
    override fun withType(type: Type) = RefValue(type as RefType, value)
    override fun clone(deep: Boolean) = RefValue(type, if (deep) value.clone(true) else value)

    override fun toString() = "->$value"
    override fun equals(other: Any?) = this === other || (other is RefValue && value == other.value)
    override fun hashCode() = Objects.hash(javaClass, value)
}


// Indexable (array/slice/tuple)

interface IndexableValue : Value {
    val length: Int
    fun get(index: Int): Value
    fun set(index: Int, value: Value)
}

class ArrayValue(
    override val type: ArrayType,
    val items: MutableList<Value>,
) : IndexableValue {
    override val length get() = items.size
    override fun get(index: Int): Value {
        if (index < 0 || index >= length)
            throw IndexOutOfBoundsException("index: ${index}, length: $length")
        return items[index]
    }
    override fun set(index: Int, value: Value) {
        if (index < 0 || index >= length)
            throw IndexOutOfBoundsException("index: ${index}, length: $length")
        items[index] = value
    }

    override fun withType(type: Type) = ArrayValue(type as ArrayType, items)
    // Arrays value semantics are pass by reference. It mimics array-as-pointer behavior.
    override fun clone(deep: Boolean) = ArrayValue(type, if (deep) items.map { it.clone() }.toMutableList() else items)

    override fun toString() = items.toString()
    override fun equals(other: Any?) = this === other || (other is ArrayValue && items == other.items)
    override fun hashCode() = Objects.hash(javaClass, items)
}

class SliceValue(
    override val type: ArrayType,
    val array: ArrayValue,
    val start: Int,
    override val length: Int,
) : IndexableValue {
    override fun get(index: Int): Value {
        if (index < 0 || index >= length)
            throw IndexOutOfBoundsException("index: ${index}, length: $length")
        return array.items[start + index]
    }
    override fun set(index: Int, value: Value) {
        if (index < 0 || index >= length)
            throw IndexOutOfBoundsException("index: ${index}, length: $length")
        array.items[start + index] = value
    }

    override fun withType(type: Type) = SliceValue(type as ArrayType, array, start, length)
    override fun clone(deep: Boolean) = if (deep) {
        TODO("Need to clone the array here")
    } else SliceValue(type, array, start, length)

    override fun toString() = "slice($start, $length, of=$array)"
    override fun equals(other: Any?) = this === other ||
            (other is SliceValue && array == other.array && start == other.start && length == other.length)
    override fun hashCode() = Objects.hash(javaClass, array, start, length)
}

class TupleValue(
    override val type: TupleType,
    val items: MutableList<Value>,
) : IndexableValue {
    override val length get() = items.size
    override fun get(index: Int): Value {
        if (index < 0 || index >= length)
            throw IndexOutOfBoundsException("index: ${index}, length: $length")
        return items[index]
    }
    override fun set(index: Int, value: Value) {
        if (index < 0 || index >= length)
            throw IndexOutOfBoundsException("index: ${index}, length: $length")
        items[index] = value
    }

    override fun withType(type: Type) = TupleValue(type as TupleType, items)
    override fun clone(deep: Boolean) = TupleValue(type, if (deep) items.map { it.clone() }.toMutableList() else items)

    override fun toString() = "(${items.joinToString(", ")})"
    override fun equals(other: Any?) = this === other || (other is TupleValue && items == other.items)
    override fun hashCode() = Objects.hash(javaClass, items)
}


// Function

abstract class FnValue(
    override val type: FnType,
) : Value {

    override fun toString() = type.toString()
}

class NativeFnValue(
    type: FnType,
    val body: HirBlock,
) : FnValue(type) {
    override fun withType(type: Type) = NativeFnValue(type as FnType, body)
    override fun clone(deep: Boolean) = NativeFnValue(type, body)

    override fun equals(other: Any?) =
        this === other || (other is NativeFnValue && body == other.body)
    override fun hashCode() = Objects.hash(javaClass, body.hashCode())
}

class ForeignFnValue(
    type: FnType,
    val handle: MethodHandle,
) : FnValue(type) {
    override fun withType(type: Type) = ForeignFnValue(type as FnType, handle)
    override fun clone(deep: Boolean) = ForeignFnValue(type, handle)

    override fun equals(other: Any?) =
        this === other || (other is ForeignFnValue && handle == other.handle)
    override fun hashCode() = Objects.hash(javaClass, handle)
    override fun toString() = "foreign ${super.toString()}"
}


// Container values

interface ContainerValue : Value {

    fun get(member: String): Value
    fun set(member: String, value: Value)

}

class EnumValue(
    override val type: EnumType,
    var value: Int,
) : Value {

    override fun withType(type: Type) = EnumValue(type as EnumType, value)
    override fun clone(deep: Boolean) = EnumValue(type, value)

    override fun equals(other: Any?) = this === other || (other is EnumValue && value == other.value)
    override fun hashCode() = Objects.hash(javaClass, value)
    override fun toString() = ".${type.cases[value]}"
}

class StructValue(
    override val type: StructType,
    val values: MutableList<Value>,
) : ContainerValue {
    override fun get(member: String): Value {
        val index = type.memberNames.indexOf(member)
        if (index == -1)
            throw IllegalArgumentException("No such field: $member")
        return values[index]
    }
    override fun set(member: String, value: Value) {
        val index = type.memberNames.indexOf(member)
        if (index == -1)
            throw IllegalArgumentException("No such field: $member")
        values[index] = value
    }

    override fun withType(type: Type) = StructValue(type as StructType, values)
    override fun clone(deep: Boolean) = StructValue(type, values.map { it.clone(deep) }.toMutableList())

    override fun toString() = "struct($values)"
    override fun equals(other: Any?) =
        this === other || (other is StructValue && type == other.type && values == other.values)
    override fun hashCode() = Objects.hash(type, values)
}


//todo union, spec, imported module


// Type value

class TypeValue(
    override val type: TypeType,
    val value: Type,
) : ContainerValue {
    override fun get(member: String): Value {
        TODO("implementation is different based on type. eg struct searches container items, enum searches container items AND cases, etc")
    }
    override fun set(member: String, value: Value) = throw IllegalStateException("Types are immutable")

    override fun withType(type: Type) = TypeValue(type as TypeType, value)
    override fun clone(deep: Boolean) = this

    override fun toString() = "some type" //todo describe the type better
    override fun equals(other: Any?) = this === other || (other is TypeValue && value == other.value)
    override fun hashCode() = value.hashCode()
}