package org.acornlang.intellij.language

import com.intellij.lexer.FlexAdapter

class AcornLexerAdapter : FlexAdapter(AcornLexer(null)) {

}