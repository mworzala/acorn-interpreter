package org.acornlang

import java.nio.file.Paths

fun main() {
    val workDir = Paths.get(".").toAbsolutePath()
    val env = Environment(workDir)

    env.loadAndExecute("test.acorn")
}