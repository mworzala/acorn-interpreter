package org.acornlang.language.reference

import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext
import org.acornlang.language.psi.AcornCallExpr

class AcornReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(AcornCallExpr::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    val callExpr = element as AcornCallExpr
                    val value = callExpr.ident.text
                    println("CALL EXPR :: $value")

                    val range = callExpr.ident.textRange

                    return arrayOf(
                        AcornReference(element, range)
                    )
                }
            }
        )
    }

}