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

|feature|description|translation|running|unit tests|integration test|
|--|--|--|--|--|--|
|conf|adding variable|✅|✅|❔|✅|
|trans_taken|adding variable|✅|✅|❔|✅|
|scopes_used|adding variable|✅|✅|❔|✅|
|stable|adding variable|✅|✅|❔|✅|
|events|adding variable|❕|❕|❔|❕|
|state literals|constructing state hierarchy using sets of strings|✅|✅|❔|✅|
|transition literals|values for trans_taken|✅|✅|❔|✅|
|pre-trans|defining pre-tans|✅|✅|❔|✅|
|pre-trans|pre-trans from states|❌|❌|❔|❌|
|pre-trans|pre-trans on|❌|❌|❔|❌|
|pre-trans|events clause|❕|❕|❔|❕|
|pre-trans|scopes clause|❌|❌|❔|❌|
|post-trans|defining post-trans|✅|✅|❔|✅|
|post-trans|handling stability|❌|❌|❔|❌|
|post-trans|sending events|❕|❕|❔|❕|
|post-trans|scopes update|❌|❌|❔|❌|
|enabled-trans|defining enabled-trans|✅|✅|❔|❌|
|enabled-trans|logic|❌|❌|❔|❌|
|transition|combining pre and post|✅|✅|❔|✅|
|nextIsStable|combining enabled|✅|✅|❔|❌|
|Init|Defining initial values (except conf)|✅|✅|❔|✅|
|Init sync|Syncing config and module|✅|✅|❔|✅|
|Init conf|defining initial value of conf|✅|✅|❔|✅|
|Small step|combine trans, pre-trans and stutter|✅|✅|❔|✅|
|Stutter|Defining stutter in TLA+|✅|✅|❔|❌|
|Implementing stutter|implementing a stutter framework|❔|❕|❕|❕|
|stable|type definition|✅|✅|❔|✅|
|conf|type definition|✅|✅|❔|✅|
|scopes_used|type definition|✅|✅|❔|✅|
|trans_taken|type definition (none included)|✅|✅|❔|✅|
|TypeOK|combining types|✅|✅|❔|✅|
|Next|Defining Next|✅|✅|❔|✅|
|Next sync|Syncing config and module|✅|✅|❔|✅|
|User-defined vars|declaration|❕|❕|❔|❕|
|User-defined vars|type definition|❕|❕|❔|❕|
|User-defined vars|sync with config|❔|❔|❔|❔|

## Core Alloy

|feature|description|translation|running|unit tests|integration tests|
|--|--|--|--|--|--|
|sigs|basic|❔|❔|❔|❔|
|sigs|subtypes|❔|❔|❔|❔|
|sigs|subsets|❔|❔|❔|❔|
|sigs|multi-sig declarations|❔|❔|❔|❔|
|multiplicities|set|❔|❔|❔|❔|
|multiplicities|none|❔|❔|❔|❔|
|multiplicities|lone|❔|❔|❔|❔|
|multiplicities|one|❔|❔|❔|❔|
|multiplicities|some|❔|❔|❔|❔|
|fields|basic (A->B)|❔|❔|❔|❔|
|fields|with multiplicities (A->lone B)|❔|❔|❔|❔|
|fields|complex (A->B->C)|❔|❔|❔|❔|
|dot join||❔|❔|❔|❔|
|box join||❔|❔|❔|❔|
|domain restriction|s <; a|❔|❔|❔|❔|
|range restriction|s :> a|❔|❔|❔|❔|
|transpose|~r|❔|❔|❔|❔|
|positive TC|^r|❔|❔|❔|❔|
|reflexive TC|*r|❔|❔|❔|❔|
|cardinality|inbuilt function in TLA+|❔|❔|❔|❔|
|set operations|union, intersection, difference|❔|❔|❔|❔|
|Cartesian product|a -> b|❔|❔|❔|❔|
|Override|r1 ++ r2|❔|❔|❔|❔|
|Expression multiplicities|refer to multiplicities|❔|❔|❔|❔|
|Comparison Negation|not/!|❔|❔|❔|❔|
|Comparison| =, <, >, <=, >=|❔|❔|❔|❔|
|in||❔|❔|❔|❔|
|Logical negation|not/!|❔|❔|❔|❔|
|Conjunction||❔|❔|❔|❔|
|Disjunction||❔|❔|❔|❔|
|Implication||❔|❔|❔|❔|
|Bi-implication||❔|❔|❔|❔|
|let, quantification||❔|❔|❔|❔|
|sum||❔|❔|❔|❔|
|let expressions||❔|❔|❔|❔|
|Arithmetic|plus/minus/mul/div/rem|❔|❔|❔|❔|
|Conditional expressions|bool implies exp (else exp)|❔|❔|❔|❔|





## Parameterized Dash

|feature|description|translation|running|unit tests|integration tests|
|--|--|--|--|--|--|
|Scopes modification|-|❔|❔|❔|❔|
|Params|-|❔|❔|❔|❔|
|Buffers|-|❔|❔|❔|❔|

## Properties

|feature|description|translation|running|unit tests|integration tests|
|--|--|--|--|--|--|
|Safety||❔|❔|❔|❔|
|Fairness constraints|-|❔|❔|❔|❔|
|Liveness|-|❔|❔|❔|❔|




