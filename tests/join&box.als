sig  S {}

pred p1[a: S, b: S] {}

pred p2[a: S, b: S] {
	a.(b.p1)
	a.p[b]
	f[a,b].p[b] // how does alloy handle this
}

