
These notes have been implemented in AMScopes.java
We can remove this file at some point.

## Summary of scope resolution:

### Problem:

- Given a model with a set of sigs S
- And a command C, which is a partial function from S to the power set of N
- Produce another command C', such that C' is total on the subset of top-level sigs of S and C' and C produce the same instances on the Analyzer

### Features to deal with:

- one sigs
- abstracts sigs
- extends relations
- in relations


### Degenerate case:

- All sigs are top-level sigs

### Experiments:

```
sig A
run {} for 2 A
// produces 3 instances: [], [A0], [A0,A1]
```

```
sig A
run {} for exactly 2 A
// produces one instance: [A0,A1]
```

```
sig A
run {}
// produces four instances: [], [A0], [A0,A1], [A0,A1,A2]
```

Solution:

```
for every sig s in S:
    if s is in C:
        let C'(s) = C(s)
    if s is not in C:
        let C'(s) = 0..3 // or, 3 s
```

- 3 is the default value assigned as the limit for the scope

Example:

```
sig A {}
sig B {}

run for exactly 1 A

```

```
C: A -> {1}

C': A -> {1} and B -> {0,1,2,3,4}

```

### One sigs:

Experiments:

```
one sig A {}
run {}
// instance found
```

```
one sig A {}
run {} for exactly 1 A
// instance found
```

```
one sig A {}
run {} for 1 A
// instance found
```

```
one sig A {}
run {} for exactly 3 A
// error
```

```
one sig A {}
run {} for 3 A
// error
```


Solution:


```
for every sig s in S:
    if s is in C:
        let C'(s) = C(s)
    if s is not in C:
        let C'(s) = 0..4
    if s is a one sig:
        if s is in C:
            if C(s) is not "one s" or "exactly one s":
                error
        let C'(s) = 1

```

### In-children

- Consider allowing a sig S to be in sig T1, T2, T3... Tn
- A sig T may have many in-children S1, S2... Sn
- The relation "is an in child" with sigs forms a lattice

### Experiments

```
sig A {}
sig B in A {}
run {}
// similar to run {} for 3 A
```

```
sig A {}
sig B in A {}
run {} for 2 A
// exactly as expected
```

```
sig A {}
sig B in A {}
run {} for 2 B
// error - Cannot specify a scope for a subset signature "this/B"
```

```
sig A {}
sig B in A {}
run {} for 2 A, 2 B
// error - Cannot specify a scope for a subset signature "this/B"
```

Solution:

```
for every sig s in S:
    if s is in C:
        if s is a subset sig:
            error
        let C'(s) = C(s)
    if s is not in C:
        let C'(s) = 0..4
    if s is a one sig:
        if s is in C:
            if C(s) is not "one s" or "exactly one s":
                error
        let C'(s) = 1

```


### Extends-children


### Experiments


```
sig A {}
sig B extends A {}
run for exactly 2 B
// You must specify a scope for sig "this/A"
```

```
sig A {}
sig A1, A2 extends A {}

run {} for exactly 5 A1, exactly 5 A2
// Same error
```

- Point: If the scope for the top-level sig with extends children is left unspecified, then the Analyzer produces an error


```
sig A {}
sig A1, A2 extends A {}

run {} for exactly 5 A1, exactly 5 A2, exactly 2 A
// single instance, with override of A
```

```
sig A {}
sig A1, A2 extends A {}

run {} for exactly 5 A1, exactly 5 A2, 2 A
// single instance, with override of A
```

```
sig A {}
sig B extends A {}

run {} for 2 B, exactly 2 A
// one instance
```

```
sig A {}
sig B extends A {}

run {} for 3 B, exactly 2 A
// instances with 2 A, instance with exactly 3 B not shown

```

This suggests that overrides happen only when a minimum is forced

```
sig A {}
sig B extends A {}

run {} for exactly 3 B, exactly 2 A
// one instance with exactly 3 B atoms
```

General principle:

- A scope needs to be given for top-level sigs
- But if this scope clashes with that of the child, then an override occurs

```
sig A {}
sig B extends A {}
sig C extends B {}

run {} for exactly 3 C, exactly 2 A
// one instance with 3 C
```

This suggests that overrides are transmitted up the hierarchy

Testing how the transmission stops:

```
sig A {}
sig B extends A {}
sig C extends B {}

run {} for 3 C, exactly 2 B, exactly 1 A
// instances have 2 atoms
```


```


Solution:

// foo returns the override from the children

def foo(sig s):
    temp = 0
    if s in C:
        temp = min(C(s))
    if s has no children:
        return 0
    if t1-tn are in-children of s:
        return max(map(t1-tn,foo)::temp)
    if t1-tn are extends-children of s:
        return summation(map(t1-tn,foo)::temp)


for every sig s in S:

    // error case for subset sigs
    if s is in C:
        if s is a subset sig:
            error
    
    // error case for extends sigs
    if s is in C:
        if s is an extends sig and ancestor(s) not in C:
            error 

    // error case for one sigs
    if s is a one sig and s is in C:
        if C(s) is not "one s" or "exactly one s":
            error

for every sig s in S:
    if s is a top-level sig:
        C'(s) = 0..max(3,foo(s))

```

### Abstract sigs

further experimentation required

