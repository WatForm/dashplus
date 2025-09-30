
open util/steps[Snapshot]
open util/ordering[Snapshot]

// Snapshot definition
    sig Snapshot extends BaseSnapshot {
        Puzzle_near : set Object,
        Puzzle_far : set Object
    }

/***************************** STATE SPACE ************************************/
    abstract sig SystemState extends StateLabel {}
    one sig Puzzle extends SystemState {}

/*************************** TRANSITIONS SPACE ********************************/
    one sig Puzzle_near2far extends TransitionLabel {}
    one sig Puzzle_far2near extends TransitionLabel {}

    // Transition Puzzle_near2far
    pred pre_Puzzle_near2far[s:Snapshot] {
        Puzzle in s.conf
        Farmer in (s.Puzzle_near)
    }

    pred pos_Puzzle_near2far[s, s':Snapshot] {
        s'.conf = s.conf - Puzzle + {
            Puzzle
        }
        {
            (one x : (s.Puzzle_near) - Farmer
             | {
                (s'.Puzzle_near) = (s.Puzzle_near) - Farmer - x - (s'.Puzzle_near).eats
                (s'.Puzzle_far) = (s.Puzzle_far) + Farmer + x
            }
            ) or {
                (s'.Puzzle_near) = (s.Puzzle_near) - Farmer - (s'.Puzzle_near).eats
                (s'.Puzzle_far) = (s.Puzzle_far) + Farmer
            }
        }
    }

    pred Puzzle_near2far[s, s': Snapshot] {
        pre_Puzzle_near2far[s]
        pos_Puzzle_near2far[s, s']
        semantics_Puzzle_near2far[s, s']
    }
    pred semantics_Puzzle_near2far[s, s': Snapshot] {
        s'.taken = Puzzle_near2far
    }
    // Transition Puzzle_far2near
    pred pre_Puzzle_far2near[s:Snapshot] {
        Puzzle in s.conf
        Farmer in (s.Puzzle_far)
    }

    pred pos_Puzzle_far2near[s, s':Snapshot] {
        s'.conf = s.conf - Puzzle + {
            Puzzle
        }
        {
            (one x : (s.Puzzle_far) - Farmer
             | {
                (s'.Puzzle_far) = (s.Puzzle_far) - Farmer - x - (s'.Puzzle_far).eats
                (s'.Puzzle_near) = (s.Puzzle_near) + Farmer + x
            }
            ) or {
                (s'.Puzzle_far) = (s.Puzzle_far) - Farmer - (s'.Puzzle_far).eats
                (s'.Puzzle_near) = (s.Puzzle_near) + Farmer
            }
        }
    }

    pred Puzzle_far2near[s, s': Snapshot] {
        pre_Puzzle_far2near[s]
        pos_Puzzle_far2near[s, s']
        semantics_Puzzle_far2near[s, s']
    }
    pred semantics_Puzzle_far2near[s, s': Snapshot] {
        s'.taken = Puzzle_far2near
    }
/****************************** INITIAL CONDITIONS ****************************/
    pred init[s: Snapshot] {
        s.conf = {
            Puzzle
        }
        no s.taken
        // Model specific constraints
        (s.Puzzle_near) = Object
            no (s.Puzzle_far)
    }


/***************************** MODEL DEFINITION *******************************/
    pred operation[s, s': Snapshot] {
        Puzzle_near2far[s, s'] or
        Puzzle_far2near[s, s']
    }

    pred small_step[s, s': Snapshot] {
        operation[s, s']
    }

    pred equals[s, s': Snapshot] {
        s'.conf = s.conf
        s'.taken = s.taken
        // Model specific declarations
        s'.Puzzle_near = s.Puzzle_near
        s'.Puzzle_far = s.Puzzle_far
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
    run path for 5 Snapshot, 0 EventLabel
        expect 1



    abstract sig Object  {
        eats : set Object
     }
    
    one sig Chicken, Farmer, Fox, Grain extends Object {}
    
    fact eating  {
        eats = Fox -> Chicken + Chicken -> Grain
    }
    
    
    /** the farmer moves everything to the far side of the river. */
    
    pred ctl_solve  {
        ctl_mc[ef[{ s: Snapshot | (s.Puzzle_far) = Object}]]
    }
    
    run ctl_solve 
        for 8 Snapshot, 0 EventLabel expect 1
    
    
    pred solve {
        some s: Snapshot | s.Puzzle_far = Object
    }
    run solve for 8 Snapshot, 0 EventLabel expect 1
    
    
    /** No Object can be in two places at once */
    
    assert ctl_no_quantum_objects  {
        ctl_mc[ag[{ s: Snapshot | (no x : Object
         | x in (s.Puzzle_near) and x in (s.Puzzle_far))}]]
    }
    
    check ctl_no_quantum_objects 
        for 8 Snapshot, 0 EventLabel expect 0
    
    
    assert no_quantum_objects {
        all s: Snapshot | no x: Object |
            x in s.Puzzle_near and x in s.Puzzle_far
    }
    check no_quantum_objects for 8 Snapshot, 0 EventLabel expect 0
    

