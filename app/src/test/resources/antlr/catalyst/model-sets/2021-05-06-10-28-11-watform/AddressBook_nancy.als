/* Authors: Sabria Farheen, Nancy A. Day, Amirhossein Vakili, Ali Abbassi
 * Date: October 1, 2017
 */

open ctl[State]

//***********************STATE SPACE*************************//

// Target is any element in a book
abstract sig Target { }
// actual addresses in a book
sig Addr extends Target { }
// name associated with other names or addresses
abstract sig Name extends Target { }
// Alias represents an element mapped to a single other element
// Group represents an element mapped to multiple other elements
sig Alias, Group extends Name { }

sig State {
  // a name can be mapped to other names (aliases or groups) or addresses
  addr: Name set -> set Target  
} 


//*****************STATE CONSTRAINTS********************// 

// constraints for valid states (State), not transitions
pred stateConstraints [s: State] { 
  // there is no name that belongs to the set of targets reachable from the name itself 
  no n:s.addr.Target | n in n.^(s.addr)
  // an Alias in a book can only be mapped to one addr (otherwise it is a Group)
  all a:Alias | a in s.addr.Target implies lone a.(s.addr)
}


//*****************INITIAL STATE CONSTRAINTS********************//

pred init [s: State]  { 
	no s.addr 
}


//***********************TRANSITION CONSTRAINTS/OPERATIONS*************************//

// add an addr mapping (name->target) (name may already exist in book)
pred pre_add [s: State, n: Name, t: Target] {
	t in Addr or some lookup [s, Name&t]
	n->t not in s.addr
}
pred post_add [s, s': State, n: Name, t: Target] {
	s'.addr = s.addr + n->t
}
pred add [s, s': State, n: Name, t: Target] {
	pre_add[s,n,t]
	post_add[s,s',n,t]
}

// del an addr mapping
pred pre_del [s: State, n: Name, t: Target] {
	no s.addr.n or some n.(s.addr) - t
	n->t in s.addr
}
pred post_del [s, s': State, n: Name, t: Target] {
	s'.addr = s.addr - n->t
}
pred del [s, s': State, n: Name, t: Target] {
	pre_del[s,n,t]
	post_del[s,s',n,t]
}


//*****************MODEL DEFINITION********************//

fact modelDefinition {
  // constraints on states (State)
  all s:State | stateConstraints[s]
  // init state constraints
  all s:State | s in initialState iff init[s]
  // only defined transitions are valid 
  all s,s':State| s->s' in nextState iff 
		(some n: Name, t: Target | add [s, s', n, t] or del [s, s', n, t])
  // equality predicate: two states with the same features are equivalent
  all s,s':State|((s.addr=s'.addr) implies s=s')
}


//*****************SIGNIFICANCE AXIOMS********************//
pred initialStateAxiom {
	some s: State | s in initialState
}
pred totalityAxiom {
	all s: State | some s':State | s->s' in nextState
}
pred operationsAxiom {
	// at least one state must satisfy precons of each op
	some s:State | some n: Name, t: Target | pre_add[s,n,t]
	some s:State | some n: Name, t: Target | pre_del[s,n,t]

	// all possible ops from state must exist
	all s:State | some n: Name, t: Target | pre_add[s,n,t] implies some s':State | post_add[s,s',n,t]	
	all s:State | some n: Name, t: Target | pre_del[s,n,t] implies some s':State | post_del[s,s',n,t]
}
pred significanceAxioms {
	initialStateAxiom
	totalityAxiom
	operationsAxiom
}
// increment scope until scope satisfies all preds including significanceAxioms
--run significanceAxioms for 4 but exactly 2 State


//*****************PROPERTIES********************//

// return actual Addr (and not Aliases) mapped to a Name
fun lookup [s: State, n: Name] : set Addr { n.^(s.addr) & Addr }
pred safety [s:State]{
  // all names in books are associated with at least one actual address
  all n: s.addr.Target | some lookup [s,n] 
}
assert MC{
	ctl_mc[ag[{s:State | safety[s]}]]
}
check MC for exactly 10 State, exactly 5 Target

