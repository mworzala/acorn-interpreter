

Some mock errors

Syntax error:
```
some/file.acorn:5:12: error: Expected expression, found ';'
    let a = ;
            ^
```

Type error:
```
some/file.acorn:5:12: error: Expected integer, found bool
    let a = 1 < true;
                ^^^^
```

mutate immutable type
(not so sure about this one)
```
some/file.acorn:5:12: error: Cannot mutate immutable value 'a'
    let a = 24;
        ^ 'a' declared here
    a = 25;
    ^^^^^^
   
some/file.acorn:4:12: 'a' declared here
    let a = 24;
```


