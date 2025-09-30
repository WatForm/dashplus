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

    pred pos_Mutex_Process1_wait[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - Mutex_Process1_NonCritical + {
            Mutex_Process1_Wait
        }
        sPrime.Mutex_semaphore_free = s.Mutex_semaphore_free
    
        testIfNextStable[s, sPrime, {none}, Mutex_Process1_wait] => {
            sPrime.stable = True
        } else {
            sPrime.stable = False
        }
    }

    pred Mutex_Process1_wait[s, sPrime: Snapshot] {
        pre_Mutex_Process1_wait[s]
        pos_Mutex_Process1_wait[s, sPrime]
        semantics_Mutex_Process1_wait[s, sPrime]
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
    pred semantics_Mutex_Process1_wait[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = Mutex_Process1_wait
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + Mutex_Process1_wait
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

    pred pos_Mutex_Process1_give_up[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - Mutex_Process1_Wait + {
            Mutex_Process1_NonCritical
        }
        sPrime.Mutex_semaphore_free = s.Mutex_semaphore_free
    
        testIfNextStable[s, sPrime, {none}, Mutex_Process1_give_up] => {
            sPrime.stable = True
        } else {
            sPrime.stable = False
        }
    }

    pred Mutex_Process1_give_up[s, sPrime: Snapshot] {
        pre_Mutex_Process1_give_up[s]
        pos_Mutex_Process1_give_up[s, sPrime]
        semantics_Mutex_Process1_give_up[s, sPrime]
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
    pred semantics_Mutex_Process1_give_up[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = Mutex_Process1_give_up
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + Mutex_Process1_give_up
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

    pred pos_Mutex_Process1_enter_critical_section[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - Mutex_Process1_Wait + {
            Mutex_Process1_Critical
        }
        (sPrime.Mutex_semaphore_free) = False
    
        testIfNextStable[s, sPrime, {none}, Mutex_Process1_enter_critical_section] => {
            sPrime.stable = True
        } else {
            sPrime.stable = False
        }
    }

    pred Mutex_Process1_enter_critical_section[s, sPrime: Snapshot] {
        pre_Mutex_Process1_enter_critical_section[s]
        pos_Mutex_Process1_enter_critical_section[s, sPrime]
        semantics_Mutex_Process1_enter_critical_section[s, sPrime]
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
    pred semantics_Mutex_Process1_enter_critical_section[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = Mutex_Process1_enter_critical_section
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + Mutex_Process1_enter_critical_section
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

    pred pos_Mutex_Process1_exit_critical_section[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - Mutex_Process1_Critical + {
            Mutex_Process1_NonCritical
        }
        (sPrime.Mutex_semaphore_free) = True
    
        testIfNextStable[s, sPrime, {none}, Mutex_Process1_exit_critical_section] => {
            sPrime.stable = True
        } else {
            sPrime.stable = False
        }
    }

    pred Mutex_Process1_exit_critical_section[s, sPrime: Snapshot] {
        pre_Mutex_Process1_exit_critical_section[s]
        pos_Mutex_Process1_exit_critical_section[s, sPrime]
        semantics_Mutex_Process1_exit_critical_section[s, sPrime]
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
    pred semantics_Mutex_Process1_exit_critical_section[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = Mutex_Process1_exit_critical_section
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + Mutex_Process1_exit_critical_section
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

    pred pos_Mutex_Process2_wait[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - Mutex_Process2_NonCritical + {
            Mutex_Process2_Wait
        }
        sPrime.Mutex_semaphore_free = s.Mutex_semaphore_free
    
        testIfNextStable[s, sPrime, {none}, Mutex_Process2_wait] => {
            sPrime.stable = True
        } else {
            sPrime.stable = False
        }
    }

    pred Mutex_Process2_wait[s, sPrime: Snapshot] {
        pre_Mutex_Process2_wait[s]
        pos_Mutex_Process2_wait[s, sPrime]
        semantics_Mutex_Process2_wait[s, sPrime]
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
    pred semantics_Mutex_Process2_wait[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = Mutex_Process2_wait
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + Mutex_Process2_wait
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

    pred pos_Mutex_Process2_give_up[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - Mutex_Process2_Wait + {
            Mutex_Process2_NonCritical
        }
        sPrime.Mutex_semaphore_free = s.Mutex_semaphore_free
    
        testIfNextStable[s, sPrime, {none}, Mutex_Process2_give_up] => {
            sPrime.stable = True
        } else {
            sPrime.stable = False
        }
    }

    pred Mutex_Process2_give_up[s, sPrime: Snapshot] {
        pre_Mutex_Process2_give_up[s]
        pos_Mutex_Process2_give_up[s, sPrime]
        semantics_Mutex_Process2_give_up[s, sPrime]
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
    pred semantics_Mutex_Process2_give_up[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = Mutex_Process2_give_up
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + Mutex_Process2_give_up
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

    pred pos_Mutex_Process2_enter_critical_section[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - Mutex_Process2_Wait + {
            Mutex_Process2_Critical
        }
        (sPrime.Mutex_semaphore_free) = False
    
        testIfNextStable[s, sPrime, {none}, Mutex_Process2_enter_critical_section] => {
            sPrime.stable = True
        } else {
            sPrime.stable = False
        }
    }

    pred Mutex_Process2_enter_critical_section[s, sPrime: Snapshot] {
        pre_Mutex_Process2_enter_critical_section[s]
        pos_Mutex_Process2_enter_critical_section[s, sPrime]
        semantics_Mutex_Process2_enter_critical_section[s, sPrime]
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
    pred semantics_Mutex_Process2_enter_critical_section[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = Mutex_Process2_enter_critical_section
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + Mutex_Process2_enter_critical_section
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

    pred pos_Mutex_Process2_exit_critical_section[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - Mutex_Process2_Critical + {
            Mutex_Process2_NonCritical
        }
        (sPrime.Mutex_semaphore_free) = True
    
        testIfNextStable[s, sPrime, {none}, Mutex_Process2_exit_critical_section] => {
            sPrime.stable = True
        } else {
            sPrime.stable = False
        }
    }

    pred Mutex_Process2_exit_critical_section[s, sPrime: Snapshot] {
        pre_Mutex_Process2_exit_critical_section[s]
        pos_Mutex_Process2_exit_critical_section[s, sPrime]
        semantics_Mutex_Process2_exit_critical_section[s, sPrime]
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
    pred semantics_Mutex_Process2_exit_critical_section[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = Mutex_Process2_exit_critical_section
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + Mutex_Process2_exit_critical_section
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
    pred operation[s, sPrime: Snapshot] {
        Mutex_Process1_wait[s, sPrime] or
        Mutex_Process1_give_up[s, sPrime] or
        Mutex_Process1_enter_critical_section[s, sPrime] or
        Mutex_Process1_exit_critical_section[s, sPrime] or
        Mutex_Process2_wait[s, sPrime] or
        Mutex_Process2_give_up[s, sPrime] or
        Mutex_Process2_enter_critical_section[s, sPrime] or
        Mutex_Process2_exit_critical_section[s, sPrime]
    }

    pred small_step[s, sPrime: Snapshot] {
        operation[s, sPrime]
    }

    pred testIfNextStable[s, sPrime: Snapshot, genEvents: set InternalEvent, t:TransitionLabel] {
        !enabledAfterStep_Mutex_Process1_wait[s, sPrime, t, genEvents]
        !enabledAfterStep_Mutex_Process1_give_up[s, sPrime, t, genEvents]
        !enabledAfterStep_Mutex_Process1_enter_critical_section[s, sPrime, t, genEvents]
        !enabledAfterStep_Mutex_Process1_exit_critical_section[s, sPrime, t, genEvents]
        !enabledAfterStep_Mutex_Process2_wait[s, sPrime, t, genEvents]
        !enabledAfterStep_Mutex_Process2_give_up[s, sPrime, t, genEvents]
        !enabledAfterStep_Mutex_Process2_enter_critical_section[s, sPrime, t, genEvents]
        !enabledAfterStep_Mutex_Process2_exit_critical_section[s, sPrime, t, genEvents]
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

    pred equals[s, sPrime: Snapshot] {
        sPrime.conf = s.conf
        sPrime.taken = s.taken
        // Model specific declarations
        sPrime.Mutex_semaphore_free = s.Mutex_semaphore_free
    }

    fact {
        all s: Snapshot | s in initial iff init[s]
        all s, sPrime: Snapshot | s->sPrime in nextStep iff small_step[s, sPrime]
        all s, sPrime: Snapshot | equals[s, sPrime] => s = sPrime
        all s: Snapshot | (isEnabled[s] && no sPrime: Snapshot | small_step[s, sPrime]) => s.stable = False
        all s: Snapshot | s.stable = False => some s.nextStep
        path
    }

    pred path {
        all s:Snapshot, sPrime: s.next | operation[s, sPrime]
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
    

