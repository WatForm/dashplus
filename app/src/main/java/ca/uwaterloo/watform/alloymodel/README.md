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



## fact
### Typechecking
- typecheck block -> typecheck all expr in block are constraints





## Fun & Pred
- no direct or indirect recursive invocations
    - get recursive invocations for every fun/pred
    - check for cycles at invocation (error not thrown if not called)
```
sig A{
	b: B
}
lone sig a extends A {}
sig B {
	c:C
}
sig C {}
lone sig c extends C {}


//fun b [a : A] : A {
//	a
//}

pred A (a : A, b1: B, c1:C) {
	a.b in B
}

pred P1 {
	P2 and a in A
}

pred P2 {
	P1 and a in A
}

fact {
	P1
}
```
- treat (same for fun) 
```
pred S.myPred(arg1: T1, arg2: T2) {
    // body
}
```
as 
```
pred myPred(this: one S, arg1: T1, arg2: T2) {
    // body
}
```

### Pred Typechecking
- typecheck arguments
- typecheck block with new context that has the arguments -> typecheck all expr in block are constraints

### Fun Typechecking
- typecheck arguments
- typecheck return type
- typecheck block with new context that has the arguments -> type(fun.block) is subtype of return type



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


## Commands (not all of them can be checked during construction, b/c need info on sig)
- consistent: at most one bound may be associated with any signature, implicitly, explicitly, or by default; and
- complete: every top-level signature must have a bound, implicitly or explicitly.
- uniform: if a subsignature is explicitly bounded, its ancestor top-level signature must be also.

ctor:
- `throw new ErrorSyntax(pos, "Sig " + sig + " cannot have a negative starting scope (" + startingScope + ")");`
- `throw new ErrorSyntax(cmd.pos, "Cannot specify a scope for the builtin signature \"" + sig + "\"");`
- `throw new ErrorSyntax(cmd.pos, "Cannot specify a negative scope for sig \"" + sig + "\"");`
- `throw new ErrorSyntax(pos, "Sig " + sig + " cannot have a negative ending scope (" + endingScope + ")");`
- `throw new ErrorSyntax(pos, "Sig " + sig + " cannot have an ending scope (" + endingScope + ") smaller than its starting scope (" + startingScope + ")");`
- `throw new ErrorSyntax(pos, "Sig " + sig + "'s increment value cannot be " + increment + ".\nThe increment must be 1 or greater.");`
- `throw new ErrorSyntax(pos, "Cannot specify a bitwidth less than 0");`
- `throw new ErrorSyntax(pos, "Cannot specify a bitwidth greater than 30");`
- `throw new ErrorSyntax(pos, "With integer bitwidth of " + bitwidth + ", you cannot have sequence length longer than " + max());`
- `throw new ErrorSyntax(cmd.pos, "The number of atoms exceeds the internal limit of " + Integer.MAX_VALUE);`
- `throw new ErrorSyntax(cmd.pos, "You cannot set a scope on \"univ\".");`
- `throw new ErrorSyntax(cmd.pos, "You can no longer set a scope on \"Int\". " + "The number of atoms in Int is always exactly equal to 2^(i" + "nteger bitwidth).\n");`
- `throw new ErrorSyntax(cmd.pos, "You cannot set a scope on \"seq/Int\". " + "To set the maximum allowed sequence length, use the seq keyword.\n");`
- `throw new ErrorSyntax(cmd.pos, "Sig \"String\" already has a scope of " + maxstring + ", so we cannot set it to be " + scope);`
- `throw new ErrorSyntax(cmd.pos, "Sig \"String\" must have an exact scope.");`
- `throw new ErrorSyntax(cmd.pos, "You cannot set a scope on \"none\".");`


stage 3:
- `throw new ErrorSyntax(cmd.pos, "Mutable sig " + et.sig + " is not top-level thus cannot have scopes assigned.");`
- `throw new ErrorSyntax(cmd.pos, "Cannot specify a scope for a subset signature \"" + sig + "\"");`
- `throw new ErrorSyntax(cmd.pos, "Sig \"" + sig + "\" already has a scope of " + old + ", so we cannot set it to be " + newValue);`
- `throw new ErrorSyntax(cmd.pos, "Sig " + et.sig + " is variable thus scope cannot be exact.");`
- `throw new ErrorSyntax(cmd.pos, "You must specify a scope for sig \"" + s + "\"");`
- `throw new ErrorSyntax(cmd.pos, "You cannot set a scope on the enum \"" + s.label + "\"");`
- `throw new ErrorSyntax(cmd.pos, "Sig \"" + s + "\" has the multiplicity of \"one\", so its scope must be 1, and cannot be " + scope);`
- `throw new ErrorSyntax(cmd.pos, "Var sig \"" + s + "\" has the multiplicity of \"one\", so its scope must be 1 or above, and cannot be " + scope);`
- `throw new ErrorSyntax(cmd.pos, "Sig \"" + s + "\" has the multiplicity of \"lone\", so its scope must 0 or 1, and cannot be " + scope);`
- `throw new ErrorSyntax(cmd.pos, "Sig \"" + s + "\" has the multiplicity of \"some\", so its scope must 1 or above, and cannot be " + scope);`
- `throw new ErrorSyntax(cmd.pos, "You cannot set a scope on \"steps\" in static models.");`

## Expr
### join (`a.b`)
- if b is disj 
    - throw disj needs at least two args
- if b is a field; type(b) = {A -> B}
    - does it belong to sig a with type A?
    - type is B
- if b is a fun/pred 
    - check no cyclic invocation
    - typecheck like it's b[a]
    - need to be careful if it's a.b[c]
- if it's both a valid field and valid invocation
    - report the ambinguity error

### box b[a,c]
- if b is disj 
    - if not enough args, throw disj needs at least two args
    - we accept any number of arguments
    - arguments must be relations
    - type is a constraint
- b must be a fun/pred
- check no cyclic invocation
- check correct number of args
- check type of args
- type is constraint if b is a pred else it's b's return type



## to find out
The syntax of Alloy does in fact admit higher-order quantifications???


# Plan
- WFF errors during construction
    1) don't check for anything, all at WFF, throw ErrorUser (better)
    2) check as much as we can in ctor, throw ErrorUser. rest check at WFF, throw ErrorUser
- Seems like there are 4 stages:
    1) alloyast ctor (to be decided)
    2) alloymodel ctor (duplicate names)
    3) checking sig map (see below) & collect fun&pred's recursive invocations & what we choose not to check in ctor, we check here
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

## fun & pred
- get all recursive invocations at step 3
    - but an error is thrown only if they are actually invoked by a dot or box

## commands
- some need to be checked at stage 3, b/c need AlloyModel.sigs
- some could be checked at ctor

## expr
- possibly a visitor and interfaces (constraint, relation, integer) on AlloyExpr to hold some fields like arity etc
- see above for what's to check at each expr

