// This is a generated file. Not intended for manual editing.
package org.acornlang.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface AcornBinaryExpr extends AcornExpr {

  @NotNull
  AcornBinaryOp getBinaryOp();

  @NotNull
  List<AcornExpr> getExprList();

  @NotNull
  AcornExpr getLhs();

  @Nullable
  AcornExpr getRhs();

}
