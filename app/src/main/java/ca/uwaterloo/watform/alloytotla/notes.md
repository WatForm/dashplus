    
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

A set -> q (B m -> n C)   // this is the left exception interpretation
(A set -> q B) m -> n C    this is the total left interpretation


sig A {
    f : B -> C -> D -> E
}

1) A -> (((B->C)->D)->E)  [explicit left-associative]
2) ((((A->B)->C)->D)->E)  [total left-associative]
3) A -> (B->(C->(D->E)))  [total right-associative]
4) (A->B)->((C->D)->E)

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


## Example

### Implicit facts

Let m and n be multiplicities

```
sig B {}
sig C {}
sig A {
    f : B m -> n C
}
```

becomes

```
all this: A | this.f in B m -> n C
```

[Source: Green Book, p97]

```
sig A {
    f : B -> C -> D -> E
}
```

1) A -> (((B->C)->D)->E)  [explicit left-associative]
2) ((((A->B)->C)->D)->E)  [total left-associative]
3) A -> (B->(C->(D->E)))  [total right-associative]
4) (A->B)->((C->D)->E)


To test this, consider this model:

```
sig B {}
sig C {}
sig A {
    f : B one -> one C
}

run {} for exactly 1 B, exactly 1 C, exactly 2 A
```

1) `A set -> set (B one -> one C)` expected: 2 instances
2) `(A set -> set B) one -> one C` expected: 0 instances 

## Testing associativity:

```
sig A {}
sig B {}
sig C {}

fact sanity {

    // run with one A
    one A
}

fact test {

    some (A one -> one A)
}

fact test2 {

    some (A one -> one A one -> one A)
}

fact test3 {

    // run with one A and 2 B
    some (A one -> one A set -> set B)
}
fact test4 {

    // run with one A and 2 B and one C
    some (A one -> one B set -> set C)
}

fact test5
```


Temp:

context for . and [], vs representing predicates as sets, propagation down the tree
nothing i discourse
tla+ no performance diff, or way to type dynamically
test cases for thing
logging changes

## Alternate experiments:


```
~sig B {}
sig C {}
sig D {}
sig A {
    f : B one -> one C set -> set D
}

run {} for exactly 1 B, exactly 1 C, exactly 2 A, exactly 3 D
```

```
sig B {}
sig C {}
sig D {}
sig A {
    f : (B one -> one C) set -> set D
}
run {} for exactly 1 B, exactly 1 C, exactly 2 A, exactly 3 D
```

```
sig B {}
sig C {}
sig D {}
sig A {
    f : B one -> one (C set -> set D)
}

run {} for exactly 1 B, exactly 1 C, exactly 2 A, exactly 3 D
```


Translating cph exprs:


```

{x : A+B | exp}

```

becomes

```
{x \in A+B : exp}

```


```

{x : A+B | exp}

```

becomes

```
{x \in A+B : exp}

```

The AlloyDecl here is a subset with no special arrows, how does it translate post-refactoring?

No need for additional variables

List of quantopheads handles multiple variables

separate class for arrowexpresions and augmented arrowexpressions


quantifiers - just a composition of a multiplicity with a comprehension set

## Cross products:

- everythig is treated as a list, for flatness


NOTE: ignore when => is used after a command, assume the list length is one

## Expect in commands


```
We introduced the expect clause to support a kind of regression test script: expect N would mean that when you run the command, you expect N solutions. It turned out that counting solutions was too dependent on symmetry and other settings, so we reverted to 1 and 0 as the options. So expect 1 means that the command is expected to produce an instance; expect 0 means that no instances are expected.

```

- Daniel Jackson, Alloy discourse

- Expect can be two int literals 1 or 0, nothing else
- Expect cannot be an alloy expression
- Expect refers to the number of instances supposed to be produced after a run
- There is a dualism with run and check:

```
run p expect 1
```

is the same as

```
run ~p expect 0
```

This dovetails in with the dualism of run and check.


## Working of TLC+

### Key

- `state` - a valuation for the state variables

- `constraint` - a symbolic representation of a formula in TLC

- `get_state(space)` - picks a random state from the states in `space`

- `check(state,constraint)` - returns true if an only if the state satisfies the constraint

- `properties()` - returns the set of constraints listed under the properties of the .cfg file

- `invariants()` - returns the set of constraints listed under the invariants of the .cfg file

- `equals(state1,state2)` - returns true if the hash of state1 equals the has of state2 (this accounts for the collision probability reported by TLC)

- `generate_states()` - returns the set of all states possible from the variables of the model

- `reachable(state,formula)` - returns all states reachable from the given state, where the formula holds

- `next()` - returns the canonical Next formula

- `init()` - returns the canonical Init formula

- `exit()` - stop the model-checking immediately



## Guess pseudocode:


```

all_states = generate_states()
state_queue = []
valid_states = []
visited_states = []

// step 1: getting the init states

for state in all_states:
    if check(state,init()):
        for inv in invariants():
            if not check(state,inv):
                exit()
        push(state_queue,state)

// step 2: getting states reachable by next

while(state_queue != [])
    state = pop(state_queue)
    visited_states.push(state)
    for state' in reachable_states(state,next()):
        for inv in invariants():
            if not check(state',inv):
                exit()
        if state' not in visited_states or state_queue:
            state_queue.push(state')





```


## Guess pseudocode V2

- `state` - a valuation for the state variables

- `write(state)` - writes a state to memory, impure function

- `constraint` - a formula, which is either true or false, when evaluated on a pair of states s and s'

- `check(constraint,s,s')` - returns true if the constraint holds, false otherwise

- `check(constraint,s)` - as above, but for constraints which don't have primed variables

- `membership(constraint)` - returns the part of the constraint to do with the membership of the variables

- `logic(constraint)` - returns the part of the constraint other than the membership part

- `get_states(constraint)` - returns all states which are true for the given constraint

other symbols inherit from the main section


```

all_states <- []
state_queue <- []

add_state(state):
    all_states <- all_states::state
    write(state)

Init_MC <- membership(init())
Init_LC <- logic(init())

# getting all valid init states

for state in get_states(Init_MC):
    if not check(Init_LC,state):
        continue
    for inv in invariants()
        if not check(inv,state):
            exit("invariant violation")
    add_state(state)
    push(state_queue,state)

# model-checking with next:

while(state_queue != []):

    current_state = pop(state_queue)
    possible_states <- get_states(membership(next()))
    
    some_state = false
    for state in possible_states:
        if check(logic(next()),current_state,state)
            for inv in invariants()
                if not check(inv,state):
                    exit("invariant violation")
            some_state = true
            if state not in all_states:
                add_state(state)
                push(state_queue,state)
    if DEADLOCK_EXIT and not some_state:
        exit("deadlock reached")



exit("success")


```



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

