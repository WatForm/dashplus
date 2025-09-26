open util/boolean

one sig stringLiteral {
	value: String
} {
	value = "this is a string literal"
}

fact {
	stringLiteral.value = "this is a string literal"
}

one sig S {
    val: one String
} {
    val = "STRING"
}

one sig N {
    n: one Int
} {
    n in Int
    n >= min and n <= max
}

fact {
    isTrue[True]
    isFalse[False]
}

fact {
    all i: Int | i->i in iden
    all i: Int | i->(i+1) in next
}

fact {
    no none
    some univ
}

run {} for 5

