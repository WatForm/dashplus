

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

fun f: Int {
	n1.value + n1.value
}

pred b [value: Int] {
	value=1
}

fact {
	true => true
	true implies not true || some n1
	t=>t=>f else t
	f=>t else f => f else t
	t=>t=>f else t => t else f
	t=>t=>f else t || some n1
	false <=> true || true
	!true or true
	let t=true, f=false | !t and f => t && t <=> t=>t=>f else t || some n1
	let t=true, f=false | ((((!t and f) => (t && t)) <=> (t=>(t=>f else t))) || (some n1))
	let t=true, f=false | !t and f => t && t <=> some n1=>t=>f else t || some n1

}

fact {
	t => t
	t => t => t
	t => t else t
	t => t else t => t
}

fact {
	t else t
}


