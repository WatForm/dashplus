## things:

- note start with _
- inconsistency in name of model
- make it consistently add everywhere
- maybe use two different types of add? useful for showing difference
- TLAplusFQN
- name the things after the thing
- arg-name-of.

in general - avoid breaking up functions



- what if Alloy does things in a roundabout way? explicit non-support




# Schedule (old)

|feature|date|notes|status|
|----|----|----|----|
|state literals|2025-11-24|completed|✅|
|transition literals|2025-11-27|copied from trans-taken|✅|
|transition pre-condition|2025-11-27|completed|✅❕|
|transition post-condition|2025-11-28|partly depends on events|✅❕|
|transition isEnabled|2025-11-29|partly depends on events|✅❕|
|event literals|2025-11-30|depends on events|❌❕|
|transition ifNextStable|2025-11-30|partly depends on events|✅❕|
|transition semantics|2025-12-01|commented out, needs reworking, depends a lot on events|❌|
|Alloy signatures|2025-12-01|implemented using config and constants|❌|
|Alloy fields (set)|2025-12-03|composed using sigs|❌|
|Multiplicities|2025-12-07|Portus translation roadmap|❌|
|Alloy operators|2025-12-11|Most of these are one-to-one translations, but complexities may arise|❌❔|
|TBD|2025-12-15|slack time in case of spillover|❔|
|misc. refactoring + tests|2025-12-16|Can be done working alone|❔|

## Key:
- ✅ - completed
- ❌ - incomplete
- ❔ - unclear definition
- ❕ - unresolved dependencies





## Todo

### General variables

- conf
- events
- stable
- trans_taken
- ct

### States

### Transitions


#### Pre-condition

- conf
- scopes_used
- events

#### Post-condition

- conf
- scopes_used
- events

#### Enabled

- scopes_used

### Init

### Next

### TypeOK

### Stutter

### small-step

### Parameters


### Signatures

- basic sigs
- sig subtypes
- sig subsets (multiple parents, not necessarily pairwise disjoint)
- multi-sig declarations

### Multiplicities

- some
- lone
- one
- none
- set

### Fields

- basic (A -> B)
- with multiplicities (some A -> lone B)
- nested (A -> B -> C)

### Operators

- unary
- dot join
- box join
- restriction
- arrow product
- intersection
- override
- cardinality
- union
- difference
- comparison (in, not, !, =, <, etc.>)
- logical (not, and , or, implication, bi-implication)
- arithmetic

### Expressions:

- let (refer multiplicities)
- conditional expressions


### Predicates

### Functions



### Buffers

### Variables

