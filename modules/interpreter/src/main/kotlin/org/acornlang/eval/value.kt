package org.acornlang.eval

import org.acornlang.syntax.AstBlock
import java.lang.invoke.MethodHandle
import java.util.*

abstract class Value(
    val context: Context,
) {
    companion object;

    abstract val type: Type

    open fun assign(to: Value): Unit = throw TypeError("cannot assign to $type")
    abstract fun withType(type: Type): Value
    abstract fun clone(): Value
}

class IntValue(
    context: Context,
    override val type: IntType,
    var value: Long,
) : Value(context) {

    val bits: Int get() = type.bits.toInt()

    override fun assign(to: Value) {
        if (to !is IntValue) throw TypeError("cannot assign $to to $type")
        value = to.value
    }
    override fun withType(type: Type) = IntValue(context, type as IntType, value)
    override fun clone() = IntValue(context, type, value)

    override fun equals(other: Any?) = this === other || (other is IntValue && value == other.value)
    override fun hashCode() = value.hashCode()
    override fun toString() = value.toString()
}

class BoolValue(
    context: Context,
    override val type: BoolType,
    var value: Boolean,
) : Value(context) {

    override fun withType(type: Type) = BoolValue(context, type as BoolType, value)
    override fun clone() = BoolValue(context, type, value)

    override fun equals(other: Any?) = this === other || (other is BoolValue && value == other.value)
    override fun hashCode() = value.hashCode()
    override fun toString() = value.toString()
}

class StrValue(
    context: Context,
    override val type: StrType,
    var value: String,
) : Value(context) {

    override fun assign(to: Value) {
        if (to !is StrValue) throw TypeError("cannot assign $to to $type")
        value = to.value
    }
    override fun withType(type: Type) = StrValue(context, type as StrType, value)
    override fun clone() = StrValue(context, type, value)

    override fun equals(other: Any?) = this === other || (other is StrValue && value == other.value)
    override fun hashCode() = value.hashCode()
    override fun toString() = "\"$value\""
}

class TypeValue(
    context: Context,
    var value: Type,
) : Value(context) {
    override val type = Type.type

    override fun withType(type: Type) = TODO("Type can only have one type?")
    override fun clone() = TypeValue(context, value)

    override fun equals(other: Any?) = this === other || (other is TypeValue && value == other.value)
    override fun hashCode() = value.hashCode()
    override fun toString() = "unnamed type" //todo describe the type better
}

class RefValue(
    context: Context,
    override val type: RefType,
    val value: Value,
) : Value(context) {

    override fun withType(type: Type) = TODO("ref type")
    override fun clone() = RefValue(context, type, value)

    override fun equals(other: Any?) = this === other || (other is RefValue && value == other.value)
    override fun hashCode() = Objects.hash(javaClass, value)
    override fun toString() = "ref($value)"

}

abstract class IndexableValue(
    context: Context,
) : Value(context) {

    abstract val length: Int

    abstract fun get(index: Value): Value
//    abstract fun set(index: Value, value: Value)
}

class ArrayValue(
    context: Context,
    override val type: ArrayType,
    val items: List<Value>,
) : IndexableValue(context) {

    override val length get() = items.size

    override fun get(index: Value): Value {
        if (index !is IntValue) throw TypeError("cannot index $type with $index")
        if (index.value < 0 || index.value >= length) throw RuntimeException("index out of bounds for $type: $index (length $length)")
        return items[index.value.toInt()]
    }

    override fun withType(type: Type) = ArrayValue(context, type as ArrayType, items)
    override fun clone() = ArrayValue(context, type, items.map { it.clone() })

    override fun equals(other: Any?) = this === other || (other is ArrayValue && items == other.items)
    override fun hashCode() = Objects.hash(javaClass, items)
    override fun toString() = "array($items)"
}

class SliceValue(
    context: Context,
    override val type: ArrayType,
    val array: ArrayValue,
    val start: Int,
    override val length: Int,
) : IndexableValue(context) {

    val end: Int get() = start + length

    override fun get(index: Value): Value {
        if (index !is IntValue) throw TypeError("cannot index $type with $index")
        if (index.value < 0 || index.value >= length) throw RuntimeException("index out of bounds for $type: $index (length $length)")
        return array.get(IntValue(context, Type.i32, start + index.value))
    }

    fun contentsCloned(): List<Value> {
        return (0 until length).map { array.items[start + it].clone() }
    }

    override fun withType(type: Type) = SliceValue(context, type as ArrayType, array, start, length)
    override fun clone() = SliceValue(context, type, array, start, length)

    override fun equals(other: Any?) = this === other ||
            (other is SliceValue && array == other.array && start == other.start && length == other.length)
    override fun hashCode() = Objects.hash(javaClass, array, start, length)
    override fun toString() = "slice($start, $length, of=$array)"
}

abstract class FnValue(
    context: Context,
) : Value(context) {

    abstract override val type: FnType

    operator fun invoke(vararg args: Value) = invoke(args.toList())
    abstract operator fun invoke(args: List<Value>): Value

    override fun withType(type: Type) = TODO()

}

class NativeFnValue(
    context: Context,
    override val type: FnType,
    private val body: AstBlock,
) : FnValue(context) {

    override fun invoke(args: List<Value>) = context.call(type, body, args)

    override fun clone() = throw UnsupportedOperationException()

    override fun equals(other: Any?) =
        this === other || (other is NativeFnValue && body == other.body)
    override fun hashCode() = Objects.hash(javaClass, body.hashCode())
    override fun toString() = "native fn"
}

class ForeignFnValue(
    context: Context,
    override val type: FnType,
    private val handle: MethodHandle,
) : FnValue(context) {

    override fun invoke(args: List<Value>) = context.callForeign(type, handle, args)

    override fun clone() = throw UnsupportedOperationException()

    override fun equals(other: Any?) =
        this === other || (other is ForeignFnValue && handle == other.handle)
    override fun hashCode() = Objects.hash(javaClass, handle)
    override fun toString() = "fn"
}

class TupleValue(
    context: Context,
    override val type: TupleType,
    private val values: MutableList<Value> = type.types.map {
        it.default(context)
    }.toMutableList(),
) : Value(context) {

    override fun withType(type: Type) = TupleValue(context, type as TupleType, values)
    override fun clone() = TupleValue(context, type, values.map { it.clone() }.toMutableList())

    override fun equals(other: Any?) =
        this === other || (other is TupleValue && values == other.values)
    override fun hashCode() = Objects.hash(javaClass, values)
    override fun toString() = "tuple($values)"
}

abstract class ContainerValue(
    context: Context,
) : Value(context) {

    abstract fun get(member: String): Value

}

class StructValue(
    context: Context,
    override val type: StructType,
    private val fields: MutableList<Value> = type.memberDefaults.mapIndexed { i, value ->
        value ?: type.memberTypes[i].default(context)
    }.toMutableList(),
) : ContainerValue(context) {

    override fun get(field: String): Value {
        val index = type.memberNames.indexOf(field)
        if (index == -1)
            throw IllegalArgumentException("No such field: $field")
        return fields[index]
    }

    fun set(field: String, value: Value) {
        val index = type.memberNames.indexOf(field)
        if (index == -1)
            throw IllegalArgumentException("No such field: $field")
        fields[index] = value
    }

    override fun withType(type: Type) = StructValue(context, type as StructType, fields)
    override fun clone() = StructValue(context, type, fields.map { it.clone() }.toMutableList())

    override fun equals(other: Any?) =
        this === other || (other is StructValue && type == other.type && fields == other.fields)
    override fun hashCode() = Objects.hash(type, fields)
    override fun toString() = "struct($fields)"
}

class EnumValue(
    context: Context,
    override val type: EnumType,
    var value: Int,
) : Value(context) {

    override fun withType(type: Type) = EnumValue(context, type as EnumType, value)
    override fun clone() = EnumValue(context, type, value)

    override fun equals(other: Any?) = this === other || (other is EnumValue && value == other.value)
    override fun hashCode() = Objects.hash(javaClass, value)
    override fun toString() = ".${type.caseNames[value]}"
}

class ModuleValue(
    context: Context,
    val interpreter: ModuleInterpreter,
) : ContainerValue(context) {
    override fun get(member: String) = interpreter.findDecl(member) ?: throw IllegalArgumentException("No such member: $member")

    override val type: Type
        get() = TODO("Not yet implemented")

    override fun withType(type: Type): Value {
        TODO("Not yet implemented")
    }

    override fun clone(): Value {
        TODO("Not yet implemented")
    }

}
