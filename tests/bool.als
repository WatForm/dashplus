sig Binary {
  value: one Int
} {
  value = 0 or value = 1
}

pred isTrue[b: Binary] { b.value = 1 }
pred isFalse[b: Binary] { b.value = 0 }

fact {
	some x, y: Binary | x.isTrue and y.isFalse
}

fact {
	all a, b, c, d, e : Binary |
		(
			(a.isTrue=>b.isTrue else c.isTrue => d.isTrue else e.isTrue) and (a.isTrue=>b.isTrue else (c.isTrue => d.isTrue else e.isTrue))
			or
			!(a.isTrue=>b.isTrue else c.isTrue => d.isTrue else e.isTrue) and !(a.isTrue=>b.isTrue else (c.isTrue => d.isTrue else e.isTrue))
		)
	! all a, b, c, d, e : Binary |
		(
			(a.isTrue=>b.isTrue else c.isTrue => d.isTrue else e.isTrue) and ((a.isTrue=>b.isTrue else c.isTrue) => d.isTrue else e.isTrue)
			or
			!(a.isTrue=>b.isTrue else c.isTrue => d.isTrue else e.isTrue) and !((a.isTrue=>b.isTrue else c.isTrue) => d.isTrue else e.isTrue)
		)
}
