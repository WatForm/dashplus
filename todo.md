TODO:
group rules together
enum decl is not used

According to book, 
3 types of expr: relational expr, boolean expr, integer expr
rost operators apply only to expr type with exceptions: conditional construct, let, join&box

- IMPLIES is not used

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
