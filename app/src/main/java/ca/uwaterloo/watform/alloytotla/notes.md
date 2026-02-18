
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


## Translating Arrow expressions in Alloy, unified with field expressions:

- An arrow expression `a -> b` refers to the set which is a cross product of a and b

- Internally, `a -> b` translates to `a set -> set b`

- This means no restrictions on `a`.

- Consider `sig B { f : A }`
- This translates to: f is a subset of `A -> B` 

- `sig B {f : mult A}` means f is a subset of `B set -> mult A`

- For the general `a m -> n b`, forall xa in a, `n({xb : (xa,xb) \in axb})` and forall xb in b, `m({xa : (xa,xb) \in axb})`

- `m(S) = TRUE` if m = set

| Alloy | Core set | Constraints |
|---|---|---|
| a -> b | a set -> set b | \A xa \in a : set (xa.(a->b)) && \A xb \in b : set ((a->b).xb) |
| a m -> n b | a m -> n b | \A xa \in a :  n(xa.(a->b)) && \A xb \in b : m((a->b).xb)  |
| sig a { f : b } | A set -> set b | f \in P(a->b) && \A xa \in a :  set(xa.f) && \A xb \in b : set(f.xb) |
| sig a { f : n b } | A set -> n b | f \in P(a->b) && \A xa \in a :  n(xa.f) && \A xb \in b : set(f.xb) |
| sig z { f : a m -> n b } | (z set -> set a) m -> n b | f \in P((z->a)->b) && |



# Associativity:

```
sig A {
f : q B m -> n C
}

A set -> q (B m -> n C)   # this is the left exception interpretation
(A set -> q B) m -> n C    # this is the total left interpretation

m = one
n = one
q = set
exactly 2 A, exactly 1 B, exactly 1 C

In the first interpretation: 
A set -> set (B one -> one C)

There will be 1 instance:
{(a0,(b0,c0)),(a1,(b0,c0))}


In the second interpretation:
(A set -> set B) one -> one C
There will be 0 instances because there cannot be a one-one relation when one set has 4 elements
```

Thus, the first interpretation is correct, since Alloy produced 1 instance.

Sanity check:

When running for `2 A, exactly 1 B, exactly 1 C` we expect 4 instances, which is what the Analyzer shows (absent symmetry breaking)

When translating with PID:
```
sig A
{
    f : B -> C -> D
}
```
cannot become 
```
sig A
{
    f : PID -> B -> C -> D
}
```
it should be
```
sig PID
{
    f : A -> (B -> C -> D)
}
```

General translation:

```
A m -> n B
```

```
f \in SUBSET (A \X B)
/\ \A xA \in A : n(xA.f)
/\ \A xB \in B : m(f.xB)
```


Run and check rules:

- All commands are translated
- Subtypes may be scoped
- run predicate or run block
- run for (n but) (exactly? m S)*
- this needs a resolution step - for each sig (may or may not be top-level), (int value, bool exact)
- this is translated into both the TLA+ and the cfg
- One command per translation
- if flag left unspecified, command is
`run {} for 4` 

## Macros:

- let is a global macro, not bound using in (i.e. not necessarily)
- thus, there are three approaches
- one - pre-translate the macro by editing the AST
- two - expand them in the translation using a DSL-generator
- three - make equivalent macros in translation