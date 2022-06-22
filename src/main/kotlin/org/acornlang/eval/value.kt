package org.acornlang.eval

import org.acornlang.ast.AstBlock
import java.lang.invoke.MethodHandle
import java.util.*

abstract class Value(
    val context: Context,
) {
    companion object;

    abstract val type: Type

    abstract fun clone(): Value
}

class IntValue(
    context: Context,
    override val type: IntType,
    var value: Long,
) : Value(context) {

    val bits: Int get() = type.bits.toInt()

    override fun clone() = IntValue(context, type, value)

    override fun equals(other: Any?) = this === other || (other is IntValue && value == other.value)
    override fun hashCode() = value.hashCode()
}

class BoolValue(
    context: Context,
    var value: Boolean,
) : Value(context) {
    override val type = Type.bool

    override fun clone() = BoolValue(context, value)

    override fun equals(other: Any?) = this === other || (other is BoolValue && value == other.value)
    override fun hashCode() = value.hashCode()
}

class StrValue(
    context: Context,
    val value: String,
) : Value(context) {
    override val type = Type.str

    override fun clone() = StrValue(context, value)

    override fun equals(other: Any?) = this === other || (other is StrValue && value == other.value)
    override fun hashCode() = value.hashCode()
}

class TypeValue(
    context: Context,
    var value: Type,
) : Value(context) {
    override val type = Type.type

    override fun clone() = TypeValue(context, value)

    override fun equals(other: Any?) = this === other || (other is TypeValue && value == other.value)
    override fun hashCode() = value.hashCode()
}

//class PtrValue(
//    context: Context,
//    val value: Value,
//) : Value(context) {
//    override val type = PtrType(value.type)
//
//    override fun clone() = PtrValue(context, value)
//}

abstract class FnValue(
    context: Context,
) : Value(context) {

    operator fun invoke(vararg args: Value) = invoke(args.toList())
    abstract operator fun invoke(args: List<Value>): Value

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
}

class TupleValue(
    context: Context,
    override val type: TupleType,
    private val values: MutableList<Value> = type.types.map {
        it.default(context)
    }.toMutableList(),
) : Value(context) {

    override fun clone() = TupleValue(context, type, values.map { it.clone() }.toMutableList())

    override fun equals(other: Any?) =
        this === other || (other is TupleValue && values == other.values)
    override fun hashCode() = Objects.hash(javaClass, values)
}

class StructValue(
    context: Context,
    override val type: StructType,
    private val fields: MutableList<Value> = type.fieldTypes.map {
        it.default(context)
    }.toMutableList(),
) : Value(context) {

    fun get(field: String): Value {
        val index = type.fieldNames.indexOf(field)
        if (index == -1)
            throw IllegalArgumentException("No such field: $field")
        return fields[index]
    }

    fun set(field: String, value: Value) {
        val index = type.fieldNames.indexOf(field)
        if (index == -1)
            throw IllegalArgumentException("No such field: $field")
        fields[index] = value
    }

    override fun clone() = StructValue(context, type, fields.map { it.clone() }.toMutableList())

    override fun equals(other: Any?) =
        this === other || (other is StructValue && type == other.type && fields == other.fields)
    override fun hashCode() = Objects.hash(type, fields)
}

class EnumValue(
    context: Context,
    override val type: EnumType,
    var value: Int,
) : Value(context) {

    override fun clone() = EnumValue(context, type, value)

    override fun equals(other: Any?) = this === other || (other is EnumValue && value == other.value)
    override fun hashCode() = Objects.hash(javaClass, value)
}
