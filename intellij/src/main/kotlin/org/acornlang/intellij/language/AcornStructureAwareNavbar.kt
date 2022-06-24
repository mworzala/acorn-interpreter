package org.acornlang.intellij.language

import com.intellij.icons.AllIcons
import com.intellij.ide.navigationToolbar.StructureAwareNavBarModelExtension
import com.intellij.lang.Language
import org.acornlang.intellij.language.psi.AcornNamedFnDecl
import javax.swing.Icon

class AcornStructureAwareNavbar : StructureAwareNavBarModelExtension() {
    override val language: Language = AcornLanguage

    override fun getPresentableText(obj: Any?): String? {
        if (obj is AcornFile) {
            return obj.name
        }

        //todo

        return null
    }

    override fun getIcon(obj: Any?): Icon? {
        if (obj is AcornNamedFnDecl) {
            return AllIcons.Nodes.Function
        }

        return null;
    }

}