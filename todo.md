# remaining issues:
- what is value qname value
- what is enumDecl

# parser-visitor & 'resolve':
if then else cases; almost like reparsing it

typecheck: see formula when expecting formula (prob use the the operators can distinguish, but not ite, let and join&join)
comprehensionValue: decl cannot contain multiplicity words 288
create LONEOF, ONEOF, SOMEOF, SETOF from cardinality, or maybe don't need to
circularity
declarations
arity
transitive closure

# changes (some WIP)
- ( qname '.' ) should be optional in 'funDecl' and 'predDecl'
- accept '<=' for less or equal to; not documented in book, but CUP accepts it
- separated the different uses of the 'multiplicity' keywords, so it's clear what they are being used for from context
- added tokens for arithmatic operations, ExprConstant (Alloy AST), WIP, so they are not just 'qname'
- added the 'until' keyword
- accept trailing commas in 'sigDecl', (WIP there may be more the I need to accept)
- accept 'funDecl' in 'paragraph'
- removed the two 'boxValue' rules in 'value'

- 'formula' concatenation in 'block': 
    - Before: optional AND operators, but this causes precedence issues with other binop rules
    - After: mandatory AND operators, but accept sequence of 'formula's in 'block'; this is consistent with CUP
- merged 'formula' and 'value', because 'ite' causes indirect left recursion between formula and value (starts with 'formula' in a 'value ite')
    - merged them to expr and ANTLR handles direct left recursion
    - need to check for usage of grammar rules (formula or value) in parser-visitors
    - most rules can be distinguished by the operator tokens alone, with exceptions such as 'ite', 'let', 'join' and 'box'
- integrated ITE
    - made two binop rules: 'implies' and 'else'
    - a 'ite's (ternary op) precedence is not handled correctly in the midst of other binop rules
    - used ANTLR's semantic predicates to reject 'else' if not in rhs 'expr' of 'implies'
    - parser-visitors need to check and restructure the ast, because some tricky cases cannot be handled by semantic predicates alone

# todo
- need to reorganize the tokens, ExprConstant

ExprConstant
                    TRUE("true"),
                    /** false */
                    FALSE("false"),
                    /** the builtin "iden" relation */
                    IDEN("iden"),
                    /** the minimum integer constant */
                    MIN("min"),
                    /** the maximum integer constant */
                    MAX("max"),
                    /** the "next" relation between integers */
                    NEXT("next"),
                    /** the emptyness relation whose type is UNIV */
                    EMPTYNESS("none"),
                    /** a String constant */
                    STRING("STRING"),
                    /** an integer constant */
                    NUMBER("NUMBER");

ExprUnary: CAST2INT, CAST2SIGINT


precedence:
non-binop that ends with formula needs to come before binop
binop arrange according to precedence described in book



group rules together
enum decl is not used

According to book, 
3 types of expr: relational expr, boolean expr, integer expr
rost operators apply only to expr type with exceptions: conditional construct, let, join&box

- IMPLIES is not used
1) merge value and formula, but we can distinguish from operators
    with three exceptions:
        - implies
        - let
        - join&dot
2) use branch of antlr
3) rewrite rules (too hard, not maintainable)

- CUP always uses Name (allows both ID and QNAME), but in ANTLR we use both name and qname
- CUP is accepting a lot more things than the grammar defined in Book. Not sure which one to follow.

- seems like CUP doesn't create ExprBinary.Op.JOIN directly
- what is ExprCall

- bit manipulation operators

