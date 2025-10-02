# remaining issues:

# parser-visitor:
if then else cases; almost like reparsing it

# well-formedness check
decide if qnames are builtins like 'plus' 'True'; check if they are overriden by user; perform checks
typecheck: see formula when expecting formula (prob use the the operators can distinguish, but not ite, let and join&join)
comprehensionValue: decl cannot contain multiplicity words 288
create LONEOF, ONEOF, SOMEOF, SETOF from cardinality, or maybe don't need to
circularity
declarations
arity
transitive closure

// This is parsed, but fails typechecking
// This must be a unary set. Instead, its type is 
// {seq/Int->this/A}
//pred alwaysTrue3 {
// 	{t:Bool, sequence: seq A | t.isTrue}.isTrue
//}

// This is parsed, but fails typechecking
// This must be a unary set. Instead, its type is 
// {seq/Int->this/A}
// fun someSum2 []: Int {
//	(sum a:Int, b:seq A | a fun/add b )
// }

`
StringBuilder sb = new StringBuilder("This cannot be a legal relational join where\nleft hand side is ");
left.toString(sb, -1);
sb.append(" (type = ").append(left.type).append(")\nright hand side is ");
right.toString(sb, -1);
sb.append(" (type = ").append(right.type).append(")\n");
errors = errors.make(new ErrorType(pos, sb.toString()));
`

`
return new ExprBad(p, toString(), new ErrorType(p, "Macro substitution too deep; possibly indicating an infinite recursion."));
`

`
throw new ErrorType(field.pos, "Two overlapping signatures cannot have\n" + "two fields with the same name \"" + field.label + "\":\n\n1) one is in sig \"" + field.sig + "\"\n" + field.pos + "\n\n2) the other is in sig \"" + field2.sig + "\"\n" + field2.pos);
ex:
sig s {
	value: Int
}
sig sExtended extends s {
	value:Bool
}
`




# changes (some WIP)
- optional ( qname '.' ) in 'funDecl' and 'predDecl'
- optional qnames inside [] in importDecl
- optional params in box rule of 'expr'
- accept '<=' for less or equal to; not documented in book, but CUP accepts it
- accept trailing commas in 'sigDecl' and 'arguments'
- added tokens for arithmatic operations, ExprConstant (Alloy AST), WIP, so they are not just 'qname'
- added the 'until' keyword to the temporal operators
- added bit shift operators
- added tokens for 'none', 'univ', 'iden', 'fun/min', 'fun/max', 'fun/next', STRING_LITERAL,
                    'int', 'Int', 'steps'
- added tokens for 'pred/totalOrder' and 'disj' as a predCall list, not just qname
- added 'seq' expr as a rule (https://alloytools.org/quickguide/seq.html)
- removed the two 'boxValue' rules in 'value'

- separated the different uses of the 'multiplicity' keywords, so it's clear what they are being used for from context
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
- replaced ('expr' 'qname' 'expr') 
    - changed to (expr ('fun/mul' | 'fun/div' | 'fun/rem') expr) and (expr ('+' | '-' | 'fun/add' | 'fun/sub') expr)
    - placed them in the correct order of precedence


# todo
- enum see dash-testing

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

precedence:
group rules together

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


