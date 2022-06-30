package org.acornlang.eval

import org.acornlang.syntax.*
import org.acornlang.fail
//import java.lang.foreign.FunctionDescriptor
//import java.lang.foreign.MemoryLayout
//import java.lang.foreign.SegmentAllocator
//import java.lang.foreign.ValueLayout

fun AstNode.asType(scope: Scope): Type = when (this) {
    is AstRefType -> RefType(inner.asType(scope))
    is AstArrayType -> ArrayType(inner.asType(scope))
    is AstType -> when (name) {
        "i8" -> Type.i8
        "i16" -> Type.i16
        "i32" -> Type.i32
        "i64" -> Type.i64
        "bool" -> Type.bool
        "str" -> Type.str
        else -> ((scope.get(name, false) as? TypeValue)?.value) ?: fail("Unknown type: $name")
    }
    else -> throw IllegalArgumentException("Not a type")
}

//fun Type.Companion.getMemoryLayout(type: Type): MemoryLayout {
//    return when (type) {
//        is IntType -> when (type.bits) {
//            8 -> ValueLayout.JAVA_BYTE
//            16 -> ValueLayout.JAVA_SHORT
//            32 -> ValueLayout.JAVA_INT
//            64 -> ValueLayout.JAVA_LONG
//            else -> fail("Unsupported integer size in foreign function: ${type.bits}")
//        }
//        is BoolType -> ValueLayout.JAVA_BOOLEAN
//        is StrType -> ValueLayout.ADDRESS // *i8
////        is PtrType -> ValueLayout.ADDRESS
//        is FnType -> ValueLayout.ADDRESS
//        is StructType -> MemoryLayout.structLayout(*type.fieldTypes.map(Type::getMemoryLayout).toTypedArray())
//        is EnumType -> ValueLayout.JAVA_INT //todo only valid if enum is 32 bits or smaller
//        else -> fail("Unsupported type in foreign function: $type")
//    }
//}
//
//fun Type.Companion.getNativeDescriptor(fnType: FnType): FunctionDescriptor {
//    val params = fnType.paramTypes.map(Type::getMemoryLayout).toTypedArray()
//    return if (fnType.ret == Type.void) {
//        FunctionDescriptor.ofVoid(*params)
//    } else {
//        val ret = Type.getMemoryLayout(fnType.ret)
//        FunctionDescriptor.of(ret, *params)
//    }
//}
//
//fun Value.Companion.toForeignValue(allocator: SegmentAllocator, native: Value): Any = when (native) {
//    is IntValue -> when (native.type.bits.toInt()) {
//        8 -> native.value.toByte()
//        16 -> native.value.toShort()
//        32 -> native.value.toInt()
//        64 -> native.value
//        else -> throw UnsupportedOperationException("Unsupported integer size: ${native.type}")
//    }
//    is BoolValue -> native.value
//    is StrValue -> allocator.allocateUtf8String(native.value)
//    is FnValue -> throw UnsupportedOperationException("Cannot convert function to foreign value")
//    is StructValue -> {
//        val layout = Type.getMemoryLayout(native.type)
//        val mem = allocator.allocate(layout)
//        var offset: Int = 0
//        //todo
//        TODO()
////
////        native.type.fieldTypes.forEach { fieldType ->
////            val fieldLayout = Type.getMemoryLayout(fieldType)
////
////        }
//    }
//    is EnumValue -> native.value
//    else -> throw UnsupportedOperationException()
//}
//
//fun Value.Companion.fromForeignValue(context: Context, native: Any, type: Type): Value {
//    return when (type) {
//        is IntType -> when (type.bits.toInt()) {
//            8 -> IntValue(context, type, (native as Byte).toLong())
//            16 -> IntValue(context, type, (native as Short).toLong())
//            32 -> IntValue(context, type, (native as Int).toLong())
//            64 -> IntValue(context, type, native as Long)
//            else -> throw UnsupportedOperationException("Unsupported integer size: ${type.bits}")
//        }
//        is BoolType -> BoolValue(context, Type.bool, native as Boolean)
//        is StrType -> TODO("String retrieval from native")
//        is StructType -> TODO("Struct retrieval from native")
//        is EnumType -> EnumValue(context, type, native as Int)
//        else -> throw UnsupportedOperationException("Unsupported type: $type")
//    }
//}


