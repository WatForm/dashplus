fact {
	t=>f else t
	t=>t=>f else t
	f=>t else f => f else t
	t=>t=>f else t => t else f
}

fact {
	true => {true true=>true else true} else {true true}
}

fact {
	true => {
		true
		true => {
			true
		} else {
			false
		}
	} else {
		false
		true => {
			true
		} else {
			false
		}
	}
}

