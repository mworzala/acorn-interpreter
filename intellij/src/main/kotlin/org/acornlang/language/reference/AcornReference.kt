package org.acornlang.language.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult
import org.acornlang.language.AcornIcons
import org.acornlang.language.findNamedFnDecls

class AcornReference(
    element: PsiElement,
    textRange: TextRange,
) : PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference {

    val key: String = element.text.substring(textRange.startOffset, textRange.endOffset)

    init {
        println("AcornReference: $key")
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        println("multiResolve, key = $key")
        val namedFnDecls = myElement.project.findNamedFnDecls(key)
        val results = mutableListOf<ResolveResult>()

        for (namedFnDecl in namedFnDecls) {
            results.add(PsiElementResolveResult(namedFnDecl))
        }

        return results.toTypedArray()
    }

    override fun resolve(): PsiElement? {
        val results = multiResolve(false)
        return results.getOrNull(0)?.element
    }

    override fun getVariants(): Array<Any> {
        val namedFnDecls = myElement.project.findNamedFnDecls()
        val variants = mutableListOf<LookupElement>()

        for (namedFnDecl in namedFnDecls) {
            variants.add(LookupElementBuilder
                .create(namedFnDecl).withIcon(AcornIcons.FILE)
                .withTypeText(namedFnDecl.containingFile.name))
        }

        return variants.toTypedArray()
    }

}

//fun Project.findAllNamedFnDecls()