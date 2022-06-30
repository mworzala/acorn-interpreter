package org.acornlang.eval

import org.acornlang.syntax.AstBlock
import java.lang.invoke.MethodHandle

interface Context : Scope {

    fun call(spec: FnType, block: AstBlock, args: List<Value>): Value
    fun callForeign(spec: FnType, handle: MethodHandle, args: List<Value>): Value

}
