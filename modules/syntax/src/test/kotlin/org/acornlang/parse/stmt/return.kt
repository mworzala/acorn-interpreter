package org.acornlang.parse.stmt

import org.acornlang.parse.checkStmt
import org.junit.jupiter.api.Test

class ReturnStmtTest {

    @Test
    fun `basic return`() {
        checkStmt("""
            return
        """.trimIndent(), """
RETURN_STMT@0..6
  RETURN@0..6 "return"
        """.trimIndent())
    }

    @Test
    fun `return with value`() {
        checkStmt("""
            return 1
        """.trimIndent(), """
RETURN_STMT@0..8
  RETURN@0..6 "return"
  WHITESPACE@6..7 " "
  LITERAL@7..8
    NUMBER@7..8 "1"
        """.trimIndent())
    }

}