# Design for AST:

- Each node is equivalent to an expression

- An expression can be anything - a constant, a variable, an operator applied to operands


## Formulae

- A Formula is an expression where the operand is `==`

- The TLAPlusModule is a list of these formulae

- Each Formula has a name on the left, with a value on the right

- the right has to be a boolean expression

- the left is an operand type, used in other expressions later


## Operator list

### Boolean operators

| operator    	 | type    | example |
|--------| ------- | ------- |
| /\ | binary | a /\ b |
| \/ | binary | a \\/ b |
| ~  | unary | ~ a |
| => | binary | a => b |
| <=> | binary | a <=> b |


### Integer operators

| operator    	 | type    | example |
|--------| ------- | ------- |
| > | binary | a > b |
| < | binary | a < b |
| >= | binary | a >= b |
| <= | binary | a <= b |
| + | binary | a + b |
| - | binary | a - b |
| * | binary | a * b |
| .. | binary | a..b |



## Structure:

- Every class inherits from the root class TLAPlusASTNode.

- Each class must have a toStringBuilderList() function, which generates a list of strings that represent all its descendants, with subsequent elements of the list assumed to be in separate lines.

- The line feature is meant to be used mainly by /\ and \/, in accordance with TLA+ standard practice.

- Indentation is handled by appending tabs by the parent

- The top-level module class has a toString() function, that handles the overall pretty-printing and formatting. This is not the responsibility of the AST classes

- Alternatively, the AST classes can be entirely agnostic about the string representation but this won't really work - the string stuff has to be implemented somewhere, and having a separate class is unergonomic, since it requires copuling the creation of a new AST class with a string function.

- Formulae can also be functions with arguments - a general function class is required. Stuff like cardinality is also a function.

- An Exp class consists of a list of subexpressions, this is an abstract class.

- An ANDExp class is one where the operator is fixed as "/\" and has exactly two subexpressions, and so on.

- change of plan - string used instead of StringBuilder, performance doesn't matter at this stage