rootProject.name = "acorn"

include("modules")
include("modules:common")
include("modules:lexer")
include("modules:syntax")
include("modules:ast")
include("modules:interpreter")

include("tools")
include("tools:intellij")
