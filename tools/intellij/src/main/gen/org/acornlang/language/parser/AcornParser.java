// This is a generated file. Not intended for manual editing.
package org.acornlang.language.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static org.acornlang.language.psi.AcornTypes.*;
import static org.acornlang.language.parser.AcornParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class AcornParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return File(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(BREAK_STMT, CONTINUE_STMT, RETURN_STMT, STMT,
      VAR_DECL_STMT),
    create_token_set_(CONST_DECL, CONTAINER_ITEM, NAMED_ENUM_DECL, NAMED_FN_DECL,
      NAMED_SPEC_DECL, NAMED_STRUCT_DECL, NAMED_UNION_DECL),
    create_token_set_(ARRAY_EXPR, ASSIGN_EXPR, BINARY_EXPR, BLOCK,
      CALL_EXPR, CONSTRUCT_EXPR, EXPR, HEADLESS_SELECT_EXPR,
      IF_EXPR, INDEX_EXPR, INTRINSIC_REF_EXPR, LITERAL_EXPR,
      NEGATE_UNARY_EXPR, NOT_UNARY_EXPR, PAREN_EXPR, REF_UNARY_EXPR,
      SELECT_EXPR, STRUCT_DECL_EXPR, TYPE_UNION_EXPR, VAR_REF_EXPR,
      WHILE_EXPR),
  };

  /* ********************************************************** */
  // PLUS | MINUS
  public static boolean AddBinOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AddBinOp")) return false;
    if (!nextTokenIs(b, "<add bin op>", MINUS, PLUS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BINARY_OP, "<add bin op>");
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // BREAK
  public static boolean BreakStmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BreakStmt")) return false;
    if (!nextTokenIs(b, BREAK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BREAK);
    exit_section_(b, m, BREAK_STMT, r);
    return r;
  }

  /* ********************************************************** */
  // (ExtendedExpr (COMMA (ExtendedExpr | &RBRACE))*)?
  static boolean CallArgList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CallArgList")) return false;
    CallArgList_0(b, l + 1);
    return true;
  }

  // ExtendedExpr (COMMA (ExtendedExpr | &RBRACE))*
  private static boolean CallArgList_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CallArgList_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ExtendedExpr(b, l + 1);
    p = r; // pin = 1
    r = r && CallArgList_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (COMMA (ExtendedExpr | &RBRACE))*
  private static boolean CallArgList_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CallArgList_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!CallArgList_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "CallArgList_0_1", c)) break;
    }
    return true;
  }

  // COMMA (ExtendedExpr | &RBRACE)
  private static boolean CallArgList_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CallArgList_0_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && CallArgList_0_1_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ExtendedExpr | &RBRACE
  private static boolean CallArgList_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CallArgList_0_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ExtendedExpr(b, l + 1);
    if (!r) r = CallArgList_0_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &RBRACE
  private static boolean CallArgList_0_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CallArgList_0_1_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // EQEQ | BANGEQ | LT | LTEQ | GT | GTEQ
  public static boolean CmpBinOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CmpBinOp")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BINARY_OP, "<cmp bin op>");
    r = consumeToken(b, EQEQ);
    if (!r) r = consumeToken(b, BANGEQ);
    if (!r) r = consumeToken(b, LT);
    if (!r) r = consumeToken(b, LTEQ);
    if (!r) r = consumeToken(b, GT);
    if (!r) r = consumeToken(b, GTEQ);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // CONST IDENT (COLON LimitedExpr)? EQ Expr SEMI
  public static boolean ConstDecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDecl")) return false;
    if (!nextTokenIs(b, CONST)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONST_DECL, null);
    r = consumeTokens(b, 1, CONST, IDENT);
    p = r; // pin = 1
    r = r && report_error_(b, ConstDecl_2(b, l + 1));
    r = p && report_error_(b, consumeToken(b, EQ)) && r;
    r = p && report_error_(b, Expr(b, l + 1, -1)) && r;
    r = p && consumeToken(b, SEMI) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (COLON LimitedExpr)?
  private static boolean ConstDecl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDecl_2")) return false;
    ConstDecl_2_0(b, l + 1);
    return true;
  }

  // COLON LimitedExpr
  private static boolean ConstDecl_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstDecl_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && LimitedExpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENT COLON ExtendedExpr
  public static boolean ConstructArg(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstructArg")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONSTRUCT_ARG, null);
    r = consumeTokens(b, 1, IDENT, COLON);
    p = r; // pin = 1
    r = r && ExtendedExpr(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // (ConstructArg (COMMA (ConstructArg | &RBRACE))*)?
  static boolean ConstructArgList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstructArgList")) return false;
    ConstructArgList_0(b, l + 1);
    return true;
  }

  // ConstructArg (COMMA (ConstructArg | &RBRACE))*
  private static boolean ConstructArgList_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstructArgList_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ConstructArg(b, l + 1);
    p = r; // pin = 1
    r = r && ConstructArgList_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (COMMA (ConstructArg | &RBRACE))*
  private static boolean ConstructArgList_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstructArgList_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ConstructArgList_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ConstructArgList_0_1", c)) break;
    }
    return true;
  }

  // COMMA (ConstructArg | &RBRACE)
  private static boolean ConstructArgList_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstructArgList_0_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && ConstructArgList_0_1_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ConstructArg | &RBRACE
  private static boolean ConstructArgList_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstructArgList_0_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ConstructArg(b, l + 1);
    if (!r) r = ConstructArgList_0_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &RBRACE
  private static boolean ConstructArgList_0_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstructArgList_0_1_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ConstDecl
  //                 | NamedFnDecl
  //                 | NamedEnumDecl
  //                 | NamedStructDecl
  //                 | NamedUnionDecl
  //                 | NamedSpecDecl
  public static boolean ContainerItem(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContainerItem")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, CONTAINER_ITEM, "<container item>");
    r = ConstDecl(b, l + 1);
    if (!r) r = NamedFnDecl(b, l + 1);
    if (!r) r = NamedEnumDecl(b, l + 1);
    if (!r) r = NamedStructDecl(b, l + 1);
    if (!r) r = NamedUnionDecl(b, l + 1);
    if (!r) r = NamedSpecDecl(b, l + 1);
    exit_section_(b, l, m, r, false, AcornParser::ContainerItem_recover);
    return r;
  }

  /* ********************************************************** */
  // !(CONST | FN | ENUM | STRUCT | UNION | SPEC)
  static boolean ContainerItem_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContainerItem_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ContainerItem_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // CONST | FN | ENUM | STRUCT | UNION | SPEC
  private static boolean ContainerItem_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContainerItem_recover_0")) return false;
    boolean r;
    r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, FN);
    if (!r) r = consumeToken(b, ENUM);
    if (!r) r = consumeToken(b, STRUCT);
    if (!r) r = consumeToken(b, UNION);
    if (!r) r = consumeToken(b, SPEC);
    return r;
  }

  /* ********************************************************** */
  // CONTINUE
  public static boolean ContinueStmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContinueStmt")) return false;
    if (!nextTokenIs(b, CONTINUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CONTINUE);
    exit_section_(b, m, CONTINUE_STMT, r);
    return r;
  }

  /* ********************************************************** */
  // LBRACE EnumCaseList? RBRACE
  static boolean EnumBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumBody")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && EnumBody_1(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // EnumCaseList?
  private static boolean EnumBody_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumBody_1")) return false;
    EnumCaseList(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // IDENT
  public static boolean EnumCase(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumCase")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    exit_section_(b, m, ENUM_CASE, r);
    return r;
  }

  /* ********************************************************** */
  // EnumCase (COMMA (EnumCase | &RBRACE))*
  public static boolean EnumCaseList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumCaseList")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = EnumCase(b, l + 1);
    r = r && EnumCaseList_1(b, l + 1);
    exit_section_(b, m, ENUM_CASE_LIST, r);
    return r;
  }

  // (COMMA (EnumCase | &RBRACE))*
  private static boolean EnumCaseList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumCaseList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!EnumCaseList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "EnumCaseList_1", c)) break;
    }
    return true;
  }

  // COMMA (EnumCase | &RBRACE)
  private static boolean EnumCaseList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumCaseList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && EnumCaseList_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // EnumCase | &RBRACE
  private static boolean EnumCaseList_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumCaseList_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = EnumCase(b, l + 1);
    if (!r) r = EnumCaseList_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &RBRACE
  private static boolean EnumCaseList_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "EnumCaseList_1_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(SEMI | RBRACE)
  static boolean Expr_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Expr_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !Expr_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMI | RBRACE
  private static boolean Expr_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Expr_recover_0")) return false;
    boolean r;
    r = consumeToken(b, SEMI);
    if (!r) r = consumeToken(b, RBRACE);
    return r;
  }

  /* ********************************************************** */
  // <<setExprMode 'ExprMode.EXTENDED' Expr>>
  static boolean ExtendedExpr(PsiBuilder b, int l) {
    return setExprMode(b, l + 1, ExprMode.EXTENDED, Expr_parser_);
  }

  /* ********************************************************** */
  // ContainerItem* <<eof>>
  static boolean File(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = File_0(b, l + 1);
    r = r && eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ContainerItem*
  private static boolean File_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ContainerItem(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "File_0", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // IDENT (COLON LimitedExpr)?
  public static boolean FnParam(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FnParam")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FN_PARAM, "<fn param>");
    r = consumeToken(b, IDENT);
    p = r; // pin = 1
    r = r && FnParam_1(b, l + 1);
    exit_section_(b, l, m, r, p, AcornParser::FnParam_recover);
    return r || p;
  }

  // (COLON LimitedExpr)?
  private static boolean FnParam_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FnParam_1")) return false;
    FnParam_1_0(b, l + 1);
    return true;
  }

  // COLON LimitedExpr
  private static boolean FnParam_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FnParam_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && LimitedExpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN (FnParam (COMMA (FnParam | &RPAREN))*)? RPAREN
  public static boolean FnParamList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FnParamList")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FN_PARAM_LIST, null);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && report_error_(b, FnParamList_1(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (FnParam (COMMA (FnParam | &RPAREN))*)?
  private static boolean FnParamList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FnParamList_1")) return false;
    FnParamList_1_0(b, l + 1);
    return true;
  }

  // FnParam (COMMA (FnParam | &RPAREN))*
  private static boolean FnParamList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FnParamList_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = FnParam(b, l + 1);
    p = r; // pin = 1
    r = r && FnParamList_1_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (COMMA (FnParam | &RPAREN))*
  private static boolean FnParamList_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FnParamList_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!FnParamList_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "FnParamList_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA (FnParam | &RPAREN)
  private static boolean FnParamList_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FnParamList_1_0_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && FnParamList_1_0_1_0_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // FnParam | &RPAREN
  private static boolean FnParamList_1_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FnParamList_1_0_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FnParam(b, l + 1);
    if (!r) r = FnParamList_1_0_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &RPAREN
  private static boolean FnParamList_1_0_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FnParamList_1_0_1_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, RPAREN);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(COMMA | RPAREN)
  static boolean FnParam_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FnParam_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !FnParam_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // COMMA | RPAREN
  private static boolean FnParam_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FnParam_recover_0")) return false;
    boolean r;
    r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, RPAREN);
    return r;
  }

  /* ********************************************************** */
  // FnParamList LimitedExpr
  static boolean FnProto(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "FnProto")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = FnParamList(b, l + 1);
    r = r && LimitedExpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // <<setExprMode 'ExprMode.LIMITED' Expr>>
  static boolean LimitedExpr(PsiBuilder b, int l) {
    return setExprMode(b, l + 1, ExprMode.LIMITED, Expr_parser_);
  }

  /* ********************************************************** */
  // AMPAMP | BARBAR
  public static boolean LogBinOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LogBinOp")) return false;
    if (!nextTokenIs(b, "<log bin op>", AMPAMP, BARBAR)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BINARY_OP, "<log bin op>");
    r = consumeToken(b, AMPAMP);
    if (!r) r = consumeToken(b, BARBAR);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // STAR | SLASH
  public static boolean MulBinOp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "MulBinOp")) return false;
    if (!nextTokenIs(b, "<mul bin op>", SLASH, STAR)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BINARY_OP, "<mul bin op>");
    r = consumeToken(b, STAR);
    if (!r) r = consumeToken(b, SLASH);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ENUM IDENT EnumBody
  public static boolean NamedEnumDecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NamedEnumDecl")) return false;
    if (!nextTokenIs(b, ENUM)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, NAMED_ENUM_DECL, null);
    r = consumeTokens(b, 1, ENUM, IDENT);
    p = r; // pin = 1
    r = r && EnumBody(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // FN IDENT FnProto (Block | SEMI)
  public static boolean NamedFnDecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NamedFnDecl")) return false;
    if (!nextTokenIs(b, FN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, NAMED_FN_DECL, null);
    r = consumeTokens(b, 1, FN, IDENT);
    p = r; // pin = 1
    r = r && report_error_(b, FnProto(b, l + 1));
    r = p && NamedFnDecl_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // Block | SEMI
  private static boolean NamedFnDecl_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NamedFnDecl_3")) return false;
    boolean r;
    r = Block(b, l + 1);
    if (!r) r = consumeToken(b, SEMI);
    return r;
  }

  /* ********************************************************** */
  // SPEC IDENT SpecBody
  public static boolean NamedSpecDecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NamedSpecDecl")) return false;
    if (!nextTokenIs(b, SPEC)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, NAMED_SPEC_DECL, null);
    r = consumeTokens(b, 1, SPEC, IDENT);
    p = r; // pin = 1
    r = r && SpecBody(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // STRUCT IDENT StructBody
  public static boolean NamedStructDecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NamedStructDecl")) return false;
    if (!nextTokenIs(b, STRUCT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, NAMED_STRUCT_DECL, null);
    r = consumeTokens(b, 1, STRUCT, IDENT);
    p = r; // pin = 1
    r = r && StructBody(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // UNION IDENT UnionBody
  public static boolean NamedUnionDecl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NamedUnionDecl")) return false;
    if (!nextTokenIs(b, UNION)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, NAMED_UNION_DECL, null);
    r = consumeTokens(b, 1, UNION, IDENT);
    p = r; // pin = 1
    r = r && UnionBody(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // RETURN ExtendedExpr?
  public static boolean ReturnStmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReturnStmt")) return false;
    if (!nextTokenIs(b, RETURN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RETURN_STMT, null);
    r = consumeToken(b, RETURN);
    p = r; // pin = 1
    r = r && ReturnStmt_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ExtendedExpr?
  private static boolean ReturnStmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ReturnStmt_1")) return false;
    ExtendedExpr(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // LBRACE NamedFnDecl* RBRACE
  static boolean SpecBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SpecBody")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && SpecBody_1(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // NamedFnDecl*
  private static boolean SpecBody_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SpecBody_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!NamedFnDecl(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "SpecBody_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ReturnStmt
  //        | BreakStmt
  //        | ContinueStmt
  //        | VarDeclStmt
  //        | ExtendedExpr
  public static boolean Stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Stmt")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, STMT, "<stmt>");
    r = ReturnStmt(b, l + 1);
    if (!r) r = BreakStmt(b, l + 1);
    if (!r) r = ContinueStmt(b, l + 1);
    if (!r) r = VarDeclStmt(b, l + 1);
    if (!r) r = ExtendedExpr(b, l + 1);
    exit_section_(b, l, m, r, false, AcornParser::Stmt_recover);
    return r;
  }

  /* ********************************************************** */
  // Expr_recover
  static boolean Stmt_recover(PsiBuilder b, int l) {
    return Expr_recover(b, l + 1);
  }

  /* ********************************************************** */
  // LBRACE StructFieldList? NamedFnDecl* RBRACE
  static boolean StructBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructBody")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && StructBody_1(b, l + 1);
    r = r && StructBody_2(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // StructFieldList?
  private static boolean StructBody_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructBody_1")) return false;
    StructFieldList(b, l + 1);
    return true;
  }

  // NamedFnDecl*
  private static boolean StructBody_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructBody_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!NamedFnDecl(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "StructBody_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // IDENT COLON LimitedExpr
  public static boolean StructField(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructField")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, STRUCT_FIELD, "<struct field>");
    r = consumeTokens(b, 1, IDENT, COLON);
    p = r; // pin = 1
    r = r && LimitedExpr(b, l + 1);
    exit_section_(b, l, m, r, p, AcornParser::StructField_recover);
    return r || p;
  }

  /* ********************************************************** */
  // (StructField COMMA)*
  public static boolean StructFieldList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructFieldList")) return false;
    Marker m = enter_section_(b, l, _NONE_, STRUCT_FIELD_LIST, "<struct field list>");
    while (true) {
      int c = current_position_(b);
      if (!StructFieldList_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "StructFieldList", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // StructField COMMA
  private static boolean StructFieldList_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructFieldList_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = StructField(b, l + 1);
    r = r && consumeToken(b, COMMA);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(COMMA | RBRACE)
  static boolean StructField_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructField_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !StructField_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // COMMA | RBRACE
  private static boolean StructField_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructField_recover_0")) return false;
    boolean r;
    r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, RBRACE);
    return r;
  }

  /* ********************************************************** */
  // COMMA (ExtendedExpr (COMMA (ExtendedExpr | &RPAREN))*)? RPAREN
  public static boolean TupleExpr_upper(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TupleExpr_upper")) return false;
    if (!nextTokenIs(b, COMMA)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _UPPER_, TUPLE_EXPR, null);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && report_error_(b, TupleExpr_upper_1(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (ExtendedExpr (COMMA (ExtendedExpr | &RPAREN))*)?
  private static boolean TupleExpr_upper_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TupleExpr_upper_1")) return false;
    TupleExpr_upper_1_0(b, l + 1);
    return true;
  }

  // ExtendedExpr (COMMA (ExtendedExpr | &RPAREN))*
  private static boolean TupleExpr_upper_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TupleExpr_upper_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ExtendedExpr(b, l + 1);
    r = r && TupleExpr_upper_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA (ExtendedExpr | &RPAREN))*
  private static boolean TupleExpr_upper_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TupleExpr_upper_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!TupleExpr_upper_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "TupleExpr_upper_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA (ExtendedExpr | &RPAREN)
  private static boolean TupleExpr_upper_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TupleExpr_upper_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && TupleExpr_upper_1_0_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ExtendedExpr | &RPAREN
  private static boolean TupleExpr_upper_1_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TupleExpr_upper_1_0_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ExtendedExpr(b, l + 1);
    if (!r) r = TupleExpr_upper_1_0_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &RPAREN
  private static boolean TupleExpr_upper_1_0_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "TupleExpr_upper_1_0_1_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, RPAREN);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // LBRACE UnionFieldList? RBRACE
  static boolean UnionBody(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnionBody")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACE);
    r = r && UnionBody_1(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // UnionFieldList?
  private static boolean UnionBody_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnionBody_1")) return false;
    UnionFieldList(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // IDENT COLON LimitedExpr
  public static boolean UnionField(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnionField")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, UNION_FIELD, "<union field>");
    r = consumeTokens(b, 1, IDENT, COLON);
    p = r; // pin = 1
    r = r && LimitedExpr(b, l + 1);
    exit_section_(b, l, m, r, p, AcornParser::UnionField_recover);
    return r || p;
  }

  /* ********************************************************** */
  // UnionField (COMMA (UnionField | &RBRACE))*
  public static boolean UnionFieldList(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnionFieldList")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = UnionField(b, l + 1);
    r = r && UnionFieldList_1(b, l + 1);
    exit_section_(b, m, UNION_FIELD_LIST, r);
    return r;
  }

  // (COMMA (UnionField | &RBRACE))*
  private static boolean UnionFieldList_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnionFieldList_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!UnionFieldList_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "UnionFieldList_1", c)) break;
    }
    return true;
  }

  // COMMA (UnionField | &RBRACE)
  private static boolean UnionFieldList_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnionFieldList_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && UnionFieldList_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // UnionField | &RBRACE
  private static boolean UnionFieldList_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnionFieldList_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = UnionField(b, l + 1);
    if (!r) r = UnionFieldList_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &RBRACE
  private static boolean UnionFieldList_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnionFieldList_1_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(COMMA | RBRACE)
  static boolean UnionField_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnionField_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !UnionField_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // COMMA | RBRACE
  private static boolean UnionField_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnionField_recover_0")) return false;
    boolean r;
    r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, RBRACE);
    return r;
  }

  /* ********************************************************** */
  // LET MUT? IDENT (COLON LimitedExpr)? (EQ ExtendedExpr)?
  public static boolean VarDeclStmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDeclStmt")) return false;
    if (!nextTokenIs(b, LET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, VAR_DECL_STMT, null);
    r = consumeToken(b, LET);
    p = r; // pin = 1
    r = r && report_error_(b, VarDeclStmt_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, IDENT)) && r;
    r = p && report_error_(b, VarDeclStmt_3(b, l + 1)) && r;
    r = p && VarDeclStmt_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // MUT?
  private static boolean VarDeclStmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDeclStmt_1")) return false;
    consumeToken(b, MUT);
    return true;
  }

  // (COLON LimitedExpr)?
  private static boolean VarDeclStmt_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDeclStmt_3")) return false;
    VarDeclStmt_3_0(b, l + 1);
    return true;
  }

  // COLON LimitedExpr
  private static boolean VarDeclStmt_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDeclStmt_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && LimitedExpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (EQ ExtendedExpr)?
  private static boolean VarDeclStmt_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDeclStmt_4")) return false;
    VarDeclStmt_4_0(b, l + 1);
    return true;
  }

  // EQ ExtendedExpr
  private static boolean VarDeclStmt_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarDeclStmt_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQ);
    r = r && ExtendedExpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Expression root: Expr
  // Operator priority table:
  // 0: PREFIX(WhileExpr)
  // 1: ATOM(IfExpr)
  // 2: ATOM(Block)
  // 3: ATOM(StructDeclExpr)
  // 4: PREFIX(AssignExpr)
  // 5: BINARY(TypeUnionExpr)
  // 6: BINARY(LogBinExpr)
  // 7: BINARY(CmpBinExpr)
  // 8: BINARY(MulBinExpr)
  // 9: BINARY(AddBinExpr)
  // 10: POSTFIX(SelectExpr)
  // 11: ATOM(HeadlessSelectExpr)
  // 12: ATOM(NegateUnaryExpr)
  // 13: ATOM(NotUnaryExpr)
  // 14: ATOM(RefUnaryExpr)
  // 15: ATOM(LiteralExpr) ATOM(ConstructExpr) ATOM(VarRefExpr) ATOM(IntrinsicRefExpr)
  //    ATOM(ParenOrTupleExpr) ATOM(ArrayExpr) POSTFIX(IndexExpr) POSTFIX(CallExpr)
  public static boolean Expr(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "Expr")) return false;
    addVariant(b, "<expr>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<expr>");
    r = WhileExpr(b, l + 1);
    if (!r) r = IfExpr(b, l + 1);
    if (!r) r = Block(b, l + 1);
    if (!r) r = StructDeclExpr(b, l + 1);
    if (!r) r = AssignExpr(b, l + 1);
    if (!r) r = HeadlessSelectExpr(b, l + 1);
    if (!r) r = NegateUnaryExpr(b, l + 1);
    if (!r) r = NotUnaryExpr(b, l + 1);
    if (!r) r = RefUnaryExpr(b, l + 1);
    if (!r) r = LiteralExpr(b, l + 1);
    if (!r) r = ConstructExpr(b, l + 1);
    if (!r) r = VarRefExpr(b, l + 1);
    if (!r) r = IntrinsicRefExpr(b, l + 1);
    if (!r) r = ParenOrTupleExpr(b, l + 1);
    if (!r) r = ArrayExpr(b, l + 1);
    p = r;
    r = r && Expr_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean Expr_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "Expr_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 5 && consumeTokenSmart(b, AMP)) {
        r = Expr(b, l, 5);
        exit_section_(b, l, m, TYPE_UNION_EXPR, r, true, null);
      }
      else if (g < 6 && LogBinOp(b, l + 1)) {
        r = Expr(b, l, 6);
        exit_section_(b, l, m, BINARY_EXPR, r, true, null);
      }
      else if (g < 7 && CmpBinOp(b, l + 1)) {
        r = Expr(b, l, 7);
        exit_section_(b, l, m, BINARY_EXPR, r, true, null);
      }
      else if (g < 8 && MulBinOp(b, l + 1)) {
        r = Expr(b, l, 8);
        exit_section_(b, l, m, BINARY_EXPR, r, true, null);
      }
      else if (g < 9 && AddBinOp(b, l + 1)) {
        r = Expr(b, l, 9);
        exit_section_(b, l, m, BINARY_EXPR, r, true, null);
      }
      else if (g < 10 && parseTokensSmart(b, 0, DOT, IDENT)) {
        r = true;
        exit_section_(b, l, m, SELECT_EXPR, r, true, null);
      }
      else if (g < 15 && IndexExpr_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, INDEX_EXPR, r, true, null);
      }
      else if (g < 15 && CallExpr_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, CALL_EXPR, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  public static boolean WhileExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WhileExpr")) return false;
    if (!nextTokenIsSmart(b, WHILE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = WhileExpr_0(b, l + 1);
    p = r;
    r = p && Expr(b, l, 1);
    exit_section_(b, l, m, WHILE_EXPR, r, p, null);
    return r || p;
  }

  // WHILE LimitedExpr
  private static boolean WhileExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "WhileExpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, WHILE);
    r = r && LimitedExpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // IF LimitedExpr Block (ELSE (IfExpr | Block))?
  public static boolean IfExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfExpr")) return false;
    if (!nextTokenIsSmart(b, IF)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, IF);
    r = r && LimitedExpr(b, l + 1);
    r = r && Block(b, l + 1);
    r = r && IfExpr_3(b, l + 1);
    exit_section_(b, m, IF_EXPR, r);
    return r;
  }

  // (ELSE (IfExpr | Block))?
  private static boolean IfExpr_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfExpr_3")) return false;
    IfExpr_3_0(b, l + 1);
    return true;
  }

  // ELSE (IfExpr | Block)
  private static boolean IfExpr_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfExpr_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, ELSE);
    r = r && IfExpr_3_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // IfExpr | Block
  private static boolean IfExpr_3_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IfExpr_3_0_1")) return false;
    boolean r;
    r = IfExpr(b, l + 1);
    if (!r) r = Block(b, l + 1);
    return r;
  }

  // LBRACE (Stmt (SEMI (Stmt | &RBRACE))*)? RBRACE
  public static boolean Block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Block")) return false;
    if (!nextTokenIsSmart(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LBRACE);
    r = r && Block_1(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, BLOCK, r);
    return r;
  }

  // (Stmt (SEMI (Stmt | &RBRACE))*)?
  private static boolean Block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Block_1")) return false;
    Block_1_0(b, l + 1);
    return true;
  }

  // Stmt (SEMI (Stmt | &RBRACE))*
  private static boolean Block_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Block_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Stmt(b, l + 1);
    r = r && Block_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (SEMI (Stmt | &RBRACE))*
  private static boolean Block_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Block_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Block_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "Block_1_0_1", c)) break;
    }
    return true;
  }

  // SEMI (Stmt | &RBRACE)
  private static boolean Block_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Block_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, SEMI);
    r = r && Block_1_0_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // Stmt | &RBRACE
  private static boolean Block_1_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Block_1_0_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Stmt(b, l + 1);
    if (!r) r = Block_1_0_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &RBRACE
  private static boolean Block_1_0_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Block_1_0_1_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeTokenSmart(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // STRUCT StructBody
  public static boolean StructDeclExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StructDeclExpr")) return false;
    if (!nextTokenIsSmart(b, STRUCT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, STRUCT_DECL_EXPR, null);
    r = consumeTokenSmart(b, STRUCT);
    p = r; // pin = 1
    r = r && StructBody(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  public static boolean AssignExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AssignExpr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = AssignExpr_0(b, l + 1);
    p = r;
    r = p && Expr(b, l, 4);
    exit_section_(b, l, m, ASSIGN_EXPR, r, p, null);
    return r || p;
  }

  // <<checkExtendedMode>> LimitedExpr EQ
  private static boolean AssignExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AssignExpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = checkExtendedMode(b, l + 1);
    r = r && LimitedExpr(b, l + 1);
    r = r && consumeToken(b, EQ);
    exit_section_(b, m, null, r);
    return r;
  }

  // DOT IDENT
  public static boolean HeadlessSelectExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "HeadlessSelectExpr")) return false;
    if (!nextTokenIsSmart(b, DOT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, HEADLESS_SELECT_EXPR, null);
    r = consumeTokensSmart(b, 1, DOT, IDENT);
    p = r; // pin = 1
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // MINUS Expr
  public static boolean NegateUnaryExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NegateUnaryExpr")) return false;
    if (!nextTokenIsSmart(b, MINUS)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, NEGATE_UNARY_EXPR, null);
    r = consumeTokenSmart(b, MINUS);
    p = r; // pin = 1
    r = r && Expr(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // BANG Expr
  public static boolean NotUnaryExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "NotUnaryExpr")) return false;
    if (!nextTokenIsSmart(b, BANG)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, NOT_UNARY_EXPR, null);
    r = consumeTokenSmart(b, BANG);
    p = r; // pin = 1
    r = r && Expr(b, l + 1, -1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // AMP MUT? Expr
  public static boolean RefUnaryExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RefUnaryExpr")) return false;
    if (!nextTokenIsSmart(b, AMP)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, REF_UNARY_EXPR, null);
    r = consumeTokenSmart(b, AMP);
    p = r; // pin = 1
    r = r && report_error_(b, RefUnaryExpr_1(b, l + 1));
    r = p && Expr(b, l + 1, -1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // MUT?
  private static boolean RefUnaryExpr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "RefUnaryExpr_1")) return false;
    consumeTokenSmart(b, MUT);
    return true;
  }

  // NUMBER | STRING | TRUE | FALSE
  public static boolean LiteralExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LiteralExpr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LITERAL_EXPR, "<literal expr>");
    r = consumeTokenSmart(b, NUMBER);
    if (!r) r = consumeTokenSmart(b, STRING);
    if (!r) r = consumeTokenSmart(b, TRUE);
    if (!r) r = consumeTokenSmart(b, FALSE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // <<checkExtendedMode>> LimitedExpr LBRACE ConstructArgList RBRACE
  public static boolean ConstructExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ConstructExpr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONSTRUCT_EXPR, "<construct expr>");
    r = checkExtendedMode(b, l + 1);
    r = r && LimitedExpr(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    p = r; // pin = 3
    r = r && report_error_(b, ConstructArgList(b, l + 1));
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // IDENT
  public static boolean VarRefExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "VarRefExpr")) return false;
    if (!nextTokenIsSmart(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, IDENT);
    exit_section_(b, m, VAR_REF_EXPR, r);
    return r;
  }

  // INTRINSIC_IDENT
  public static boolean IntrinsicRefExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IntrinsicRefExpr")) return false;
    if (!nextTokenIsSmart(b, INTRINSIC_IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, INTRINSIC_IDENT);
    exit_section_(b, m, INTRINSIC_REF_EXPR, r);
    return r;
  }

  // LPAREN ExtendedExpr (TupleExpr_upper | RPAREN)
  public static boolean ParenOrTupleExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParenOrTupleExpr")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PAREN_EXPR, null);
    r = consumeTokenSmart(b, LPAREN);
    r = r && ExtendedExpr(b, l + 1);
    p = r; // pin = 2
    r = r && ParenOrTupleExpr_2(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // TupleExpr_upper | RPAREN
  private static boolean ParenOrTupleExpr_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParenOrTupleExpr_2")) return false;
    boolean r;
    r = TupleExpr_upper(b, l + 1);
    if (!r) r = consumeTokenSmart(b, RPAREN);
    return r;
  }

  // LBRACKET (ExtendedExpr (COMMA (ExtendedExpr | &RBRACKET))*)? RBRACKET
  public static boolean ArrayExpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayExpr")) return false;
    if (!nextTokenIsSmart(b, LBRACKET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARRAY_EXPR, null);
    r = consumeTokenSmart(b, LBRACKET);
    p = r; // pin = 1
    r = r && report_error_(b, ArrayExpr_1(b, l + 1));
    r = p && consumeToken(b, RBRACKET) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (ExtendedExpr (COMMA (ExtendedExpr | &RBRACKET))*)?
  private static boolean ArrayExpr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayExpr_1")) return false;
    ArrayExpr_1_0(b, l + 1);
    return true;
  }

  // ExtendedExpr (COMMA (ExtendedExpr | &RBRACKET))*
  private static boolean ArrayExpr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayExpr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ExtendedExpr(b, l + 1);
    r = r && ArrayExpr_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COMMA (ExtendedExpr | &RBRACKET))*
  private static boolean ArrayExpr_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayExpr_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ArrayExpr_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ArrayExpr_1_0_1", c)) break;
    }
    return true;
  }

  // COMMA (ExtendedExpr | &RBRACKET)
  private static boolean ArrayExpr_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayExpr_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, COMMA);
    r = r && ArrayExpr_1_0_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ExtendedExpr | &RBRACKET
  private static boolean ArrayExpr_1_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayExpr_1_0_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ExtendedExpr(b, l + 1);
    if (!r) r = ArrayExpr_1_0_1_0_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // &RBRACKET
  private static boolean ArrayExpr_1_0_1_0_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ArrayExpr_1_0_1_0_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeTokenSmart(b, RBRACKET);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // LBRACKET ExtendedExpr RBRACKET
  private static boolean IndexExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IndexExpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LBRACKET);
    r = r && ExtendedExpr(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // LPAREN CallArgList RPAREN
  private static boolean CallExpr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "CallExpr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, LPAREN);
    r = r && CallArgList(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  static final Parser Expr_parser_ = (b, l) -> Expr(b, l + 1, -1);
}
