package org.acornlang.language.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.presentation.java.SymbolPresentationUtil
import org.acornlang.language.findDocComment
import org.acornlang.language.psi.AcornNamedFnDecl

class AcornDocumentationProvider : AbstractDocumentationProvider() {

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        if (element is AcornNamedFnDecl) {
            val file = SymbolPresentationUtil.getFilePathPresentation(element.containingFile)
            val name = element.name ?: "Unknown element"
            return renderDoc(file, name, element.findDocComment())
        }
        return super.generateDoc(element, originalElement)
    }

    override fun generateHoverDoc(element: PsiElement, originalElement: PsiElement?): String? {
        return generateDoc(element, originalElement)
    }

    private fun renderDoc(file: String, name: String, doc: String): String {
        val builder = StringBuilder()
        builder.append(DocumentationMarkup.DEFINITION_START)
        builder.append(name)
        builder.append(DocumentationMarkup.DEFINITION_END)
        builder.append(DocumentationMarkup.CONTENT_START)
        builder.append(doc)
        builder.append(DocumentationMarkup.CONTENT_END)
        builder.append(DocumentationMarkup.SECTIONS_START)

        builder.append(DocumentationMarkup.SECTION_HEADER_START)
        builder.append("File: ")
        builder.append(DocumentationMarkup.SECTION_SEPARATOR)
        builder.append("<p>")
        builder.append(file)
        builder.append(DocumentationMarkup.SECTION_END)

        builder.append(DocumentationMarkup.SECTIONS_END)

        return doc
    }

}