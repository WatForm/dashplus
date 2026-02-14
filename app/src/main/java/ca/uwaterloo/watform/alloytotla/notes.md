
## Translation


```
_domain_restrict(R,S) : {e \in R : e[1] \in S}
_range_restrict(R,S) : {e \in R : e[Len(e)] \in S}

R1 ++ R2
_override(R1,R2) : R1 \ {x \in R1 : \E y \in R2 : x[1] = y[1]} + R2

_transpose(R) : {<<y,x>> : <<x,y>> \in R}

R1.R2
_inner_product(R1,R2) = { <<e1[1],e2[2]>> : e1 \in R1, e2 \in R2 and e1[2] = e2[1] }

_inner_product(R1,R2) = { SubSeq(e1,1,Len(e1)-1) \o SubSeq(e2,2,Len(e2)) : e1 \in R1, e2 \in R2 and e1[Len(e1)] = e2[1] }

```

Goal: {f(a,b) where a \in SA, b \in SB and P(a,b) }

Steps for general translation:
1) construct SA \X SB
2) filter it such that P(a,b) is true
3) map it to f(a,b) 

```
{f(a,b) : <<a,b>> \in {<<a,b>> \in A \X B : P(a,b)} }
```

Putting it all together:

```
_ip_map(e1,e2) == SubSeq(e1,1,Len(e1)-1) \o SubSeq(e2,2,Len(e2))
_ip_filter(e1,e2) == e1[Len(e1)] = e2[1]
_inner_product(R1,R2) == {_join(e1,e2) : <<e1,e2>> \in {<<f1,f2>> \in R1 \X R2 : _ip_filter(f1,f2) } }

```

for sets:

```
_ip_RS_map(e,x) == SubSeq(e1,1,Len(e1)-1)
_ip_RS_filter(e,x) == e[Len(e)] = x
_ip_RS(R,S) == {_ip_RS_map(e,x) : <<e,x>> \in {<<f,y>> \in R \X S} }

```

Empty things are supported, so unification of sets and relations is done by treating all sets are relations of arity 1

It eliminates the need for type inference at both the translation stage and run stage

Note: cross products are not associative




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