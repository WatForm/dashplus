fact {
	a until b
	a since b
	a triggered b
	a releases b
}

fact {
	a until let b = c | b
	a since let b = c | b
	a triggered some x:X | x
	a releases some x:X | x
}
