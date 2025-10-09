# parser-visitor:

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
more ...; grep for "new ErrorType"

