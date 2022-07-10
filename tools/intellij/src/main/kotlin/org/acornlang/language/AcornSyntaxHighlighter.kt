package org.acornlang.language

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.acornlang.language.lexer.AcornLexerAdapter
import org.acornlang.language.psi.AcornTypes

class AcornSyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        val IDENT = createTextAttributesKey("ACORN_IDENT", DefaultLanguageHighlighterColors.IDENTIFIER)

        //todo redo these to be more focused on acorn & define named attributes above (look above)
        // also add some missing ones
        val IDENTIFIER_KEYS = arrayOf(IDENT)
        val KEYWORD_KEYS = arrayOf(DefaultLanguageHighlighterColors.KEYWORD)
        val NUMBER_KEYS = arrayOf(DefaultLanguageHighlighterColors.NUMBER)
        val STRING_KEYS = arrayOf(DefaultLanguageHighlighterColors.STRING)
        val OP_KEYS = arrayOf(DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val BRACE_KEYS = arrayOf(DefaultLanguageHighlighterColors.BRACES)
        val DOT_KEYS = arrayOf(DefaultLanguageHighlighterColors.DOT)
        val SEMICOLON_KEYS = arrayOf(DefaultLanguageHighlighterColors.SEMICOLON)
        val COMMA_KEYS = arrayOf(DefaultLanguageHighlighterColors.COMMA)
        val PAREN_KEYS = arrayOf(DefaultLanguageHighlighterColors.PARENTHESES)
//        val DOC_COMMENT_KEYS = arrayOf(DefaultLanguageHighlighterColors.DOC_COMMENT)

        val EMPTY_KEYS = arrayOf<TextAttributesKey>()

        val KEYWORDS = listOf(
            AcornTypes.CONST, AcornTypes.ELSE, AcornTypes.ENUM,
            AcornTypes.FALSE, AcornTypes.FN, AcornTypes.FOREIGN,
            AcornTypes.IF, AcornTypes.LET, AcornTypes.MUT,
            AcornTypes.RETURN, AcornTypes.SPEC, AcornTypes.STRUCT,
            AcornTypes.TRUE, AcornTypes.WHILE,
        )
        val OPS = listOf(
            AcornTypes.MINUS, AcornTypes.PLUS, AcornTypes.STAR,
            AcornTypes.SLASH, AcornTypes.EQEQ, AcornTypes.BANG,
            AcornTypes.BANGEQ, AcornTypes.LT, AcornTypes.LTEQ,
            AcornTypes.GT, AcornTypes.GTEQ, AcornTypes.AMPAMP,
            AcornTypes.BARBAR, AcornTypes.EQ,
        )
    }

    override fun getHighlightingLexer() = AcornLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType?) = when (tokenType) {
        AcornTypes.IDENT -> IDENTIFIER_KEYS
        in KEYWORDS -> KEYWORD_KEYS
        AcornTypes.NUMBER -> NUMBER_KEYS
        AcornTypes.STRING -> STRING_KEYS
        in OPS -> OP_KEYS
        AcornTypes.LBRACE, AcornTypes.RBRACE -> BRACE_KEYS
        AcornTypes.DOT -> DOT_KEYS
        AcornTypes.SEMI -> SEMICOLON_KEYS
        AcornTypes.COMMA -> COMMA_KEYS
        AcornTypes.LPAREN, AcornTypes.RPAREN -> PAREN_KEYS
//        AcornTypes.DOC_COMMENT -> DOC_COMMENT_KEYS
        else -> EMPTY_KEYS
    }



}