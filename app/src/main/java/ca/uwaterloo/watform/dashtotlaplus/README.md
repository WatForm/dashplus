# Dash to TLA+ Translator

## Introduction:

- This is a high-level description of the Dash to TLA+ translator
- This serves as a feature checklist for the developer of the translator
- This documents design decisions, notes and potential roadblocks involved

## Variables:

- transtaken
- conf
- scopes used

## Overview:

- Leaf states become singleton sets of strings which bear their name
- AND and OR states become sets that contain all their child states
- The set of states is stored in _conf

- "In state S" is written as `_conf \union S \= {}`.

## Dash Accessors:

- These are present for both the object and the various tables, which are public

## Feature list

### Alloy

The Alloy features require translation beginning with a core group of most-used language constructs, with progressive development of an expanding fringe of lesser-used constructs.

- signatures
- sub-signatures
- multiplicities - set, one, lone, some
- fields
- arithmetic operations
- set operations
- quantification
- relations
- signature hierarchies
- functions
- predicates
- sum and cardinality

Signatures and Sub-signatures are to be implemented as sets, using set operators to describe the relations between the sub-signatures and their parent signatures. Thus, set operations (which have direct analogues in TLA+) and signatures are part of the core group.

Since guards and actions are boolean expressions, the next translation target involve everything necessary to generate a simple valid guard.

In the example of the musical chairs model:

```
default state Start {
        trans Walk {
            on MusicStarts
            when #activePlayers > 1
            goto Walking
            do occupiedChairs' = none -> none
        }
```

Constructing simple expressions requires the use of arithmetic operators and the cardinality operator. This, along with the prime notation, have direct analogues in TLA+, making it part of the core group.

Translating quantification is closely tied to multiplicities, which are both bundled together into the next group.

- run commands
- check commands

Implementing these require careful consideration of the config file and TLC's mode of operation, and the translation requires that an API combining the config and the module is implemented first (i.e. ensuring the config is always valid for a given module and vice versa)

### Dash

- states - basic, AND, OR
- transitions
- events - environmental and internal (modelled using big-step/small-step semantics)

The above are to be ported directly from the earlier codebase. The below depend on having a working translation of Alloy first.


- guards
- actions

States are represented as a set of strings, with each leaf state mapped to a string, and non-leaf states represented by sets that contain leaf states. This is possible because:

1) If in an OR state, the configuration is in exactly one of its child states
2) If in an AND state, the configuration is in all of its child states

This means that given a state, there is exactly one unique set of leaf states that fully describe the configuration. 

Events are also modelled using sets of strings. There are no event hierarchies. Every event is either an internal event or an external event. The internal and external events are stored in separate sets. The big-step-small-step semantics are implemented by:

1) Lookahead for valid transitions using primed variables and
2) Forcing all valid small steps to be taken before an environmental event is triggered

Transitions are defined in terms of the primed configuration variable - a state is added or removed in the `from` and `goto` sections.

For brevity, formulae are made that represent complex states in terms of sets containing leaf states, or in terms of the formulae used to represent their leaf states. When writing transitions, these formulae are used instead of having the translator generate the leaf-state-list on the fly, since this make the translated output readable.


### Dash+

- parameterized states

### LTL properties

- temporal operators

### Roadmap:

| Feature  | Deadline |
| -------- | ------- |
| States   | 2025-11-18    |
| Transitions |  2025-11-18   |
| Events   |  2025-11-19   |
| Core Alloy |  |
| guards | |
| actions | |
| Extra Alloy | |
| Dash+ | |


- Implement TLA+ AST elements
- Implement TLA+ AST module API functions
- Port earlier translator from the CUP-grammar variant
- Extend translator to read Alloy expressions for guards and conditions and place them in the appropriate location in the model
- Translate Alloy (note: this description lacks sufficient granularity)



## Notes:

- String representations of non-leaf states are not used, since it provides an advantage in terms of the memory needed per configuration. It does not meaningfully change the size of the statespace, since that is determined by the model, not its representation in the translation.

- The implementation of the TLA+ AST and Module is expected to ensure that Modules with obvious semantic errors are impossible to build, by implementing a layer of abstraction to manipulate the AST.

- Full feature coverage is not expected, features are implemented as needed.

- The `sum` function in Alloy has no clear translation in TLA+. TLA+ has ways to filter and map sets and records, but no way to reduce them. `sum` is a reduction on a list.

- Symmetry-breaking is carried out by the Alloy Analyzer. Since everything is a set in Alloy, these could be represented as lists in the translation without any loss of detail. However, it is likely that there are optimizations involved in handling sets that are implemented behind the scenes by TLC, which makes it reasonable to translate sets to sets and not lists.

## Temp

- Transitions into OR states vs transitions into AND states - what do they mean semantically?

- Issue: Clauses containing primed variables don't make sense, also under what condition does it occur exactly? isn't checking the preconditions for the translation enough

- Issue: moving to the actual Dash-model

- Issue: toposort states

- Coding style: use addVariable, addConstant etc but these expect objects not Strings. The objects are created on the fly. Checks use equality of objects, which are implemented anyway. Checks within an expression will deal with objects, not strings. Also style is irrelevant.

- Shifting to Alloy, but Alloy is contained inside Dash, so runnable models still require Dash. Only snippets can be done without, which can't be tested.