module tour/addressBook1g ----- Page 14

sig Name, Addr { }

sig Book {
	addr: Name -> lone Addr
}

pred add [b, bPrime: Book, n: Name, a: Addr] {
	bPrime.addr = b.addr + n->a
}

pred del [b, bPrime: Book, n: Name] {
	bPrime.addr = b.addr - n->Addr
}

fun lookup [b: Book, n: Name] : set Addr {
	n.(b.addr)
}

assert delUndoesAdd {
	all b, bPrime, bPrimePrime: Book, n: Name, a: Addr |
		add [b, bPrime, n, a] and del [bPrime, bPrimePrime, n]
		implies
		b.addr = bPrimePrime.addr
}

// This command generates an instance similar to Fig 2.6
check delUndoesAdd for 3
