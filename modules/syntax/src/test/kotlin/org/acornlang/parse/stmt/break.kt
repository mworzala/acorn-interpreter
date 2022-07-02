package org.acornlang.parse.stmt

import org.acornlang.parse.checkStmt
import org.junit.jupiter.api.Test

class BreakStmtTest {

    @Test
    fun `basic break`() {
        checkStmt("""
            break
        """.trimIndent(), """
BREAK_STMT@0..5
  BREAK@0..5 "break"
        """.trimIndent())
    }

}