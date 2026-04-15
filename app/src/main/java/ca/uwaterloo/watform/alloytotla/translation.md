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
C(V) -> UNCHANGED V
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

## Base Scheme:

```
CONSTANTS V_set
VARIABLES V

TC(X) == X \in SUBSET V_set

Inv = SC

Init == TC(V) /\ MC

Next == UNCHANGED V
```

```
INVARIANTS Inv

INIT Init
NEXT Next
```


## Exact scope scheme

```
CONSTANTS V_set
VARIABLES V

TC(X) == X = V_set

Inv = SC

Init == TC(V) /\ MC

Next == UNCHANGED V
```

```
INVARIANTS Inv

INIT Init
NEXT Next
```


## Global property scheme

```
CONSTANTS V_set
VARIABLES V

TC(X) == X = V_set

Inv = SC

Init == TC(V) /\ MC

Next == UNCHANGED V
```

```
PROPERTIES [] Inv

INIT Init
NEXT Next
```


- This is functionally no different from the base scheme 
- Properties are evaluated over traces
- Since Next is unchanged, the size of the trace is always one
- If the property is evaluated after each trace is identified, then this scheme is no different from the base scheme, in terms of computation
- In terms of logic, this specification of this scheme is the same as the base scheme

## Force SC in Init scheme

```
CONSTANTS V_set
VARIABLES V

TC(X) == X = V_set

Inv = SC

Init == TC(V) /\ MC /\ SC

Next == UNCHANGED V
```

```
INIT Init
NEXT Next
```

## Valid transition scheme

```
CONSTANTS V_set
VARIABLES V

TC(X) == X = V_set

Inv = SC

Init == TC(V)

Next == TC(V') /\ MC(V') /\ SC(V')
```

```
INIT Init
NEXT Next
```

## Split special constraint scheme

```
CONSTANTS V_set
VARIABLES V

TC(X) == X = V_set

Inv = SC

Init == TC(V) /\ MC(V)

Next == TC(V') /\ SC(V')
```

```
INIT Init
NEXT Next
```

## CHOOSE scheme

```
CONSTANTS V_set
VARIABLES V

TC(X) == X = V_set

Inv = SC

Init == V = CHOOSE X \in V_set : TC(V) /\ MC(V) /\ SC(V)

Next == UNCHANGED V
```

```
INIT Init
NEXT Next
```


## Benchmark models

- Models are expected to be SAT or UNSAT
- Models can be check or run
- Interesting instances can be non-existent, rare, or abundant
- The state space of the whole model (without MC or SC) can be vast

Goals:

- If an interesting instance is found, the scheme ought to terminate instantly
- If an interesting instance is rare, the scheme ought to allow smart search for that instance


Model 1: (run for a trivially true property, interesting instances are abundant)

```
sig A 
{
	f : A
}

run {f=f} for 10 A 
```

Model 2: (run for a trivially false property, interesting instances are non-existent)

```
sig A 
{
	f : A
}

run {f!=f} for 10 A 
```

Model 3: (check for a trivially true property, interesting instances are non-existent)

```
sig A 
{
	f : A
}

check {f=f} for 10 A 
```

Model 4: (check for a trivially false property, interesting instances are abundant)

```
sig A 
{
	f : A
}

check {f!=f} for 10 A 
```