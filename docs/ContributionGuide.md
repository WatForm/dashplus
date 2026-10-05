# Guide for Contributors

This document explains the working and organization of the code in this repository. The reader is expected to understand Dash and Alloy before reading this.

## Reading List

- [Alloy language reference](https://alloy.readthedocs.io)
- [DASH: A New Language for Declarative Behavioural Requirements with Control State Hierarchy](https://ieeexplore.ieee.org/document/8054831)
- [Dash+: Extending Alloy with Hierarchical States and Replicated Processes for Modelling Transition Systems](https://ieeexplore.ieee.org/document/9582381)

## Getting started

- The entry-point for the code is in `cli/Main.java`, by default. When adding a new package, a separate Main class needs to be created and 
- The Pico-cli library is used to parse arguments everywhere
- Use the following code to produce a fully resolved DashModel object:

```
DashFile dashFile = (DashFile) parse(inPath);
DashModel dashModel = (DashModel) Parser.parseToModel(inPath);
```

- The available accessor functions are listed in `DashAccessors.java`. Individual accessors are also listed in the various Table files. 
- The accessors that depend on the resolution of information from multiple tables is listed in DashAccessors.java.
- Computations on the DashAST is to be done by accessors, not in separate packages.
- The FQN of an element consists of the location of the definition of the element in the .dsh file, separated using `/` (DashStrings.java)
- It is expected to place all hard-coded strings in a file called `XStrings.java`, where X is the name of the feature being developed.
- If a function with general use outside the feature is being written, it should be placed in GeneralUtils.java, with parameterized types
- Before writing such a function, make sure that it doesn't already exist in GeneralUtils.java
- When making a new package, include a README.md file in it, to explain the features of the package at a high-level





## Notes on Code

* the parser can create a DashRef. It is later resolved.  Which means that printing should not chopPrefixFQN.
	- does our parser make it an AlloyQname and break it up??

* transition are not added to table until resolution so entries can be final.  We walk over the state hierarchy again to add these in.

* in functions for accessing transition parts:
	- ...R means post-resolved objects
	- ...P means post-parsing

* run dashplus with -ea option to see assertions that fail

* all parts of Alloy model are loaded before Dash parts so resolution can check for name clashes with Alloy sigs/fields

* vars not mentioned in action do not change is built in to semantics now

* all of dash-to-alloy translation is currently wrapped in try-catch so get stacktrace for exceptions

* A m -> n B in decls means   FIX !!!
	- each member of A maps to n members of B 
	- each member of B maps to m members of A
	- A and B can be relations also
	all a:A | n a.r
	all b:B | m r.b
* if only x: B, default multiplicity is "one" x: one B
* need a script to run all models and make sure can generate instances (run_cmd_on_all_models.py)

* In Dash, a var decl x : one Y produces
DashVarDecls("x", "one",  "Y") as is done in AlloyDecl

* in var decls, we support  FIX !!!
	x: m A, where "m A" is an AlloyQtExpr and A" is an AlloyQnameVar or AlloyIntExpr
	or
	x: A m -> n B, "A m -> n B" is an AlloyArrowExpr
	- generally supporting any AlloyDecl would be too much b/c of fields like var, disj, private


* translating VarDM
	- could have x: Int
	- Var Table:
		state S {
			x: one X ("x", Quant.ONE, AlloyQname "X")
			y: set (X -> Y) ("y", Quant.SET, AlloyArrowExpr)
			a: one (X + Y) ("a", Quant.ONE, AlloyUnionExpr)
		}
		sig Snapshot {
			x: set (PID set->one X)
			y: set (PID set -> set (X -> Y))
			a: set (PID set -> one (X + Y))
		}

