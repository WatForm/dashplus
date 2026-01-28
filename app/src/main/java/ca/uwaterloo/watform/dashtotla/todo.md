# Schedule

## Overview:

- Dash (states, transitions, events, stability, concurrency, variables)
- Alloy
- Dash+ (parameters, buffers)
- Properties (safety, liveness)


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
|enabled-trans|scope|❔|✅|❕|
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
|valid|formulae|✅|✅||❕|
|valid|primed and unprimed|✅|✅||❕|
|Next|Defining Next|✅|✅|❕|
|Next sync|Syncing config and module|✅|✅|❕|
|Implementing stutter|implementing a stutter framework|❔|❔|❔|
|User-defined vars|declaration|❔|❔|❕|
|User-defined vars|type definition|❔|❔|❕|
|User-defined vars|sync with config|❔|❔|❔|

## Core Alloy

|feature|description|translation|running|tests|
|--|--|--|--|--|
|sigs|basic|❔|❔|❔|
|sigs|subtypes|❔|❔|❔|
|sigs|subsets|❔|❔|❔|
|sigs|multi-sig declarations|❔|❔|❔|
|multiplicities|set|❔|❔|❔|
|multiplicities|none|❔|❔|❔|
|multiplicities|lone|❔|❔|❔|
|multiplicities|one|❔|❔|❔|
|multiplicities|some|❔|❔|❔|
|fields|basic (A->B)|❔|❔|❔|
|fields|with multiplicities (A->lone B)|❔|❔|❔|
|fields|complex (A->B->C)|❔|❔|❔|
|dot join||❔|❔|❔|
|box join||❔|❔|❔|
|domain restriction|s <; a|❔|❔|❔|
|range restriction|s :> a|❔|❔|❔|
|transpose|~r|❔|❔|❔|
|positive TC|^r|❔|❔|❔|
|reflexive TC|*r|❔|❔|❔|
|cardinality|inbuilt function in TLA+|❔|❔|❔|
|set operations|union, intersection, difference|❔|❔|❔|
|Cartesian product|a -> b|❔|❔|❔|
|Override|r1 ++ r2|❔|❔|❔|
|Expression multiplicities|refer to multiplicities|❔|❔|❔|
|Comparison Negation|not/!|❔|❔|❔|
|Comparison| =, <, >, <=, >=|❔|❔|❔|
|in||❔|❔|❔|
|Logical negation|not/!|❔|❔|❔|
|Conjunction||❔|❔|❔|
|Disjunction||❔|❔|❔|
|Implication||❔|❔|❔|
|Bi-implication||❔|❔|❔|
|let, quantification||❔|❔|❔|
|sum||❔|❔|❔|
|let expressions||❔|❔|❔|
|Arithmetic|plus/minus/mul/div/rem|❔|❔|❔|
|Conditional expressions|bool implies exp (else exp)|❔|❔|❔|





## Parameterized Dash

|feature|description|translation|running|tests|
|--|--|--|--|--|
|param|identifying param depth||||
|conf|enumeration adding variable|✅|✅|❕|
|scopes_used|enumeration adding variable|✅|✅|❕|
|stable|enumeration adding variable|✅|✅|❕|
|events|enumeration adding variable|✅|✅|❕|
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
|enabled-trans|scope|❔|✅|❕|
|enabled-trans|events|✅|✅|❕|
|transition|combining pre and post|✅|✅|❕|
|nextIsStable|combining enabled|✅|✅|❕|
|Init|Defining initial values (except conf)|✅|✅|❕|
|Init sync|Syncing config and module|✅|✅|❕|
|Init conf|defining initial value of all conf|✅|✅|❕|
|Stutter|Defining stutter in TLA+|✅|✅|❕|
|stable|type definition|✅|✅|❕|
|conf|type definition|✅|✅|❕|
|scopes_used|type definition|✅|✅|❕|
|trans_taken|type definition (none included)|✅|✅|❕|
|valid|formulae|✅|✅||❕|
|valid|primed and unprimed|✅|✅||❕|
|Next|Defining Next|✅|✅|❕|
|User-defined vars|declaration|❔|❔|❕|
|User-defined vars|type definition|❔|❔|❕|
|User-defined vars|sync with config|❔|❔|❔|



## Properties

|feature|description|translation|running|tests|
|--|--|--|--|--|
|Safety||❔|❔|❔|
|Fairness constraints|-|❔|❔|❔|
|Liveness|-|❔|❔|❔|




