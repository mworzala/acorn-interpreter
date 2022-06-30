package org.acornlang.language.lexer

import com.intellij.lexer.FlexAdapter

class AcornLexerAdapter : FlexAdapter(AcornLexer(null))