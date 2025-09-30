/* Authors: Sabria Farheen, Nancy A. Day, Amirhossein Vakili, Ali Abbassi
 * Date: October 1, 2017
 */

open ctl[State]
open util/boolean

//***********************STATE SPACE*************************//

// Feature={CW,CF} is the set of features.
abstract sig Feature{}
one sig CW,CF extends Feature{}

// Each phone number can have some features. 
//If a number has call-forwarding (CF), fw points to forwarded number.
sig PhoneNumber{ 
	feature: set Feature, 
	fw: set PhoneNumber, 
} 
fact { // facts about types (PhoneNumber)
	// any PN can only have 0 or 1 PN as its fw number
	all n:PhoneNumber| lone n.fw
	// CF is a feature of PN only if the PN has a fw number set
	all n:PhoneNumber| CF in n.feature iff some n.fw
	// no number is forwarded to itself thru other numbers	
	no (iden & (^fw))  
}

// Used to model the global states.
sig State{
	// Numbers that are idle,
	idle: set PhoneNumber,
	// (a->b) in busy iff a wants to talk to b, but b is not idle
	busy: PhoneNumber -> PhoneNumber,
	// (a->b) in calling iff a is trying to call b
	calling: PhoneNumber -> PhoneNumber,
	// (a->b) in talking iff a is talking to b
	talkingTo: PhoneNumber -> PhoneNumber,
	// (a->b) in waitingFor iff a is waiting for b
	waitingFor: PhoneNumber -> PhoneNumber,
	// (a->b) in forwardedTo iff a is forwarded to b
	forwardedTo: PhoneNumber -> PhoneNumber
}


//*****************INITIAL STATE CONSTRAINTS********************//

pred initial[s:State]{
	s.idle = PhoneNumber
	no s.calling
	no s.talkingTo
	no s.busy
	no s.waitingFor
	no s.forwardedTo
}

//*****************TRANSITION CONSTRAINTS/OPERATIONS********************//

pred pre_idle_calling[s: State,n,nPrime:PhoneNumber]{
	n in s.idle
	n != nPrime	
}
pred post_idle_calling[s,sPrime: State,n,nPrime:PhoneNumber]{
	sPrime.idle = ((s.idle) - n)
	sPrime.calling = s.calling + (n->nPrime)	

	sPrime.talkingTo = s.talkingTo
	sPrime.busy = s.busy
	sPrime.waitingFor = s.waitingFor
	sPrime.forwardedTo = s.forwardedTo
}
pred idle_calling[s,sPrime: State,n,nPrime:PhoneNumber]{
	pre_idle_calling[s,n,nPrime]	
	post_idle_calling[s,sPrime,n,nPrime]
}


pred pre_calling_talkingTo[s:State,n,nPrime:PhoneNumber]{
	n->nPrime in s.calling
	nPrime in s.idle
}
pred post_calling_talkingTo[s,sPrime:State,n,nPrime:PhoneNumber]{
	sPrime.idle = s.idle - nPrime
	sPrime.calling = s.calling - (n -> nPrime)
	sPrime.talkingTo = s.talkingTo + (n -> nPrime)

	sPrime.busy = s.busy
	sPrime.waitingFor = s.waitingFor
	sPrime.forwardedTo = s.forwardedTo
}
pred calling_talkingTo[s,sPrime:State,n,nPrime:PhoneNumber]{
	pre_calling_talkingTo[s,n,nPrime]
	post_calling_talkingTo[s,sPrime,n,nPrime]
}

pred pre_talkingTo_idle[s:State,n,nPrime:PhoneNumber]{
	n -> nPrime in s.talkingTo
}
pred post_talkingTo_idle[s,sPrime:State,n,nPrime:PhoneNumber]{
	sPrime.talkingTo = s.talkingTo - (n->nPrime)
	sPrime.idle = s.idle + (n + nPrime)

	sPrime.busy = s.busy
	sPrime.calling = s.calling
	sPrime.waitingFor = s.waitingFor
	sPrime.forwardedTo = s.forwardedTo
}
pred talkingTo_idle[s,sPrime:State,n,nPrime:PhoneNumber]{
	pre_talkingTo_idle[s,n,nPrime]
	post_talkingTo_idle[s,sPrime,n,nPrime]
}

pred pre_calling_busy[s:State,n,nPrime:PhoneNumber]{
	n->nPrime in s.calling
	nPrime not in s.idle
}
pred post_calling_busy[s,sPrime:State,n,nPrime:PhoneNumber]{
	sPrime.calling = s.calling - (n->nPrime)
	sPrime.busy = s.busy + (n->nPrime)
	
	sPrime.idle = s.idle
	sPrime.talkingTo = s.talkingTo
	sPrime.waitingFor = s.waitingFor
	sPrime.forwardedTo = s.forwardedTo
}
pred calling_busy[s,sPrime:State,n,nPrime:PhoneNumber]{
	pre_calling_busy[s,n,nPrime]
	post_calling_busy[s,sPrime,n,nPrime]
}

pred pre_busy_waitingFor[s:State,n,nPrime:PhoneNumber]{
	(n->nPrime) in s.busy
	CW in nPrime.feature
	// PN is not already being waited for, i.e.,
	// can have only one call in CW queue, otherwise stay busy
	nPrime not in PhoneNumber.(s.waitingFor)
}
pred post_busy_waitingFor[s,sPrime:State,n,nPrime:PhoneNumber]{
	sPrime.busy = s.busy - (n->nPrime)
	sPrime.waitingFor = s.waitingFor + (n->nPrime)

	sPrime.forwardedTo = s.forwardedTo	
	sPrime.idle = s.idle
	sPrime.calling = s.calling
	sPrime.talkingTo = s.talkingTo
}
pred busy_waitingFor[s,sPrime:State,n,nPrime:PhoneNumber]{
	pre_busy_waitingFor[s,n,nPrime]
	post_busy_waitingFor[s,sPrime,n,nPrime]
}

// caller on CW hangs up
pred pre_waitingFor_idle[s:State,n,nPrime:PhoneNumber]{
	n -> nPrime in s.waitingFor
}
pred post_waitingFor_idle[s,sPrime:State,n,nPrime:PhoneNumber]{	
	sPrime.waitingFor = s.waitingFor - (n -> nPrime)	
	sPrime.idle = s.idle + n

	sPrime.calling = s.calling
	sPrime.talkingTo = s.talkingTo
	sPrime.busy = s.busy
	sPrime.forwardedTo = s.forwardedTo
}
pred waitingFor_idle[s,sPrime:State,n,nPrime:PhoneNumber]{
	pre_waitingFor_idle[s,n,nPrime]
	post_waitingFor_idle[s,sPrime,n,nPrime]
}

pred pre_waitingFor_talkingTo[s:State,n,nPrime:PhoneNumber]{
	n -> nPrime in s.waitingFor
}
pred post_waitingFor_talkingTo[s,sPrime:State,n,nPrime:PhoneNumber]{
	sPrime.waitingFor = s.waitingFor - (n -> nPrime)
	sPrime.talkingTo = s.talkingTo + (n -> nPrime)

	sPrime.idle = s.idle 
	s.busy = sPrime.busy
	s.forwardedTo = sPrime.forwardedTo
	s.calling = sPrime.calling
}
pred waitingFor_talkingTo[s,sPrime:State,n,nPrime:PhoneNumber]{
	pre_waitingFor_talkingTo[s,n,nPrime]
	post_waitingFor_talkingTo[s,sPrime,n,nPrime]
}

pred pre_busy_forwardedTo[s:State,n,nPrime:PhoneNumber]{
	n -> nPrime in s.busy
	CF in nPrime.feature
}
pred post_busy_forwardedTo[s,sPrime:State,n,nPrime:PhoneNumber]{
	sPrime.busy = s.busy - (n -> nPrime)
	sPrime.forwardedTo = s.forwardedTo + (n -> nPrime.fw)

	sPrime.idle = s.idle
	sPrime.talkingTo = s.talkingTo
	sPrime.calling = s.calling 
	sPrime.waitingFor = s.waitingFor
}
pred busy_forwardedTo[s,sPrime:State,n,nPrime:PhoneNumber]{
	pre_busy_forwardedTo[s,n,nPrime]
	post_busy_forwardedTo[s,sPrime,n,nPrime]
}

pred pre_forwardedTo_calling[s:State,n,nPrime:PhoneNumber]{
	n -> nPrime in s.forwardedTo
}
pred post_forwardedTo_calling[s,sPrime:State,n,nPrime:PhoneNumber]{
	sPrime.forwardedTo = s.forwardedTo - (n->nPrime)
	sPrime.calling = s.calling + (n -> nPrime)

	sPrime.idle = s.idle
	sPrime.busy = s.busy
	sPrime.talkingTo = s.talkingTo
	sPrime.waitingFor = s.waitingFor
}
pred forwardedTo_calling[s,sPrime:State,n,nPrime:PhoneNumber]{
	pre_forwardedTo_calling[s,n,nPrime]
	post_forwardedTo_calling[s,sPrime,n,nPrime]
}

pred pre_busy_idle[s:State,n,nPrime:PhoneNumber]{
	n -> nPrime in s.busy
	no nPrime.feature
}
pred post_busy_idle[s,sPrime:State,n,nPrime:PhoneNumber]{
	sPrime.busy = s.busy - (n -> nPrime)
	sPrime.idle = s.idle + n

	s.talkingTo = sPrime.talkingTo
	s.waitingFor = sPrime.waitingFor
	s.forwardedTo = sPrime.forwardedTo
	s.calling = sPrime.calling
}
pred busy_idle[s,sPrime:State,n,nPrime:PhoneNumber]{
	pre_busy_idle[s,n,nPrime]
	post_busy_idle[s,sPrime,n,nPrime]
}


//*****************MODEL DEFINITION********************//

fact md{
	// init state constraint
	all s:State | s in initialState iff initial[s]	
	// transition constraints
	all s,sPrime: State| 
		((s->sPrime) in nextState) iff
		(some n,nPrime:PhoneNumber|(
			idle_calling[s,sPrime,n,nPrime] or calling_talkingTo[s,sPrime,n,nPrime] or talkingTo_idle[s,sPrime,n,nPrime] or
			calling_busy[s,sPrime,n,nPrime] or busy_waitingFor[s,sPrime,n,nPrime] or busy_forwardedTo[s,sPrime,n,nPrime] or
			busy_idle[s,sPrime,n,nPrime] or waitingFor_idle[s,sPrime,n,nPrime] or waitingFor_talkingTo[s,sPrime,n,nPrime] or
			forwardedTo_calling[s,sPrime,n,nPrime]))
	// equality predicate: states are records
	all s,sPrime:State|(
		((s.idle = sPrime.idle) and (s.calling = sPrime.calling) and 
		(s.talkingTo = sPrime.talkingTo) and (s.busy = sPrime.busy) and
		(s.waitingFor = sPrime.waitingFor) and (s.forwardedTo = sPrime.forwardedTo)) implies (s =sPrime))
}

//*****************SIGNIFICANCE AXIOMS********************//
pred initialStateAxiom {
	some s: State | s in initialState
}
pred totalityAxiom {
	all s: State | some sPrime:State | s->sPrime in nextState
}
pred operationsAxiom {
	// at least one state must satisfy precons of each op
	some s:State | some n,nPrime:PhoneNumber | pre_idle_calling[s,n,nPrime]
	some s:State | some n,nPrime:PhoneNumber | pre_calling_talkingTo[s,n,nPrime]
	some s:State | some n,nPrime:PhoneNumber | pre_talkingTo_idle[s,n,nPrime]
	some s:State | some n,nPrime:PhoneNumber | pre_calling_busy[s,n,nPrime]
	some s:State | some n,nPrime:PhoneNumber | pre_busy_waitingFor[s,n,nPrime]
	some s:State | some n,nPrime:PhoneNumber | pre_busy_forwardedTo[s,n,nPrime]
	some s:State | some n,nPrime:PhoneNumber | pre_busy_idle[s,n,nPrime]
	some s:State | some n,nPrime:PhoneNumber | pre_waitingFor_idle[s,n,nPrime]
	some s:State | some n,nPrime:PhoneNumber | pre_waitingFor_talkingTo[s,n,nPrime]
	some s:State | some n,nPrime:PhoneNumber | pre_forwardedTo_calling[s,n,nPrime]
	// all possible ops from state must exist
	all s:State | some n,nPrime:PhoneNumber | pre_idle_calling[s,n,nPrime] implies some sPrime:State | post_idle_calling[s,sPrime,n,nPrime]
	all s:State | some n,nPrime:PhoneNumber | pre_calling_talkingTo[s,n,nPrime] implies some sPrime:State | post_calling_talkingTo[s,sPrime,n,nPrime]
	all s:State | some n,nPrime:PhoneNumber | pre_talkingTo_idle[s,n,nPrime] implies some sPrime:State | post_talkingTo_idle[s,sPrime,n,nPrime]
	all s:State | some n,nPrime:PhoneNumber | pre_calling_busy[s,n,nPrime] implies some sPrime:State | post_calling_busy[s,sPrime,n,nPrime]
	all s:State | some n,nPrime:PhoneNumber | pre_busy_waitingFor[s,n,nPrime] implies some sPrime:State | post_busy_waitingFor[s,sPrime,n,nPrime]
	all s:State | some n,nPrime:PhoneNumber | pre_busy_forwardedTo[s,n,nPrime] implies some sPrime:State | post_busy_forwardedTo[s,sPrime,n,nPrime]
	all s:State | some n,nPrime:PhoneNumber | pre_busy_idle[s,n,nPrime] implies some sPrime:State | post_busy_idle[s,sPrime,n,nPrime]
	all s:State | some n,nPrime:PhoneNumber | pre_waitingFor_idle[s,n,nPrime] implies some sPrime:State | post_waitingFor_idle[s,sPrime,n,nPrime]
	all s:State | some n,nPrime:PhoneNumber | pre_waitingFor_talkingTo[s,n,nPrime] implies some sPrime:State | post_waitingFor_talkingTo[s,sPrime,n,nPrime]
	all s:State | some n,nPrime:PhoneNumber | pre_forwardedTo_calling[s,n,nPrime] implies some sPrime:State | post_forwardedTo_calling[s,sPrime,n,nPrime]
}
pred significanceAxioms {
	initialStateAxiom
	totalityAxiom
	operationsAxiom
}
// increment scope until scope satisfies all preds including Sig. Axioms
--run significanceAxioms for exactly 6 State, exactly 4 PhoneNumber

//*****************PROPERTIES/CHECK********************//
pred safety [s:State] {
	// no PN is both being waited for and being forwarded to
	no s.waitingFor.PhoneNumber & s.forwardedTo.PhoneNumber
}
assert MC { ctl_mc[ag[{s:State | safety[s]}]] }
check MC for exactly 6 State, exactly 4 PhoneNumber
