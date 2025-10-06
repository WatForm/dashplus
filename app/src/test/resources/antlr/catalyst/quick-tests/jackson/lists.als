module chapter5/lists ---- page 157

some sig Element {}

abstract sig List {}
one sig EmptyList extends List {}
sig NonEmptyList extends List {
	element: Element,
	rest: List
	}

fact ListGenerator {
	all list: List, e: Element |
		some listPrime: List | listPrime.rest = list and listPrime.element = e
	}

assert FalseAssertion {
	all list: List | list != list
	}

// This check finds no counterexample since
// the only possible counterexamples are infinite.
check FalseAssertion
