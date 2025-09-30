/* Authors: Sabria Farheen, Nancy A. Day, Amirhossein Vakili, Ali Abbassi
 * Date: October 1, 2017
 */

open util/integer
open util/boolean
open ctlfc_path[State]

//***********************STATE SPACE*************************//

abstract sig Counter{}
one sig f0, f1, f2, f3 extends Counter{}

abstract sig Sense{}
one sig N_Sense, S_Sense, E_Sense extends Sense{}

// Go is for modeling which direction is allowed to go
abstract sig Go{}
one sig N_Go, S_Go, E_Go extends Go{}

// Request is to latch the traffic sensors
abstract sig Request{}
one sig N_Req, S_Req, E_Req extends Request{}

sig State{
	sensors: set Sense,
	goes: set Go,  
	req: set Request,
	NS_Lock: Bool, // NS_Lock is true iff East is not allowed to go
	// counter for fairness
	counter: set Counter
}

//***********************INITIAL STATE CONSTRAINT*************************//

pred initial[s:State]{
	!no s.sensors
	no s.goes
	no s.req
	s.NS_Lock = False
	s.counter = f0
}
//***********************TRANSITION CONSTRAINTS/OPERATIONS*************************//

// Predicates for N_Go
pred N_Go_True[s:State]{
	N_Req in s.req
	N_Go !in s.goes
	E_Req !in s.req
}
pred N_Go_False[s:State]{
	N_Go in s.goes
	N_Sense !in s.sensors
}

pred pre_N_Go[s:State]{
	N_Go_True[s]
}
pred pre_N_Not_Go[s:State]{
	!N_Go_True[s] and N_Go_False[s]
}
pred pre_N_Go_Unchanged[s:State]{
	!N_Go_True[s] and !N_Go_False[s]
}

pred N_Go_[s,s':State]{
	pre_N_Go[s] 
	N_Go in s'.goes 
}
pred N_Not_Go[s,s':State]{
	pre_N_Not_Go[s] 
	N_Go !in s'.goes
}
pred N_Go_Unchanged[s,s':State]{
	pre_N_Go_Unchanged[s] 
	N_Go in s'.goes iff N_Go in s.goes
}


// Predicates for S_Go
pred S_Go_True[s:State]{
	S_Req in s.req
	S_Go !in s.goes
	E_Req !in s.req
}
pred S_Go_False[s:State]{
	S_Go in s.goes
	S_Sense !in s.sensors
}

pred pre_S_Go[s:State]{
	S_Go_True[s]
}
pred pre_S_Not_Go[s:State]{
	!S_Go_True[s] and S_Go_False[s]
}
pred pre_S_Go_Unchanged[s:State]{
	!S_Go_True[s] and !S_Go_False[s]
}

pred S_Go_[s,s':State]{
	pre_S_Go[s] 
	S_Go in s'.goes 
}
pred S_Not_Go[s,s':State]{
	pre_S_Not_Go[s] 
	S_Go !in s'.goes
}
pred S_Go_Unchanged[s,s':State]{
	pre_S_Go_Unchanged[s] 
	S_Go in s'.goes iff S_Go in s.goes
}


// Predicates for E_Go
pred E_Go_True[s:State]{
	E_Req in s.req
	E_Go !in s.goes
	s.NS_Lock = False
}
pred E_Go_False[s:State]{
	E_Go in s.goes
	E_Sense !in s.sensors
}

pred pre_E_Go[s:State]{
	E_Go_True[s]
}
pred pre_E_Not_Go[s:State]{
	!E_Go_True[s] and E_Go_False[s]
}
pred pre_E_Go_Unchanged[s:State]{
	!E_Go_True[s] and !E_Go_False[s]
}

pred E_Go_[s,s':State]{
	pre_E_Go[s] 
	E_Go in s'.goes 
}
pred E_Not_Go[s,s':State]{
	pre_E_Not_Go[s] 
	E_Go !in s'.goes
}
pred E_Go_Unchanged[s,s':State]{
	pre_E_Go_Unchanged[s] 
	E_Go in s'.goes iff E_Go in s.goes
}

// Predicates for N_Req
pred N_Req_True[s:State]{
	N_Sense in s.sensors
}
pred N_Req_False[s:State]{
	N_Go_False[s]
}

pred pre_N_Req[s:State]{
	N_Req_True[s]
}
pred pre_N_Not_Req[s:State]{
	!N_Req_True[s] and N_Req_False[s]
}
pred pre_N_Req_Unchanged[s:State]{
	!N_Req_True[s] and !N_Req_False[s]
}

pred N_Req_[s,s':State]{
	pre_N_Req[s] 
	N_Req in s'.req 
}
pred N_Not_Req[s,s':State]{
	pre_N_Not_Req[s] 
	N_Req !in s'.req
}
pred N_Req_Unchanged[s,s':State]{
	pre_N_Req_Unchanged[s]
	N_Req in s'.req iff N_Req in s.req
}


// Predicates for S_Req
pred S_Req_True[s:State]{
	S_Sense in s.sensors
}
pred S_Req_False[s:State]{
	S_Go_False[s]
}

pred pre_S_Req[s:State]{
	S_Req_True[s]
}
pred pre_S_Not_Req[s:State]{
	!S_Req_True[s] and S_Req_False[s]
}
pred pre_S_Req_Unchanged[s:State]{
	!S_Req_True[s] and !S_Req_False[s]
}

pred S_Req_[s,s':State]{
	pre_S_Req[s] 
	S_Req in s'.req 
}
pred S_Not_Req[s,s':State]{
	pre_S_Not_Req[s] 
	S_Req !in s'.req
}
pred S_Req_Unchanged[s,s':State]{
	pre_S_Req_Unchanged[s] 
	S_Req in s'.req iff S_Req in s.req
}

// Predicates for E_Req
pred E_Req_True[s:State]{
	E_Sense in s.sensors
}
pred E_Req_False[s:State]{
	E_Go_False[s]
}

pred pre_E_Req[s:State]{
	E_Req_True[s]
}
pred pre_E_Not_Req[s:State]{
	!E_Req_True[s] and E_Req_False[s]
}
pred pre_E_Req_Unchanged[s:State]{
	!E_Req_True[s] and !E_Req_False[s]
}

pred E_Req_[s,s':State]{
	pre_E_Req[s] 
	E_Req in s'.req 
}
pred E_Not_Req[s,s':State]{
	pre_E_Not_Req[s] 
	E_Req !in s'.req
}
pred E_Req_Unchanged[s,s':State]{
	pre_E_Req_Unchanged[s] 
	E_Req in s'.req iff E_Req in s.req
}

// Predicates for NS_Lock
pred NS_Lock_True[s:State]{
	N_Go_True[s] or S_Go_True[s]
}
pred NS_Lock_False[s:State]{
	(N_Go_False [s] and S_Go !in s.goes) or (S_Go_False [s] and N_Go !in s.goes)
}

pred pre_NS_Lock[s:State]{
	NS_Lock_True[s]
}
pred pre_NS_Not_Lock[s:State]{
	!NS_Lock_True[s] and NS_Lock_False[s]
}
pred pre_NS_Lock_Unchanged[s:State]{
	!NS_Lock_True[s] and !NS_Lock_False[s]
}

pred NS_Lock_[s,s':State]{
	pre_NS_Lock[s] 
	s'.NS_Lock = True
}
pred NS_Not_Lock[s,s':State]{
	pre_NS_Not_Lock[s] 
	s'.NS_Lock = False 
}
pred NS_Lock_Unchanged[s,s':State]{
	pre_NS_Lock_Unchanged[s] 
	s'.NS_Lock=s.NS_Lock
}


//***********************FAIRNESS CONSTRAINTS*************************//
// Modeling fairness constraints
fun N_fair[]:State{ 
	State - (sensors.N_Sense & goes.N_Go)
}
fun S_fair[]:State{ 
	State - (sensors.S_Sense & goes.S_Go)
}
fun E_fair[]:State{ 
	State - (sensors.E_Sense & goes.E_Go)
}

// combines 3 fcs into 1 fc by checking that all 3 fcs occur infinitely often thru a counter
fact fairness {
	all s,s':State | s->s' in nextState implies (  (s in N_fair[] and s.counter=f0) implies s'.counter=f1 else
		(s in S_fair[] and s.counter=f1) implies s'.counter=f2 else
		(s in E_fair[] and s.counter=f2) implies s'.counter=f3 else 
		s.counter=f3 implies s'.counter=f0 else 
		s'.counter=s.counter)
	// don't have to keep track of immediate next state fcs when counter=f3
	// because it doesn't matter in the context of infinitely often  
}
pred fair[s:State] {
	s.counter = f3
}


//***********************MODEL DEFINITION*************************//

fact md{
	// init state constraints
	all s:State| initial[s] iff (s in initialState)
	// transition constraints
	all s,s':State| s->s' in nextState iff (
		N_Go_[s,s'] or
		N_Not_Go[s,s'] or
		N_Go_Unchanged[s,s'] or 
		S_Go_[s,s'] or
		S_Not_Go[s,s'] or
		S_Go_Unchanged[s,s'] or
		E_Go_[s,s'] or
		E_Not_Go[s,s'] or
		E_Go_Unchanged[s,s'] or

		N_Req_[s,s'] or
		N_Not_Req[s,s'] or
		N_Req_Unchanged[s,s'] or
		S_Req_[s,s'] or
		S_Not_Req[s,s'] or
		S_Req_Unchanged[s,s'] or
		E_Req_[s,s'] or
		E_Not_Req[s,s'] or
		E_Req_Unchanged[s,s'] or

		NS_Lock_[s,s'] or
		NS_Not_Lock[s,s'] or
		NS_Lock_Unchanged[s,s']
	)
	// fairness constraints
	all s:State | s in fc iff fair[s]
	// equality predicate: states are records
	all s,s':State| (s.sensors = s'.sensors and s.goes = s'.goes and s.req = s'.req and s.NS_Lock = s'.NS_Lock) implies s = s'
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
	some s:State | pre_N_Go[s]
	some s:State | pre_N_Not_Go[s]
	some s:State | pre_N_Go_Unchanged[s]
	some s:State | pre_S_Go[s]
	some s:State | pre_S_Not_Go[s]
	some s:State | pre_S_Go_Unchanged[s] 
	some s:State | pre_E_Go[s]
	some s:State | pre_E_Not_Go[s]
	some s:State | pre_E_Go_Unchanged[s]

 	some s:State | pre_N_Req[s]
	some s:State | pre_N_Not_Req[s]
	some s:State | pre_N_Req_Unchanged[s]
	some s:State | pre_S_Req[s]
	some s:State | pre_S_Not_Req[s]
	some s:State | pre_S_Req_Unchanged[s]
	some s:State | pre_E_Req[s]
	some s:State | pre_E_Not_Req[s]
	some s:State | pre_E_Req_Unchanged[s]

	some s:State | pre_NS_Lock[s]
	some s:State | pre_NS_Not_Lock[s]
	some s:State | pre_NS_Lock_Unchanged[s]

	// all possible ops from state exist
	all s:State | pre_N_Go[s] implies some s':State | N_Go_[s,s']
	all s:State | pre_N_Not_Go[s] implies some s':State | N_Not_Go[s,s']
	all s:State | pre_N_Go_Unchanged[s] implies some s':State | N_Go_Unchanged[s,s'] 
	all s:State | pre_S_Go[s] implies some s':State | S_Go_[s,s']
	all s:State | pre_S_Not_Go[s] implies some s':State | S_Not_Go[s,s']
	all s:State | pre_S_Go_Unchanged[s] implies some s':State | S_Go_Unchanged[s,s'] 
	all s:State | pre_E_Go[s] implies some s':State | E_Go_[s,s']
	all s:State | pre_E_Not_Go[s] implies some s':State | E_Not_Go[s,s']
	all s:State | pre_E_Go_Unchanged[s] implies some s':State | E_Go_Unchanged[s,s'] 

 	all s:State | pre_N_Req[s] implies some s':State | N_Req_[s,s']
	all s:State | pre_N_Not_Req[s] implies some s':State | N_Not_Req[s,s']
	all s:State | pre_N_Req_Unchanged[s] implies some s':State | N_Req_Unchanged[s,s'] 
	all s:State | pre_S_Req[s] implies some s':State | S_Req_[s,s']
	all s:State | pre_S_Not_Req[s] implies some s':State | S_Not_Req[s,s']
	all s:State | pre_S_Req_Unchanged[s] implies some s':State | S_Req_Unchanged[s,s'] 
	all s:State | pre_E_Req[s] implies some s':State | E_Req_[s,s']
	all s:State | pre_E_Not_Req[s] implies some s':State | E_Not_Req[s,s']
	all s:State | pre_E_Req_Unchanged[s] implies some s':State | E_Req_Unchanged[s,s'] 

	all s:State | pre_NS_Lock[s] implies some s':State | NS_Lock_[s,s']
	all s:State | pre_NS_Not_Lock[s] implies some s':State | NS_Not_Lock[s,s']
	all s:State | pre_NS_Lock_Unchanged[s] implies some s':State | NS_Lock_Unchanged[s,s'] 
}
pred significanceAxioms {
	initialStateAxiom
	totalityAxiom
	operationsAxiom
}
--run significanceAxioms for exactly 15 State

//***********************PROPERTIES*************************//

// safety property
assert MC{
	// light in cross directions never on at same time
	// G ~(E_Go & (N_Go | S_Go))	
	ctlfc_mc[ag[not_[goes.E_Go & goes.(N_Go + S_Go)]]]
}
check MC for exactly 16 State
