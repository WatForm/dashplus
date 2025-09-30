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

pred pre_idle_calling[s: State,n,n':PhoneNumber]{
	n in s.idle
	n != n'	
}
pred post_idle_calling[s,s': State,n,n':PhoneNumber]{
	s'.idle = ((s.idle) - n)
	s'.calling = s.calling + (n->n')	

	s'.talkingTo = s.talkingTo
	s'.busy = s.busy
	s'.waitingFor = s.waitingFor
	s'.forwardedTo = s.forwardedTo
}
pred idle_calling[s,s': State,n,n':PhoneNumber]{
	pre_idle_calling[s,n,n']	
	post_idle_calling[s,s',n,n']
}


pred pre_calling_talkingTo[s:State,n,n':PhoneNumber]{
	n->n' in s.calling
	n' in s.idle
}
pred post_calling_talkingTo[s,s':State,n,n':PhoneNumber]{
	s'.idle = s.idle - n'
	s'.calling = s.calling - (n -> n')
	s'.talkingTo = s.talkingTo + (n -> n')

	s'.busy = s.busy
	s'.waitingFor = s.waitingFor
	s'.forwardedTo = s.forwardedTo
}
pred calling_talkingTo[s,s':State,n,n':PhoneNumber]{
	pre_calling_talkingTo[s,n,n']
	post_calling_talkingTo[s,s',n,n']
}

pred pre_talkingTo_idle[s:State,n,n':PhoneNumber]{
	n -> n' in s.talkingTo
}
pred post_talkingTo_idle[s,s':State,n,n':PhoneNumber]{
	s'.talkingTo = s.talkingTo - (n->n')
	s'.idle = s.idle + (n + n')

	s'.busy = s.busy
	s'.calling = s.calling
	s'.waitingFor = s.waitingFor
	s'.forwardedTo = s.forwardedTo
}
pred talkingTo_idle[s,s':State,n,n':PhoneNumber]{
	pre_talkingTo_idle[s,n,n']
	post_talkingTo_idle[s,s',n,n']
}

pred pre_calling_busy[s:State,n,n':PhoneNumber]{
	n->n' in s.calling
	n' not in s.idle
}
pred post_calling_busy[s,s':State,n,n':PhoneNumber]{
	s'.calling = s.calling - (n->n')
	s'.busy = s.busy + (n->n')
	
	s'.idle = s.idle
	s'.talkingTo = s.talkingTo
	s'.waitingFor = s.waitingFor
	s'.forwardedTo = s.forwardedTo
}
pred calling_busy[s,s':State,n,n':PhoneNumber]{
	pre_calling_busy[s,n,n']
	post_calling_busy[s,s',n,n']
}

pred pre_busy_waitingFor[s:State,n,n':PhoneNumber]{
	(n->n') in s.busy
	CW in n'.feature
	// PN is not already being waited for, i.e.,
	// can have only one call in CW queue, otherwise stay busy
	n' not in PhoneNumber.(s.waitingFor)
}
pred post_busy_waitingFor[s,s':State,n,n':PhoneNumber]{
	s'.busy = s.busy - (n->n')
	s'.waitingFor = s.waitingFor + (n->n')

	s'.forwardedTo = s.forwardedTo	
	s'.idle = s.idle
	s'.calling = s.calling
	s'.talkingTo = s.talkingTo
}
pred busy_waitingFor[s,s':State,n,n':PhoneNumber]{
	pre_busy_waitingFor[s,n,n']
	post_busy_waitingFor[s,s',n,n']
}

// caller on CW hangs up
pred pre_waitingFor_idle[s:State,n,n':PhoneNumber]{
	n -> n' in s.waitingFor
}
pred post_waitingFor_idle[s,s':State,n,n':PhoneNumber]{	
	s'.waitingFor = s.waitingFor - (n -> n')	
	s'.idle = s.idle + n

	s'.calling = s.calling
	s'.talkingTo = s.talkingTo
	s'.busy = s.busy
	s'.forwardedTo = s.forwardedTo
}
pred waitingFor_idle[s,s':State,n,n':PhoneNumber]{
	pre_waitingFor_idle[s,n,n']
	post_waitingFor_idle[s,s',n,n']
}

pred pre_waitingFor_talkingTo[s:State,n,n':PhoneNumber]{
	n -> n' in s.waitingFor
}
pred post_waitingFor_talkingTo[s,s':State,n,n':PhoneNumber]{
	s'.waitingFor = s.waitingFor - (n -> n')
	s'.talkingTo = s.talkingTo + (n -> n')

	s'.idle = s.idle 
	s.busy = s'.busy
	s.forwardedTo = s'.forwardedTo
	s.calling = s'.calling
}
pred waitingFor_talkingTo[s,s':State,n,n':PhoneNumber]{
	pre_waitingFor_talkingTo[s,n,n']
	post_waitingFor_talkingTo[s,s',n,n']
}

pred pre_busy_forwardedTo[s:State,n,n':PhoneNumber]{
	n -> n' in s.busy
	CF in n'.feature
}
pred post_busy_forwardedTo[s,s':State,n,n':PhoneNumber]{
	s'.busy = s.busy - (n -> n')
	s'.forwardedTo = s.forwardedTo + (n -> n'.fw)

	s'.idle = s.idle
	s'.talkingTo = s.talkingTo
	s'.calling = s.calling 
	s'.waitingFor = s.waitingFor
}
pred busy_forwardedTo[s,s':State,n,n':PhoneNumber]{
	pre_busy_forwardedTo[s,n,n']
	post_busy_forwardedTo[s,s',n,n']
}

pred pre_forwardedTo_calling[s:State,n,n':PhoneNumber]{
	n -> n' in s.forwardedTo
}
pred post_forwardedTo_calling[s,s':State,n,n':PhoneNumber]{
	s'.forwardedTo = s.forwardedTo - (n->n')
	s'.calling = s.calling + (n -> n')

	s'.idle = s.idle
	s'.busy = s.busy
	s'.talkingTo = s.talkingTo
	s'.waitingFor = s.waitingFor
}
pred forwardedTo_calling[s,s':State,n,n':PhoneNumber]{
	pre_forwardedTo_calling[s,n,n']
	post_forwardedTo_calling[s,s',n,n']
}

pred pre_busy_idle[s:State,n,n':PhoneNumber]{
	n -> n' in s.busy
	no n'.feature
}
pred post_busy_idle[s,s':State,n,n':PhoneNumber]{
	s'.busy = s.busy - (n -> n')
	s'.idle = s.idle + n

	s.talkingTo = s'.talkingTo
	s.waitingFor = s'.waitingFor
	s.forwardedTo = s'.forwardedTo
	s.calling = s'.calling
}
pred busy_idle[s,s':State,n,n':PhoneNumber]{
	pre_busy_idle[s,n,n']
	post_busy_idle[s,s',n,n']
}


//*****************MODEL DEFINITION********************//

fact md{
	// init state constraint
	all s:State | s in initialState iff initial[s]	
	// transition constraints
	all s,s': State| 
		((s->s') in nextState) iff
		(some n,n':PhoneNumber|(
			idle_calling[s,s',n,n'] or calling_talkingTo[s,s',n,n'] or talkingTo_idle[s,s',n,n'] or
			calling_busy[s,s',n,n'] or busy_waitingFor[s,s',n,n'] or busy_forwardedTo[s,s',n,n'] or
			busy_idle[s,s',n,n'] or waitingFor_idle[s,s',n,n'] or waitingFor_talkingTo[s,s',n,n'] or
			forwardedTo_calling[s,s',n,n']))
	// equality predicate: states are records
	all s,s':State|(
		((s.idle = s'.idle) and (s.calling = s'.calling) and 
		(s.talkingTo = s'.talkingTo) and (s.busy = s'.busy) and
		(s.waitingFor = s'.waitingFor) and (s.forwardedTo = s'.forwardedTo)) implies (s =s'))
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
	some s:State | some n,n':PhoneNumber | pre_idle_calling[s,n,n']
	some s:State | some n,n':PhoneNumber | pre_calling_talkingTo[s,n,n']
	some s:State | some n,n':PhoneNumber | pre_talkingTo_idle[s,n,n']
	some s:State | some n,n':PhoneNumber | pre_calling_busy[s,n,n']
	some s:State | some n,n':PhoneNumber | pre_busy_waitingFor[s,n,n']
	some s:State | some n,n':PhoneNumber | pre_busy_forwardedTo[s,n,n']
	some s:State | some n,n':PhoneNumber | pre_busy_idle[s,n,n']
	some s:State | some n,n':PhoneNumber | pre_waitingFor_idle[s,n,n']
	some s:State | some n,n':PhoneNumber | pre_waitingFor_talkingTo[s,n,n']
	some s:State | some n,n':PhoneNumber | pre_forwardedTo_calling[s,n,n']
	// all possible ops from state must exist
	all s:State | some n,n':PhoneNumber | pre_idle_calling[s,n,n'] implies some s':State | post_idle_calling[s,s',n,n']
	all s:State | some n,n':PhoneNumber | pre_calling_talkingTo[s,n,n'] implies some s':State | post_calling_talkingTo[s,s',n,n']
	all s:State | some n,n':PhoneNumber | pre_talkingTo_idle[s,n,n'] implies some s':State | post_talkingTo_idle[s,s',n,n']
	all s:State | some n,n':PhoneNumber | pre_calling_busy[s,n,n'] implies some s':State | post_calling_busy[s,s',n,n']
	all s:State | some n,n':PhoneNumber | pre_busy_waitingFor[s,n,n'] implies some s':State | post_busy_waitingFor[s,s',n,n']
	all s:State | some n,n':PhoneNumber | pre_busy_forwardedTo[s,n,n'] implies some s':State | post_busy_forwardedTo[s,s',n,n']
	all s:State | some n,n':PhoneNumber | pre_busy_idle[s,n,n'] implies some s':State | post_busy_idle[s,s',n,n']
	all s:State | some n,n':PhoneNumber | pre_waitingFor_idle[s,n,n'] implies some s':State | post_waitingFor_idle[s,s',n,n']
	all s:State | some n,n':PhoneNumber | pre_waitingFor_talkingTo[s,n,n'] implies some s':State | post_waitingFor_talkingTo[s,s',n,n']
	all s:State | some n,n':PhoneNumber | pre_forwardedTo_calling[s,n,n'] implies some s':State | post_forwardedTo_calling[s,s',n,n']
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
