module chapter6/memory/cacheMemory [Addr, Data] ----- the model from page 219

sig CacheSystem {
	main, cache: Addr -> lone Data
	}

pred init [c: CacheSystem] {
	no c.main + c.cache
	}

pred write [c, cPrime: CacheSystem, a: Addr, d: Data] {
	cPrime.main = c.main
	cPrime.cache = c.cache ++ a -> d
	}

pred read [c: CacheSystem, a: Addr, d: Data] {
	some d
	d = c.cache [a]
	}

pred load [c, cPrime: CacheSystem] {
	some addrs: set c.main.Data - c.cache.Data |
		cPrime.cache = c.cache ++ addrs <: c.main
	cPrime.main = c.main
	}

pred flush [c, cPrime: CacheSystem] {
	some addrs: some c.cache.Data {
		cPrime.main = c.main ++ addrs <: c.cache
		cPrime.cache = c.cache - addrs -> Data
		}
	}

// This command should not find any counterexample
LoadNotObservable: check {
	all c, cPrime, c": CacheSystem, a1, a2: Addr, d1, d2, d3: Data |
		{
		read [c, a2, d2]
		write [c, cPrime, a1, d1]
		load [cPrime, c"]
		read [c", a2, d3]
		} implies d3 = (a1=a2 => d1 else d2)
	}
