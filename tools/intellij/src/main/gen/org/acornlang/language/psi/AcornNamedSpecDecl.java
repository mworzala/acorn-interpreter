// This is a generated file. Not intended for manual editing.
package org.acornlang.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface AcornNamedSpecDecl extends AcornContainerItem {

  @NotNull
  List<AcornNamedFnDecl> getNamedFnDeclList();

  @Nullable
  PsiElement getIdent();

}
