package org.acornlang.language.psi

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

object AcornPsiImplUtil {

    @JvmStatic
    fun getName(element: AcornNamedFnDecl): String? {
//        println("getName() == ${element.ident.text}")
        return element.ident.text
    }

    @JvmStatic
    fun setName(element: AcornNamedFnDecl, name: String): PsiElement {
        return element //todo
    }

    @JvmStatic
    fun getNameIdentifier(element: AcornNamedFnDecl): PsiElement {
        return element.ident
    }

    @JvmStatic
    fun getTextOffset(element: AcornNamedFnDecl): Int {
        return element.ident.textOffset
    }

}