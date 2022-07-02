package org.acornlang.language.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.acornlang.language.psi.AcornBlock
import org.acornlang.language.psi.AcornNamedEnumDecl
import org.acornlang.language.psi.AcornNamedStructDecl
import org.acornlang.language.psi.AcornTypes

class AcornFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()

        val blocks = PsiTreeUtil.findChildrenOfType(root, AcornBlock::class.java)
        for (block in blocks) {
            descriptors.add(FoldingDescriptor(block, block.textRange))
        }

        val structs = PsiTreeUtil.findChildrenOfType(root, AcornNamedStructDecl::class.java)
        for (struct in structs) {
            val lbrace = struct.node.findChildByType(AcornTypes.LBRACE) ?: continue
            val rbrace = struct.node.findChildByType(AcornTypes.RBRACE) ?: continue
            descriptors.add(FoldingDescriptor(struct, TextRange.create(lbrace.startOffset, rbrace.startOffset + 1)))
        }

        val enums = PsiTreeUtil.findChildrenOfType(root, AcornNamedEnumDecl::class.java)
        for (enum in enums) {
            val lbrace = enum.node.findChildByType(AcornTypes.LBRACE) ?: continue
            val rbrace = enum.node.findChildByType(AcornTypes.RBRACE) ?: continue
            descriptors.add(FoldingDescriptor(enum, TextRange.create(lbrace.startOffset, rbrace.startOffset + 1)))
        }

        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return "{...}"
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return false
    }


}