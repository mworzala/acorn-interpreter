package org.acornlang.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import org.acornlang.language.psi.AcornNamedElement

abstract class AcornNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), AcornNamedElement {

}