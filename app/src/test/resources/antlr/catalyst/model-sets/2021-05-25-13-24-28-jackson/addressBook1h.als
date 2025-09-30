module tour/addressBook1h ------- Page 14..16

sig Name, Addr { }

sig Book {
	addr: Name -> lone Addr
}

pred show [b: Book] {
	#b.addr > 1
	#Name.(b.addr) > 1
}
run show for 3 but 1 Book

pred add [b, bPrime: Book, n: Name, a: Addr] {
	bPrime.addr = b.addr + n->a
}

pred del [b, bPrime: Book, n: Name] {
	bPrime.addr = b.addr - n->Addr
}

fun lookup [b: Book, n: Name] : set Addr {
	n.(b.addr)
}

pred showAdd [b, bPrime: Book, n: Name, a: Addr] {
	add [b, bPrime, n, a]
	#Name.(bPrime.addr) > 1
}
run showAdd for 3 but 2 Book

assert delUndoesAdd {
	all b, bPrime, bPrimePrime: Book, n: Name, a: Addr |
		no n.(b.addr) and add [b, bPrime, n, a] and del [bPrime, bPrimePrime, n]
		implies
		b.addr = bPrimePrime.addr
}

assert addIdempotent {
	all b, bPrime, bPrimePrime: Book, n: Name, a: Addr |
		add [b, bPrime, n, a] and add [bPrime, bPrimePrime, n, a]
		implies
		bPrime.addr = bPrimePrime.addr
}

assert addLocal {
	all b, bPrime: Book, n, nPrime: Name, a: Addr |
		add [b, bPrime, n, a] and n != n
		implies
		lookup [b, nPrime] = lookup [bPrime, nPrime]
}

// This command should not find any counterexample.
check delUndoesAdd for 3

// This command should not find any counterexample.
check delUndoesAdd for 10 but 3 Book

// This command should not find any counterexample.
check addIdempotent for 3

// This command should not find any counterexample.
check addLocal for 3 but 2 Book
