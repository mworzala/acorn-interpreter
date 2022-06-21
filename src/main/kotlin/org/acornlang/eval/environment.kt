package org.acornlang

import org.acornlang.eval.FunctionValue
import org.acornlang.eval.IntValue
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.system.exitProcess

// One environment exists per execution
class Environment(
    val workDir: Path,
) {
    val interpreters = mutableMapOf<String, Interpreter>()

    fun loadAndExecute(path: String) {
        val file = workDir.resolve(path)
        val source = file.readText()
        if (interpreters.containsKey(path))
            fail("$path is already running")

        val interpreter = Interpreter(this, path, source)
        interpreters[path] = interpreter

        interpreter.parse()
        val main = interpreter.findDeclByName("main")
            ?: fail("main function not found")
        if (main !is FunctionValue)
            fail("main is not a function")
        val result = main()

        exitProcess((result as IntValue).value.toInt())
    }

}