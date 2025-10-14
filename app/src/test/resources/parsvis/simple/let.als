// bind			: LET assignment ( COMMA assignment )* body 	# let
// assignment	: name EQUAL expr1 ;

fact letEx {
	let a = A {a}
}

fact {
	let a = A | a
}

fact {
	let a = A, b = B { a and b }
}
