package org.acornlang.eval

import org.acornlang.Interpreter

interface Context {
    val owner: Interpreter
    val returnType: Type

    fun define(name: String, value: Value)
    fun get(name: String): Value?
}

class ContextImpl(
    override val owner: Interpreter,
    val parent: Context? = null,

    // The expected return type of whatever is being evaluated
    override val returnType: Type,
) : Context {
    // Represents scope, but only some operations may be done without creating a new context
    private val _definitions = mutableMapOf<String, Value>()
    val definitions: Map<String, Value> = _definitions

    override fun define(name: String, value: Value) {
        _definitions[name] = value
    }

    override fun get(name: String): Value? {
        if (definitions.containsKey(name))
            return definitions[name]
        return parent?.get(name)
    }
}