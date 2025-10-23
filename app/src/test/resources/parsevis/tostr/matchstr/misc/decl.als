fact {
	{a : A}
	{a1,a2,a3 : A}
	{disj a : A}
	{a : disj A}
	{var private disj a : disj lone A}
	{var private disj a : disj one A}
	{var private disj a : disj some A}
	{var private disj a : disj set A}
}

fact {
	let a1=a3, a2 = a3 | a1
}
