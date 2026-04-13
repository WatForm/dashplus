# Schemes to translate Alloy to TLA+


## Problem setup:


```
// model constraints MC

check { 
	//special constraint SC
}
for scope S

```


## Common features for all schemes:

- Every field and signature is a variable
- Signature variable S is a subset of signature set S_set, which is a constant
- V represents every variable

- goal: TLA+ needs to produce outputs which correspond to SAT/UNSAT

## Constraints:

- Every model needs an Init and a Next
- Next is written in terms of V and V'
- A member ship constraint MC(V) is a constraint of the form (as a CFG):

```
MC(V) -> C(V) \/ MC(V)
C(V) -> V \in <set_expression>
C(V) -> V = <expression>
```

- Init starts with MC(V) 
- Next starts with MC(V')
- Init and Next must both be boolean expressions

- TLA+ models have a `PROPERTIES` and `INVARIANTS` field. The `PROPERTIES` refer to temporal properties, evaluated over a trace. The `INVARIANTS` refer to formulae that must be true in every state.

```
INVARIANT P
```

is equivalent to 

```
PROPERTY []P  // [] is the "globally" operator
```

Neither of these constrain the state-space. If either of these are violated, TLC reports the violation and stops model-checking.



