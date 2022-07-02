package org.acornlang.common.text

import com.github.difflib.text.DiffRowGenerator

object Diff {
    private val generator = DiffRowGenerator.create()
        .showInlineDiffs(true)
        .mergeOriginalRevised(true)
        .inlineDiffByWord(true)
        .oldTag { open -> if (open) "\u001b[41m" else "\u001b[0m" }
        .newTag { open -> if (open) "\u001b[42m" else "\u001b[0m" }
        .build()

    fun emitInlineDiff(original: String, revised: String) {
        generator.generateDiffRows(original.split("\n"), revised.split("\n")).forEach {
            println(it.oldLine)
        }
    }
}