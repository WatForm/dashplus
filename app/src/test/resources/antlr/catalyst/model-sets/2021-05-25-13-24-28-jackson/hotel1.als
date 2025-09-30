module chapter6/hotel1 --- the model up to the top of page 191

open util/ordering[Time] as to
open util/ordering[Key] as ko

sig Key {}
sig Time {}

sig Room {
	keys: set Key,
	currentKey: keys one -> Time
	}

fact DisjointKeySets {
	-- each key belongs to at most one room
	Room<:keys   in   Room lone-> Key
	}

one sig FrontDesk {
	lastKey: (Room -> lone Key) -> Time,
	occupant: (Room -> Guest) -> Time
	}

sig Guest {
	keys: Key -> Time
	}

fun nextKey [k: Key, ks: set Key]: set Key {
	min [k.nexts & ks]
	}

pred init [t: Time] {
	no Guest.keys.t
	no FrontDesk.occupant.t
	all r: Room | FrontDesk.lastKey.t [r] = r.currentKey.t
	}

pred entry [t, tPrime: Time, g: Guest, r: Room, k: Key] {
	k in g.keys.t
	let ck = r.currentKey |
		(k = ck.t and ck.tPrime = ck.t) or 
		(k = nextKey[ck.t, r.keys] and ck.tPrime = k)
	noRoomChangeExcept [t, tPrime, r]
	noGuestChangeExcept [t, tPrime, none]
	noFrontDeskChange [t, tPrime]
	}

pred noFrontDeskChange [t, tPrime: Time] {
	FrontDesk.lastKey.t = FrontDesk.lastKey.tPrime
	FrontDesk.occupant.t = FrontDesk.occupant.tPrime
	}

pred noRoomChangeExcept [t, tPrime: Time, rs: set Room] {
	all r: Room - rs | r.currentKey.t = r.currentKey.tPrime
	}
	
pred noGuestChangeExcept [t, tPrime: Time, gs: set Guest] {
	all g: Guest - gs | g.keys.t = g.keys.tPrime
	}

pred checkout [t, tPrime: Time, g: Guest] {
	let occ = FrontDesk.occupant {
		some occ.t.g
		occ.tPrime = occ.t - Room ->g
		}
	FrontDesk.lastKey.t = FrontDesk.lastKey.tPrime
	noRoomChangeExcept [t, tPrime, none]
	noGuestChangeExcept [t, tPrime, none]
	}

pred checkin [t, tPrime: Time, g: Guest, r: Room, k: Key] {
	g.keys.tPrime = g.keys.t + k
	let occ = FrontDesk.occupant {
		no occ.t [r]
		occ.tPrime = occ.t + r -> g
		}
	let lk = FrontDesk.lastKey {
		lk.tPrime = lk.t ++ r -> k
		k = nextKey [lk.t [r], r.keys]
		}
	noRoomChangeExcept [t, tPrime, none]
	noGuestChangeExcept [t, tPrime, g]
	}

fact traces {
	init [first]
	all t: Time-last | let tPrime = t.next |
		some g: Guest, r: Room, k: Key |
			entry [t, tPrime, g, r, k]
			or checkin [t, tPrime, g, r, k]
			or checkout [t, tPrime, g]
	}

assert NoBadEntry {
	all t: Time, r: Room, g: Guest, k: Key |
		let tPrime = t.next, o = FrontDesk.occupant.t[r] | 
			entry [t, tPrime, g, r, k] and some o => g in o
	}

// This generates a counterexample similar to Fig 6.6
check NoBadEntry for 3 but 2 Room, 2 Guest, 5 Time
