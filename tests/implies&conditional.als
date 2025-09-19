one sig n1 {value: Int} {
	value = 1
}

one sig n2 {value: Int} {
	value = 0
}

pred true {
	n1.value = 1
}

pred false {
	n1.value = 0
}

pred huh {
	((true) => n2.value+n2.value else n2.value) < f + f
}

fact {
	(huh) => true else false
	f = 1
	true => true => true else false
}

fun f: Int {
	true => n1.value else n2.value
}


