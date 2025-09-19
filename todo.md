parser-visitor:
typecheck: see bool when expecting bool
maybe some weird if then else cases



TODO:

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

- UNTIL keyword
- bit manipulation operators

CHECKED:
- moduleDecl
- importDecl
- enumDecl
- factDecl
- assertDecl

- some
- one
- lone

=< operator

reorder the rules

arithmatic operations (not just join or box)

so many issues with the join operator
a.plus[b] should be parsed as (a.plus)[b]
