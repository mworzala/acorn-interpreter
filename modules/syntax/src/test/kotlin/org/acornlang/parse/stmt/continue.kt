package org.acornlang.parse.stmt

import org.acornlang.parse.checkStmt
import org.junit.jupiter.api.Test

class ContinueStmtTest {

    @Test
    fun `basic continue`() {
        checkStmt("""
            continue
        """.trimIndent(), """
CONTINUE_STMT@0..8
  CONTINUE@0..8 "continue"
        """.trimIndent())
    }

}