package org.acornlang.intellij.language

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

class AcornFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, AcornLanguage) {

    override fun getFileType() = AcornFileType

    override fun toString() = "Acorn File"
}