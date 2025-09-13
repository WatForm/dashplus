// Cardinality
// as varDecl
sig S1 {
	x: lone S,
	x: some S,
	x: one S,
	x: some S,
	x: S
}

// as arguments
pred p1 [x: lone S] {}
pred p2 [x: some S] {}
pred p3 [x: one S] {}
pred p4 [x: set S, y: S] {}





// CountingQualifier
// countingQuantifierFormula
sig S2 {}

fact {
	no S2
	one S2
	some S2
	lone S2
}




// bindingQuantifier
// bindingQuantifierFormula
sig S3 {}
fact {
	all x : some S3 | x.F
	no x : some S3 | x.F
	some x : some S3 | x.F
	lone x : some S3 | x.F
	one x : some S3 | x.F
}
pred F[x: S3] {}


