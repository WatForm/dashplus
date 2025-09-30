module appendixA/addressBook2

sig Addr, Name { }

sig Book {
	addr: Name -> (Name + Addr)
	}

pred inv [b: Book] {
	let addr = b.addr |
		all n: Name {
			n not in n.^addr
			some addr.n => some n.^addr & Addr
		}
	}

pred add [b, bPrime: Book, n: Name, t: Name+Addr] {
	bPrime.addr = b.addr + n->t
	}

pred del [b, bPrime: Book, n: Name, t: Name+Addr] {
	bPrime.addr = b.addr - n->t
	}

fun lookup [b: Book, n: Name] : set Addr {
	n.^(b.addr) & Addr
	}
