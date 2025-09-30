open util/ordering[Floor] 
open util/boolean[] 
open util/integer[] 

open util/steps[Snapshot]
open util/ordering[Snapshot]

// Snapshot definition
    sig Snapshot extends BaseSnapshot {
        Elevator_direction : one Direction,
        Elevator_called : set Floor,
        Elevator_maintenance : Int,
        Elevator_current : set Floor
    }

/***************************** STATE SPACE ************************************/
    abstract sig SystemState extends StateLabel {}
    one sig Elevator extends SystemState {}

/*************************** TRANSITIONS SPACE ********************************/
    one sig Elevator_maintenance extends TransitionLabel {}
    one sig Elevator_ChangeDirToDown extends TransitionLabel {}
    one sig Elevator_ChangeDirToUp extends TransitionLabel {}
    one sig Elevator_MoveUp extends TransitionLabel {}
    one sig Elevator_MoveDown extends TransitionLabel {}
    one sig Elevator_DefaultToGround extends TransitionLabel {}
    one sig Elevator_Idle extends TransitionLabel {}

    // Transition Elevator_maintenance
    pred pre_Elevator_maintenance[s:Snapshot] {
        Elevator in s.conf
        (s.Elevator_maintenance) = 2
    }

    pred pos_Elevator_maintenance[s, s':Snapshot] {
        s'.conf = s.conf - Elevator + {
            Elevator
        }
        {
            (s'.Elevator_current) = min[ Floor]
            (s'.Elevator_direction) = Down
            (s'.Elevator_maintenance) = 0
            {
                (s.Elevator_called) - (s'.Elevator_current)
            }
             in (s'.Elevator_called)
            (s'.Elevator_current) !in (s'.Elevator_called)
        }
    }

    pred Elevator_maintenance[s, s': Snapshot] {
        pre_Elevator_maintenance[s]
        pos_Elevator_maintenance[s, s']
        semantics_Elevator_maintenance[s, s']
    }
    pred semantics_Elevator_maintenance[s, s': Snapshot] {
        s'.taken = Elevator_maintenance
    }
    // Transition Elevator_ChangeDirToDown
    pred pre_Elevator_ChangeDirToDown[s:Snapshot] {
        Elevator in s.conf
        {
            some (s.Elevator_called)
            (s.Elevator_maintenance) < 2
            (s.Elevator_direction) = Up
            no nexts[ (s.Elevator_current)] & (s.Elevator_called)
        }
    }

    pred pos_Elevator_ChangeDirToDown[s, s':Snapshot] {
        s'.conf = s.conf - Elevator + {
            Elevator
        }
        {
            (s'.Elevator_direction) = Down
            (s'.Elevator_maintenance) = (s.Elevator_maintenance).plus[ 1]
            {
                (s.Elevator_called) - (s'.Elevator_current)
            }
             in (s'.Elevator_called)
            (s'.Elevator_current) !in (s'.Elevator_called)
        }
    }

    pred Elevator_ChangeDirToDown[s, s': Snapshot] {
        pre_Elevator_ChangeDirToDown[s]
        pos_Elevator_ChangeDirToDown[s, s']
        semantics_Elevator_ChangeDirToDown[s, s']
    }
    pred semantics_Elevator_ChangeDirToDown[s, s': Snapshot] {
        s'.taken = Elevator_ChangeDirToDown
    }
    // Transition Elevator_ChangeDirToUp
    pred pre_Elevator_ChangeDirToUp[s:Snapshot] {
        Elevator in s.conf
        {
            some (s.Elevator_called)
            (s.Elevator_maintenance) < 2
            (s.Elevator_direction) = Down
            no prevs[ (s.Elevator_current)] & (s.Elevator_called)
        }
    }

    pred pos_Elevator_ChangeDirToUp[s, s':Snapshot] {
        s'.conf = s.conf - Elevator + {
            Elevator
        }
        {
            (s'.Elevator_direction) = Up
            (s'.Elevator_maintenance) = (s.Elevator_maintenance).plus[ 1]
            {
                (s.Elevator_called) - (s'.Elevator_current)
            }
             in (s'.Elevator_called)
            (s'.Elevator_current) !in (s'.Elevator_called)
        }
    }

    pred Elevator_ChangeDirToUp[s, s': Snapshot] {
        pre_Elevator_ChangeDirToUp[s]
        pos_Elevator_ChangeDirToUp[s, s']
        semantics_Elevator_ChangeDirToUp[s, s']
    }
    pred semantics_Elevator_ChangeDirToUp[s, s': Snapshot] {
        s'.taken = Elevator_ChangeDirToUp
    }
    // Transition Elevator_MoveUp
    pred pre_Elevator_MoveUp[s:Snapshot] {
        Elevator in s.conf
        {
            some (s.Elevator_called)
            (s.Elevator_direction) = Up
            some nexts[ (s.Elevator_current)] & (s.Elevator_called)
        }
    }

    pred pos_Elevator_MoveUp[s, s':Snapshot] {
        s'.conf = s.conf - Elevator + {
            Elevator
        }
        s'.Elevator_direction = s.Elevator_direction
        s'.Elevator_maintenance = s.Elevator_maintenance
        {
            (s'.Elevator_current) = min[ (nexts[ (s.Elevator_current)] & (s.Elevator_called))]
            (s'.Elevator_current) !in (s'.Elevator_called)
            {
                (s.Elevator_called) - (s'.Elevator_current)
            }
             in (s'.Elevator_called)
        }
    }

    pred Elevator_MoveUp[s, s': Snapshot] {
        pre_Elevator_MoveUp[s]
        pos_Elevator_MoveUp[s, s']
        semantics_Elevator_MoveUp[s, s']
    }
    pred semantics_Elevator_MoveUp[s, s': Snapshot] {
        s'.taken = Elevator_MoveUp
    }
    // Transition Elevator_MoveDown
    pred pre_Elevator_MoveDown[s:Snapshot] {
        Elevator in s.conf
        {
            some (s.Elevator_called)
            (s.Elevator_direction) = Down
            some prevs[ (s.Elevator_current)] & (s.Elevator_called)
        }
    }

    pred pos_Elevator_MoveDown[s, s':Snapshot] {
        s'.conf = s.conf - Elevator + {
            Elevator
        }
        s'.Elevator_direction = s.Elevator_direction
        s'.Elevator_maintenance = s.Elevator_maintenance
        {
            (s'.Elevator_current) = max[ (prevs[ (s.Elevator_current)] & (s.Elevator_called))]
            (s'.Elevator_current) !in (s'.Elevator_called)
            {
                (s.Elevator_called) - (s'.Elevator_current)
            }
             in (s'.Elevator_called)
        }
    }

    pred Elevator_MoveDown[s, s': Snapshot] {
        pre_Elevator_MoveDown[s]
        pos_Elevator_MoveDown[s, s']
        semantics_Elevator_MoveDown[s, s']
    }
    pred semantics_Elevator_MoveDown[s, s': Snapshot] {
        s'.taken = Elevator_MoveDown
    }
    // Transition Elevator_DefaultToGround
    pred pre_Elevator_DefaultToGround[s:Snapshot] {
        Elevator in s.conf
        {
            no (s.Elevator_called)
            min[ Floor] !in (s.Elevator_current)
        }
    }

    pred pos_Elevator_DefaultToGround[s, s':Snapshot] {
        s'.conf = s.conf - Elevator + {
            Elevator
        }
        s'.Elevator_maintenance = s.Elevator_maintenance
        {
            (s'.Elevator_current) = min[ Floor]
            (s'.Elevator_direction) = Down
            {
                (s.Elevator_called) - (s'.Elevator_current)
            }
             in (s'.Elevator_called)
            (s'.Elevator_current) !in (s'.Elevator_called)
        }
    }

    pred Elevator_DefaultToGround[s, s': Snapshot] {
        pre_Elevator_DefaultToGround[s]
        pos_Elevator_DefaultToGround[s, s']
        semantics_Elevator_DefaultToGround[s, s']
    }
    pred semantics_Elevator_DefaultToGround[s, s': Snapshot] {
        s'.taken = Elevator_DefaultToGround
    }
    // Transition Elevator_Idle
    pred pre_Elevator_Idle[s:Snapshot] {
        Elevator in s.conf
        {
            no (s.Elevator_called)
            (s.Elevator_current) = min[ Floor]
        }
    }

    pred pos_Elevator_Idle[s, s':Snapshot] {
        s'.conf = s.conf - Elevator + {
            Elevator
        }
        s'.Elevator_direction = s.Elevator_direction
        {
            (s'.Elevator_maintenance) = 0
            {
                (s.Elevator_called) - (s'.Elevator_current)
            }
             in (s'.Elevator_called)
            (s'.Elevator_current) !in (s'.Elevator_called)
        }
    }

    pred Elevator_Idle[s, s': Snapshot] {
        pre_Elevator_Idle[s]
        pos_Elevator_Idle[s, s']
        semantics_Elevator_Idle[s, s']
    }
    pred semantics_Elevator_Idle[s, s': Snapshot] {
        s'.taken = Elevator_Idle
    }
/****************************** INITIAL CONDITIONS ****************************/
    pred init[s: Snapshot] {
        s.conf = {
            Elevator
        }
        no s.taken
        // Model specific constraints
        no (s.Elevator_called)
            (s.Elevator_maintenance) = 1
            (s.Elevator_direction) = Down
            (s.Elevator_current) = max[ Floor]
    }


/***************************** MODEL DEFINITION *******************************/
    pred operation[s, s': Snapshot] {
        Elevator_maintenance[s, s'] or
        Elevator_ChangeDirToDown[s, s'] or
        Elevator_ChangeDirToUp[s, s'] or
        Elevator_MoveUp[s, s'] or
        Elevator_MoveDown[s, s'] or
        Elevator_DefaultToGround[s, s'] or
        Elevator_Idle[s, s']
    }

    pred small_step[s, s': Snapshot] {
        operation[s, s']
    }

    pred equals[s, s': Snapshot] {
        s'.conf = s.conf
        s'.taken = s.taken
        // Model specific declarations
        s'.Elevator_direction = s.Elevator_direction
        s'.Elevator_called = s.Elevator_called
        s'.Elevator_maintenance = s.Elevator_maintenance
        s'.Elevator_current = s.Elevator_current
    }

    fact {
        all s: Snapshot | s in initial iff init[s]
        all s, s': Snapshot | s->s' in nextStep iff small_step[s, s']
        all s, s': Snapshot | equals[s, s'] => s = s'
        path
    }

    pred path {
        all s:Snapshot, s': s.next | operation[s, s']
        init[first]
    }
    run path for 5 Snapshot, 0 EventLabel,
        3 Floor
        expect 1



    sig Floor {}
    
    abstract sig Direction {}
    
    one sig Up, Down extends Direction {}
    
    
    assert infiniteLiveness {
        // a floor called is always eventually reached as current
        // AG ( floorCalled = > AF ( floorCurrent ) )
        all f : Floor | ctl_mc[ag[imp_ctl[Elevator_called.f , af[Elevator_current.f]]]]
    }
    check infiniteLiveness for exactly 6 Floor , 8 Snapshot, 0 EventLabel
        expect 0
    
    assert safety  {
        ctl_mc[ag[{ s: Snapshot | (one (s.Elevator_current))}]]
    }
    
    check safety 
        for exactly 6 Floor, 8 Snapshot, 0 EventLabel expect 0
    
    assert finiteLiveness  {
        ctl_mc[af[{ s: Snapshot | ((s.Elevator_maintenance) = 0)}]]
    }
    
    check finiteLiveness 
        for exactly 6 Floor, 8 Snapshot, 0 EventLabel expect 0
    

