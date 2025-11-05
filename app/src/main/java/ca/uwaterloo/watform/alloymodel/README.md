# WFF 
- https://alloytools.org/spec.html
- notes of places where user errors can be reported

## Namespace
- 3 Namespaces
- does AlloyAnalyzer allow for overlapping names in different Namespaces?

## relation exp, bool expr, int expr
- what operators can be used for each???

## Type error
1) Arity: attempt to apply an operator to an expr of the wrong arity
    - taking closure of nonbinary relation
    - restricting a relation to a non-set 
    - taking union, intersection, difference, compare equality or subset of two relationsl of unequal arity
2) (ignore)disjointness: combining two relations such that the result will always be empty
    - This we ignore
3) (ignore)redundancy: a redundant expr

## Field Overloading
- if there's more than one way to resolve an overload, an err msg is generated to report the ambinguous reference
- something is probably reported if there are no ways to resolve an overload

## function and predicates
- invocations are type-checked to ensure the actual arguments are not disjoint from the formal arguments
- they can be overloaded, but need to be unambiguously resolved by the type checker
```
When declaring a set variable, the default is one, so in a declaration

x: X
in which X has unary type, x will be constrained to be a scalar. In this case, the set keyword overrides the default, so

x: set X
would allow x to contain any number of elements.
```

## Modules
- The path must correspond to the directory location of the module’s file with respect to the default root directory, which is the directory of the main file being analyzed
- A module with the module identifier m must be stored in the file named m.als.

## (ignore)Imports
- A module may not contain references to components of another module that it does not import, even if that module is imported along with it in another module. 
- There must be an instantiating signature parameter for each parameter of the imported module. An instantiating signature may be a type, subtype, or subset, or one of the predefined types Int and univ. If the imported module declares a signature that is an extension of a signature parameter, instantiating that parameter with a subset signature or with Int is an error.
- and more...

## sigs (AlloyModel ctor)
- check unique names

## sigs (need access to all sigs to check, only need access to other sigs)
- can be static (default) or mutable
- can be subset signature or type signature 
- can be abstract or not
- Notice that if a mutable signature extends a static one, it is in fact necessarily static (which is signalled by a warning message).

### subset signature
- A subset signature may not be extended
- A subset signature may not be its own parent, directly or indirectly
- The subset signatures and their parents therefore form a directed acyclic graph

### type signature (subtypes)
- they are subsets, but they are mutually disjoint in all states of an instance
- A signature may not extend itself, directly or indirectly
- they form a type hierarchy whose structure is a forest: a collection of trees rooted in the top-level types

### fields
- A signature may not declare a field whose name conflicts with the name of an inherited field.
- Moreover, two subset signatures may not declare a field of the same name if their types overlap.

## Decl & Multiplicity
- x : X, if X is unary relation, default multiplicity is one

### Typechecking
- in sig it's a relation 
    - `sig A {b : B}`, type(b) = {A,B}
- multiplicity symbols don't change the type of the LHS
    - `r: e1 -> (e2 m2 -> n2 e3)`, Type\(r\) = T1 -> (T2 -> T3)
    - `x : (lone | some | one | no) expr`, type(x) = {type(expr)}
- need to check: After the some/lone/one multiplicity symbol, this expression must be a unary set.

### Checks that can happen in ctors
- Any variable that appears in a bounding expression must have been declared already, either earlier in the sequence of declarations in which this declaration appears, or earlier elsewhere.
- var isn't allowed everywhere
    - quantificationExpr
    - maybe more???
- right disj isn't allowed everywhere
```
if (d.disjoint2 != null) {
    ExprHasName name = d.names.get(d.names.size() - 1);
    throw new ErrorSyntax(d.disjoint2.merge(name.pos), "Function parameter \"" + name.label + "\" cannot be bound to a 'disjoint' expression.");
}

sig Person {}
fact {
    all p: disj Person | p in Person
}
Local variable "p" cannot be bound to a 'disjoint'
expression.
```
- private is not needed
```
if (d.isPrivate != null) {
    ExprHasName name = d.names.get(0);
    throw new ErrorSyntax(d.isPrivate.merge(name.pos), "Function parameter \"" + name.label + "\" is always private already.");
}
```
- possibly more???


## to find out
The syntax of Alloy does in fact admit higher-order quantifications???


# Plan
- WFF errors during construction
    1) don't check for anything, all at WFF, throw ErrorUser (better)
    2) check as much as we can in ctor, throw ErrorUser. rest check at WFF, throw ErrorUser
- Seems like there are 4 stages:
    1) alloyast ctor (to be decided)
    2) alloymodel ctor (duplicate names)
    3) checking sig map (see below) & what we choose not to check in ctor, we check here
    4) typechecking


## sig
- see AlloyAnalyzer/sig.java for errors thrown at construction
- check for unique names in AlloyModel.ctor
- iterate sig map:
    - var sigs walk up branch: var sig's parent is not static
    - subset sig walk up branch: acyclic. Note: at a split, dfs, don't share visited set
    - type sig walk up branch: acyclic and doens't extend from subset sig
    - every sig walk up branch: no name conflicts
    - check subset sig with overlapping types don't have same name:
        1. hashmap (string field, set\<AlloySigPara\>)
        2. iterate thru sig map, add to hashmap and check for overlap
            1. get type of sig (look up branch, stop at first type sig or top-level sig)
            2. no overlap if types distinct and mutually unreachible types 
```
sig F {}
sig top1 {}
sig top2 {}

sig t1 extends top1 {}
sig t2 extends top1 {}
sig s1 in top1 {}
sig s2 in top1 {}
sig s8 in top2 {}

sig t3 extends t1 {}
sig t4 extends t1 {}
sig t5 extends t2{}
sig s3 in s1 {}
sig s4 in s1 + s2 {

}
sig s5 in s2 {
}

sig s6 in t4 {
	f: F
}

sig s7 in t5 {
	f: F
}
```

## decl & multiplicity
- during typechecking, need to ensure some/lone/one only appears before a unary relation
- there's a bunch other other checks, which can occur in ctor or before typechecking

