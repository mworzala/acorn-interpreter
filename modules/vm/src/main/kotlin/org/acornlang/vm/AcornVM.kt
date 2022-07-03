package org.acornlang.vm

import java.nio.file.Files
import java.nio.file.Path

class AcornVM {
    val workDir: Path = Path.of(".")
    val modules = mutableMapOf<Path, Module>()

    fun loadModule(path: String, relativeTo: Module? = null): Module {
        val startPath = relativeTo?.path ?: workDir
        val modulePath = startPath.resolve(path).toAbsolutePath()
        if (modules.containsKey(modulePath))
            return modules[modulePath]!!

        val moduleSrc = Files.readString(modulePath)
        val module = Module(modulePath.fileName.toString(), modulePath.parent, moduleSrc)
        modules[modulePath] = module
        return module
    }



}