package org.acornlang.eval

import org.acornlang.ast.AstBlock
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


}

abstract class FnValue(
    context: Context,
) : Value(context) {

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

    override fun withType(type: Type) = TupleValue(context, type as TupleType, values)
    override fun clone() = TupleValue(context, type, values.map { it.clone() }.toMutableList())

    override fun equals(other: Any?) =
        this === other || (other is TupleValue && values == other.values)
    override fun hashCode() = Objects.hash(javaClass, values)
}

abstract class ContainerValue(
    context: Context,
) : Value(context) {

    abstract fun get(member: String): Value

}

class StructValue(
    context: Context,
    override val type: StructType,
    private val fields: MutableList<Value> = type.fieldTypes.map {
        it.default(context)
    }.toMutableList(),
) : ContainerValue(context) {

    override fun get(field: String): Value {
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

    override fun withType(type: Type) = StructValue(context, type as StructType, fields)
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

    override fun withType(type: Type) = EnumValue(context, type as EnumType, value)
    override fun clone() = EnumValue(context, type, value)

    override fun equals(other: Any?) = this === other || (other is EnumValue && value == other.value)
    override fun hashCode() = Objects.hash(javaClass, value)
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
