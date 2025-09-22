parser-visitor:
typecheck: see bool when expecting bool
if then else cases; almost like reparsing it
comprehensionValue: decl cannot contain multiplicity words 288
make someof, loneof, etc




what does @, $ do? precedence? 
precedence of prime?




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

=< operator: needed both <= and =<

reorder the rules

arithmatic operations (not just join or box): they are built-ins


a.plus[b] should be parsed as (a.plus)[b]

comprehension is a formula


