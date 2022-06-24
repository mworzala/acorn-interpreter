package org.acornlang.eval

interface Scope {
    val parent: Scope

    fun define(name: String, value: Value, mut: Boolean)
    fun get(name: String, mut: Boolean): Value?
}

class ScopeImpl(
    override val parent: Scope,
) : Scope {

    private val values = mutableMapOf<String, Pair<Value, Boolean>>()

    override fun define(name: String, value: Value, mut: Boolean) {
        values[name] = Pair(value, mut)
    }

    override fun get(name: String, mut: Boolean): Value? {
        val (value, mutable) = values[name] ?: return parent.get(name, mut)
        if (mutable)
            throw IllegalStateException("Cannot get (immutable) '$name' as mutable")
        return value
    }
}
