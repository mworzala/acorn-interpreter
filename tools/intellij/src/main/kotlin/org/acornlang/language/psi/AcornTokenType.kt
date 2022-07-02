package org.acornlang.language.psi

import com.intellij.psi.tree.IElementType
import org.acornlang.language.AcornLanguage

class AcornTokenType(debugName: String) : IElementType(debugName, AcornLanguage) {

    override fun toString() = "AcornTokenType.${super.toString()}"

}