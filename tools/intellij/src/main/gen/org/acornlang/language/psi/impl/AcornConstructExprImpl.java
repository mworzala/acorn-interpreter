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

public class AcornConstructExprImpl extends AcornExprImpl implements AcornConstructExpr {

  public AcornConstructExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull AcornVisitor visitor) {
    visitor.visitConstructExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AcornVisitor) accept((AcornVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<AcornConstructArg> getConstructArgList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AcornConstructArg.class);
  }

  @Override
  @NotNull
  public AcornExpr getExpr() {
    return findNotNullChildByClass(AcornExpr.class);
  }

}
