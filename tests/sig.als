sig A {}

sig B {
	a1: A, 
	a2: A,
}

sig D {
	R: A -> B -> C
}

sig D {
	R: A one -> one B -> one C
}
