module chapter6/memory/abstractMemory [Addr, Data] ----- the model from page 217

sig Memory {
	data: Addr -> lone Data
	}

pred init [m: Memory] {
	no m.data
	}

pred write [m, mPrime: Memory, a: Addr, d: Data] {
	mPrime.data = m.data ++ a -> d
	}

pred read [m: Memory, a: Addr, d: Data] {
	let dPrime = m.data [a] | some dPrime implies d = dPrime
	}

fact Canonicalize {
	no disj m, mPrime: Memory | m.data = mPrime.data
	}

// This command should not find any counterexample
WriteRead: check {
	all m, mPrime: Memory, a: Addr, d1, d2: Data |
		write [m, mPrime, a, d1] and read [mPrime, a, d2] => d1 = d2
	}

// This command should not find any counterexample
WriteIdempotent: check {
	all m, mPrime, m": Memory, a: Addr, d: Data |
		write [m, mPrime, a, d] and write [mPrime, m", a, d] => mPrime = m"
	}
