# TLA+ Model

- The TLA+ model consists of two parts: `<name>.tla` and `<name>.cfg`

- The TLC model-checker requires both these files to exist when checking a model

- These files usually live in the same folder.

## Module

```
---- module <name> ----
====

EXTENDS <standard libraries>
CONSTANTS <constants>
VARIABLES <variables>

<formulae>

====
```

- The `<name>` in the module head must match the name of the .tla file.

## Configuration:

```
CONSTANTS

PROPERTIES

INVARIANTS

INIT

NEXT
```

- CONSTANTS in the configuration specify the values taken by their counterparts in the model when running

- INIT and NEXT are formulae

- PROPERTIES - these are checked while running the model to ensure the model doesn't violate them. They do not constrain the model, allows use of LTL operators

- INVARIANTS - a specific type of property, namely a safety property that must hold true in every state

- PROPERTIES and INVARIANTS can refer to formulae specified inside the module.

## Model:

- The model is created by the TLC model-checker from `<name>.tla` and `<name>.cfg` when TLC is run. It is not created by the translator.

- The purpose of `class Model` is to ensure consistency between the module and its config. Various checks are implemented to ensure consistency.




