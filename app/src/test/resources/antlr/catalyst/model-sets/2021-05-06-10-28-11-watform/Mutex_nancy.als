open util/boolean[] 

open util/steps[Snapshot]
open util/ordering[Snapshot]
open util/boolean

// Snapshot definition
    sig Snapshot extends BaseSnapshot {
        stable: one Bool,
        Mutex_semaphore_free : one Bool
    }

/***************************** STATE SPACE ************************************/
    abstract sig SystemState extends StateLabel {}
    abstract sig Mutex extends SystemState {}
    abstract sig Mutex_Process1 extends Mutex {}
    one sig Mutex_Process1_NonCritical extends Mutex_Process1 {}
    one sig Mutex_Process1_Critical extends Mutex_Process1 {}
    one sig Mutex_Process1_Wait extends Mutex_Process1 {}
    abstract sig Mutex_Process2 extends Mutex {}
    one sig Mutex_Process2_NonCritical extends Mutex_Process2 {}
    one sig Mutex_Process2_Critical extends Mutex_Process2 {}
    one sig Mutex_Process2_Wait extends Mutex_Process2 {}

/*************************** TRANSITIONS SPACE ********************************/
    one sig Mutex_Process1_wait extends TransitionLabel {}
    one sig Mutex_Process1_give_up extends TransitionLabel {}
    one sig Mutex_Process1_enter_critical_section extends TransitionLabel {}
    one sig Mutex_Process1_exit_critical_section extends TransitionLabel {}
    one sig Mutex_Process2_wait extends TransitionLabel {}
    one sig Mutex_Process2_give_up extends TransitionLabel {}
    one sig Mutex_Process2_enter_critical_section extends TransitionLabel {}
    one sig Mutex_Process2_exit_critical_section extends TransitionLabel {}

    // Transition Mutex_Process1_wait
    pred pre_Mutex_Process1_wait[s:Snapshot] {
        Mutex_Process1_NonCritical in s.conf
    }

    pred pos_Mutex_Process1_wait[s, s':Snapshot] {
        s'.conf = s.conf - Mutex_Process1_NonCritical + {
            Mutex_Process1_Wait
        }
        s'.Mutex_semaphore_free = s.Mutex_semaphore_free
    
        testIfNextStable[s, s', {none}, Mutex_Process1_wait] => {
            s'.stable = True
        } else {
            s'.stable = False
        }
    }

    pred Mutex_Process1_wait[s, s': Snapshot] {
        pre_Mutex_Process1_wait[s]
        pos_Mutex_Process1_wait[s, s']
        semantics_Mutex_Process1_wait[s, s']
    }

    pred enabledAfterStep_Mutex_Process1_wait[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        Mutex_Process1_NonCritical in s.conf
        _s.stable = True => {
            no t & {
                Mutex_Process1_give_up + 
                Mutex_Process1_exit_critical_section + 
                Mutex_Process1_enter_critical_section + 
                Mutex_Process1_wait
            }
        } else {
            no {_s.taken + t} & {
                Mutex_Process1_give_up + 
                Mutex_Process1_exit_critical_section + 
                Mutex_Process1_enter_critical_section + 
                Mutex_Process1_wait
            }
        }
    }
    pred semantics_Mutex_Process1_wait[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = Mutex_Process1_wait
        } else {
            // SINGLE semantics
            s'.taken = s.taken + Mutex_Process1_wait
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                Mutex_Process1_give_up + 
                Mutex_Process1_exit_critical_section + 
                Mutex_Process1_enter_critical_section + 
                Mutex_Process1_wait
            }
        }
    }
    // Transition Mutex_Process1_give_up
    pred pre_Mutex_Process1_give_up[s:Snapshot] {
        Mutex_Process1_Wait in s.conf
    }

    pred pos_Mutex_Process1_give_up[s, s':Snapshot] {
        s'.conf = s.conf - Mutex_Process1_Wait + {
            Mutex_Process1_NonCritical
        }
        s'.Mutex_semaphore_free = s.Mutex_semaphore_free
    
        testIfNextStable[s, s', {none}, Mutex_Process1_give_up] => {
            s'.stable = True
        } else {
            s'.stable = False
        }
    }

    pred Mutex_Process1_give_up[s, s': Snapshot] {
        pre_Mutex_Process1_give_up[s]
        pos_Mutex_Process1_give_up[s, s']
        semantics_Mutex_Process1_give_up[s, s']
    }

    pred enabledAfterStep_Mutex_Process1_give_up[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        Mutex_Process1_Wait in s.conf
        _s.stable = True => {
            no t & {
                Mutex_Process1_give_up + 
                Mutex_Process1_exit_critical_section + 
                Mutex_Process1_enter_critical_section + 
                Mutex_Process1_wait
            }
        } else {
            no {_s.taken + t} & {
                Mutex_Process1_give_up + 
                Mutex_Process1_exit_critical_section + 
                Mutex_Process1_enter_critical_section + 
                Mutex_Process1_wait
            }
        }
    }
    pred semantics_Mutex_Process1_give_up[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = Mutex_Process1_give_up
        } else {
            // SINGLE semantics
            s'.taken = s.taken + Mutex_Process1_give_up
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                Mutex_Process1_give_up + 
                Mutex_Process1_exit_critical_section + 
                Mutex_Process1_enter_critical_section + 
                Mutex_Process1_wait
            }
        }
    }
    // Transition Mutex_Process1_enter_critical_section
    pred pre_Mutex_Process1_enter_critical_section[s:Snapshot] {
        Mutex_Process1_Wait in s.conf
        (s.Mutex_semaphore_free) = True
    }

    pred pos_Mutex_Process1_enter_critical_section[s, s':Snapshot] {
        s'.conf = s.conf - Mutex_Process1_Wait + {
            Mutex_Process1_Critical
        }
        (s'.Mutex_semaphore_free) = False
    
        testIfNextStable[s, s', {none}, Mutex_Process1_enter_critical_section] => {
            s'.stable = True
        } else {
            s'.stable = False
        }
    }

    pred Mutex_Process1_enter_critical_section[s, s': Snapshot] {
        pre_Mutex_Process1_enter_critical_section[s]
        pos_Mutex_Process1_enter_critical_section[s, s']
        semantics_Mutex_Process1_enter_critical_section[s, s']
    }

    pred enabledAfterStep_Mutex_Process1_enter_critical_section[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        Mutex_Process1_Wait in s.conf
        (s.Mutex_semaphore_free) = True
        _s.stable = True => {
            no t & {
                Mutex_Process1_give_up + 
                Mutex_Process1_exit_critical_section + 
                Mutex_Process1_enter_critical_section + 
                Mutex_Process1_wait
            }
        } else {
            no {_s.taken + t} & {
                Mutex_Process1_give_up + 
                Mutex_Process1_exit_critical_section + 
                Mutex_Process1_enter_critical_section + 
                Mutex_Process1_wait
            }
        }
    }
    pred semantics_Mutex_Process1_enter_critical_section[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = Mutex_Process1_enter_critical_section
        } else {
            // SINGLE semantics
            s'.taken = s.taken + Mutex_Process1_enter_critical_section
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                Mutex_Process1_give_up + 
                Mutex_Process1_exit_critical_section + 
                Mutex_Process1_enter_critical_section + 
                Mutex_Process1_wait
            }
        }
    }
    // Transition Mutex_Process1_exit_critical_section
    pred pre_Mutex_Process1_exit_critical_section[s:Snapshot] {
        Mutex_Process1_Critical in s.conf
        (s.Mutex_semaphore_free) = False
    }

    pred pos_Mutex_Process1_exit_critical_section[s, s':Snapshot] {
        s'.conf = s.conf - Mutex_Process1_Critical + {
            Mutex_Process1_NonCritical
        }
        (s'.Mutex_semaphore_free) = True
    
        testIfNextStable[s, s', {none}, Mutex_Process1_exit_critical_section] => {
            s'.stable = True
        } else {
            s'.stable = False
        }
    }

    pred Mutex_Process1_exit_critical_section[s, s': Snapshot] {
        pre_Mutex_Process1_exit_critical_section[s]
        pos_Mutex_Process1_exit_critical_section[s, s']
        semantics_Mutex_Process1_exit_critical_section[s, s']
    }

    pred enabledAfterStep_Mutex_Process1_exit_critical_section[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        Mutex_Process1_Critical in s.conf
        (s.Mutex_semaphore_free) = False
        _s.stable = True => {
            no t & {
                Mutex_Process1_give_up + 
                Mutex_Process1_exit_critical_section + 
                Mutex_Process1_enter_critical_section + 
                Mutex_Process1_wait
            }
        } else {
            no {_s.taken + t} & {
                Mutex_Process1_give_up + 
                Mutex_Process1_exit_critical_section + 
                Mutex_Process1_enter_critical_section + 
                Mutex_Process1_wait
            }
        }
    }
    pred semantics_Mutex_Process1_exit_critical_section[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = Mutex_Process1_exit_critical_section
        } else {
            // SINGLE semantics
            s'.taken = s.taken + Mutex_Process1_exit_critical_section
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                Mutex_Process1_give_up + 
                Mutex_Process1_exit_critical_section + 
                Mutex_Process1_enter_critical_section + 
                Mutex_Process1_wait
            }
        }
    }
    // Transition Mutex_Process2_wait
    pred pre_Mutex_Process2_wait[s:Snapshot] {
        Mutex_Process2_NonCritical in s.conf
    }

    pred pos_Mutex_Process2_wait[s, s':Snapshot] {
        s'.conf = s.conf - Mutex_Process2_NonCritical + {
            Mutex_Process2_Wait
        }
        s'.Mutex_semaphore_free = s.Mutex_semaphore_free
    
        testIfNextStable[s, s', {none}, Mutex_Process2_wait] => {
            s'.stable = True
        } else {
            s'.stable = False
        }
    }

    pred Mutex_Process2_wait[s, s': Snapshot] {
        pre_Mutex_Process2_wait[s]
        pos_Mutex_Process2_wait[s, s']
        semantics_Mutex_Process2_wait[s, s']
    }

    pred enabledAfterStep_Mutex_Process2_wait[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        Mutex_Process2_NonCritical in s.conf
        _s.stable = True => {
            no t & {
                Mutex_Process2_give_up + 
                Mutex_Process2_exit_critical_section + 
                Mutex_Process2_enter_critical_section + 
                Mutex_Process2_wait
            }
        } else {
            no {_s.taken + t} & {
                Mutex_Process2_give_up + 
                Mutex_Process2_exit_critical_section + 
                Mutex_Process2_enter_critical_section + 
                Mutex_Process2_wait
            }
        }
    }
    pred semantics_Mutex_Process2_wait[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = Mutex_Process2_wait
        } else {
            // SINGLE semantics
            s'.taken = s.taken + Mutex_Process2_wait
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                Mutex_Process2_give_up + 
                Mutex_Process2_exit_critical_section + 
                Mutex_Process2_enter_critical_section + 
                Mutex_Process2_wait
            }
        }
    }
    // Transition Mutex_Process2_give_up
    pred pre_Mutex_Process2_give_up[s:Snapshot] {
        Mutex_Process2_Wait in s.conf
    }

    pred pos_Mutex_Process2_give_up[s, s':Snapshot] {
        s'.conf = s.conf - Mutex_Process2_Wait + {
            Mutex_Process2_NonCritical
        }
        s'.Mutex_semaphore_free = s.Mutex_semaphore_free
    
        testIfNextStable[s, s', {none}, Mutex_Process2_give_up] => {
            s'.stable = True
        } else {
            s'.stable = False
        }
    }

    pred Mutex_Process2_give_up[s, s': Snapshot] {
        pre_Mutex_Process2_give_up[s]
        pos_Mutex_Process2_give_up[s, s']
        semantics_Mutex_Process2_give_up[s, s']
    }

    pred enabledAfterStep_Mutex_Process2_give_up[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        Mutex_Process2_Wait in s.conf
        _s.stable = True => {
            no t & {
                Mutex_Process2_give_up + 
                Mutex_Process2_exit_critical_section + 
                Mutex_Process2_enter_critical_section + 
                Mutex_Process2_wait
            }
        } else {
            no {_s.taken + t} & {
                Mutex_Process2_give_up + 
                Mutex_Process2_exit_critical_section + 
                Mutex_Process2_enter_critical_section + 
                Mutex_Process2_wait
            }
        }
    }
    pred semantics_Mutex_Process2_give_up[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = Mutex_Process2_give_up
        } else {
            // SINGLE semantics
            s'.taken = s.taken + Mutex_Process2_give_up
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                Mutex_Process2_give_up + 
                Mutex_Process2_exit_critical_section + 
                Mutex_Process2_enter_critical_section + 
                Mutex_Process2_wait
            }
        }
    }
    // Transition Mutex_Process2_enter_critical_section
    pred pre_Mutex_Process2_enter_critical_section[s:Snapshot] {
        Mutex_Process2_Wait in s.conf
        (s.Mutex_semaphore_free) = True
    }

    pred pos_Mutex_Process2_enter_critical_section[s, s':Snapshot] {
        s'.conf = s.conf - Mutex_Process2_Wait + {
            Mutex_Process2_Critical
        }
        (s'.Mutex_semaphore_free) = False
    
        testIfNextStable[s, s', {none}, Mutex_Process2_enter_critical_section] => {
            s'.stable = True
        } else {
            s'.stable = False
        }
    }

    pred Mutex_Process2_enter_critical_section[s, s': Snapshot] {
        pre_Mutex_Process2_enter_critical_section[s]
        pos_Mutex_Process2_enter_critical_section[s, s']
        semantics_Mutex_Process2_enter_critical_section[s, s']
    }

    pred enabledAfterStep_Mutex_Process2_enter_critical_section[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        Mutex_Process2_Wait in s.conf
        (s.Mutex_semaphore_free) = True
        _s.stable = True => {
            no t & {
                Mutex_Process2_give_up + 
                Mutex_Process2_exit_critical_section + 
                Mutex_Process2_enter_critical_section + 
                Mutex_Process2_wait
            }
        } else {
            no {_s.taken + t} & {
                Mutex_Process2_give_up + 
                Mutex_Process2_exit_critical_section + 
                Mutex_Process2_enter_critical_section + 
                Mutex_Process2_wait
            }
        }
    }
    pred semantics_Mutex_Process2_enter_critical_section[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = Mutex_Process2_enter_critical_section
        } else {
            // SINGLE semantics
            s'.taken = s.taken + Mutex_Process2_enter_critical_section
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                Mutex_Process2_give_up + 
                Mutex_Process2_exit_critical_section + 
                Mutex_Process2_enter_critical_section + 
                Mutex_Process2_wait
            }
        }
    }
    // Transition Mutex_Process2_exit_critical_section
    pred pre_Mutex_Process2_exit_critical_section[s:Snapshot] {
        Mutex_Process2_Critical in s.conf
        (s.Mutex_semaphore_free) = False
    }

    pred pos_Mutex_Process2_exit_critical_section[s, s':Snapshot] {
        s'.conf = s.conf - Mutex_Process2_Critical + {
            Mutex_Process2_NonCritical
        }
        (s'.Mutex_semaphore_free) = True
    
        testIfNextStable[s, s', {none}, Mutex_Process2_exit_critical_section] => {
            s'.stable = True
        } else {
            s'.stable = False
        }
    }

    pred Mutex_Process2_exit_critical_section[s, s': Snapshot] {
        pre_Mutex_Process2_exit_critical_section[s]
        pos_Mutex_Process2_exit_critical_section[s, s']
        semantics_Mutex_Process2_exit_critical_section[s, s']
    }

    pred enabledAfterStep_Mutex_Process2_exit_critical_section[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        Mutex_Process2_Critical in s.conf
        (s.Mutex_semaphore_free) = False
        _s.stable = True => {
            no t & {
                Mutex_Process2_give_up + 
                Mutex_Process2_exit_critical_section + 
                Mutex_Process2_enter_critical_section + 
                Mutex_Process2_wait
            }
        } else {
            no {_s.taken + t} & {
                Mutex_Process2_give_up + 
                Mutex_Process2_exit_critical_section + 
                Mutex_Process2_enter_critical_section + 
                Mutex_Process2_wait
            }
        }
    }
    pred semantics_Mutex_Process2_exit_critical_section[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = Mutex_Process2_exit_critical_section
        } else {
            // SINGLE semantics
            s'.taken = s.taken + Mutex_Process2_exit_critical_section
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                Mutex_Process2_give_up + 
                Mutex_Process2_exit_critical_section + 
                Mutex_Process2_enter_critical_section + 
                Mutex_Process2_wait
            }
        }
    }
/****************************** INITIAL CONDITIONS ****************************/
    pred init[s: Snapshot] {
        s.conf = {
            Mutex_Process1_NonCritical + 
            Mutex_Process2_NonCritical
        }
        no s.taken
        s.stable = True
        // Model specific constraints
        (s.Mutex_semaphore_free) = True
    }


/***************************** MODEL DEFINITION *******************************/
    pred operation[s, s': Snapshot] {
        Mutex_Process1_wait[s, s'] or
        Mutex_Process1_give_up[s, s'] or
        Mutex_Process1_enter_critical_section[s, s'] or
        Mutex_Process1_exit_critical_section[s, s'] or
        Mutex_Process2_wait[s, s'] or
        Mutex_Process2_give_up[s, s'] or
        Mutex_Process2_enter_critical_section[s, s'] or
        Mutex_Process2_exit_critical_section[s, s']
    }

    pred small_step[s, s': Snapshot] {
        operation[s, s']
    }

    pred testIfNextStable[s, s': Snapshot, genEvents: set InternalEvent, t:TransitionLabel] {
        !enabledAfterStep_Mutex_Process1_wait[s, s', t, genEvents]
        !enabledAfterStep_Mutex_Process1_give_up[s, s', t, genEvents]
        !enabledAfterStep_Mutex_Process1_enter_critical_section[s, s', t, genEvents]
        !enabledAfterStep_Mutex_Process1_exit_critical_section[s, s', t, genEvents]
        !enabledAfterStep_Mutex_Process2_wait[s, s', t, genEvents]
        !enabledAfterStep_Mutex_Process2_give_up[s, s', t, genEvents]
        !enabledAfterStep_Mutex_Process2_enter_critical_section[s, s', t, genEvents]
        !enabledAfterStep_Mutex_Process2_exit_critical_section[s, s', t, genEvents]
    }

    pred isEnabled[s:Snapshot] {
        pre_Mutex_Process1_wait[s]or
        pre_Mutex_Process1_give_up[s]or
        pre_Mutex_Process1_enter_critical_section[s]or
        pre_Mutex_Process1_exit_critical_section[s]or
        pre_Mutex_Process2_wait[s]or
        pre_Mutex_Process2_give_up[s]or
        pre_Mutex_Process2_enter_critical_section[s]or
        pre_Mutex_Process2_exit_critical_section[s]
    }

    pred equals[s, s': Snapshot] {
        s'.conf = s.conf
        s'.taken = s.taken
        // Model specific declarations
        s'.Mutex_semaphore_free = s.Mutex_semaphore_free
    }

    fact {
        all s: Snapshot | s in initial iff init[s]
        all s, s': Snapshot | s->s' in nextStep iff small_step[s, s']
        all s, s': Snapshot | equals[s, s'] => s = s'
        all s: Snapshot | (isEnabled[s] && no s': Snapshot | small_step[s, s']) => s.stable = False
        all s: Snapshot | s.stable = False => some s.nextStep
        path
    }

    pred path {
        all s:Snapshot, s': s.next | operation[s, s']
        init[first]
    }
    run path for 5 Snapshot, 0 EventLabel
        expect 1



    assert ctl_mutex  {
        ctl_mc[ag[{ s: Snapshot | s.stable = True => !(({
            Mutex_Process1_Critical + Mutex_Process2_Critical
        }
         in s.conf))}]]
    }
    
    check ctl_mutex 
        for 9 Snapshot, exactly 0 EventLabel expect 0
    
    
    assert mutex {
        no s: Snapshot | s.stable = True and
            {Mutex_Process1_Critical + Mutex_Process2_Critical} in s.conf
    }
    check mutex for 9 Snapshot, exactly 0 EventLabel expect 0
    

