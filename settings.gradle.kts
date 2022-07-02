rootProject.name = "acorn"

include("modules")
include("modules:common")
include("modules:lexer")
include("modules:syntax")
include("modules:ast")
include("modules:hir")
include("modules:vm")
include("modules:interpreter")

include("tools")
include("tools:intellij")
