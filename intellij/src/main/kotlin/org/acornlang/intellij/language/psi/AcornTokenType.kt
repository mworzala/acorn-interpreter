package org.acornlang.intellij.language.psi

import com.intellij.psi.tree.IElementType
import org.acornlang.intellij.language.AcornLanguage

class AcornTokenType(debugName: String) : IElementType(debugName, AcornLanguage) {

    override fun toString() = "AcornTokenType.${super.toString()}"

}