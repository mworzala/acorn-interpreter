package org.acornlang.eval

import java.util.concurrent.atomic.AtomicLong

/**
 * Virtual memory for the app. Exists to emulate pointer behavior.
 *
 * Every `value` gets a unique "address" (globally starting at zero and counting).
 * Values must be removed to allow GC, although their address will never be reused.
 */
object VirtualMemory {
    private var address: AtomicLong = AtomicLong(1)
    private var values = mutableMapOf<Long, Any>()

    fun alloc(type: Type): Address {
        val address = address.getAndIncrement()
        values[address] = Value2.getDefaultValue(type)
        return Address(address, type)
    }

    fun alloc(type: Type, value: Any): Address {
        val address = address.getAndIncrement()
        values[address] = value
        return Address(address, type)
    }

    fun set(address: Address, value: Value2) {
        if (!values.containsKey(address.raw)) {
            throw IllegalArgumentException("Access after free for $address ")
        }

        values[address.raw] = value
    }

    fun <T> get(address: Address): T {
        return values[address.raw] as T ?: throw IllegalArgumentException("Access after free for $address ")
    }

    fun free(address: Address) {
        if (!values.containsKey(address.raw)) {
            throw IllegalArgumentException("Access after free for $address ")
        }
        values.remove(address.raw)
    }
}