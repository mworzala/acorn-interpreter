package org.acornlang.vm

interface Scope {

    fun get(name: String): Value?
    fun define(name: String, value: Value)

}

class ScopeImpl(private val parent: Scope) : Scope {
    private val values = mutableMapOf<String, Value>()

    override fun get(name: String): Value? {
        return values[name] ?: parent.get(name)
    }

    override fun define(name: String, value: Value) {
        values[name] = value
    }
}

class ModuleScope(val module: Module) : Scope {
    override fun get(name: String): Value {
        return module.getDecl(name)
    }

    override fun define(name: String, value: Value) {
        TODO("Not yet implemented")
    }

}
