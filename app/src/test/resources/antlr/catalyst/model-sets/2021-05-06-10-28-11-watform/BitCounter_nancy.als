
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
    abstract sig Counter extends SystemState {}
    abstract sig Counter_Bit1 extends Counter {}
    one sig Counter_Bit1_Bit11 extends Counter_Bit1 {}
    one sig Counter_Bit1_Bit12 extends Counter_Bit1 {}
    abstract sig Counter_Bit2 extends Counter {}
    one sig Counter_Bit2_Bit21 extends Counter_Bit2 {}
    one sig Counter_Bit2_Bit22 extends Counter_Bit2 {}

/***************************** EVENTS SPACE ***********************************/
    one sig Counter_Tk0 extends EnvironmentEvent {}
    one sig Counter_Bit1_Tk1 extends InternalEvent {}
    one sig Counter_Bit2_Done extends InternalEvent {}

/*************************** TRANSITIONS SPACE ********************************/
    one sig Counter_Bit1_T1 extends TransitionLabel {}
    one sig Counter_Bit1_T2 extends TransitionLabel {}
    one sig Counter_Bit2_T3 extends TransitionLabel {}
    one sig Counter_Bit2_T4 extends TransitionLabel {}

    // Transition Counter_Bit1_T1
    pred pre_Counter_Bit1_T1[s:Snapshot] {
        Counter_Bit1_Bit11 in s.conf
        s.stable = True => {
            Counter_Tk0 in (s.events & EnvironmentEvent)
        } else {
            Counter_Tk0 in s.events
        }
    }

    pred pos_Counter_Bit1_T1[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - Counter_Bit1_Bit11 + {
            Counter_Bit1_Bit12
        }
    
        testIfNextStable[s, sPrime, {none}, Counter_Bit1_T1] => {
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

    pred Counter_Bit1_T1[s, sPrime: Snapshot] {
        pre_Counter_Bit1_T1[s]
        pos_Counter_Bit1_T1[s, sPrime]
        semantics_Counter_Bit1_T1[s, sPrime]
    }

    pred enabledAfterStep_Counter_Bit1_T1[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        Counter_Bit1_Bit11 in s.conf
        _s.stable = True => {
            no t & {
                Counter_Bit1_T2 + 
                Counter_Bit1_T1
            }
            Counter_Tk0 in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                Counter_Bit1_T2 + 
                Counter_Bit1_T1
            }
            Counter_Tk0 in {_s.events  + genEvents}
        }
    }
    pred semantics_Counter_Bit1_T1[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = Counter_Bit1_T1
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + Counter_Bit1_T1
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                Counter_Bit1_T2 + 
                Counter_Bit1_T1
            }
        }
    }
    // Transition Counter_Bit1_T2
    pred pre_Counter_Bit1_T2[s:Snapshot] {
        Counter_Bit1_Bit12 in s.conf
        s.stable = True => {
            Counter_Tk0 in (s.events & EnvironmentEvent)
        } else {
            Counter_Tk0 in s.events
        }
    }

    pred pos_Counter_Bit1_T2[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - Counter_Bit1_Bit12 + {
            Counter_Bit1_Bit11
        }
    
        testIfNextStable[s, sPrime, {Counter_Bit1_Tk1}, Counter_Bit1_T2] => {
            sPrime.stable = True
            s.stable = True => {
                no ((sPrime.events & InternalEvent) - {Counter_Bit1_Tk1})
            } else {
                no ((sPrime.events & InternalEvent) - {{Counter_Bit1_Tk1} + (InternalEvent & s.events)})
            }
        } else {
            sPrime.stable = False
            s.stable = True => {
                sPrime.events & InternalEvent = {Counter_Bit1_Tk1}
                sPrime.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                sPrime.events = s.events + {Counter_Bit1_Tk1}
            }
        }
        {Counter_Bit1_Tk1} in sPrime.events
    }

    pred Counter_Bit1_T2[s, sPrime: Snapshot] {
        pre_Counter_Bit1_T2[s]
        pos_Counter_Bit1_T2[s, sPrime]
        semantics_Counter_Bit1_T2[s, sPrime]
    }

    pred enabledAfterStep_Counter_Bit1_T2[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        Counter_Bit1_Bit12 in s.conf
        _s.stable = True => {
            no t & {
                Counter_Bit1_T2 + 
                Counter_Bit1_T1
            }
            Counter_Tk0 in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                Counter_Bit1_T2 + 
                Counter_Bit1_T1
            }
            Counter_Tk0 in {_s.events  + genEvents}
        }
    }
    pred semantics_Counter_Bit1_T2[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = Counter_Bit1_T2
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + Counter_Bit1_T2
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                Counter_Bit1_T2 + 
                Counter_Bit1_T1
            }
        }
    }
    // Transition Counter_Bit2_T3
    pred pre_Counter_Bit2_T3[s:Snapshot] {
        Counter_Bit2_Bit21 in s.conf
        s.stable = True => {
            Counter_Bit1_Tk1 in (s.events & EnvironmentEvent)
        } else {
            Counter_Bit1_Tk1 in s.events
        }
    }

    pred pos_Counter_Bit2_T3[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - Counter_Bit2_Bit21 + {
            Counter_Bit2_Bit22
        }
    
        testIfNextStable[s, sPrime, {none}, Counter_Bit2_T3] => {
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

    pred Counter_Bit2_T3[s, sPrime: Snapshot] {
        pre_Counter_Bit2_T3[s]
        pos_Counter_Bit2_T3[s, sPrime]
        semantics_Counter_Bit2_T3[s, sPrime]
    }

    pred enabledAfterStep_Counter_Bit2_T3[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        Counter_Bit2_Bit21 in s.conf
        _s.stable = True => {
            no t & {
                Counter_Bit2_T3 + 
                Counter_Bit2_T4
            }
            Counter_Bit1_Tk1 in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                Counter_Bit2_T3 + 
                Counter_Bit2_T4
            }
            Counter_Bit1_Tk1 in {_s.events  + genEvents}
        }
    }
    pred semantics_Counter_Bit2_T3[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = Counter_Bit2_T3
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + Counter_Bit2_T3
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                Counter_Bit2_T3 + 
                Counter_Bit2_T4
            }
        }
    }
    // Transition Counter_Bit2_T4
    pred pre_Counter_Bit2_T4[s:Snapshot] {
        Counter_Bit2_Bit22 in s.conf
        s.stable = True => {
            Counter_Bit1_Tk1 in (s.events & EnvironmentEvent)
        } else {
            Counter_Bit1_Tk1 in s.events
        }
    }

    pred pos_Counter_Bit2_T4[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - Counter_Bit2_Bit22 + {
            Counter_Bit2_Bit21
        }
    
        testIfNextStable[s, sPrime, {Counter_Bit2_Done}, Counter_Bit2_T4] => {
            sPrime.stable = True
            s.stable = True => {
                no ((sPrime.events & InternalEvent) - {Counter_Bit2_Done})
            } else {
                no ((sPrime.events & InternalEvent) - {{Counter_Bit2_Done} + (InternalEvent & s.events)})
            }
        } else {
            sPrime.stable = False
            s.stable = True => {
                sPrime.events & InternalEvent = {Counter_Bit2_Done}
                sPrime.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                sPrime.events = s.events + {Counter_Bit2_Done}
            }
        }
        {Counter_Bit2_Done} in sPrime.events
    }

    pred Counter_Bit2_T4[s, sPrime: Snapshot] {
        pre_Counter_Bit2_T4[s]
        pos_Counter_Bit2_T4[s, sPrime]
        semantics_Counter_Bit2_T4[s, sPrime]
    }

    pred enabledAfterStep_Counter_Bit2_T4[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        Counter_Bit2_Bit22 in s.conf
        _s.stable = True => {
            no t & {
                Counter_Bit2_T3 + 
                Counter_Bit2_T4
            }
            Counter_Bit1_Tk1 in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                Counter_Bit2_T3 + 
                Counter_Bit2_T4
            }
            Counter_Bit1_Tk1 in {_s.events  + genEvents}
        }
    }
    pred semantics_Counter_Bit2_T4[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = Counter_Bit2_T4
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + Counter_Bit2_T4
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                Counter_Bit2_T3 + 
                Counter_Bit2_T4
            }
        }
    }
/****************************** INITIAL CONDITIONS ****************************/
    pred init[s: Snapshot] {
        s.conf = {
            Counter_Bit1_Bit11 + 
            Counter_Bit2_Bit21
        }
        no s.taken
        s.stable = True
        no s.events & InternalEvent
    }


/***************************** MODEL DEFINITION *******************************/
    pred operation[s, sPrime: Snapshot] {
        Counter_Bit1_T1[s, sPrime] or
        Counter_Bit1_T2[s, sPrime] or
        Counter_Bit2_T3[s, sPrime] or
        Counter_Bit2_T4[s, sPrime]
    }

    pred small_step[s, sPrime: Snapshot] {
        operation[s, sPrime]
    }

    pred testIfNextStable[s, sPrime: Snapshot, genEvents: set InternalEvent, t:TransitionLabel] {
        !enabledAfterStep_Counter_Bit1_T1[s, sPrime, t, genEvents]
        !enabledAfterStep_Counter_Bit1_T2[s, sPrime, t, genEvents]
        !enabledAfterStep_Counter_Bit2_T3[s, sPrime, t, genEvents]
        !enabledAfterStep_Counter_Bit2_T4[s, sPrime, t, genEvents]
    }

    pred isEnabled[s:Snapshot] {
        pre_Counter_Bit1_T1[s]or
        pre_Counter_Bit1_T2[s]or
        pre_Counter_Bit2_T3[s]or
        pre_Counter_Bit2_T4[s]
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
    run path for 5 Snapshot, 3 EventLabel
        expect 1



    
    // The final status of bits when done counting
    
    assert ctl_final_bitStatus  {
        ctl_mc[ag[{ s: Snapshot | s.stable = True => (Counter_Bit2_Done in s.events => {
            Counter_Bit1_Bit11 + Counter_Bit2_Bit21
        }
         in s.conf)}]]
    }
    
    check ctl_final_bitStatus 
        for 7 Snapshot, exactly 2 EventLabel expect 0
    
    
    assert final_bitStatus {
        all s: Snapshot| s.stable = True and Counter_Bit2_Done in s.events =>
            {Counter_Bit1_Bit11 + Counter_Bit2_Bit21} in s.conf
    }
    check final_bitStatus for 7 Snapshot, exactly 2 EventLabel expect 0
    
    
    // The bitcounter has a significant scope of 10 Snapshots
    // Uncomment when not using the path-based instances option
    // run significance for 7 Snapshot, exactly 2 EventLabel expect 1
    
    // Model is responsive
    assert ctl_model_responsive {
        ctl_mc[
            ag[
                imp_ctl[
                    {s: Snapshot| Counter_Tk0 in s.events},
                    af[{s: Snapshot |
                        s.stable = True and
                        (Counter_Bit1_T1 in s.taken or
                        Counter_Bit1_T2 in s.taken or
                        Counter_Bit2_T3 in s.taken or
                        Counter_Bit2_T4 in s.taken)
                    }]
                ]
            ]
        ]
    }
    check ctl_model_responsive for 7 Snapshot, exactly 2 EventLabel expect 0
    
    
    assert model_responsive {
        all s: Snapshot | Counter_Tk0 in s.events and some nextStep =>
            some sPrime: s.*nextStep | sPrime.stable = True and
                        (Counter_Bit1_T1 in sPrime.taken or
                        Counter_Bit1_T2 in sPrime.taken or
                        Counter_Bit2_T3 in sPrime.taken or
                        Counter_Bit2_T4 in sPrime.taken)
    }
    check model_responsive for 7 Snapshot, exactly 2 EventLabel expect 0
    

