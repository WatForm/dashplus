open util/integer[] 

open util/steps[Snapshot]
open util/ordering[Snapshot]

// Snapshot definition
    sig Snapshot extends BaseSnapshot {
        events: set EventLabel,
        Game_active_players : set Player,
        Game_active_chairs : set Chair,
        Game_occupied : Chair set -> set Player
    }

/***************************** STATE SPACE ************************************/
    abstract sig SystemState extends StateLabel {}
    abstract sig Game extends SystemState {}
    one sig Game_Start extends Game {}
    one sig Game_Walking extends Game {}
    one sig Game_Sitting extends Game {}
    one sig Game_End extends Game {}

/***************************** EVENTS SPACE ***********************************/
    one sig Game_MusicStarts extends EnvironmentEvent {}
    one sig Game_MusicStops extends EnvironmentEvent {}

/*************************** TRANSITIONS SPACE ********************************/
    one sig Game_Start_Walk extends TransitionLabel {}
    one sig Game_Start_DeclareWinner extends TransitionLabel {}
    one sig Game_Walking_Sit extends TransitionLabel {}
    one sig Game_Sitting_EliminateLoser extends TransitionLabel {}

    // Transition Game_Start_Walk
    pred pre_Game_Start_Walk[s:Snapshot] {
        Game_Start in s.conf
        Game_MusicStarts in (s.events & EnvironmentEvent)
        #(s.Game_active_players) > 1
    }

    pred pos_Game_Start_Walk[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - Game_Start + {
            Game_Walking
        }
        sPrime.Game_active_players = s.Game_active_players
        sPrime.Game_active_chairs = s.Game_active_chairs
        (sPrime.Game_occupied) = none -> none
        no ((sPrime.events & InternalEvent) )
    }

    pred Game_Start_Walk[s, sPrime: Snapshot] {
        pre_Game_Start_Walk[s]
        pos_Game_Start_Walk[s, sPrime]
        semantics_Game_Start_Walk[s, sPrime]
    }
    pred semantics_Game_Start_Walk[s, sPrime: Snapshot] {
        sPrime.taken = Game_Start_Walk
    }
    // Transition Game_Start_DeclareWinner
    pred pre_Game_Start_DeclareWinner[s:Snapshot] {
        Game_Start in s.conf
        one (s.Game_active_players)
    }

    pred pos_Game_Start_DeclareWinner[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - Game_Start + {
            Game_End
        }
        sPrime.Game_active_players = s.Game_active_players
        sPrime.Game_active_chairs = s.Game_active_chairs
        sPrime.Game_occupied = s.Game_occupied
        no ((sPrime.events & InternalEvent) )
    }

    pred Game_Start_DeclareWinner[s, sPrime: Snapshot] {
        pre_Game_Start_DeclareWinner[s]
        pos_Game_Start_DeclareWinner[s, sPrime]
        semantics_Game_Start_DeclareWinner[s, sPrime]
    }
    pred semantics_Game_Start_DeclareWinner[s, sPrime: Snapshot] {
        sPrime.taken = Game_Start_DeclareWinner
    }
    // Transition Game_Walking_Sit
    pred pre_Game_Walking_Sit[s:Snapshot] {
        Game_Walking in s.conf
        Game_MusicStops in (s.events & EnvironmentEvent)
    }

    pred pos_Game_Walking_Sit[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - Game_Walking + {
            Game_Sitting
        }
        {
            (sPrime.Game_occupied) in (s.Game_active_chairs) -> (s.Game_active_players)
            (sPrime.Game_active_chairs) = (s.Game_active_chairs)
            (sPrime.Game_active_players) = (s.Game_active_players)
            all c : (sPrime.Game_active_chairs)
             | one c.((sPrime.Game_occupied))
            all p : Chair.((sPrime.Game_occupied))
             | one (sPrime.Game_occupied).p
        }
        no ((sPrime.events & InternalEvent) )
    }

    pred Game_Walking_Sit[s, sPrime: Snapshot] {
        pre_Game_Walking_Sit[s]
        pos_Game_Walking_Sit[s, sPrime]
        semantics_Game_Walking_Sit[s, sPrime]
    }
    pred semantics_Game_Walking_Sit[s, sPrime: Snapshot] {
        sPrime.taken = Game_Walking_Sit
    }
    // Transition Game_Sitting_EliminateLoser
    pred pre_Game_Sitting_EliminateLoser[s:Snapshot] {
        Game_Sitting in s.conf
    }

    pred pos_Game_Sitting_EliminateLoser[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - Game_Sitting + {
            Game_Start
        }
        sPrime.Game_occupied = s.Game_occupied
        {
            (sPrime.Game_active_players) = Chair.(s.Game_occupied)
            #(sPrime.Game_active_chairs) = (#(s.Game_active_chairs)).minus[ 1]
        }
        no ((sPrime.events & InternalEvent) )
    }

    pred Game_Sitting_EliminateLoser[s, sPrime: Snapshot] {
        pre_Game_Sitting_EliminateLoser[s]
        pos_Game_Sitting_EliminateLoser[s, sPrime]
        semantics_Game_Sitting_EliminateLoser[s, sPrime]
    }
    pred semantics_Game_Sitting_EliminateLoser[s, sPrime: Snapshot] {
        sPrime.taken = Game_Sitting_EliminateLoser
    }
/****************************** INITIAL CONDITIONS ****************************/
    pred init[s: Snapshot] {
        s.conf = {
            Game_Start
        }
        no s.taken
        no s.events & InternalEvent
        // Model specific constraints
        #(s.Game_active_players) > 1
            #(s.Game_active_players) = (#(s.Game_active_chairs)).plus[ 1]
            (s.Game_active_players) = Player
            (s.Game_active_chairs) = Chair
            (s.Game_occupied) = none -> none
    }


/***************************** MODEL DEFINITION *******************************/
    pred operation[s, sPrime: Snapshot] {
        Game_Start_Walk[s, sPrime] or
        Game_Start_DeclareWinner[s, sPrime] or
        Game_Walking_Sit[s, sPrime] or
        Game_Sitting_EliminateLoser[s, sPrime]
    }

    pred small_step[s, sPrime: Snapshot] {
        operation[s, sPrime]
    }

    pred equals[s, sPrime: Snapshot] {
        sPrime.conf = s.conf
        sPrime.events = s.events
        sPrime.taken = s.taken
        // Model specific declarations
        sPrime.Game_active_players = s.Game_active_players
        sPrime.Game_active_chairs = s.Game_active_chairs
        sPrime.Game_occupied = s.Game_occupied
    }

    fact {
        all s: Snapshot | s in initial iff init[s]
        all s, sPrime: Snapshot | s->sPrime in nextStep iff small_step[s, sPrime]
        all s, sPrime: Snapshot | equals[s, sPrime] => s = sPrime
        path
    }

    pred path {
        all s:Snapshot, sPrime: s.next | operation[s, sPrime]
        init[first]
    }
    run path for 5 Snapshot, 2 EventLabel,
        3 Chair, 3 Player
        expect 1



    sig Chair, Player {}
    
    assert ctl_safety  {
        ctl_mc[ag[{ s: Snapshot | (#(s.Game_active_players) = (#(s.Game_active_chairs)).plus[ 1])}]]
    }
    
    check ctl_safety 
        for exactly 3 Player, exactly 2 Chair, exactly 8 Snapshot, 2 EventLabel expect 0
    
    
    assert safety {
        all s: Snapshot | #s.Game_active_players = (#s.Game_active_chairs).plus[1]
    }
    check safety for exactly 3 Player , exactly 2 Chair,
        exactly 8 Snapshot, 2 EventLabel expect 0
    
    one sig Alice extends Player {}
    
    pred ctl_existential  {
        ctl_mc[ef[{ s: Snapshot | (Game_End in s.conf and (s.Game_active_players) = Alice)}]]
    }
    
    run ctl_existential 
        for exactly 3 Player, exactly 2 Chair, exactly 8 Snapshot, 2 EventLabel expect 1
    
    
    pred existential {
        some s: Snapshot | Game_End in s.conf and s.Game_active_players = Alice
    }
    run existential for exactly 3 Player , exactly 2 Chair,
        exactly 8 Snapshot, 2 EventLabel expect 1
    
    assert ctl_finiteLiveness  {
        ctl_mc[af[{ s: Snapshot | (Game_Sitting in s.conf)}]]
    }
    
    check ctl_finiteLiveness 
        for exactly 3 Player, exactly 2 Chair, exactly 8 Snapshot, 2 EventLabel expect 0
    
    
    /******************************* INFINITE LIVENESS ****************************/
    assert ctl_infiniteLiveness {
        // number of active_players eventually always reaches and remains at 1
        ctl_mc[af[ag[{ s : Snapshot | #s.Game_active_players = 1}]]]
    }
    check ctl_infiniteLiveness for exactly 3 Player , exactly 2 Chair,
        exactly 8 Snapshot, 2 EventLabel expect 0
    

