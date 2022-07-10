package org.acornlang.language

import com.google.common.collect.Lists
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import org.acornlang.language.psi.AcornNamedFnDecl

//fun Project.findNamedFnDecls(name: String): List<AcornNamedFnDecl> {
//    val result = mutableListOf<AcornNamedFnDecl>()
//
//    val virtualFiles = FileTypeIndex.getFiles(AcornFileType, GlobalSearchScope.allScope(this))
//    for (virtualFile in virtualFiles) {
//        val acornFile = PsiManager.getInstance(this).findFile(virtualFile) as? AcornFile ?: continue
//        val namedFnDecls = PsiTreeUtil.getChildrenOfType(acornFile, AcornNamedFnDecl::class.java) ?: continue
//        for (namedFnDecl in namedFnDecls) {
//            if (namedFnDecl.name == name) {
//                result.add(namedFnDecl)
//            }
//        }
//    }
//
//    return result
//}
//
//fun Project.findNamedFnDecls(): List<AcornNamedFnDecl> {
//    val result = mutableListOf<AcornNamedFnDecl>()
//
//    val virtualFiles = FileTypeIndex.getFiles(AcornFileType, GlobalSearchScope.allScope(this))
//    for (virtualFile in virtualFiles) {
//        val acornFile = PsiManager.getInstance(this).findFile(virtualFile) as? AcornFile ?: continue
//        val namedFnDecls = PsiTreeUtil.getChildrenOfType(acornFile, AcornNamedFnDecl::class.java) ?: continue
//        for (namedFnDecl in namedFnDecls) {
//            result.add(namedFnDecl)
//        }
//    }
//
//    return result
//}
//
//fun AcornNamedFnDecl.findDocComment(): String {
//    val result = mutableListOf<String>()
//
//    var element = this.prevSibling
//    while (element is PsiComment || element is PsiWhiteSpace) {
//        if (element is PsiComment) {
//            result.add(element.text)
//        }
//        element = element.prevSibling
//    }
//
//    return StringUtil.join(Lists.reverse(result), "\n")
//}