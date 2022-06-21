package org.acornlang.eval

import org.acornlang.fail

class Address(
    // Virtual memory address. See VirtualMemory for details.
    val raw: Long,
    // The type at this address
    val type: Type
) {
    inline fun <reified T> getValue(): T {
        val value: T = VirtualMemory.get(this)
        return value

        //todo try to coerce
//        fail("Cannot coerce ${value.type} to $type")
    }

    override fun toString(): String = "($type) ${stringify()}"
    fun stringify(): String = raw.toString(16)
}

interface Value2 {
    val type: Type

    companion object {
        fun getDefaultValue(type: Type): Any = when (type) {
            is IntType -> 0L
            else -> fail("Unsupported type: $type")
        }
    }

}

class IntValue2(
    override val type: Type,
    val value: Long,
) : Value2

class FunctionValue2(

)


//todo what if Value simply becomes an address and type & nothing else. Then the actual data is stored in VirtualMemory
