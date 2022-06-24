package org.acornlang.intellij.language

import com.intellij.openapi.fileTypes.LanguageFileType

object AcornFileType : LanguageFileType(AcornLanguage) {

    override fun getName() = "Acorn File"

    override fun getDescription() = "Acorn language file"

    override fun getDefaultExtension() = "acorn"

    override fun getIcon() = AcornIcons.FILE

}