# Acorn Interpreter (and other infrastructure)

Acorn
- `common`: Common types and utilities across multiple unrelated dependencies
- `lexer`: Standalone lexer for Acorn
- `syntax`: Standalone syntax tree implementation with language specific syntax elements
- - Depends `lexer`
- `ast`: Abstract syntax tree on top of the syntax tree built by the parser
  - Depends `syntax`
- `hir`: High level intermediate representation, no longer maintains a full source code representation.
  - Depends `ast`
- `vm`: Virtual machine which executes HIR directly.
  - Depends `hir`
- `interpreter`: (todo) Tree walking interpreter based on HIR
  - Depends `hir`
- `cli`: (todo) General purpose command line interface.
  - Depends `vm` for launching vm
  - Probably depends on parse, ast, hir, etc for pretty error rendering? Or maybe common has an interface for error rendering and this implements that.

Tooling
- `intellij`: Intellij plugin for Acorn
- `lsp`: (maybe) LSP implementation for Acorn
- `vscode`: (maybe) VSCode plugin for Acorn

Bootstrapping
- `examples`: will contain example Acorn programs
- Eventually there will be some Acorn sources for standard library and the bootstrapped compiler.




Scratch spot:
- Currently have a syntax problem. A void return type is an empty tuple, but typing `fn() ()` is extremely weird. Need to either introduce a `:` after param list, or figure somethign else out.