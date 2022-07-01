# Acorn Interpreter (and other infrastructure)

Acorn
- `common`: Common types and utilities across multiple unrelated dependencies
- `lexer`: Standalone lexer for Acorn
- `syntax`: Standalone syntax tree implementation with language specific syntax elements
- `parser`: Parser for going from source code into a syntax tree (this step is lossless)
  - Depends `lexer`, `syntax`
  - todo should this just be merged into syntax? `ast` will contain conversion, `hir` will contain ast lowering step. it probably makes some sense.
- `ast`: (todo) Abstract syntax tree on top of the syntax tree built by the parser
  - Depends `syntax`
- `hir`: (todo) High level intermediate representation, no longer maintains a full source code representation.
  - Depends `ast`
- `interpreter`: (todo) Tree walking interpreter based on HIR
  - Depends `hir`
- `cli`: (todo) Command line interface for the interpreter.
  - Depends `interpreter`
  - Probably depends on parse, ast, hir, etc for pretty error rendering? Or maybe common has an interface for error rendering and this implements that.

Tooling
- `intellij`: Intellij plugin for Acorn
- `lsp`: (maybe) LSP implementation for Acorn
- `vscode`: (maybe) VSCode plugin for Acorn

Bootstrapping
- `examples`: will contain example Acorn programs
- Eventually there will be some Acorn sources for standard library and the bootstrapped compiler.