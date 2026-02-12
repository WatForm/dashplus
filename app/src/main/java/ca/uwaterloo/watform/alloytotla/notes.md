
## Translation

## Alloy

## TLA

```
factorial[n \in Int] ==
    IF n = 0 THEN 1 ELSE n * factorial[n-1]
```

This type of recursion works, without needing a RECURSIVE keyword

```
\A x,y \in S : exp
```

```
\A <<x,y>> \in S : exp
```

Both of these constructs are valid, so QuantOps may need to be a list of variables with a boolean to determine if it's a tuple

```
\A <<x+y,z>> \in S : exp
```

Doesn't work, so it's not a simple as just making it a TLA+ expression, plus the syntax of comma-separated variables is unique here