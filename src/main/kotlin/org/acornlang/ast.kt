package org.acornlang.ast

import org.acornlang.lexer.Token

abstract class AstNode {
    abstract fun <R, P> visit(visitor: AstVisitor<R, P>, param: P): R

    abstract fun stringify(indent: String = ""): String
}

class AstInt(val value: Long) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitInt(this, param)

    override fun stringify(indent: String): String = "${indent}INT \"$value\"\n"
}

class AstRef(val name: String) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitRef(this, param)

    override fun stringify(indent: String): String = "${indent}REF \"$name\"\n"
}

class AstString(val value: String) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitString(this, param)

    override fun stringify(indent: String): String = "${indent}STR \"$value\"\n"
}

class AstBool(val value: Boolean) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitBool(this, param)

    override fun stringify(indent: String): String = "${indent}BOOL \"$value\"\n"
}

class AstBinary(
    val op: Token,
    val lhs: AstNode,
    val rhs: AstNode,
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitBinary(this, param)

    override fun stringify(indent: String): String {
        return "${indent}BINARY ${op.type}\n" +
                lhs.stringify("$indent  ") +
                rhs.stringify("$indent  ")
    }
}

class AstUnary(
    val op: Token,
    val operand: AstNode,
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitUnary(this, param)

    override fun stringify(indent: String): String {
        return "${indent}UNARY ${op.type}\n" +
                operand.stringify("$indent  ")
    }
}

class AstBlock(
    val stmts: List<AstNode>,
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitBlock(this, param)

    override fun stringify(indent: String): String {
        return "${indent}BLOCK\n" +
                stmts.joinToString("") { it.stringify("$indent  ") }
    }
}

class AstReturn(
    val expr: AstNode?,
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitReturn(this, param)

    override fun stringify(indent: String): String {
        return "${indent}RETURN\n" +
                (expr?.stringify("$indent  ") ?: "")
    }
}

// Index operator (e.g. `a[b]`)
class AstIndex(
    val target: AstNode,
    val index: AstNode,
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitIndex(this, param)

    override fun stringify(indent: String): String {
        return "${indent}INDEX\n" +
                target.stringify("$indent  ") +
                index.stringify("$indent  ")
    }
}

class AstCall(
    val target: AstNode,
    val args: List<AstNode>,
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitCall(this, param)

    override fun stringify(indent: String): String {
        return "${indent}CALL\n" +
                target.stringify("$indent  ") +
                args.joinToString("") { it.stringify("$indent  ") }
    }
}

class AstIf(
    val condition: AstNode,
    val thenBlock: AstNode,
    val elseBlock: AstNode?,
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitIf(this, param)

    override fun stringify(indent: String): String {
        return "${indent}IF\n" +
                condition.stringify("$indent  ") +
                thenBlock.stringify("$indent  ") +
                (elseBlock?.stringify("$indent  ") ?: "")
    }
}

class AstWhile(
    val condition: AstNode,
    val block: AstNode,
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitWhile(this, param)

    override fun stringify(indent: String): String {
        return "${indent}WHILE\n" +
                condition.stringify("$indent  ") +
                block.stringify("$indent  ")
    }
}

class AstType(
    val name: String,
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitType(this, param)

    override fun stringify(indent: String): String {
        return "${indent}TYPE \"${name}\"\n"
    }
}

class AstPtrType(
    val inner: AstNode,
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitPtrType(this, param)

    override fun stringify(indent: String): String {
        return "${indent}PTR_TYPE\n" +
                inner.stringify("$indent  ")
    }
}


// SECTION: Statements
// AST Nodes related to statements

class AstLet(
    val name: String,
    val type: AstNode, //todo type is required for now
    val init: AstNode, //todo init expr is required for now
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitLet(this, param)

    override fun stringify(indent: String): String {
        return "${indent}LET \"${name}\"\n" +
                type.stringify("$indent  ") +
                init.stringify("$indent  ")
    }
}


// SECTION: Declarations
// AST nodes for declarations, eg const, named func, struct, enum

class AstConstDecl(
    val name: String,
    val type: AstNode, //todo type is required for now
    val init: AstNode, //todo init expr is required for now
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitConstDecl(this, param)

    override fun stringify(indent: String): String {
        return "${indent}CONST \"${name}\"\n" +
                type.stringify("$indent  ") +
                init.stringify("$indent  ")
    }
}

class AstNamedFnDecl(
    val name: String,
    val foreign: Boolean,
    val retType: AstNode?, // Null == void
    val params: List<AstNode>,
    val body: AstNode?, // Present if not foreign
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitNamedFnDecl(this, param)

    override fun stringify(indent: String): String {
        return "${indent}NAMED_FN \"${name}\"${if (foreign) " .foreign" else ""}\n" +
                (retType?.stringify("$indent  ") ?: "${"$indent  "}TYPE \"void\"\n") +
                params.joinToString("") { it.stringify("$indent  ") } +
                (body?.stringify("$indent  ") ?: "")
    }
}

class AstFnParam(
    val name: String,
    val type: AstNode,
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitFnParam(this, param)

    override fun stringify(indent: String): String {
        return "${indent}PARAM \"${name}\"\n" +
                type.stringify("$indent  ")
    }
}

class AstStructDecl(
    val name: String,
    val fields: List<AstNode>,
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitStructDecl(this, param)

    override fun stringify(indent: String): String {
        return "${indent}STRUCT \"${name}\"\n" +
                fields.joinToString("") { it.stringify("$indent  ") }
    }
}

class AstStructField(
    val name: String,
    val type: AstNode,
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitStructField(this, param)

    override fun stringify(indent: String): String {
        return "${indent}FIELD \"${name}\"\n" +
                type.stringify("$indent  ")
    }
}

class AstEnumDecl(
    val name: String,
    val cases: List<AstNode>,
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitEnumDecl(this, param)

    override fun stringify(indent: String): String {
        return "${indent}ENUM \"${name}\"\n" +
                cases.joinToString("") { it.stringify("$indent  ") }
    }
}

class AstEnumCase(
    val name: String,
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitEnumCase(this, param)

    override fun stringify(indent: String): String {
        return "${indent}CASE \"${name}\"\n"
    }
}


class AstModule(
    val decls: List<AstNode>,
) : AstNode() {

    override fun <R, P> visit(visitor: AstVisitor<R, P>, param: P) = visitor.visitModule(this, param)

    override fun stringify(indent: String): String {
        return "${indent}MODULE\n" +
                decls.joinToString("") { it.stringify("$indent  ") }
    }
}



abstract class AstVisitor<R, P>(
    val default: R
) {

    open fun visit(node: AstNode, ctx: P): R = node.visit(this, ctx)

    // Expressions

    open fun visitInt(node: AstInt, ctx: P): R = default
    open fun visitRef(node: AstRef, ctx: P): R = default
    open fun visitString(node: AstString, ctx: P): R = default
    open fun visitBool(node: AstBool, ctx: P): R = default

    open fun visitBinary(node: AstBinary, ctx: P): R {
        visit(node.lhs, ctx)
        visit(node.rhs, ctx)
        return default
    }
    open fun visitUnary(node: AstUnary, ctx: P): R {
        visit(node.operand, ctx)
        return default
    }

    open fun visitBlock(node: AstBlock, ctx: P): R {
        for (arg in node.stmts)
            visit(arg, ctx)
        return default
    }
    open fun visitReturn(node: AstReturn, ctx: P): R {
        if (node.expr != null)
            visit(node.expr, ctx)
        return default
    }

    open fun visitIf(node: AstIf, ctx: P): R {
        visit(node.condition, ctx)
        visit(node.thenBlock, ctx)
        if (node.elseBlock != null)
            visit(node.elseBlock, ctx)
        return default
    }
    open fun visitWhile(node: AstWhile, ctx: P): R {
        visit(node.condition, ctx)
        visit(node.block, ctx)
        return default
    }

    open fun visitIndex(node: AstIndex, ctx: P): R {
        visit(node.target, ctx)
        visit(node.index, ctx)
        return default
    }

    open fun visitCall(node: AstCall, ctx: P): R {
        visit(node.target, ctx)
        for (arg in node.args)
            visit(arg, ctx)
        return default
    }

    open fun visitType(node: AstType, ctx: P): R = default
    open fun visitPtrType(node: AstPtrType, ctx: P): R {
        visit(node.inner, ctx)
        return default
    }

    // Statements

    open fun visitLet(node: AstLet, ctx: P): R {
        visit(node.type, ctx)
        visit(node.init, ctx)
        return default
    }

    // Declarations

    open fun visitConstDecl(node: AstConstDecl, ctx: P): R {
        visit(node.type, ctx)
        visit(node.init, ctx)
        return default
    }

    open fun visitNamedFnDecl(node: AstNamedFnDecl, ctx: P): R {
        if (node.retType != null)
            visit(node.retType, ctx)
        for (fnctx in node.params)
            visit(fnctx, ctx)
        if (node.body != null)
            visit(node.body, ctx)
        return default
    }
    open fun visitFnParam(node: AstFnParam, ctx: P): R {
        visit(node.type, ctx)
        return default
    }

    open fun visitStructDecl(node: AstStructDecl, ctx: P): R {
        for (field in node.fields)
            visit(field, ctx)
        return default
    }
    open fun visitStructField(node: AstStructField, ctx: P): R {
        visit(node.type, ctx)
        return default
    }

    open fun visitEnumDecl(node: AstEnumDecl, ctx: P): R {
        for (case in node.cases)
            visit(case, ctx)
        return default
    }
    open fun visitEnumCase(node: AstEnumCase, ctx: P): R = default

    // Module

    open fun visitModule(node: AstModule, ctx: P): R {
        for (decl in node.decls)
            visit(decl, ctx)
        return default
    }


}

