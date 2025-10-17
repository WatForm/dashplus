fact {
	a << b
	a >>> b
	a >> b
}

fact {
	a << let b=c | b
	a >>> let b=c | b
	a >> let b=c | b
}
