package org.acornlang

import org.acornlang.eval.IntValue
import org.acornlang.eval.FnValue
import org.acornlang.eval.ModuleInterpreter
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.readText
import kotlin.system.exitProcess

// One environment exists per execution
class Environment(
    val workDir: Path,
) {

    private val interpreters = mutableMapOf<Path, ModuleInterpreter>()

    fun getInterpreter(path: Path): ModuleInterpreter {
        return interpreters.getOrPut(path) {
            val source = path.readText()
            ModuleInterpreter(this, path.parent, path.name, source)
                .also { it.parse() }
        }
    }

    fun loadAndExecute(path: String) {
        val file = workDir.resolve(path)
        val interpreter = getInterpreter(file)

        val main = interpreter.findDecl("main")
            ?: fail("main function not found")
        if (main !is FnValue)
            fail("main is not a function")
        val result = main()

        exitProcess((result as IntValue).value.toInt())
    }

}