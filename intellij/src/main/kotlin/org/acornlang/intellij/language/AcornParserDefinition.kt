package org.acornlang.intellij.language

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.acornlang.intellij.language.parser.AcornParser
import org.acornlang.intellij.language.psi.AcornTypes

class AcornParserDefinition : ParserDefinition {
    companion object {
        private val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)
        private val STRINGS = TokenSet.create(AcornTypes.STRING)
//        private val COMMENTS = TokenSet.create(AcornTypes.COMMENT)

        private val FILE = IFileElementType(AcornLanguage)
    }

    override fun createLexer(project: Project?) = AcornLexerAdapter()

    override fun createParser(project: Project?) = AcornParser()

    override fun getFileNodeType() = FILE

    override fun getWhitespaceTokens() = WHITE_SPACES

    override fun getCommentTokens(): TokenSet = TokenSet.EMPTY

    override fun getStringLiteralElements(): TokenSet = STRINGS

    override fun createElement(node: ASTNode?): PsiElement = AcornTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider) = AcornFile(viewProvider)
}