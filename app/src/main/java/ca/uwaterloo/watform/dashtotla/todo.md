# Schedule

## Overview:

- Dash (states, transitions, events, stability, concurrency, variables)
- Alloy
- Dash+ (parameters, buffers)
- Properties (safety, liveness)

## Temp list:

- Use pretty printer
- Make list for parameterized dash
- Visitor for Alloy


## Key:

- ✅ - completed
- ❌ - incomplete
- ❔ - discussion required
- ❕ - unresolved dependencies

- translation: there exists working code in the translator to produce this
- running: the produced code results in a runnable TLA+ model
- tests: the produced code is tested and produces correct output

## Core Dash

|feature|description|translation|running|tests|
|--|--|--|--|--|
|conf|adding variable|✅|✅|❕|
|trans_taken|adding variable|✅|✅|❕|
|scopes_used|adding variable|✅|✅|❕|
|stable|adding variable|✅|✅|❕|
|events|adding variable|✅|✅|❕|
|state literals|constructing state hierarchy using sets of strings|✅|✅|❕|
|transition literals|values for trans_taken|✅|✅|❕|
|pre-trans|defining pre-tans|✅|✅|❕|
|pre-trans|pre-trans from states|✅|✅|❕|
|pre-trans|scopes clause|✅|✅|❕|
|pre-trans|events clause|✅|✅|❕|
|post-trans|defining post-trans|✅|✅|❕|
|post-trans|handling stability|✅|✅|❕|
|post-trans|scopes update|✅|✅|❕|
|post-trans|sending events|✅|✅|❕|
|enabled-trans|defining enabled-trans|✅|✅|❕|
|enabled-trans|conf|✅|✅|❕|
|enabled-trans|scope|✅|✅|❕|
|enabled-trans|events|✅|✅|❕|
|transition|combining pre and post|✅|✅|❕|
|nextIsStable|combining enabled|✅|✅|❕|
|Init|Defining initial values (except conf)|✅|✅|❕|
|Init sync|Syncing config and module|✅|✅|❕|
|Init conf|defining initial value of conf|✅|✅|❕|
|Small step|combine trans, pre-trans and stutter|✅|✅|❕|
|Stutter|Defining stutter in TLA+|✅|✅|❕|
|stable|type definition|✅|✅|❕|
|conf|type definition|✅|✅|❕|
|scopes_used|type definition|✅|✅|❕|
|trans_taken|type definition (none included)|✅|✅|❕|
|valid|formulae|✅|✅|❕|
|valid|primed and unprimed|✅|✅|❕|
|Next|Defining Next|✅|✅|❕|
|Next sync|Syncing config and module|✅|✅|❕|
|Implementing stutter|implementing a stutter framework|❔|❔|❔|
|User-defined vars|declaration|❔|❔|❕|
|User-defined vars|type definition|❔|❔|❕|
|User-defined vars|sync with config|❔|❔|❔|

## Core Alloy

### Sigs

|feature|description|translation|running|tests|
|--|--|--|--|--|
|sigs|consts|✅|✅|❔|
|sigs|vars|✅|✅|❔|
|sigs|types|✅|✅|❔|
|sigs|subtypes|✅|✅|❔|
|sigs|subsets|✅|✅|❔|
|sigs|abstract|✅|✅|❔|
|sigs|multiplicities|✅|✅|❔|
|sigs|multi-sig declarations|✅|✅|❔|
|constants|univ|✅|✅|❔|
|constants|none|✅|✅|❔|
|constants|iden|✅|✅|❔|
|multiplicities|lone|✅|✅|❔|
|multiplicities|one|✅|✅|❔|
|multiplicities|some|✅|✅|❔|
|multiplicities|no|✅|✅|❔|
|fields|vars|✅|✅|❔|
|fields|type|❔|❔|❔|
|fields|basic (A->B)|❔|❔|❔|
|fields|with multiplicities (A->lone B)|❔|❔|❔|
|fields|complex (A->B->C)|❔|❔|❔|
|fields|arbitrary expressions (A+B)|❔|❔|❔|
|operator|dot join a.b|❔|❔|❔|
|operator|inv dot join a;b|❔|❔|❔|
|operator|box join a\[b\]|❔|❔|❔|
|operator|domain restriction s <: a|❌|❌|❔|
|operator|range restriction s :> a|❌|❌|❔|
|operator|transpose ~r|❌|❌|❔|
|operator|positive TC ^r|❌|❌|❔|
|operator|reflexive TC *r|❌|❌|❔|
|operator|cardinality #S|✅|✅|❔|
|operator|override r1 ++ r2|❌|❌|❔|
|operator|set union|✅|✅|❔|
|operator|set intersection|✅|✅|❔|
|operator|set difference|✅|✅|❔|
|operator|set membership|✅|✅|❔|
|operator| = |✅|✅|❔|
|operator| != |✅|✅|❔|
|operator| <= |✅|✅|❔|
|operator| =< |✅|✅|❔|
|operator| < |✅|✅|❔|
|operator| > |✅|✅|❔|
|operator| >= |✅|✅|❔|
|operator|Conjunction|✅|✅|❔|
|operator|Disjunction|✅|✅|❔|
|operator|Implication|✅|✅|❔|
|operator|Bi-Implication|✅|✅|❔|
|operator|Negation|✅|✅|❔|
|operator|if-then-else|❌|❌|❔|
|operator|let binding|❌|❌|❔|
|in-built-function|disj|❔|❔|❔|
|in-built-function|sum|❔|❔|❔|
|in-built-function|plus|❌|❌|❔|
|in-built-function|minus|❌|❌|❔|
|in-built-function|rem|❌|❌|❔|
|in-built-function|mult|❌|❌|❔|
|fact|named|✅|✅|❔|
|fact|signed|✅|✅|❔|
|fact|unidentified|✅|✅|❔|
|fact|localized to sig|❔|❔|❔|
|function|unparameterized|❔|❔|❔|
|function|parameterized|❔|❔|❔|
|predicate|unparameterized|❔|❔|❔|
|predicate|parameterized|❔|❔|❔|

- arrow expressions
- run commands
- check commands


## Parameterized Dash

|feature|description|translation|running|tests|
|--|--|--|--|--|
|param|identifying param depth||||
|conf|enumeration adding variable|||❕|
|scopes_used|enumeration adding variable|||❕|
|stable|enumeration adding variable|||❕|
|events|enumeration adding variable|||❕|
|state literals|constructing state hierarchy using sets of strings|||❕|
|transition literals|values for trans_taken|||❕|
|pre-trans|defining pre-tans|||❕|
|pre-trans|pre-trans from states|||❕|
|pre-trans|scopes clause|||❕|
|pre-trans|events clause|||❕|
|post-trans|defining post-trans|||❕|
|post-trans|handling stability|||❕|
|post-trans|scopes update|||❕|
|post-trans|sending events|||❕|
|enabled-trans|defining enabled-trans|||❕|
|enabled-trans|conf|||❕|
|enabled-trans|scope|❔||❕|
|enabled-trans|events|||❕|
|transition|combining pre and post|||❕|
|nextIsStable|combining enabled|||❕|
|Init|Defining initial values (except conf)|||❕|
|Init sync|Syncing config and module|||❕|
|Init conf|defining initial value of all conf|||❕|
|Stutter|Defining stutter in TLA+|||❕|
|stable|type definition|||❕|
|conf|type definition|||❕|
|scopes_used|type definition|||❕|
|trans_taken|type definition (none included)|||❕|
|valid|formulae||||❕|
|valid|primed and unprimed||||❕|
|Next|Defining Next|||❕|
|User-defined vars|declaration|❔|❔|❕|
|User-defined vars|type definition|❔|❔|❕|
|User-defined vars|sync with config|❔|❔|❔|



## Properties

|feature|description|translation|running|tests|
|--|--|--|--|--|
|Safety||❔|❔|❔|
|Fairness constraints|-|❔|❔|❔|
|Liveness|-|❔|❔|❔|




