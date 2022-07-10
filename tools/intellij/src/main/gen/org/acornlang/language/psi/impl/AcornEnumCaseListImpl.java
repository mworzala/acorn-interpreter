// This is a generated file. Not intended for manual editing.
package org.acornlang.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.acornlang.language.psi.AcornTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import org.acornlang.language.psi.*;

public class AcornEnumCaseListImpl extends ASTWrapperPsiElement implements AcornEnumCaseList {

  public AcornEnumCaseListImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull AcornVisitor visitor) {
    visitor.visitEnumCaseList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof AcornVisitor) accept((AcornVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<AcornEnumCase> getEnumCaseList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, AcornEnumCase.class);
  }

}
