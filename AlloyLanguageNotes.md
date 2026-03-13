# Alloy Language
This document described any differences between the Alloy language supported by the Alloy Analyzer (AA) and the Alloy language supported in the Dash+ tools.  
Additionally, we document any notes or assumptions we make about defaults and meaning of parts of the Alloy language.

## Differences from AA
- overloading of function/predicate/field names for different types of arguments is not supported

## Alloy Declarations and Arrow Expression Multiplicities 
- → is right associative (Associativity is only relevant with multiplicities). We note that right associativity for → contradicts the Alloy cheat sheet at [https://esb-dev.github.io/mat/alloy-cheatsheet.pdf](https://esb-dev.github.io/mat/alloy-cheatsheet.pdf), which says all binary operators besides implication associate left. Although, the point is not addressed in the declarations section of the cheat sheet.

In the following,
- in `f: mul Expr`,  `mul Expr` is the declaration formula
- `mul` in { `set`, `some`, `lone`, `one`} (not `all` or `no`)
- `limiting_mul` = {`some`, `lone`, `one`} (a subset of `mul` that does not include `set`)
- `set_quant` in { `no`, `some`, `one`, `lone`} (not `set` or `all`)
- `formula_quant` in { `all`, `some`, `one`, `lone`, `no`} (not `set`)

General patterns in rules that appear below (some rules can be used together):
1. if `mul` in `limiting_mul`, the Expr must be unary set (depends on arity)
2. if no `mul` with a unary set Expr, value of `mul` is `one` (p. 77 Jackson book) (depends on arity)
3. a missing mul in an arrow expression is  `set` (syntax)
4. an arrow expression can contain only `set` multiplicities (syntax)
5. an arrow expression can contain `limiting_mu`l, but not below a non-arrow operator, in which case rule#4 applies (syntax)
6. if Expr is not a unary set, `mul` is not allowed explicitly and its value is `set` ( p. 77 Jackson book p. 77 says no mul is supposed to be there at all, but the AA allows a multiplicity of `set` here but no other mul (depends on arity)
7. Expr must be a unary set (depends on arity)

### Arrow Expressions NOT used in Alloy Declarations 
e.g., ` ...+ a → b + ...`
Rules: 3, 4

### Field Declarations
```
sig A {
	f: mul Expr
}
```
References:
- Jackson's Software Abstractions book says: `f: A m→n B` means  `all a:A | n (a.f)` `all b:B | m (f.b)`
Rules: 1, 2, 3, 5, 6
Meaning:
- general case `f:  A → B → ((C→D) m→n (E→F))→G→H`
- for every non-set n the type, it means: `all a:A,b:B, c:C, d:D, g:G, h:H | n ( (d.(c.(b.(a.f))).g.h)` for every a, b, c, d, g, h tuple in f, there are n distinct (e,f) pair(s) allowed
- for every non-set m the `type`, it means: `all a:A,b:B, e:E, f:F, g:G, h:H | m ( (b.(a.f)).h.g.f.e )` for every a, b, e, f, g, h tuple in f, there are m distinct (c,d) pair(s) allowed.
- higher order quantification is not useful because  `all a:A,b:B, ef:E→F, g:G, h:H | m ( (b.(a.f)).h.g.(ef) )` is not possible to write in Alloy; join must be on one column only.
- For `f: A →(B→one C)→ D`, the following is not correct: `all b:B | one (b.((A.f).D))` in this one if A or D are empty, then this cannot be true because the `one` multiplicity is outside the quantification over elements in A and D, whereas in `all a:A, b:B, d:D | one ((b.(a.f)).d)` if A or D are empty, the above is true because there is nothing to quantify over.
Notes:
- it is not clear, why a multiplicity is not allowed before a multi-arity type, as in:  
```
sig A {
	f: one B → C
}
```
It seems possible to give this a meaning.

### QtExpr

```
set_quant Expr
```

Rules: 4 for Expr
- no defaults required because set_quant must be present to make it a formula (rather than a set value)

### QuantificationExpr

```
formula_quant x:mul Expr | Expr2
```

Rules: 1,2,3,5,6
- if Expr is a relation, this is higher-order and the AA will reject it 

### Set Comprehension Declarations

```
{ a: Expr | Expr2 }
```

Rules: 4, 7, 2(?)
- no multiplicity/quantification before Expr

### Predicate Argument Declarations

```
pred p[a: mul Expr] { Expr2 }
```

References:
- https://alloytools.org/spec.html "The constraints implicit in the declarations of arguments of functions and predicates are conjoined to the body constraint when a function or predicate is run. When a function or predicate is invoked (that is, used within another function or predicate but not run directly), however, these implicit constraints are ignored. You should therefore not rely on such declaration constraints to have a semantic effect; they are intended as redundant documentation. A future version of Alloy may include a checking scheme that determines whether actual expressions have values compatible with the declaration constraints of formals.", which means
	- when a pred p is used directly in a `check` command, the constraints in `mul Expr` ` are added to the pred
	- when a pred p is invoked with a fact or another pred of the model, these constraints are not used
Rules: 1, 2(?), 3, 5
Meaning:
- p[x] in a command means:
```
mul x
x in Expr
Expr2[x/a]
```

Best practice:
- disallow any `mul Expr` in predicate declarations? (but that will affect current Alloy models)

### Fact Argument Declarations

- how are these combined with facts since this returns a value?
