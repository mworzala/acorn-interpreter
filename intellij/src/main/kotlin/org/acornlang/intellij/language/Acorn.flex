// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.acornlang.intellij.language;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.acornlang.intellij.language.psi.AcornTypes;
import com.intellij.psi.TokenType;

%%

%class AcornLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

WHITE_SPACE=[\ \n\t\f\r]
DIGIT=[0-9]
FIRST_IDENT_CHAR=[a-zA-Z_]
IDENT_CHAR=[a-zA-Z0-9_]

STRING_LITERAL=\"[^[\"\n]]*\"

%%

<YYINITIAL> {

    // Symbols
    "("    { return AcornTypes.LPAREN; }
    ")"    { return AcornTypes.RPAREN; }
    "{"    { return AcornTypes.LBRACE; }
    "}"    { return AcornTypes.RBRACE; }
    "-"    { return AcornTypes.MINUS; }
    "+"    { return AcornTypes.PLUS; }
    "*"    { return AcornTypes.STAR; }
    "/"    { return AcornTypes.SLASH; }
    "="    { return AcornTypes.EQ; }
    "=="   { return AcornTypes.EQEQ; }
    "!"    { return AcornTypes.BANG; }
    "!="   { return AcornTypes.BANGEQ; }
    "<"    { return AcornTypes.LT; }
    "<="   { return AcornTypes.LTEQ; }
    ">"    { return AcornTypes.GT; }
    ">="   { return AcornTypes.GTEQ; }
    "&&"   { return AcornTypes.AMPAMP; }
    "||"   { return AcornTypes.BARBAR; }
    ";"    { return AcornTypes.SEMI; }
    ","    { return AcornTypes.COMMA; }
    ":"    { return AcornTypes.COLON; }
    "."    { return AcornTypes.DOT; }

    // Keywords
    "else"    { return AcornTypes.ELSE; }
    "enum"    { return AcornTypes.ENUM; }
    "false"   { return AcornTypes.FALSE; }
    "fn"      { return AcornTypes.FN; }
    "foreign" { return AcornTypes.FOREIGN; }
    "if"      { return AcornTypes.IF; }
    "let"     { return AcornTypes.LET; }
    "return"  { return AcornTypes.RETURN; }
    "struct"  { return AcornTypes.STRUCT; }
    "true"    { return AcornTypes.TRUE; }
    "while"   { return AcornTypes.WHILE; }

    // Literals
    {DIGIT}+                          { return AcornTypes.NUMBER; }
    {FIRST_IDENT_CHAR}{IDENT_CHAR}*   { return AcornTypes.IDENT; }
    {STRING_LITERAL}                  { return AcornTypes.STRING; }

    // Special
    {WHITE_SPACE}+                    { return TokenType.WHITE_SPACE; }
}

[^]                                   { return TokenType.BAD_CHARACTER; }
