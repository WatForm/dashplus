/* Authors: Sabria Farheen, Nancy A. Day, Amirhossein Vakili, Ali Abbassi
 * Date: October 1, 2017
 */

open ctl[State]
open util/integer

//***********************STATE SPACE*************************//
sig Chair, Player {}
abstract sig Mode {}
one sig start, walking, sitting, end extends Mode {}

sig State {
  // current players
  players: set Player, 
  //current chairs
  chairs: set Chair,
  // current chair player relation
  occupied: set Chair -> set Player,  
  // current state of game, should always be 1
  mode : set Mode
} 

//*****************INITIAL STATE CONSTRAINTS********************//

pred init [s:State] {
    s.mode = start
    #s.players > 1
    #s.players = (#s.chairs).plus[1]
    // force all Chair and Player to be included 
    s.players = Player
    s.chairs = Chair
    s.occupied = none -> none
}
 
//**********************TRANSITION CONSTRAINTS***********************//
pred pre_music_starts [s: State] { 
  #s.players > 1  
  s.mode = start
}
pred post_music_starts [s, sPrime: State] { 
  sPrime.players = s.players
  sPrime.chairs = s.chairs
  // no one is sitting after music starts
  sPrime.occupied = none -> none   
  sPrime.mode= walking
}
pred music_starts [s, sPrime: State] { 
  pre_music_starts[s]   
  post_music_starts[s,sPrime]
}

pred pre_music_stops [s: State] { 
  s.mode = walking 
}
pred post_music_stops [s, sPrime: State] { 
  sPrime.players = s.players
  sPrime.chairs = s.chairs
  // no other chair/player than chairs/players
  sPrime.occupied in sPrime.chairs -> sPrime.players
  // forcing occupied to be total and 
  //each chair mapped to only one player
  all c:sPrime.chairs | one c.(sPrime.occupied)
  // each "occupying" player is sitting on one chair 
  all p:Chair.(sPrime.occupied) | one sPrime.occupied.p
  sPrime.mode = sitting
}
pred music_stops [s, sPrime: State] { 
  pre_music_stops[s]   
  post_music_stops[s,sPrime]
}

pred pre_eliminate_loser [s: State] { 
  s.mode = sitting 
}
pred post_eliminate_loser [s, sPrime: State] { 
  // loser is the player in the game not in the range of occupied
  sPrime.players = Chair.(s.occupied)
  #sPrime.chairs = (#s.chairs).minus[1]
  sPrime.mode = start 
}
pred eliminate_loser [s, sPrime: State] { 
  pre_eliminate_loser[s]   
  post_eliminate_loser[s,sPrime]
}

pred pre_declare_winner [s: State] { 
  #s.players = 1
  s.mode = start
}
pred post_declare_winner [s, sPrime: State] { 
  sPrime.players = s.players
  sPrime.chairs = s.chairs
  sPrime.mode = end
}
pred declare_winner [s, sPrime: State] { 
  pre_declare_winner[s]   
  post_declare_winner[s,sPrime]
}

pred pre_end_loop [s: State] {
  s.mode = end
}
pred post_end_loop [s, sPrime: State] {
  sPrime.mode = end
  sPrime.players = s.players
  sPrime.chairs = s.chairs
  sPrime.occupied = s.occupied
}
pred end_loop [s, sPrime: State] {
  pre_end_loop[s]   
  post_end_loop[s,sPrime]
}

// helper to define valid transitions
pred trans [s,sPrime: State] {
    music_starts[s,sPrime] or
    music_stops[s,sPrime] or
    eliminate_loser[s,sPrime] or
    declare_winner[s,sPrime] or
    end_loop[s,sPrime]
}

//************************MODEL DEFINITION*********************//
fact {
   all s:State | s in initialState iff init[s]
   all s,sPrime:State | s->sPrime in nextState iff trans[s,sPrime]        
   // equality pred: two states with the same features are equivalent
   all s, sPrime: State | s.players=sPrime.players and 
                           s.chairs=sPrime.chairs and 
                           s.occupied=sPrime.occupied and 
                           s.mode=sPrime.mode
                    implies s = sPrime
}

//**********************SIGNIFICANCE AXIOMS*********************//
pred initialStateAxiom {
	some s: State | s in initialState
}
pred totalityAxiom {
	all s: State | some sPrime:State | s->sPrime in nextState
}
pred operationsAxiom {
	// at least one state must satisfy precons of each op
	some s:State | pre_music_starts[s]
	some s:State | pre_music_stops[s]
	some s:State | pre_eliminate_loser[s]
	some s:State | pre_declare_winner[s]
	some s:State | pre_end_loop[s]

	// all possible ops from state must exist
	all s:State | pre_music_starts[s] implies some sPrime:State | post_music_starts[s,sPrime] 
	all s:State | pre_music_stops[s] implies some sPrime:State | post_music_stops[s,sPrime]
	all s:State | pre_eliminate_loser[s] implies some sPrime:State | post_eliminate_loser[s,sPrime]
	all s:State | pre_declare_winner[s] implies some sPrime:State | post_declare_winner[s,sPrime]
	all s:State | pre_end_loop[s] implies some sPrime:State | post_end_loop[s,sPrime]
}
pred significanceAxioms {
	initialStateAxiom
	totalityAxiom
	operationsAxiom
}
--run significanceAxioms for 5 but exactly 2 Player

//**********************PROPERTIES*********************//
// constrain next-state relation to be total, remove/comment out to disable
--fact { totalityAxiom }

//***********************SAFETY************************//
pred safety [s:State] {
  #s.players = (#s.chairs).plus[1]
}
check {ctl_mc[ag[{s: State| safety[s]}]]} for exactly 3 Player, exactly 2 Chair, exactly 8 State

//***********************EXISTENTIAL************************//
one sig Alice extends Player{}
pred existential [s:State] {
	s.mode=end and	s.players=Alice
}
run {ctl_mc[ef[{s: State| existential[s]}]]} for exactly 3 Player, exactly 2 Chair, exactly 8 State

//***********************FINITE LIVENESS***************************//
pred finiteLiveness [s:State] {
  s.mode=walking
}
check {ctl_mc[af[{s: State| finiteLiveness[s]}]]} for exactly 3 Player, exactly 2 Chair, exactly 8 State

//**********************INFINITE LIVENESS***************************//
pred infiniteLiveness [s:State] {
   #s.players=1
}
check {ctl_mc[af[ag[{s: State| infiniteLiveness[s]}]]]} for exactly 3 Player, exactly 2 Chair, exactly 8 State
