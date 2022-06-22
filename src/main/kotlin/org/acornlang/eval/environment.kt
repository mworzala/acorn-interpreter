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

    fun loadAndExecute(path: String) {
        val file = workDir.resolve(path)
        val source = file.readText()

        val interpreter = ModuleInterpreter(this, file.name, source)

        interpreter.parse()
        val main = interpreter.findDecl("main")
            ?: fail("main function not found")
        if (main !is FnValue)
            fail("main is not a function")
        val result = main()

        exitProcess((result as IntValue).value.toInt())
    }

}