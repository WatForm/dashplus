module appendixA/closure_koen

pred transClosure' [	r, R:  univ -> univ, C: univ -> univ -> univ ] {
	R = {x, y: univ | x -> y -> y in C or x = y}
	all x, y, z, u: univ {
		x -> x -> y not in C
		x -> y -> u in C and y -> z -> u in C implies x -> z -> u in C
		x -> y -> y in C and y -> z -> z in C and x != z implies x -> z -> z in C
		x -> y in r and x != y implies x -> y -> y in C
		x -> y -> y in C implies some v: univ | x -> v in r and x -> v -> y in C
		x -> y -> z in C and y != z implies y -> z -> z in C
		}
	}

check {
	all r, R:  univ -> univ, C: univ -> univ -> univ | transClosure' [r, R, C] implies R = *r
	}

