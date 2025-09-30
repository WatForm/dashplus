
open util/steps[Snapshot]
open util/ordering[Snapshot]
open util/boolean

// Snapshot definition
    sig Snapshot extends BaseSnapshot {
        stable: one Bool,
        events: set EventLabel,
    }

/***************************** STATE SPACE ************************************/
    abstract sig SystemState extends StateLabel {}
    abstract sig TrafficLight extends SystemState {}
    abstract sig TrafficLight_NorthSouth extends TrafficLight {}
    one sig TrafficLight_NorthSouth_Green extends TrafficLight_NorthSouth {}
    one sig TrafficLight_NorthSouth_Yellow extends TrafficLight_NorthSouth {}
    one sig TrafficLight_NorthSouth_Red extends TrafficLight_NorthSouth {}
    abstract sig TrafficLight_EastWest extends TrafficLight {}
    one sig TrafficLight_EastWest_Green extends TrafficLight_EastWest {}
    one sig TrafficLight_EastWest_Yellow extends TrafficLight_EastWest {}
    one sig TrafficLight_EastWest_Red extends TrafficLight_EastWest {}

/***************************** EVENTS SPACE ***********************************/
    one sig TrafficLight_Change extends EnvironmentEvent {}
    one sig TrafficLight_End extends EnvironmentEvent {}

/*************************** TRANSITIONS SPACE ********************************/
    one sig TrafficLight_NorthSouth_Green_T1 extends TransitionLabel {}
    one sig TrafficLight_NorthSouth_Yellow_T2 extends TransitionLabel {}
    one sig TrafficLight_NorthSouth_Red_T3 extends TransitionLabel {}
    one sig TrafficLight_EastWest_Green_T5 extends TransitionLabel {}
    one sig TrafficLight_EastWest_Yellow_T6 extends TransitionLabel {}
    one sig TrafficLight_EastWest_Red_T4 extends TransitionLabel {}

    // Transition TrafficLight_NorthSouth_Green_T1
    pred pre_TrafficLight_NorthSouth_Green_T1[s:Snapshot] {
        TrafficLight_NorthSouth_Green in s.conf
        s.stable = True => {
            TrafficLight_End in (s.events & EnvironmentEvent)
        } else {
            TrafficLight_End in s.events
        }
    }

    pred pos_TrafficLight_NorthSouth_Green_T1[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - TrafficLight_NorthSouth_Green + {
            TrafficLight_NorthSouth_Yellow
        }
    
        testIfNextStable[s, sPrime, {none}, TrafficLight_NorthSouth_Green_T1] => {
            sPrime.stable = True
            s.stable = True => {
                no ((sPrime.events & InternalEvent) )
            } else {
                no ((sPrime.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            sPrime.stable = False
            s.stable = True => {
                sPrime.events & InternalEvent = {none}
                sPrime.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                sPrime.events = s.events + {none}
            }
        }
    }

    pred TrafficLight_NorthSouth_Green_T1[s, sPrime: Snapshot] {
        pre_TrafficLight_NorthSouth_Green_T1[s]
        pos_TrafficLight_NorthSouth_Green_T1[s, sPrime]
        semantics_TrafficLight_NorthSouth_Green_T1[s, sPrime]
    }

    pred enabledAfterStep_TrafficLight_NorthSouth_Green_T1[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        TrafficLight_NorthSouth_Green in s.conf
        _s.stable = True => {
            no t & {
                TrafficLight_NorthSouth_Yellow_T2 + 
                TrafficLight_NorthSouth_Red_T3 + 
                TrafficLight_NorthSouth_Green_T1
            }
            TrafficLight_End in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                TrafficLight_NorthSouth_Yellow_T2 + 
                TrafficLight_NorthSouth_Red_T3 + 
                TrafficLight_NorthSouth_Green_T1
            }
            TrafficLight_End in {_s.events  + genEvents}
        }
    }
    pred semantics_TrafficLight_NorthSouth_Green_T1[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = TrafficLight_NorthSouth_Green_T1
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + TrafficLight_NorthSouth_Green_T1
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                TrafficLight_NorthSouth_Yellow_T2 + 
                TrafficLight_NorthSouth_Red_T3 + 
                TrafficLight_NorthSouth_Green_T1
            }
        }
    }
    // Transition TrafficLight_NorthSouth_Yellow_T2
    pred pre_TrafficLight_NorthSouth_Yellow_T2[s:Snapshot] {
        TrafficLight_NorthSouth_Yellow in s.conf
        s.stable = True => {
            TrafficLight_Change in (s.events & EnvironmentEvent)
        } else {
            TrafficLight_Change in s.events
        }
    }

    pred pos_TrafficLight_NorthSouth_Yellow_T2[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - TrafficLight_NorthSouth_Yellow + {
            TrafficLight_NorthSouth_Red
        }
    
        testIfNextStable[s, sPrime, {none}, TrafficLight_NorthSouth_Yellow_T2] => {
            sPrime.stable = True
            s.stable = True => {
                no ((sPrime.events & InternalEvent) )
            } else {
                no ((sPrime.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            sPrime.stable = False
            s.stable = True => {
                sPrime.events & InternalEvent = {none}
                sPrime.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                sPrime.events = s.events + {none}
            }
        }
    }

    pred TrafficLight_NorthSouth_Yellow_T2[s, sPrime: Snapshot] {
        pre_TrafficLight_NorthSouth_Yellow_T2[s]
        pos_TrafficLight_NorthSouth_Yellow_T2[s, sPrime]
        semantics_TrafficLight_NorthSouth_Yellow_T2[s, sPrime]
    }

    pred enabledAfterStep_TrafficLight_NorthSouth_Yellow_T2[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        TrafficLight_NorthSouth_Yellow in s.conf
        _s.stable = True => {
            no t & {
                TrafficLight_NorthSouth_Yellow_T2 + 
                TrafficLight_NorthSouth_Red_T3 + 
                TrafficLight_NorthSouth_Green_T1
            }
            TrafficLight_Change in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                TrafficLight_NorthSouth_Yellow_T2 + 
                TrafficLight_NorthSouth_Red_T3 + 
                TrafficLight_NorthSouth_Green_T1
            }
            TrafficLight_Change in {_s.events  + genEvents}
        }
    }
    pred semantics_TrafficLight_NorthSouth_Yellow_T2[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = TrafficLight_NorthSouth_Yellow_T2
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + TrafficLight_NorthSouth_Yellow_T2
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                TrafficLight_NorthSouth_Yellow_T2 + 
                TrafficLight_NorthSouth_Red_T3 + 
                TrafficLight_NorthSouth_Green_T1
            }
        }
    }
    // Transition TrafficLight_NorthSouth_Red_T3
    pred pre_TrafficLight_NorthSouth_Red_T3[s:Snapshot] {
        TrafficLight_NorthSouth_Red in s.conf
        s.stable = True => {
            TrafficLight_Change in (s.events & EnvironmentEvent)
        } else {
            TrafficLight_Change in s.events
        }
    }

    pred pos_TrafficLight_NorthSouth_Red_T3[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - TrafficLight_NorthSouth_Red + {
            TrafficLight_NorthSouth_Green
        }
    
        testIfNextStable[s, sPrime, {none}, TrafficLight_NorthSouth_Red_T3] => {
            sPrime.stable = True
            s.stable = True => {
                no ((sPrime.events & InternalEvent) )
            } else {
                no ((sPrime.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            sPrime.stable = False
            s.stable = True => {
                sPrime.events & InternalEvent = {none}
                sPrime.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                sPrime.events = s.events + {none}
            }
        }
    }

    pred TrafficLight_NorthSouth_Red_T3[s, sPrime: Snapshot] {
        pre_TrafficLight_NorthSouth_Red_T3[s]
        pos_TrafficLight_NorthSouth_Red_T3[s, sPrime]
        semantics_TrafficLight_NorthSouth_Red_T3[s, sPrime]
    }

    pred enabledAfterStep_TrafficLight_NorthSouth_Red_T3[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        TrafficLight_NorthSouth_Red in s.conf
        _s.stable = True => {
            no t & {
                TrafficLight_NorthSouth_Yellow_T2 + 
                TrafficLight_NorthSouth_Red_T3 + 
                TrafficLight_NorthSouth_Green_T1
            }
            TrafficLight_Change in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                TrafficLight_NorthSouth_Yellow_T2 + 
                TrafficLight_NorthSouth_Red_T3 + 
                TrafficLight_NorthSouth_Green_T1
            }
            TrafficLight_Change in {_s.events  + genEvents}
        }
    }
    pred semantics_TrafficLight_NorthSouth_Red_T3[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = TrafficLight_NorthSouth_Red_T3
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + TrafficLight_NorthSouth_Red_T3
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                TrafficLight_NorthSouth_Yellow_T2 + 
                TrafficLight_NorthSouth_Red_T3 + 
                TrafficLight_NorthSouth_Green_T1
            }
        }
    }
    // Transition TrafficLight_EastWest_Green_T5
    pred pre_TrafficLight_EastWest_Green_T5[s:Snapshot] {
        TrafficLight_EastWest_Green in s.conf
        s.stable = True => {
            TrafficLight_End in (s.events & EnvironmentEvent)
        } else {
            TrafficLight_End in s.events
        }
    }

    pred pos_TrafficLight_EastWest_Green_T5[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - TrafficLight_EastWest_Green + {
            TrafficLight_EastWest_Yellow
        }
    
        testIfNextStable[s, sPrime, {none}, TrafficLight_EastWest_Green_T5] => {
            sPrime.stable = True
            s.stable = True => {
                no ((sPrime.events & InternalEvent) )
            } else {
                no ((sPrime.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            sPrime.stable = False
            s.stable = True => {
                sPrime.events & InternalEvent = {none}
                sPrime.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                sPrime.events = s.events + {none}
            }
        }
    }

    pred TrafficLight_EastWest_Green_T5[s, sPrime: Snapshot] {
        pre_TrafficLight_EastWest_Green_T5[s]
        pos_TrafficLight_EastWest_Green_T5[s, sPrime]
        semantics_TrafficLight_EastWest_Green_T5[s, sPrime]
    }

    pred enabledAfterStep_TrafficLight_EastWest_Green_T5[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        TrafficLight_EastWest_Green in s.conf
        _s.stable = True => {
            no t & {
                TrafficLight_EastWest_Red_T4 + 
                TrafficLight_EastWest_Yellow_T6 + 
                TrafficLight_EastWest_Green_T5
            }
            TrafficLight_End in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                TrafficLight_EastWest_Red_T4 + 
                TrafficLight_EastWest_Yellow_T6 + 
                TrafficLight_EastWest_Green_T5
            }
            TrafficLight_End in {_s.events  + genEvents}
        }
    }
    pred semantics_TrafficLight_EastWest_Green_T5[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = TrafficLight_EastWest_Green_T5
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + TrafficLight_EastWest_Green_T5
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                TrafficLight_EastWest_Red_T4 + 
                TrafficLight_EastWest_Yellow_T6 + 
                TrafficLight_EastWest_Green_T5
            }
        }
    }
    // Transition TrafficLight_EastWest_Yellow_T6
    pred pre_TrafficLight_EastWest_Yellow_T6[s:Snapshot] {
        TrafficLight_EastWest_Yellow in s.conf
        s.stable = True => {
            TrafficLight_Change in (s.events & EnvironmentEvent)
        } else {
            TrafficLight_Change in s.events
        }
    }

    pred pos_TrafficLight_EastWest_Yellow_T6[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - TrafficLight_EastWest_Yellow + {
            TrafficLight_EastWest_Red
        }
    
        testIfNextStable[s, sPrime, {none}, TrafficLight_EastWest_Yellow_T6] => {
            sPrime.stable = True
            s.stable = True => {
                no ((sPrime.events & InternalEvent) )
            } else {
                no ((sPrime.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            sPrime.stable = False
            s.stable = True => {
                sPrime.events & InternalEvent = {none}
                sPrime.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                sPrime.events = s.events + {none}
            }
        }
    }

    pred TrafficLight_EastWest_Yellow_T6[s, sPrime: Snapshot] {
        pre_TrafficLight_EastWest_Yellow_T6[s]
        pos_TrafficLight_EastWest_Yellow_T6[s, sPrime]
        semantics_TrafficLight_EastWest_Yellow_T6[s, sPrime]
    }

    pred enabledAfterStep_TrafficLight_EastWest_Yellow_T6[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        TrafficLight_EastWest_Yellow in s.conf
        _s.stable = True => {
            no t & {
                TrafficLight_EastWest_Red_T4 + 
                TrafficLight_EastWest_Yellow_T6 + 
                TrafficLight_EastWest_Green_T5
            }
            TrafficLight_Change in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                TrafficLight_EastWest_Red_T4 + 
                TrafficLight_EastWest_Yellow_T6 + 
                TrafficLight_EastWest_Green_T5
            }
            TrafficLight_Change in {_s.events  + genEvents}
        }
    }
    pred semantics_TrafficLight_EastWest_Yellow_T6[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = TrafficLight_EastWest_Yellow_T6
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + TrafficLight_EastWest_Yellow_T6
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                TrafficLight_EastWest_Red_T4 + 
                TrafficLight_EastWest_Yellow_T6 + 
                TrafficLight_EastWest_Green_T5
            }
        }
    }
    // Transition TrafficLight_EastWest_Red_T4
    pred pre_TrafficLight_EastWest_Red_T4[s:Snapshot] {
        TrafficLight_EastWest_Red in s.conf
        s.stable = True => {
            TrafficLight_Change in (s.events & EnvironmentEvent)
        } else {
            TrafficLight_Change in s.events
        }
    }

    pred pos_TrafficLight_EastWest_Red_T4[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - TrafficLight_EastWest_Red + {
            TrafficLight_EastWest_Green
        }
    
        testIfNextStable[s, sPrime, {none}, TrafficLight_EastWest_Red_T4] => {
            sPrime.stable = True
            s.stable = True => {
                no ((sPrime.events & InternalEvent) )
            } else {
                no ((sPrime.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            sPrime.stable = False
            s.stable = True => {
                sPrime.events & InternalEvent = {none}
                sPrime.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                sPrime.events = s.events + {none}
            }
        }
    }

    pred TrafficLight_EastWest_Red_T4[s, sPrime: Snapshot] {
        pre_TrafficLight_EastWest_Red_T4[s]
        pos_TrafficLight_EastWest_Red_T4[s, sPrime]
        semantics_TrafficLight_EastWest_Red_T4[s, sPrime]
    }

    pred enabledAfterStep_TrafficLight_EastWest_Red_T4[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        TrafficLight_EastWest_Red in s.conf
        _s.stable = True => {
            no t & {
                TrafficLight_EastWest_Red_T4 + 
                TrafficLight_EastWest_Yellow_T6 + 
                TrafficLight_EastWest_Green_T5
            }
            TrafficLight_Change in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                TrafficLight_EastWest_Red_T4 + 
                TrafficLight_EastWest_Yellow_T6 + 
                TrafficLight_EastWest_Green_T5
            }
            TrafficLight_Change in {_s.events  + genEvents}
        }
    }
    pred semantics_TrafficLight_EastWest_Red_T4[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = TrafficLight_EastWest_Red_T4
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + TrafficLight_EastWest_Red_T4
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                TrafficLight_EastWest_Red_T4 + 
                TrafficLight_EastWest_Yellow_T6 + 
                TrafficLight_EastWest_Green_T5
            }
        }
    }
/****************************** INITIAL CONDITIONS ****************************/
    pred init[s: Snapshot] {
        s.conf = {
            TrafficLight_NorthSouth_Green + 
            TrafficLight_EastWest_Red
        }
        no s.taken
        s.stable = True
        no s.events & InternalEvent
    }


/***************************** MODEL DEFINITION *******************************/
    pred operation[s, sPrime: Snapshot] {
        TrafficLight_NorthSouth_Green_T1[s, sPrime] or
        TrafficLight_NorthSouth_Yellow_T2[s, sPrime] or
        TrafficLight_NorthSouth_Red_T3[s, sPrime] or
        TrafficLight_EastWest_Green_T5[s, sPrime] or
        TrafficLight_EastWest_Yellow_T6[s, sPrime] or
        TrafficLight_EastWest_Red_T4[s, sPrime]
    }

    pred small_step[s, sPrime: Snapshot] {
        operation[s, sPrime]
    }

    pred testIfNextStable[s, sPrime: Snapshot, genEvents: set InternalEvent, t:TransitionLabel] {
        !enabledAfterStep_TrafficLight_NorthSouth_Green_T1[s, sPrime, t, genEvents]
        !enabledAfterStep_TrafficLight_NorthSouth_Yellow_T2[s, sPrime, t, genEvents]
        !enabledAfterStep_TrafficLight_NorthSouth_Red_T3[s, sPrime, t, genEvents]
        !enabledAfterStep_TrafficLight_EastWest_Green_T5[s, sPrime, t, genEvents]
        !enabledAfterStep_TrafficLight_EastWest_Yellow_T6[s, sPrime, t, genEvents]
        !enabledAfterStep_TrafficLight_EastWest_Red_T4[s, sPrime, t, genEvents]
    }

    pred isEnabled[s:Snapshot] {
        pre_TrafficLight_NorthSouth_Green_T1[s]or
        pre_TrafficLight_NorthSouth_Yellow_T2[s]or
        pre_TrafficLight_NorthSouth_Red_T3[s]or
        pre_TrafficLight_EastWest_Green_T5[s]or
        pre_TrafficLight_EastWest_Yellow_T6[s]or
        pre_TrafficLight_EastWest_Red_T4[s]
    }

    pred equals[s, sPrime: Snapshot] {
        sPrime.conf = s.conf
        sPrime.events = s.events
        sPrime.taken = s.taken
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
    run path for 5 Snapshot, 2 EventLabel
        expect 1



    
    // Get a significant model
    // Uncomment when not using the path-based instances option
    // run significance for exactly 7 Snapshot, 2 EventLabel expect 1
    
    fact {
        // Environemnt generates End First
        all s: Snapshot | {
            init[s] => s.events = TrafficLight_End
        }
        // Events alternate after each stable snapshot
        all s, sPrime: Snapshot | s->sPrime in nextState => {
            s.stable = True and sPrime.stable = True => sPrime.events != s.events
            s.stable = False and sPrime.stable = True => sPrime.events != s.events
            s.stable = True and sPrime.stable = False => sPrime.events = s.events
            s.stable = False and sPrime.stable = False => sPrime.events = s.events
        }
    }
    
    
    // Property fails is the single input assumption is not used
    
    assert ctl_no_both_lights_green  {
        ctl_mc[ag[{ s: Snapshot | s.stable = True => !(({
            TrafficLight_EastWest_Green + TrafficLight_NorthSouth_Green
        }
         in s.conf))}]]
    }
    
    check ctl_no_both_lights_green 
        for 10 Snapshot, exactly 2 EventLabel expect 0
    
    
    assert no_both_lights_green {
        no s: Snapshot | s.stable = True and {TrafficLight_EastWest_Green + TrafficLight_NorthSouth_Green} in s.conf
    }
    check no_both_lights_green for 10 Snapshot, exactly 2 EventLabel expect 0
    

