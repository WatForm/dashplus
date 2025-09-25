open util/boolean
open util/integer 
open util/traces[DshSnapshot] as DshSnapshot

sig s1,s2  {
	value: Int
}

enum DoorStatus {CLOSED, OPENING, OPEN, CLOSING}

let indices[n1,n2]= { 0 + 1 + 2 + 3 + 4 + 5 + 6 }

abstract sig DshStates {}
abstract sig Transitions {}
sig DshSnapshot {
  dsh_conf0: set DshStates,
  dsh_taken0: set Transitions
}

sig s {
	a: Int,
}

pred p (a:Bool, b:Bool,) {
	isTrue[True]
}

pred p2 [a:Bool, b:Bool,] {
	isTrue[True]
}

pred alwaysTrue {
	let t=True, f=False | t.isTrue and f.isFalse
}

pred alwaysTrue2 {
	some a:Int | a=0
}

pred alwaysTrue3 {
 	{t:Bool | t.isTrue}.isTrue
}

fun someSum []: Int {
	(sum a:Int, b:Int | integer/plus[a,b] )
}

fun someSum2 []: Int {
	(sum a:Int, b:Int | a fun/add b )
}


fact {
	p[True, True]
	alwaysTrue
	alwaysTrue2
	alwaysTrue3
}

assert myAssertion{
	alwaysTrue
}

check myAssertion for 30 DshSnapshot, 2 s1, 2 s2, 3 DshStates, 1 Transitions, 1 s    
check myAssertion for 30 but 1 s, 2 s1 
