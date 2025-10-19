# TLA+ Model

- The TLA+ model consists of two parts: module.tla and configuration.cfg

- The TLC model-checker requires both these files to exist when checking a model

## Module

```
---- module <filename> ----
====

EXTENDS
CONSTANTS
VARIABLES

<formulae>

<spec>

====
```

## Configuration:

```
CONSTANTS

INIT

NEXT

PROPERTY

INVARIANT
```

- CONSTANTS in the configuration specify the values taken by their counterparts in the model when running

- INIT and NEXT are formulae

- PROPERTY - these are checked while running the model to ensure the model doesn't violate them. They do not constrain the model, allows use of LTL operators

- INVARIANT - a specific type of property, namely a safety property that must hold true in every state

## Model:

- The purpose of the model class is to ensure consistency between the module and its config.

- In addition, it needs to ensure the filename of the output matches the filename of the model, so it cannot expose the string contents of the model itself.



