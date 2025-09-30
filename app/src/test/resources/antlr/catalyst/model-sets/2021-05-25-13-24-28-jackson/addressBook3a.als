module tour/addressBook3a ----- Page 25

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

pred show { }

// This command generates an instance similar to Fig 2.15
run show for 4
