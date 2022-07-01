I am a big fan of the way that Zig handles build scripts, by having the user create them within the language itself.

Project structure may look something like
```
/project_name
  src/
    main.acorn
    ..
  build.acorn
  ..
```

Within the build file, it should contain declared tasks which can do whatever (they are just acorn functions), as well
as some builtin tasks for e.g. compiling/linking to a binary. Tasks can be executed with `acorn run task_name [args]`,
or to run the `build` task, simply `acorn build` (equivalent to `acorn run build`).

Multi module projects might be relevant at some point, in which case there would be a `build.acorn` in each module.
