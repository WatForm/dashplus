module tour/addressBook3b ----- Page 26

open util/ordering [Book] as BookOrder

abstract sig Target { }
sig Addr extends Target { }
abstract sig Name extends Target { }

sig Alias, Group extends Name { }

sig Book {
	names: set Name,
	addr: names->some Target
} {
	no n: Name | n in n.^addr
	all a: Alias | lone a.addr
}

pred add [b, bPrime: Book, n: Name, t: Target] { bPrime.addr = b.addr + n->t }
pred del [b, bPrime: Book, n: Name, t: Target] { bPrime.addr = b.addr - n->t }
fun lookup [b: Book, n: Name] : set Addr { n.^(b.addr) & Addr }

pred init [b: Book]  { no b.addr }

fact traces {
	init [first]
	all b: Book-last |
	  let bPrime = b.next |
	    some n: Name, t: Target |
	      add [b, bPrime, n, t] or del [b, bPrime, n, t]
}

------------------------------------------------------

assert delUndoesAdd {
	all b, bPrime, bPrimePrime: Book, n: Name, t: Target |
		no n.(b.addr) and add [b, bPrime, n, t] and del [bPrime, bPrimePrime, n, t]
		implies
		b.addr = bPrimePrime.addr
}

// This should not find any counterexample.
check delUndoesAdd for 3

------------------------------------------------------

assert addIdempotent {
	all b, bPrime, bPrimePrime: Book, n: Name, t: Target |
		add [b, bPrime, n, t] and add [bPrime, bPrimePrime, n, t]
		implies
		bPrime.addr = bPrimePrime.addr
}

// This should not find any counterexample.
check addIdempotent for 3

------------------------------------------------------

assert addLocal {
	all b, bPrime: Book, n, nPrime: Name, t: Target |
		add [b, bPrime, n, t] and n != nPrime
		implies
		lookup [b, nPrime] = lookup [bPrime, nPrime]
}

// This should not find any counterexample.
check addLocal for 3 but 2 Book

------------------------------------------------------

assert lookupYields {
	all b: Book, n: b.names | some lookup [b,n]
}

// This shows a counterexample similar to Fig 2.16
check lookupYields for 3 but 4 Book
