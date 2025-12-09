# Guide for Contributors

This document explains the working and organization of the code in this repository. The reader is expected to understand Dash and Alloy before reading this.

## Reading List

- [Alloy language reference](https://alloy.readthedocs.io)
- [DASH: A New Language for Declarative Behavioural Requirements with Control State Hierarchy](https://ieeexplore.ieee.org/document/8054831)
- [Dash+: Extending Alloy with Hierarchical States and Replicated Processes for Modelling Transition Systems](https://ieeexplore.ieee.org/document/9582381)

## Getting started

- The entry-point for the code is in `parser/Main.java`, by default. When adding a new package, a separate Main class needs to be created and 
- The Pico-cli library is used to parse arguments everywhere
- Use the following code to produce a fully resolved DashModel object:

```
DashFile dashFile = (DashFile) parse(inPath);
DashModel dashModel = (DashModel) ParserUtils.parseToModel(inPath);
```

- The available accessor functions are listed in `DashAccessors.java`
- The FQN of an element consists of the location of the definition of the element in the .dsh file, separated using `/` (DashStrings.java)
- It is expected to place all hard-coded strings in a file called `XStrings.java`, where X is the name of the feature being developed.
- If a function with general use outside the feature is being written, it should be placed in GeneralUtils.java, with parameterized types
- Before writing such a function, make sure that it doesn't already exist in GeneralUtils.java
- When making a new package, include a README.md file in it, to explain the features of the package at a high-level
