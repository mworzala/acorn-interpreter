package org.acornlang.ast

import org.acornlang.ast.util.collectNonNull
import org.acornlang.syntax.SyntaxNode

class AstModule(syntax: SyntaxNode) : AstNode(syntax) {

    val decls: List<AstDecl>
        get() = children().nodes().map(AstDecl::castOrNull).collectNonNull()

}