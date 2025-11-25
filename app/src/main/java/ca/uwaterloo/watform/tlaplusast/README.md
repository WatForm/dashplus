# TLA+ AST

This is a guide to the TLA+ AST package. Familiarity with the TLA+ language and TLC model-checker is presumed.

## Class Hierarchy

- All classes are sub-classes of `class ASTNode` in `utils/ASTNode.java`.

- `class TLAPlusExpression` is a direct sub-class of `class ASTNode`. Except for `class TLAPlusStandardLibraries`, all other classes are descendants of `class TLAPlusExpression`.

- `class TLAPlusSimpleExpression` is a direct sub-class of `class TLAPlusExpression`, representing TLA+ expressions without any children, like variables, constants and literals.

- `class TLAPlusUnaryOperator` in `tlaplusunaryoperators/TLAPlusUnaryOperator.java` is a direct sub-class of `class TLAPlusExpression`, representing TLA+ operators with exactly one operand.

- `class TLAPlusBinaryOperator` in `tlaplusbinaryoperators/TLAPlusBinaryOperator.java` is a direct sub-class of `class TLAPlusExpression`, representing TLA+ operators with exactly two operands. `class TLAPlusBinaryInfixOperator` represents TLA+ binary infix operators, where the two operands are to the left and right of the operator.

- `class TLAPlusNaryOperator` in `tlaplusnaryoperators/TLAPlusNaryOperator.java` is a direct sub-class of `class TLAPlusExpression`, representing TLA+ operators which can have any number of operands.

- `class TLAPlusQuantifier` in `tlaplusnaryoperators/TLAPlusQuantifier.java` is a direct sub-class of `class TLAPlusExpression`, representing TLA+ operators which have three operands - a bound variable, a set and an expression.

The above classes are all abstract. Each individual TLAPlus element is a concrete class derived from one of the abstract classes.


## Operator list

Source: [TLA+ cheat sheet](https://mbt.informal.systems/docs/tla_basics_tutorials/tla+cheatsheet.html)

### Implemented

```
Name == e                   \* defines operator Name without parameters, and with expression e as a body
Name(x, y, ...) == e        \* defines operator Name with parameters x, y, ..., and body e (may refer to x, y, ...)

(* Boolean logic *)

BOOLEAN                     \* the set of all booleans (same as {TRUE, FALSE})
TRUE                        \* Boolean true
FALSE                       \* Boolean false
~x                          \* not x; negation
x /\ y                      \* x and y; conjunction (can be also put at line start, in multi-line conjunctions)
x \/ y                      \* x or y; disjunction (can be also put at line start, in multi-line disjunctions)
x = y                       \* x equals y
x /= y                      \* x not equals y
x => y                      \* implication: y is true whenever x is true
x <=> y                     \* equivalence: x is true if and only if y is true

(* Integers *)              \* EXTENDS Integers (should extend standard module Integers)

Int                         \* the set of all integers (an infinite set)
1, -2, 1234567890           \* integer literals; integers are unbounded
a..b                        \* integer range: all integers between a and b inclusive
x + y, x - y, x * y         \* integer addition, subtraction, multiplication
x < y, x <= y               \* less than, less than or equal
x > y, x >= y               \* greater than, greater than or equal

(* Strings *)               

STRING                      \* the set of all finite strings (an infinite set)
"", "a", "hello, world"     \* string literals (can be compared for equality; otherwise uninterpreted)

(* Finite sets *)           \* EXTENDS FiniteSets (should extend standard module FiniteSets)

{a, b, c}                   \* set constructor: the set containing a, b, c
Cardinality(S)              \* number of elements in set S
x \in S                     \* x belongs to set S
x \notin S                  \* x does not belong to set S
S \subseteq T               \* is set S a subset of set T? true of all elements of S belong to T
S \union T                  \* union of sets S and T: all x belonging to S or T
S \intersect T              \* intersection of sets S and T: all x belonging to S and T
S \ T                       \* set difference, S less T: all x belonging to S but not T
{x \in S: P(x)}             \* set filter: selects all elements x in S such that P(x) is true
{e: x \in S}                \* set map: maps all elements x in set S to expression e (which may contain x)

(* Functions *) 

[x \in S |-> e]             \* function constructor: maps all keys x from set S to expression e (may refer to x) 
f[x]                        \* function application: the value of function f at key x
DOMAIN f                    \* function domain: the set of keys of function f
[S -> T]                    \* function set constructor: set of all functions with keys from S and values from T


(* Sequences *)             \* EXTENDS Sequences (should extend standard module Sequences)

<<a, b, c>>                 \* sequence constructor: a sequence containing elements a, b, c
s[i]                        \* the ith element of the sequence s (1-indexed!)
s \o t                      \* the sequences s and t concatenated
Len(s)                      \* the length of sequence s
Append(s, x)                \* the sequence s with x added to the end
Head(s)                     \* the first element of sequence s

(* Tuples *)

<<a, b, c>>                 \* tuple constructor: a tuple of a,b,c (yes! the <<>> constructor is overloaded)
                            \* - sequence elements should be same type; tuple elements may have different types
t[i]                        \* the ith element of the tuple t (1-indexed!)
S \X T                      \* Cartesian product: set of all tuples <<x, y>>, where x is from S, y is from T

(* Quantifiers *)

\A x \in S: e               \* for all elements x in set S it holds that expression e is true
\E x \in S: e               \* there exists an element x in set S such that expression e is true

(* State changes *)

x', y'                      \* a primed variable (suffixed with ') denotes variable value in the next state 
UNCHANGED <<x,y>>           \* variables x, y are unchanged in the next state (same as x'=x /\ y'=y)

(* Control structures *)

LET x == e1 IN e2           \* introduces a local definition: every occurrence of x in e2 is replaced with e1
IF P THEN e1 ELSE e2        \* if P is true, then e1 should be true; otherwise e2 should be true

```

### Not Implemented

```
(* Comments *)

(* This is 
   multiline comment *)
\* This is single line comment

(* Records *)

[x |-> e1, y |-> e2, ...]   \* record constructor: a record which field x equals to e1, field y equals to e2, ... 
r.x                         \* record field access: the value of field x of record r
[r EXCEPT !.x = e]          \* record r with field x remapped to expression e (may reference @, the original r.x)
[r EXCEPT !.x = e1,         \* record r with multiple fields remapped: 
          !.y = e2, ...]    \*   x to e1 (@ in e1 is equal to r.x), y to e2 (@ in e2 is equal to r.y)
[x: S, y: T, ...]           \* record set constructor: set of all records with field x from S, field y from T, ...

(* Functions *) 

[f EXCEPT ![x] = e]         \* function f with key x remapped to expression e (may reference @, the original f[x])
[f EXCEPT ![x] = e1,        \* function f with multiple keys remapped: 
          ![y] = e2, ...]   \*   x to e1 (@ in e1 will be equal to f[x]), y to e2 (@ in e2 will be equal to f[y])

```

# Formulae nomenclature:

Consider the following examples:

```
┏━━━━━━━━━━▶ Formula Declaration
┃     ┏━━━━▶ Formula Body
F == exp
┗━━┳━━━┛
   ┗━━━━━━━▶ Formula Definition
```

```
┏━━━━━━━━━━▶ Formula name
┃ ┏━━━━━━━━▶ Parameter
┃ ┃     ┏━━▶ Formula Application
G(X) == F
┗━┳┛
  ┗━━━━━━━━▶ Formula Declaration
```

```
┏━━━━━━━━━━▶ Formula Declaration
┃      ┏━━━▶ Formula Application
┃    ┏━┻━━┓ 
H == G(exp)
┗━━━━┳━━━━┛
     ┗━━━━━━━▶ Formula Definition
```

- The module consists of a list of definitions of formulae.
- Each formula definition consists of a formula declaration and a formula body, which is a TLA+ expression.
- A formula declaration consists of the name of the formula, along with a list of variables as parameters.
- A formula application refers to the use of a formula within an expression, consisting of a name, along with a list of expressions as arguments.


- Note that FormulaApplication is not treated as an n-ary operator because it is a meta-operator that constructs an n-ary operator from a custom formula name. It's an n-ary operator-generator, not an n-ary operator

- Similarly, FormulaDefinition is not a binary infix operator because it can't be used arbitrarily in the tree, other uses of == can be found only in LET bindings, which are their own operator.

# Plan for AST checks:

- scope arguments: whenever a variable is used, it must be declared globally, else belong to the declaration of the local definition

- All constants must be declared globally

- All applications of formulae must be preceded by their definition

- STL imports must match their use - no unnecessary imports, and no missing imports


## Structure:

- Every class inherits from the root class ASTNode.

- Each class must have a toStringBuilderList() function, which generates a list of strings that represent all its descendants, with subsequent elements of the list assumed to be in separate lines.

- The line feature is meant to be used mainly by /\ and \/, in accordance with TLA+ standard practice.

- Indentation is handled by appending tabs by the parent

- The top-level module class has a toString() function, that handles the overall pretty-printing and formatting. This is not the responsibility of the AST classes

- Alternatively, the AST classes can be entirely agnostic about the string representation but this won't really work - the string stuff has to be implemented somewhere, and having a separate class is unergonomic, since it requires coupling the creation of a new AST class with a string function.

- Formulae can also be functions with arguments - a general function class is required. Stuff like cardinality is also a function.

- An Exp class consists of a list of subexpressions, this is an abstract class.

- An ANDExp class is one where the operator is fixed as "/\" and has exactly two subexpressions, and so on.

- change of plan - string used instead of StringBuilder, performance doesn't matter at this stage

## Formulae:

- Formulae are of the form A == B
- Complicated by parameterized formulae, where A can have variables bound to it
- The expression B can make use of these bound variables, but these cannot be used elsewhere
- Enforcing this can be done by a check of the AST - every variable used at any point must be defined in the global list, or in the formula itself
- Formula constructors need to have a list of variables
- Formulae can be part of an expression, and when it's used in an expression, the parameters are themselves replaced with actual expressions
- In general, the formula itself can be described as an expression with a children array the length of the number of parameters
- The enforcement of abstract variables in the LHS of the definition and the use of concrete expressions in the RHS is left to the Module class, not done at the AST level.

## Class hierarchy:

- ASTNode is the root of all classes.

- TLAPlusAtom is the class for things that have a name and nothing else, when turned into a string. This includes constants, variables, literals (strings and ints)

- TLAPlusExp is for things that get expanded out - these have children, which are either atoms or expressions. These don't have a name field, the name info is supposed to be captured by the class type.

- BinOP is an abstraction for binary operators, BinOPMid is an abstraction for binary operators which are printed in the middle of the operators.


## Misc:

```
---- MODULE m ----

EXTENDS Integers, FiniteSets

VARIABLES t

_Init == t = {10}
_Next == (t' \in {{t-1},{t-2}}) /\ t > 0 

====
```
This produces an error in TLA+ and cannot be used. Note that replacing `{{t-1},{t-2}}` with `{t-1,t-2}` and `t={10}` with `t=10` makes the program work.

- Using /\ and \/ in the same expression requires the use of parentheses, this is VERY IMPORTANT. TLC cannot parse this




