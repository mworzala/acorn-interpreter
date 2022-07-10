// This is a generated file. Not intended for manual editing.
package org.acornlang.language.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class AcornVisitor extends PsiElementVisitor {

  public void visitArrayExpr(@NotNull AcornArrayExpr o) {
    visitExpr(o);
  }

  public void visitBinaryExpr(@NotNull AcornBinaryExpr o) {
    visitExpr(o);
  }

  public void visitBinaryOp(@NotNull AcornBinaryOp o) {
    visitPsiElement(o);
  }

  public void visitBlock(@NotNull AcornBlock o) {
    visitExpr(o);
  }

  public void visitBreakStmt(@NotNull AcornBreakStmt o) {
    visitStmt(o);
  }

  public void visitCallExpr(@NotNull AcornCallExpr o) {
    visitExpr(o);
  }

  public void visitConstDecl(@NotNull AcornConstDecl o) {
    visitContainerItem(o);
  }

  public void visitConstructArg(@NotNull AcornConstructArg o) {
    visitPsiElement(o);
  }

  public void visitConstructExpr(@NotNull AcornConstructExpr o) {
    visitExpr(o);
  }

  public void visitContainerItem(@NotNull AcornContainerItem o) {
    visitPsiElement(o);
  }

  public void visitContinueStmt(@NotNull AcornContinueStmt o) {
    visitStmt(o);
  }

  public void visitEnumCase(@NotNull AcornEnumCase o) {
    visitPsiElement(o);
  }

  public void visitEnumCaseList(@NotNull AcornEnumCaseList o) {
    visitPsiElement(o);
  }

  public void visitExpr(@NotNull AcornExpr o) {
    visitPsiElement(o);
  }

  public void visitFnParam(@NotNull AcornFnParam o) {
    visitPsiElement(o);
  }

  public void visitFnParamList(@NotNull AcornFnParamList o) {
    visitPsiElement(o);
  }

  public void visitIfExpr(@NotNull AcornIfExpr o) {
    visitExpr(o);
  }

  public void visitIndexExpr(@NotNull AcornIndexExpr o) {
    visitExpr(o);
  }

  public void visitIntrinsicRefExpr(@NotNull AcornIntrinsicRefExpr o) {
    visitExpr(o);
  }

  public void visitLiteralExpr(@NotNull AcornLiteralExpr o) {
    visitExpr(o);
  }

  public void visitNamedEnumDecl(@NotNull AcornNamedEnumDecl o) {
    visitContainerItem(o);
  }

  public void visitNamedFnDecl(@NotNull AcornNamedFnDecl o) {
    visitContainerItem(o);
  }

  public void visitNamedSpecDecl(@NotNull AcornNamedSpecDecl o) {
    visitContainerItem(o);
  }

  public void visitNamedStructDecl(@NotNull AcornNamedStructDecl o) {
    visitContainerItem(o);
  }

  public void visitNamedUnionDecl(@NotNull AcornNamedUnionDecl o) {
    visitContainerItem(o);
  }

  public void visitParenOrTupleExpr(@NotNull AcornParenOrTupleExpr o) {
    visitExpr(o);
  }

  public void visitReturnStmt(@NotNull AcornReturnStmt o) {
    visitStmt(o);
  }

  public void visitSelectExpr(@NotNull AcornSelectExpr o) {
    visitExpr(o);
  }

  public void visitStmt(@NotNull AcornStmt o) {
    visitPsiElement(o);
  }

  public void visitStructField(@NotNull AcornStructField o) {
    visitPsiElement(o);
  }

  public void visitStructFieldList(@NotNull AcornStructFieldList o) {
    visitPsiElement(o);
  }

  public void visitTupleExprUpper(@NotNull AcornTupleExprUpper o) {
    visitPsiElement(o);
  }

  public void visitTypeUnionExpr(@NotNull AcornTypeUnionExpr o) {
    visitExpr(o);
  }

  public void visitUnionField(@NotNull AcornUnionField o) {
    visitPsiElement(o);
  }

  public void visitUnionFieldList(@NotNull AcornUnionFieldList o) {
    visitPsiElement(o);
  }

  public void visitVarDeclStmt(@NotNull AcornVarDeclStmt o) {
    visitStmt(o);
  }

  public void visitVarRefExpr(@NotNull AcornVarRefExpr o) {
    visitExpr(o);
  }

  public void visitWhileExpr(@NotNull AcornWhileExpr o) {
    visitExpr(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
