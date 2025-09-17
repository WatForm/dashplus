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

fun myPlus(a: Int, b: Int): Int {
	a.plus[b]
}

fun myPlus2(a: Int, b:Int): Int {
	let x=a, y=b {x.plus[y]}
}

fact {
	let x=n1.value, y=n1.value | x.myPlus[y] = myPlus2[x,y]
}

