module chapter6/memory/checkCache [Addr, Data]

open chapter6/memory/cacheMemory [Addr, Data] as cache
open chapter6/memory/abstractMemory [Addr, Data] as amemory

fun alpha [c: CacheSystem]: Memory {
	{m: Memory | m.data = c.main ++ c.cache}
	}

// This check should not produce a counterexample
ReadOK: check {
	// introduction of m, mPrime ensures that they exist, and gives witnesses if counterexample
	all c: CacheSystem, a: Addr, d: Data, m: Memory |
		cache/read [c, a, d] and m = alpha [c] => amemory/read [m, a, d]
	}

// This check should not produce a counterexample
WriteOK: check {
	all c, cPrime: CacheSystem, a: Addr, d: Data, m, mPrime: Memory |
		cache/write [c, cPrime, a, d] and m = alpha [c] and mPrime = alpha [cPrime]
 			=> amemory/write [m, mPrime, a, d]
	}

// This check should not produce a counterexample
LoadOK: check {
	all c, cPrime: CacheSystem, m, mPrime: Memory |
		cache/load [c, cPrime] and m = alpha [c] and mPrime = alpha [cPrime] => m = mPrime
	}

// This check should not produce a counterexample
FlushOK: check {
	all c, cPrime: CacheSystem, m, mPrime: Memory |
		cache/flush [c, cPrime] and m = alpha [c] and mPrime = alpha [cPrime] => m = mPrime
	}
