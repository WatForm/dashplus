fact quantificationEx {
	all x : X | x
}

fact {
	no x : X | x
}

fact {
	some x : X | x
}

fact {
	lone x : X | x
}

fact {
	one x : X | x
}

fact {
	sum x : X | x
}

fact {

}

fact {
	all x : X, y : Y | x and y
	all disj x : disj X | x
	all disj x : disj lone X | x
	all disj x : disj one X | x
	all disj x : disj some X | x
	all disj x : disj set X | x
}


