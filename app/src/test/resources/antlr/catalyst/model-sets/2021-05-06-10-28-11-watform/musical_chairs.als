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
pred post_music_starts [s, s': State] { 
  s'.players = s.players
  s'.chairs = s.chairs
  // no one is sitting after music starts
  s'.occupied = none -> none   
  s'.mode= walking
}
pred music_starts [s, s': State] { 
  pre_music_starts[s]   
  post_music_starts[s,s']
}

pred pre_music_stops [s: State] { 
  s.mode = walking 
}
pred post_music_stops [s, s': State] { 
  s'.players = s.players
  s'.chairs = s.chairs
  // no other chair/player than chairs/players
  s'.occupied in s'.chairs -> s'.players
  // forcing occupied to be total and 
  //each chair mapped to only one player
  all c:s'.chairs | one c.(s'.occupied)
  // each "occupying" player is sitting on one chair 
  all p:Chair.(s'.occupied) | one s'.occupied.p
  s'.mode = sitting
}
pred music_stops [s, s': State] { 
  pre_music_stops[s]   
  post_music_stops[s,s']
}

pred pre_eliminate_loser [s: State] { 
  s.mode = sitting 
}
pred post_eliminate_loser [s, s': State] { 
  // loser is the player in the game not in the range of occupied
  s'.players = Chair.(s.occupied)
  #s'.chairs = (#s.chairs).minus[1]
  s'.mode = start 
}
pred eliminate_loser [s, s': State] { 
  pre_eliminate_loser[s]   
  post_eliminate_loser[s,s']
}

pred pre_declare_winner [s: State] { 
  #s.players = 1
  s.mode = start
}
pred post_declare_winner [s, s': State] { 
  s'.players = s.players
  s'.chairs = s.chairs
  s'.mode = end
}
pred declare_winner [s, s': State] { 
  pre_declare_winner[s]   
  post_declare_winner[s,s']
}

pred pre_end_loop [s: State] {
  s.mode = end
}
pred post_end_loop [s, s': State] {
  s'.mode = end
  s'.players = s.players
  s'.chairs = s.chairs
  s'.occupied = s.occupied
}
pred end_loop [s, s': State] {
  pre_end_loop[s]   
  post_end_loop[s,s']
}

// helper to define valid transitions
pred trans [s,s': State] {
    music_starts[s,s'] or
    music_stops[s,s'] or
    eliminate_loser[s,s'] or
    declare_winner[s,s'] or
    end_loop[s,s']
}

//************************MODEL DEFINITION*********************//
fact {
   all s:State | s in initialState iff init[s]
   all s,s':State | s->s' in nextState iff trans[s,s']        
   // equality pred: two states with the same features are equivalent
   all s, s': State | s.players=s'.players and 
                           s.chairs=s'.chairs and 
                           s.occupied=s'.occupied and 
                           s.mode=s'.mode
                    implies s = s'
}

//**********************SIGNIFICANCE AXIOMS*********************//
pred initialStateAxiom {
	some s: State | s in initialState
}
pred totalityAxiom {
	all s: State | some s':State | s->s' in nextState
}
pred operationsAxiom {
	// at least one state must satisfy precons of each op
	some s:State | pre_music_starts[s]
	some s:State | pre_music_stops[s]
	some s:State | pre_eliminate_loser[s]
	some s:State | pre_declare_winner[s]
	some s:State | pre_end_loop[s]

	// all possible ops from state must exist
	all s:State | pre_music_starts[s] implies some s':State | post_music_starts[s,s'] 
	all s:State | pre_music_stops[s] implies some s':State | post_music_stops[s,s']
	all s:State | pre_eliminate_loser[s] implies some s':State | post_eliminate_loser[s,s']
	all s:State | pre_declare_winner[s] implies some s':State | post_declare_winner[s,s']
	all s:State | pre_end_loop[s] implies some s':State | post_end_loop[s,s']
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
