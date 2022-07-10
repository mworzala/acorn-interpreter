// This is a generated file. Not intended for manual editing.
package org.acornlang.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import org.acornlang.language.psi.impl.*;

public interface AcornTypes {

  IElementType ARRAY_EXPR = new AcornElementType("ARRAY_EXPR");
  IElementType BINARY_EXPR = new AcornElementType("BINARY_EXPR");
  IElementType BINARY_OP = new AcornElementType("BINARY_OP");
  IElementType BLOCK = new AcornElementType("BLOCK");
  IElementType BREAK_STMT = new AcornElementType("BREAK_STMT");
  IElementType CALL_EXPR = new AcornElementType("CALL_EXPR");
  IElementType CONSTRUCT_ARG = new AcornElementType("CONSTRUCT_ARG");
  IElementType CONSTRUCT_EXPR = new AcornElementType("CONSTRUCT_EXPR");
  IElementType CONST_DECL = new AcornElementType("CONST_DECL");
  IElementType CONTAINER_ITEM = new AcornElementType("CONTAINER_ITEM");
  IElementType CONTINUE_STMT = new AcornElementType("CONTINUE_STMT");
  IElementType ENUM_CASE = new AcornElementType("ENUM_CASE");
  IElementType ENUM_CASE_LIST = new AcornElementType("ENUM_CASE_LIST");
  IElementType EXPR = new AcornElementType("EXPR");
  IElementType FN_PARAM = new AcornElementType("FN_PARAM");
  IElementType FN_PARAM_LIST = new AcornElementType("FN_PARAM_LIST");
  IElementType IF_EXPR = new AcornElementType("IF_EXPR");
  IElementType INDEX_EXPR = new AcornElementType("INDEX_EXPR");
  IElementType INTRINSIC_REF_EXPR = new AcornElementType("INTRINSIC_REF_EXPR");
  IElementType LITERAL_EXPR = new AcornElementType("LITERAL_EXPR");
  IElementType NAMED_ENUM_DECL = new AcornElementType("NAMED_ENUM_DECL");
  IElementType NAMED_FN_DECL = new AcornElementType("NAMED_FN_DECL");
  IElementType NAMED_SPEC_DECL = new AcornElementType("NAMED_SPEC_DECL");
  IElementType NAMED_STRUCT_DECL = new AcornElementType("NAMED_STRUCT_DECL");
  IElementType NAMED_UNION_DECL = new AcornElementType("NAMED_UNION_DECL");
  IElementType PAREN_EXPR = new AcornElementType("PAREN_EXPR");
  IElementType RETURN_STMT = new AcornElementType("RETURN_STMT");
  IElementType SELECT_EXPR = new AcornElementType("SELECT_EXPR");
  IElementType STMT = new AcornElementType("STMT");
  IElementType STRUCT_FIELD = new AcornElementType("STRUCT_FIELD");
  IElementType STRUCT_FIELD_LIST = new AcornElementType("STRUCT_FIELD_LIST");
  IElementType TUPLE_EXPR = new AcornElementType("TUPLE_EXPR");
  IElementType TYPE_UNION_EXPR = new AcornElementType("TYPE_UNION_EXPR");
  IElementType UNION_FIELD = new AcornElementType("UNION_FIELD");
  IElementType UNION_FIELD_LIST = new AcornElementType("UNION_FIELD_LIST");
  IElementType VAR_DECL_STMT = new AcornElementType("VAR_DECL_STMT");
  IElementType VAR_REF_EXPR = new AcornElementType("VAR_REF_EXPR");
  IElementType WHILE_EXPR = new AcornElementType("WHILE_EXPR");

  IElementType AMP = new AcornTokenType("&");
  IElementType AMPAMP = new AcornTokenType("&&");
  IElementType AT = new AcornTokenType("@");
  IElementType BANG = new AcornTokenType("!");
  IElementType BANGEQ = new AcornTokenType("!=");
  IElementType BARBAR = new AcornTokenType("||");
  IElementType BREAK = new AcornTokenType("break");
  IElementType COLON = new AcornTokenType(":");
  IElementType COMMA = new AcornTokenType(",");
  IElementType CONST = new AcornTokenType("const");
  IElementType CONTINUE = new AcornTokenType("continue");
  IElementType DOC_COMMENT = new AcornTokenType("DOC_COMMENT");
  IElementType DOT = new AcornTokenType(".");
  IElementType ELSE = new AcornTokenType("else");
  IElementType ENUM = new AcornTokenType("enum");
  IElementType EQ = new AcornTokenType("=");
  IElementType EQEQ = new AcornTokenType("==");
  IElementType FALSE = new AcornTokenType("false");
  IElementType FN = new AcornTokenType("fn");
  IElementType FOREIGN = new AcornTokenType("foreign");
  IElementType GT = new AcornTokenType(">");
  IElementType GTEQ = new AcornTokenType(">=");
  IElementType IDENT = new AcornTokenType("IDENT");
  IElementType IF = new AcornTokenType("if");
  IElementType INTRINSIC_IDENT = new AcornTokenType("INTRINSIC_IDENT");
  IElementType LBRACE = new AcornTokenType("{");
  IElementType LBRACKET = new AcornTokenType("[");
  IElementType LET = new AcornTokenType("let");
  IElementType LINE_COMMENT = new AcornTokenType("LINE_COMMENT");
  IElementType LPAREN = new AcornTokenType("(");
  IElementType LT = new AcornTokenType("<");
  IElementType LTEQ = new AcornTokenType("<=");
  IElementType MINUS = new AcornTokenType("-");
  IElementType MUT = new AcornTokenType("mut");
  IElementType NUMBER = new AcornTokenType("NUMBER");
  IElementType PLUS = new AcornTokenType("+");
  IElementType RBRACE = new AcornTokenType("}");
  IElementType RBRACKET = new AcornTokenType("]");
  IElementType RETURN = new AcornTokenType("return");
  IElementType RPAREN = new AcornTokenType(")");
  IElementType SEMI = new AcornTokenType(";");
  IElementType SLASH = new AcornTokenType("/");
  IElementType SPEC = new AcornTokenType("spec");
  IElementType STAR = new AcornTokenType("*");
  IElementType STRING = new AcornTokenType("STRING");
  IElementType STRUCT = new AcornTokenType("struct");
  IElementType TRUE = new AcornTokenType("true");
  IElementType UNION = new AcornTokenType("union");
  IElementType WHILE = new AcornTokenType("while");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ARRAY_EXPR) {
        return new AcornArrayExprImpl(node);
      }
      else if (type == BINARY_EXPR) {
        return new AcornBinaryExprImpl(node);
      }
      else if (type == BINARY_OP) {
        return new AcornBinaryOpImpl(node);
      }
      else if (type == BLOCK) {
        return new AcornBlockImpl(node);
      }
      else if (type == BREAK_STMT) {
        return new AcornBreakStmtImpl(node);
      }
      else if (type == CALL_EXPR) {
        return new AcornCallExprImpl(node);
      }
      else if (type == CONSTRUCT_ARG) {
        return new AcornConstructArgImpl(node);
      }
      else if (type == CONSTRUCT_EXPR) {
        return new AcornConstructExprImpl(node);
      }
      else if (type == CONST_DECL) {
        return new AcornConstDeclImpl(node);
      }
      else if (type == CONTAINER_ITEM) {
        return new AcornContainerItemImpl(node);
      }
      else if (type == CONTINUE_STMT) {
        return new AcornContinueStmtImpl(node);
      }
      else if (type == ENUM_CASE) {
        return new AcornEnumCaseImpl(node);
      }
      else if (type == ENUM_CASE_LIST) {
        return new AcornEnumCaseListImpl(node);
      }
      else if (type == FN_PARAM) {
        return new AcornFnParamImpl(node);
      }
      else if (type == FN_PARAM_LIST) {
        return new AcornFnParamListImpl(node);
      }
      else if (type == IF_EXPR) {
        return new AcornIfExprImpl(node);
      }
      else if (type == INDEX_EXPR) {
        return new AcornIndexExprImpl(node);
      }
      else if (type == INTRINSIC_REF_EXPR) {
        return new AcornIntrinsicRefExprImpl(node);
      }
      else if (type == LITERAL_EXPR) {
        return new AcornLiteralExprImpl(node);
      }
      else if (type == NAMED_ENUM_DECL) {
        return new AcornNamedEnumDeclImpl(node);
      }
      else if (type == NAMED_FN_DECL) {
        return new AcornNamedFnDeclImpl(node);
      }
      else if (type == NAMED_SPEC_DECL) {
        return new AcornNamedSpecDeclImpl(node);
      }
      else if (type == NAMED_STRUCT_DECL) {
        return new AcornNamedStructDeclImpl(node);
      }
      else if (type == NAMED_UNION_DECL) {
        return new AcornNamedUnionDeclImpl(node);
      }
      else if (type == PAREN_EXPR) {
        return new AcornParenOrTupleExprImpl(node);
      }
      else if (type == RETURN_STMT) {
        return new AcornReturnStmtImpl(node);
      }
      else if (type == SELECT_EXPR) {
        return new AcornSelectExprImpl(node);
      }
      else if (type == STMT) {
        return new AcornStmtImpl(node);
      }
      else if (type == STRUCT_FIELD) {
        return new AcornStructFieldImpl(node);
      }
      else if (type == STRUCT_FIELD_LIST) {
        return new AcornStructFieldListImpl(node);
      }
      else if (type == TUPLE_EXPR) {
        return new AcornTupleExprUpperImpl(node);
      }
      else if (type == TYPE_UNION_EXPR) {
        return new AcornTypeUnionExprImpl(node);
      }
      else if (type == UNION_FIELD) {
        return new AcornUnionFieldImpl(node);
      }
      else if (type == UNION_FIELD_LIST) {
        return new AcornUnionFieldListImpl(node);
      }
      else if (type == VAR_DECL_STMT) {
        return new AcornVarDeclStmtImpl(node);
      }
      else if (type == VAR_REF_EXPR) {
        return new AcornVarRefExprImpl(node);
      }
      else if (type == WHILE_EXPR) {
        return new AcornWhileExprImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
