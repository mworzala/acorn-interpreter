package org.acornlang.ast

import org.junit.jupiter.api.Assertions.assertTrue

private fun parse(source: String): AstModule {
    val result = org.acornlang.parse.parse(source)
    assertTrue(result.errors.isEmpty())
    return AstModule(result.node)
}

class TestModule {

}
