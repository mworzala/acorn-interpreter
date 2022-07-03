package org.acornlang.vm

import org.acornlang.ast.AstModule
import org.acornlang.hir.HirConstDecl
import org.acornlang.hir.HirModule
import org.acornlang.hir.lower.AstLoweringVisitor
import org.acornlang.parse.parse
import org.acornlang.vm.eval.ImplicitReturn
import org.acornlang.vm.eval.ScopedExprEvaluator
import java.nio.file.Path

class Module(
    val name: String,
    val path: Path,
    val source: String,
) {
    private val hir: HirModule
    // Map of decl name to the value it represents.
    private val declCache = mutableMapOf<String, Value>()
    // todo eventually need to "comptime" calls to functions.

    private val rootScope = ModuleScope(this)

    init {
        val parse = parse(source)
        if (parse.errors.isNotEmpty()) {
            println(parse.errors.joinToString("\n"))
            throw IllegalArgumentException("Invalid source")
        }

        val ast = AstModule(parse.node)
        hir = AstLoweringVisitor().visitModule(ast, Unit) as HirModule
    }

    fun getDecl(name: String): Value {
        return declCache.getOrPut(name) {
            val decl = hir.decls.map { it as HirConstDecl }.firstOrNull { it.name == name }
                ?: throw IllegalArgumentException("No such decl: $name")

            // Eval the expression.
            val scope = ScopeImpl(rootScope)
            val evaluator = ScopedExprEvaluator()

            //todo const decl type is relevant here
            try {
                evaluator.visit(decl.init, scope)
            } catch (ret: ImplicitReturn) {
                ret.value
            }

        }
    }
}