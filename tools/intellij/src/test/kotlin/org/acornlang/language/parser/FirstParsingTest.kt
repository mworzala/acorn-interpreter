package org.acornlang.language.parser

import com.intellij.testFramework.ParsingTestCase
import org.acornlang.language.AcornParserDefinition

class FirstParsingTest : ParsingTestCase("", "acorn", AcornParserDefinition()) {

    fun testEmptyFn() {
        doTest(true)
    }

    override fun getTestDataPath() = "src/test/data/parser"
    override fun skipSpaces() = false
    override fun includeRanges() = true
}