package org.acornlang.language.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.openapi.util.Key

object AcornParserUtil : GeneratedParserUtilBase() {

    enum class ExprMode {
        LIMITED,
        EXTENDED,
    }

    private val EXPR_MODE_KEY: Key<ExprMode> = Key("AcornParserUtil.EXPR_MODE")
    private var PsiBuilder.exprMode: ExprMode
        get() = getUserData(EXPR_MODE_KEY) ?: ExprMode.EXTENDED
        set(value) = putUserData(EXPR_MODE_KEY, value)


    @JvmStatic
    fun setExprMode(b: PsiBuilder, level: Int, mode: ExprMode): Boolean {
        b.exprMode = mode
//        val oldMode = b.exprMode
//        b.exprMode = mode
//        val result = parser.parse(b, level)
//        b.exprMode = oldMode
//        return result
        return true
    }

    @JvmStatic
    fun checkExtendedMode(b: PsiBuilder, level: Int): Boolean {
        return b.exprMode == ExprMode.EXTENDED
    }
}