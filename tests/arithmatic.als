one sig n1 {value: Int} {
	value = 3
}
one sig n2 {value: Int} {
	value = 4
}
one sig n3 {value: Int} {
//	value = plus[n1.value, n2.value]
	value = myPlus[n1.value, n2.value]
}

fun myPlus(a: Int, b:Int): Int {
	a.(b.plus)
	// you can call plus like this
}

fun myPlus2(a: Int, b: Int): Int {
	a.plus[b]
}

fact {
	n1.value + n2.value <= n3.value // implicitly sums({n1.value, n2.value}) <= sum({n3.value})
	n1.value + n2.value >= n3.value // implicitly sums({n1.value, n2.value}) <= sum({n3.value})
	not (n1.value + n2.value = n3.value) 
	// '=' IS NOT OVERLOADED, it does set comparison
	// if not is removed it won't find instance, b/c we try to compare set with two values to set with one value
}

run {} for 5 int
