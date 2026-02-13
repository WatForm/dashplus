
## Translation


```
_domain_restrict(R,S) : {e \in R : e[1] \in S}
_range_restrict(R,S) : {e \in R : e[Len(e)] \in S}

R1 ++ R2
_override(R1,R2) : R1 \ {x \in R1 : \E y \in R2 : x[1] = y[1]} + R2

_transpose(R) : {<<y,x>> : <<x,y>> \in R}

R1.R2
_inner_product(R1,R2) = {}

```

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

comprehension expressions are omre complex than that would suggest

```
\E x \in Sx, y \in Sy : x = y
```

this is also valid