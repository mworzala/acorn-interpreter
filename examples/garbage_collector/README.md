# Baby’s First Garbage Collector
An Acorn implementation of [Baby’s First Garbage Collector](https://journal.stuffwithstuff.com/2013/12/08/babys-first-garbage-collector/).



### Notes
Some notes after writing
* &self/&mut self
* match/if let/while let


```
if let Some(x) = something {

}

if let Blah { x, y } = something {

}

while let Some(x) = something {

}

match something {
    Some(x) => {

    },
    None => {
    
    },
}
```