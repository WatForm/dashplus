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
- what defaults are used for x:X is it set or one???
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




## to find out
The syntax of Alloy does in fact admit higher-order quantifications???

