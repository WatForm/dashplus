# Schedule

## Overview:

- Core Dash
- Core Alloy
- Properties
- Parameterized Dash

## Key:

- ✅ - completed
- ❌ - incomplete
- ❔ - discussion required
- ❕ - unresolved dependencies

- translation: there exists working code in the translator to produce this
- running: the produced code results in a runnable TLA+ model
- integration tests: the produced code is tested and produces correct output
- unit tests: unit tests written for the translator

## Core Dash

|feature|description|translation|running|output-test|code-test|
|--|--|--|--|--|--|
|conf|adding variable|||||
|trans_taken|adding variable|||||
|scopes_used|adding variable|||||
|stable|adding variable|||||
|events|adding variable|||||
|state literals|constructing state hierarchy using sets of strings|||||
|transition literals|values for trans_taken|||||
|pre-trans|defining pre-tans|||||
|pre-trans|pre-trans from states|||||
|pre-trans|pre-trans on|||||
|pre-trans|events clause|||||
|pre-trans|scopes clause|||||
|post-trans|defining post-trans|||||
|post-trans|handling stability|||||
|post-trans|sending events|||||
|post-trans|scopes update|||||
|enabled-trans|defining enabled-trans|||||
|transition|combining pre and post|||||
|nextIsStable|combining enabled|||||
|Init|Defining initial values|||||
|Init sync|Syncing config and module|||||
|Init conf|defining initial value of conf|||||
|Small step|combine trans, pre-trans and stutter|||||
|Stutter|Defining stutter in TLA+|||||
|Implementing stutter|implementing a stutter framework|||||
|stable|type definition|||||
|conf|type definition|||||
|scopes_used|type definition|||||
|trans_taken|type definition (none included)|||||
|TypeOK|combining types|||||
|Next|Defining Next|||||
|Next sync|Syncing config and module|||||
|User-defined vars|declaration|||||
|User-defined vars|type definition|||||
|User-defined vars|sync with config|||||

## Core Alloy

|feature|description|translation|running|output-test|code-test|
|--|--|--|--|--|--|
|sigs|basic|||||
|sigs|subtypes|||||
|sigs|subsets|||||
|sigs|multi-sig declarations|||||
|multiplicities|set|||||
|multiplicities|none|||||
|multiplicities|lone|||||
|multiplicities|one|||||
|multiplicities|some|||||
|fields|basic (A->B)|||||
|fields|with multiplicities (A->lone B)|||||
|fields|complex (A->B->C)|||||

- TODO transfer operators





## Parameterized Dash

|feature|description|translation|running|output-test|code-test|
|--|--|--|--|--|--|
|Scopes modification|-|||||
|Params|-|||||
|Buffers|-|||||

## Properties

|feature|description|translation|running|output-test|code-test|
|--|--|--|--|--|--|
|Safety|-|||||
|Fairness constraints|-|||||
|Liveness|-|||||

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




