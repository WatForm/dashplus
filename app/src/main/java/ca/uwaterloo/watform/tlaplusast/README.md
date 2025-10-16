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



