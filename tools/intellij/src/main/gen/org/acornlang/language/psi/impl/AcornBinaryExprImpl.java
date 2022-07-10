// This is a generated file. Not intended for manual editing.
package org.acornlang.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.acornlang.language.psi.AcornTypes.*;
import org.acornlang.language.psi.*;

public class AcornBinaryExprImpl extends AcornExprImpl implements AcornBinaryExpr {

  public AcornBinaryExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull AcornVisitor visitor) {
    visitor.visitBinaryExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AcornVisitor) accept((AcornVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public AcornBinaryOp getBinaryOp() {
    return findNotNullChildByClass(AcornBinaryOp.class);
  }

  @Override
  @NotNull
  public List<AcornExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AcornExpr.class);
  }

  @Override
  @NotNull
  public AcornExpr getLhs() {
    List<AcornExpr> p1 = getExprList();
    return p1.get(0);
  }

  @Override
  @Nullable
  public AcornExpr getRhs() {
    List<AcornExpr> p1 = getExprList();
    return p1.size() < 2 ? null : p1.get(1);
  }

}
