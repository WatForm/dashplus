
# Introduction

Apalache is a tool to translate TLA+ into the logic supported by SMT solvers. The following are its modes of operation:

- Randomized symbolic execution to reason about some executions up to length k,
- Bounded model checking to reason about all executions up to length k and
- Inductiveness checking to reason about all executions of all lengths.

When running the output of the translator, the length of the execution is exactly 1, since all constraints are part of `Init`. The SAT/UNSAT problem in Alloy corresponds to the existence of a valid `Init` state in TLA+. Since all `Init` states need to be explored and since k is fixed to be 1, the second mode of operation is chosen to run translated TLA+ models.


## Snowcat

Snowcat is a typechecker that runs before Apalache. The following rules are used by the translator to satisfy Snowcat:

- All variables are typed as `Set(List(String))`
- Constants, if they exist as a result of optimizations, are typed similarly
- Given a macro, the types of its parameters are defined in the following way:

```
\* type: (U,V,W) => T
macro_name(x,y,z) ==
	<body>
```

Where the type of `x` is `U`, the type of `y` is `V` and the type of `z` is `W`. If `z` is a function from `R` to `S`, then `W` is written as `R -> S`. The `->` for typing a function is different from the `=>` used to type the macro.

Snowcat's type system is more complex, with support for type aliases, which is not used by the translator, since every variable is typed as `Set(List(String))`.


## Running Apalache

These are the commands used by Apalache:

- parse: uses the SANY parser (also used by TLC) to verify that the `.tlc` file compiles
- typecheck: runs snowcat
- simulate: checks invariants on a randomly selected subset of executions
- check: checks invariants of all executions, with the `--length` parameter as the upper limit for execution length
- test: similar to check, but tests a single action

Summary of tool:

```
apalache-mc check [--config=filename] [--init=Init] [--cinit=ConstInit] \
    [--next=Next] [--inv=Inv1,...,Invn] [--length=10] \
    [--temporal=TemporalProp1,...,TemporalPropn] \
    [--algo=(incremental|offline)] \
    [--discard-disabled] [--no-deadlock] \
    [--tuning-options-file=filename] [--tuning-options=key1=val1:...:keyn=valn] \
    [--smt-solver=(z3|cvc5)] \
    [--smt-encoding=(oopsla19|arrays|funArrays)] \
    [--out-dir=./path/to/dir] \
    [--write-intermediate=(true|false)] \
    [--config-file=./path/to/file] \
    [--profiling=false] \
    [--output-traces=false] \
    <myspec>.tla
```
