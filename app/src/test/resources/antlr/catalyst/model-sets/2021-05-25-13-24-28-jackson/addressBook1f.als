module tour/addressBook1f ----- Page 12

sig Name, Addr { }

sig Book {
	addr: Name -> lone Addr
}

pred add [b, bPrime: Book, n: Name, a: Addr] {
	bPrime.addr = b.addr + n->a
}

pred showAdd [b, bPrime: Book, n: Name, a: Addr] {
	add [b, bPrime, n, a]
	#Name.(bPrime.addr) > 1
}

// This command generates an instance similar to Fig 2.5
run showAdd for 3 but 2 Book
