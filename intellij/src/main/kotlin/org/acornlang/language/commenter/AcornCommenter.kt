package org.acornlang.language.commenter

import com.intellij.lang.Commenter

class AcornCommenter : Commenter {

    override fun getLineCommentPrefix() = "//"

    override fun getBlockCommentPrefix(): String? = null

    override fun getBlockCommentSuffix(): String? = null

    override fun getCommentedBlockCommentPrefix(): String? = null

    override fun getCommentedBlockCommentSuffix(): String? = null

}