
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
    abstract sig RDT extends SystemState {}
    abstract sig RDT_Sender extends RDT {}
    one sig RDT_Sender_ReadySendNext extends RDT_Sender {}
    one sig RDT_Sender_WaitAck extends RDT_Sender {}
    one sig RDT_Sender_ReadyResend extends RDT_Sender {}
    abstract sig RDT_Receiver extends RDT {}
    one sig RDT_Receiver_ReadyReceiveNext extends RDT_Receiver {}
    one sig RDT_Receiver_ReceiveSuccess extends RDT_Receiver {}
    one sig RDT_Receiver_ReceiveError extends RDT_Receiver {}
    one sig RDT_Receiver_ReadyReceiveResend extends RDT_Receiver {}

/***************************** EVENTS SPACE ***********************************/
    one sig RDT_SendSuccess extends EnvironmentEvent {}
    one sig RDT_SendError extends EnvironmentEvent {}
    one sig RDT_AckSuccess extends EnvironmentEvent {}
    one sig RDT_AckError extends EnvironmentEvent {}

/*************************** TRANSITIONS SPACE ********************************/
    one sig RDT_Sender_OnSendSucess_1 extends TransitionLabel {}
    one sig RDT_Sender_OnSendSucess_2 extends TransitionLabel {}
    one sig RDT_Sender_OnSendSucess_3 extends TransitionLabel {}
    one sig RDT_Sender_OnSendError_4 extends TransitionLabel {}
    one sig RDT_Sender_OnSendError_5 extends TransitionLabel {}
    one sig RDT_Sender_OnSendError_6 extends TransitionLabel {}
    one sig RDT_Sender_t_3 extends TransitionLabel {}
    one sig RDT_Sender_t_4 extends TransitionLabel {}
    one sig RDT_Receiver__7 extends TransitionLabel {}
    one sig RDT_Receiver__8 extends TransitionLabel {}
    one sig RDT_Receiver__9 extends TransitionLabel {}
    one sig RDT_Receiver__10 extends TransitionLabel {}
    one sig RDT_Receiver__11 extends TransitionLabel {}
    one sig RDT_Receiver__12 extends TransitionLabel {}
    one sig RDT_Receiver__13 extends TransitionLabel {}
    one sig RDT_Receiver__14 extends TransitionLabel {}
    one sig RDT_Receiver_t_13 extends TransitionLabel {}
    one sig RDT_Receiver_t_14 extends TransitionLabel {}
    one sig RDT_Receiver_t_15 extends TransitionLabel {}

    // Transition RDT_Sender_OnSendSucess_1
    pred pre_RDT_Sender_OnSendSucess_1[s:Snapshot] {
        RDT_Sender_ReadySendNext in s.conf
        s.stable = True => {
            RDT_SendSuccess in (s.events & EnvironmentEvent)
        } else {
            RDT_SendSuccess in s.events
        }
    }

    pred pos_RDT_Sender_OnSendSucess_1[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Sender_ReadySendNext + {
            RDT_Sender_WaitAck
        }
    
        testIfNextStable[s, s', {none}, RDT_Sender_OnSendSucess_1] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Sender_OnSendSucess_1[s, s': Snapshot] {
        pre_RDT_Sender_OnSendSucess_1[s]
        pos_RDT_Sender_OnSendSucess_1[s, s']
        semantics_RDT_Sender_OnSendSucess_1[s, s']
    }

    pred enabledAfterStep_RDT_Sender_OnSendSucess_1[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Sender_ReadySendNext in s.conf
        _s.stable = True => {
            no t & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
            RDT_SendSuccess in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
            RDT_SendSuccess in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Sender_OnSendSucess_1[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Sender_OnSendSucess_1
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Sender_OnSendSucess_1
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
        }
    }
    // Transition RDT_Sender_OnSendSucess_2
    pred pre_RDT_Sender_OnSendSucess_2[s:Snapshot] {
        RDT_Sender_WaitAck in s.conf
        s.stable = True => {
            RDT_SendSuccess in (s.events & EnvironmentEvent)
        } else {
            RDT_SendSuccess in s.events
        }
    }

    pred pos_RDT_Sender_OnSendSucess_2[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Sender_WaitAck + {
            RDT_Sender_WaitAck
        }
    
        testIfNextStable[s, s', {none}, RDT_Sender_OnSendSucess_2] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Sender_OnSendSucess_2[s, s': Snapshot] {
        pre_RDT_Sender_OnSendSucess_2[s]
        pos_RDT_Sender_OnSendSucess_2[s, s']
        semantics_RDT_Sender_OnSendSucess_2[s, s']
    }

    pred enabledAfterStep_RDT_Sender_OnSendSucess_2[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Sender_WaitAck in s.conf
        _s.stable = True => {
            no t & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
            RDT_SendSuccess in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
            RDT_SendSuccess in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Sender_OnSendSucess_2[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Sender_OnSendSucess_2
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Sender_OnSendSucess_2
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
        }
    }
    // Transition RDT_Sender_OnSendSucess_3
    pred pre_RDT_Sender_OnSendSucess_3[s:Snapshot] {
        RDT_Sender_ReadyResend in s.conf
        s.stable = True => {
            RDT_SendSuccess in (s.events & EnvironmentEvent)
        } else {
            RDT_SendSuccess in s.events
        }
    }

    pred pos_RDT_Sender_OnSendSucess_3[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Sender_ReadyResend + {
            RDT_Sender_WaitAck
        }
    
        testIfNextStable[s, s', {none}, RDT_Sender_OnSendSucess_3] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Sender_OnSendSucess_3[s, s': Snapshot] {
        pre_RDT_Sender_OnSendSucess_3[s]
        pos_RDT_Sender_OnSendSucess_3[s, s']
        semantics_RDT_Sender_OnSendSucess_3[s, s']
    }

    pred enabledAfterStep_RDT_Sender_OnSendSucess_3[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Sender_ReadyResend in s.conf
        _s.stable = True => {
            no t & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
            RDT_SendSuccess in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
            RDT_SendSuccess in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Sender_OnSendSucess_3[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Sender_OnSendSucess_3
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Sender_OnSendSucess_3
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
        }
    }
    // Transition RDT_Sender_OnSendError_4
    pred pre_RDT_Sender_OnSendError_4[s:Snapshot] {
        RDT_Sender_ReadySendNext in s.conf
        s.stable = True => {
            RDT_SendError in (s.events & EnvironmentEvent)
        } else {
            RDT_SendError in s.events
        }
    }

    pred pos_RDT_Sender_OnSendError_4[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Sender_ReadySendNext + {
            RDT_Sender_WaitAck
        }
    
        testIfNextStable[s, s', {none}, RDT_Sender_OnSendError_4] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Sender_OnSendError_4[s, s': Snapshot] {
        pre_RDT_Sender_OnSendError_4[s]
        pos_RDT_Sender_OnSendError_4[s, s']
        semantics_RDT_Sender_OnSendError_4[s, s']
    }

    pred enabledAfterStep_RDT_Sender_OnSendError_4[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Sender_ReadySendNext in s.conf
        _s.stable = True => {
            no t & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
            RDT_SendError in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
            RDT_SendError in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Sender_OnSendError_4[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Sender_OnSendError_4
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Sender_OnSendError_4
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
        }
    }
    // Transition RDT_Sender_OnSendError_5
    pred pre_RDT_Sender_OnSendError_5[s:Snapshot] {
        RDT_Sender_WaitAck in s.conf
        s.stable = True => {
            RDT_SendError in (s.events & EnvironmentEvent)
        } else {
            RDT_SendError in s.events
        }
    }

    pred pos_RDT_Sender_OnSendError_5[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Sender_WaitAck + {
            RDT_Sender_WaitAck
        }
    
        testIfNextStable[s, s', {none}, RDT_Sender_OnSendError_5] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Sender_OnSendError_5[s, s': Snapshot] {
        pre_RDT_Sender_OnSendError_5[s]
        pos_RDT_Sender_OnSendError_5[s, s']
        semantics_RDT_Sender_OnSendError_5[s, s']
    }

    pred enabledAfterStep_RDT_Sender_OnSendError_5[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Sender_WaitAck in s.conf
        _s.stable = True => {
            no t & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendError_5 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4
            }
            RDT_SendError in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendError_5 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4
            }
            RDT_SendError in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Sender_OnSendError_5[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Sender_OnSendError_5
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Sender_OnSendError_5
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendError_5 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4
            }
        }
    }
    // Transition RDT_Sender_OnSendError_6
    pred pre_RDT_Sender_OnSendError_6[s:Snapshot] {
        RDT_Sender_ReadyResend in s.conf
        s.stable = True => {
            RDT_SendError in (s.events & EnvironmentEvent)
        } else {
            RDT_SendError in s.events
        }
    }

    pred pos_RDT_Sender_OnSendError_6[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Sender_ReadyResend + {
            RDT_Sender_WaitAck
        }
    
        testIfNextStable[s, s', {none}, RDT_Sender_OnSendError_6] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Sender_OnSendError_6[s, s': Snapshot] {
        pre_RDT_Sender_OnSendError_6[s]
        pos_RDT_Sender_OnSendError_6[s, s']
        semantics_RDT_Sender_OnSendError_6[s, s']
    }

    pred enabledAfterStep_RDT_Sender_OnSendError_6[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Sender_ReadyResend in s.conf
        _s.stable = True => {
            no t & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
            RDT_SendError in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
            RDT_SendError in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Sender_OnSendError_6[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Sender_OnSendError_6
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Sender_OnSendError_6
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
        }
    }
    // Transition RDT_Sender_t_3
    pred pre_RDT_Sender_t_3[s:Snapshot] {
        RDT_Sender_WaitAck in s.conf
        s.stable = True => {
            RDT_AckSuccess in (s.events & EnvironmentEvent)
        } else {
            RDT_AckSuccess in s.events
        }
    }

    pred pos_RDT_Sender_t_3[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Sender_WaitAck + {
            RDT_Sender_ReadySendNext
        }
    
        testIfNextStable[s, s', {none}, RDT_Sender_t_3] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Sender_t_3[s, s': Snapshot] {
        pre_RDT_Sender_t_3[s]
        pos_RDT_Sender_t_3[s, s']
        semantics_RDT_Sender_t_3[s, s']
    }

    pred enabledAfterStep_RDT_Sender_t_3[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Sender_WaitAck in s.conf
        _s.stable = True => {
            no t & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
            RDT_AckSuccess in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
            RDT_AckSuccess in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Sender_t_3[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Sender_t_3
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Sender_t_3
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendError_5
            }
        }
    }
    // Transition RDT_Sender_t_4
    pred pre_RDT_Sender_t_4[s:Snapshot] {
        RDT_Sender_WaitAck in s.conf
        s.stable = True => {
            RDT_AckError in (s.events & EnvironmentEvent)
        } else {
            RDT_AckError in s.events
        }
    }

    pred pos_RDT_Sender_t_4[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Sender_WaitAck + {
            RDT_Sender_ReadyResend
        }
    
        testIfNextStable[s, s', {none}, RDT_Sender_t_4] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Sender_t_4[s, s': Snapshot] {
        pre_RDT_Sender_t_4[s]
        pos_RDT_Sender_t_4[s, s']
        semantics_RDT_Sender_t_4[s, s']
    }

    pred enabledAfterStep_RDT_Sender_t_4[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Sender_WaitAck in s.conf
        _s.stable = True => {
            no t & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_OnSendError_5
            }
            RDT_AckError in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_OnSendError_5
            }
            RDT_AckError in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Sender_t_4[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Sender_t_4
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Sender_t_4
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Sender_OnSendError_4 + 
                RDT_Sender_t_3 + 
                RDT_Sender_OnSendSucess_1 + 
                RDT_Sender_OnSendSucess_3 + 
                RDT_Sender_OnSendError_6 + 
                RDT_Sender_t_4 + 
                RDT_Sender_OnSendSucess_2 + 
                RDT_Sender_OnSendError_5
            }
        }
    }
    // Transition RDT_Receiver__7
    pred pre_RDT_Receiver__7[s:Snapshot] {
        RDT_Receiver_ReadyReceiveNext in s.conf
        s.stable = True => {
            RDT_SendSuccess in (s.events & EnvironmentEvent)
        } else {
            RDT_SendSuccess in s.events
        }
    }

    pred pos_RDT_Receiver__7[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Receiver_ReadyReceiveNext + {
            RDT_Receiver_ReceiveSuccess
        }
    
        testIfNextStable[s, s', {none}, RDT_Receiver__7] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Receiver__7[s, s': Snapshot] {
        pre_RDT_Receiver__7[s]
        pos_RDT_Receiver__7[s, s']
        semantics_RDT_Receiver__7[s, s']
    }

    pred enabledAfterStep_RDT_Receiver__7[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Receiver_ReadyReceiveNext in s.conf
        _s.stable = True => {
            no t & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver__7 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_SendSuccess in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver__7 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_SendSuccess in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Receiver__7[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Receiver__7
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Receiver__7
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver__7 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
        }
    }
    // Transition RDT_Receiver__8
    pred pre_RDT_Receiver__8[s:Snapshot] {
        RDT_Receiver_ReceiveSuccess in s.conf
        s.stable = True => {
            RDT_SendSuccess in (s.events & EnvironmentEvent)
        } else {
            RDT_SendSuccess in s.events
        }
    }

    pred pos_RDT_Receiver__8[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Receiver_ReceiveSuccess + {
            RDT_Receiver_ReceiveSuccess
        }
    
        testIfNextStable[s, s', {none}, RDT_Receiver__8] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Receiver__8[s, s': Snapshot] {
        pre_RDT_Receiver__8[s]
        pos_RDT_Receiver__8[s, s']
        semantics_RDT_Receiver__8[s, s']
    }

    pred enabledAfterStep_RDT_Receiver__8[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Receiver_ReceiveSuccess in s.conf
        _s.stable = True => {
            no t & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_SendSuccess in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_SendSuccess in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Receiver__8[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Receiver__8
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Receiver__8
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
        }
    }
    // Transition RDT_Receiver__9
    pred pre_RDT_Receiver__9[s:Snapshot] {
        RDT_Receiver_ReceiveError in s.conf
        s.stable = True => {
            RDT_SendSuccess in (s.events & EnvironmentEvent)
        } else {
            RDT_SendSuccess in s.events
        }
    }

    pred pos_RDT_Receiver__9[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Receiver_ReceiveError + {
            RDT_Receiver_ReceiveSuccess
        }
    
        testIfNextStable[s, s', {none}, RDT_Receiver__9] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Receiver__9[s, s': Snapshot] {
        pre_RDT_Receiver__9[s]
        pos_RDT_Receiver__9[s, s']
        semantics_RDT_Receiver__9[s, s']
    }

    pred enabledAfterStep_RDT_Receiver__9[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Receiver_ReceiveError in s.conf
        _s.stable = True => {
            no t & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver__9 + 
                RDT_Receiver_t_14
            }
            RDT_SendSuccess in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver__9 + 
                RDT_Receiver_t_14
            }
            RDT_SendSuccess in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Receiver__9[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Receiver__9
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Receiver__9
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver__9 + 
                RDT_Receiver_t_14
            }
        }
    }
    // Transition RDT_Receiver__10
    pred pre_RDT_Receiver__10[s:Snapshot] {
        RDT_Receiver_ReadyReceiveResend in s.conf
        s.stable = True => {
            RDT_SendSuccess in (s.events & EnvironmentEvent)
        } else {
            RDT_SendSuccess in s.events
        }
    }

    pred pos_RDT_Receiver__10[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Receiver_ReadyReceiveResend + {
            RDT_Receiver_ReceiveSuccess
        }
    
        testIfNextStable[s, s', {none}, RDT_Receiver__10] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Receiver__10[s, s': Snapshot] {
        pre_RDT_Receiver__10[s]
        pos_RDT_Receiver__10[s, s']
        semantics_RDT_Receiver__10[s, s']
    }

    pred enabledAfterStep_RDT_Receiver__10[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Receiver_ReadyReceiveResend in s.conf
        _s.stable = True => {
            no t & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_SendSuccess in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_SendSuccess in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Receiver__10[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Receiver__10
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Receiver__10
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
        }
    }
    // Transition RDT_Receiver__11
    pred pre_RDT_Receiver__11[s:Snapshot] {
        RDT_Receiver_ReadyReceiveNext in s.conf
        s.stable = True => {
            RDT_SendError in (s.events & EnvironmentEvent)
        } else {
            RDT_SendError in s.events
        }
    }

    pred pos_RDT_Receiver__11[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Receiver_ReadyReceiveNext + {
            RDT_Receiver_ReceiveError
        }
    
        testIfNextStable[s, s', {none}, RDT_Receiver__11] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Receiver__11[s, s': Snapshot] {
        pre_RDT_Receiver__11[s]
        pos_RDT_Receiver__11[s, s']
        semantics_RDT_Receiver__11[s, s']
    }

    pred enabledAfterStep_RDT_Receiver__11[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Receiver_ReadyReceiveNext in s.conf
        _s.stable = True => {
            no t & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_SendError in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_SendError in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Receiver__11[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Receiver__11
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Receiver__11
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
        }
    }
    // Transition RDT_Receiver__12
    pred pre_RDT_Receiver__12[s:Snapshot] {
        RDT_Receiver_ReceiveSuccess in s.conf
        s.stable = True => {
            RDT_SendError in (s.events & EnvironmentEvent)
        } else {
            RDT_SendError in s.events
        }
    }

    pred pos_RDT_Receiver__12[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Receiver_ReceiveSuccess + {
            RDT_Receiver_ReceiveError
        }
    
        testIfNextStable[s, s', {none}, RDT_Receiver__12] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Receiver__12[s, s': Snapshot] {
        pre_RDT_Receiver__12[s]
        pos_RDT_Receiver__12[s, s']
        semantics_RDT_Receiver__12[s, s']
    }

    pred enabledAfterStep_RDT_Receiver__12[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Receiver_ReceiveSuccess in s.conf
        _s.stable = True => {
            no t & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_SendError in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_SendError in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Receiver__12[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Receiver__12
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Receiver__12
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
        }
    }
    // Transition RDT_Receiver__13
    pred pre_RDT_Receiver__13[s:Snapshot] {
        RDT_Receiver_ReceiveError in s.conf
        s.stable = True => {
            RDT_SendError in (s.events & EnvironmentEvent)
        } else {
            RDT_SendError in s.events
        }
    }

    pred pos_RDT_Receiver__13[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Receiver_ReceiveError + {
            RDT_Receiver_ReceiveError
        }
    
        testIfNextStable[s, s', {none}, RDT_Receiver__13] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Receiver__13[s, s': Snapshot] {
        pre_RDT_Receiver__13[s]
        pos_RDT_Receiver__13[s, s']
        semantics_RDT_Receiver__13[s, s']
    }

    pred enabledAfterStep_RDT_Receiver__13[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Receiver_ReceiveError in s.conf
        _s.stable = True => {
            no t & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_SendError in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_SendError in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Receiver__13[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Receiver__13
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Receiver__13
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
        }
    }
    // Transition RDT_Receiver__14
    pred pre_RDT_Receiver__14[s:Snapshot] {
        RDT_Receiver_ReadyReceiveResend in s.conf
        s.stable = True => {
            RDT_SendError in (s.events & EnvironmentEvent)
        } else {
            RDT_SendError in s.events
        }
    }

    pred pos_RDT_Receiver__14[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Receiver_ReadyReceiveResend + {
            RDT_Receiver_ReceiveError
        }
    
        testIfNextStable[s, s', {none}, RDT_Receiver__14] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Receiver__14[s, s': Snapshot] {
        pre_RDT_Receiver__14[s]
        pos_RDT_Receiver__14[s, s']
        semantics_RDT_Receiver__14[s, s']
    }

    pred enabledAfterStep_RDT_Receiver__14[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Receiver_ReadyReceiveResend in s.conf
        _s.stable = True => {
            no t & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_SendError in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_SendError in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Receiver__14[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Receiver__14
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Receiver__14
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
        }
    }
    // Transition RDT_Receiver_t_13
    pred pre_RDT_Receiver_t_13[s:Snapshot] {
        RDT_Receiver_ReceiveError in s.conf
        s.stable = True => {
            RDT_AckError in (s.events & EnvironmentEvent)
        } else {
            RDT_AckError in s.events
        }
    }

    pred pos_RDT_Receiver_t_13[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Receiver_ReceiveError + {
            RDT_Receiver_ReadyReceiveResend
        }
    
        testIfNextStable[s, s', {none}, RDT_Receiver_t_13] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Receiver_t_13[s, s': Snapshot] {
        pre_RDT_Receiver_t_13[s]
        pos_RDT_Receiver_t_13[s, s']
        semantics_RDT_Receiver_t_13[s, s']
    }

    pred enabledAfterStep_RDT_Receiver_t_13[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Receiver_ReceiveError in s.conf
        _s.stable = True => {
            no t & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_AckError in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_AckError in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Receiver_t_13[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Receiver_t_13
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Receiver_t_13
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
        }
    }
    // Transition RDT_Receiver_t_14
    pred pre_RDT_Receiver_t_14[s:Snapshot] {
        RDT_Receiver_ReceiveSuccess in s.conf
        s.stable = True => {
            RDT_AckSuccess in (s.events & EnvironmentEvent)
        } else {
            RDT_AckSuccess in s.events
        }
    }

    pred pos_RDT_Receiver_t_14[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Receiver_ReceiveSuccess + {
            RDT_Receiver_ReadyReceiveNext
        }
    
        testIfNextStable[s, s', {none}, RDT_Receiver_t_14] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Receiver_t_14[s, s': Snapshot] {
        pre_RDT_Receiver_t_14[s]
        pos_RDT_Receiver_t_14[s, s']
        semantics_RDT_Receiver_t_14[s, s']
    }

    pred enabledAfterStep_RDT_Receiver_t_14[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Receiver_ReceiveSuccess in s.conf
        _s.stable = True => {
            no t & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_AckSuccess in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_AckSuccess in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Receiver_t_14[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Receiver_t_14
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Receiver_t_14
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
        }
    }
    // Transition RDT_Receiver_t_15
    pred pre_RDT_Receiver_t_15[s:Snapshot] {
        RDT_Receiver_ReceiveSuccess in s.conf
        s.stable = True => {
            RDT_AckError in (s.events & EnvironmentEvent)
        } else {
            RDT_AckError in s.events
        }
    }

    pred pos_RDT_Receiver_t_15[s, s':Snapshot] {
        s'.conf = s.conf - RDT_Receiver_ReceiveSuccess + {
            RDT_Receiver_ReadyReceiveNext
        }
    
        testIfNextStable[s, s', {none}, RDT_Receiver_t_15] => {
            s'.stable = True
            s.stable = True => {
                no ((s'.events & InternalEvent) )
            } else {
                no ((s'.events & InternalEvent) - { (InternalEvent & s.events)})
            }
        } else {
            s'.stable = False
            s.stable = True => {
                s'.events & InternalEvent = {none}
                s'.events & EnvironmentEvent = s.events & EnvironmentEvent
            } else {
                s'.events = s.events + {none}
            }
        }
    }

    pred RDT_Receiver_t_15[s, s': Snapshot] {
        pre_RDT_Receiver_t_15[s]
        pos_RDT_Receiver_t_15[s, s']
        semantics_RDT_Receiver_t_15[s, s']
    }

    pred enabledAfterStep_RDT_Receiver_t_15[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        RDT_Receiver_ReceiveSuccess in s.conf
        _s.stable = True => {
            no t & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_AckError in {(_s.events & EnvironmentEvent)  + genEvents}
        } else {
            no {_s.taken + t} & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
            RDT_AckError in {_s.events  + genEvents}
        }
    }
    pred semantics_RDT_Receiver_t_15[s, s': Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            s'.taken = RDT_Receiver_t_15
        } else {
            // SINGLE semantics
            s'.taken = s.taken + RDT_Receiver_t_15
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                RDT_Receiver__11 + 
                RDT_Receiver__12 + 
                RDT_Receiver__14 + 
                RDT_Receiver__10 + 
                RDT_Receiver_t_15 + 
                RDT_Receiver_t_13 + 
                RDT_Receiver__7 + 
                RDT_Receiver__8 + 
                RDT_Receiver__13 + 
                RDT_Receiver_t_14 + 
                RDT_Receiver__9
            }
        }
    }
/****************************** INITIAL CONDITIONS ****************************/
    pred init[s: Snapshot] {
        s.conf = {
            RDT_Sender_ReadySendNext + 
            RDT_Receiver_ReadyReceiveNext
        }
        no s.taken
        s.stable = True
        no s.events & InternalEvent
    }


/***************************** MODEL DEFINITION *******************************/
    pred operation[s, s': Snapshot] {
        RDT_Sender_OnSendSucess_1[s, s'] or
        RDT_Sender_OnSendSucess_2[s, s'] or
        RDT_Sender_OnSendSucess_3[s, s'] or
        RDT_Sender_OnSendError_4[s, s'] or
        RDT_Sender_OnSendError_5[s, s'] or
        RDT_Sender_OnSendError_6[s, s'] or
        RDT_Sender_t_3[s, s'] or
        RDT_Sender_t_4[s, s'] or
        RDT_Receiver__7[s, s'] or
        RDT_Receiver__8[s, s'] or
        RDT_Receiver__9[s, s'] or
        RDT_Receiver__10[s, s'] or
        RDT_Receiver__11[s, s'] or
        RDT_Receiver__12[s, s'] or
        RDT_Receiver__13[s, s'] or
        RDT_Receiver__14[s, s'] or
        RDT_Receiver_t_13[s, s'] or
        RDT_Receiver_t_14[s, s'] or
        RDT_Receiver_t_15[s, s']
    }

    pred small_step[s, s': Snapshot] {
        operation[s, s']
    }

    pred testIfNextStable[s, s': Snapshot, genEvents: set InternalEvent, t:TransitionLabel] {
        !enabledAfterStep_RDT_Sender_OnSendSucess_1[s, s', t, genEvents]
        !enabledAfterStep_RDT_Sender_OnSendSucess_2[s, s', t, genEvents]
        !enabledAfterStep_RDT_Sender_OnSendSucess_3[s, s', t, genEvents]
        !enabledAfterStep_RDT_Sender_OnSendError_4[s, s', t, genEvents]
        !enabledAfterStep_RDT_Sender_OnSendError_5[s, s', t, genEvents]
        !enabledAfterStep_RDT_Sender_OnSendError_6[s, s', t, genEvents]
        !enabledAfterStep_RDT_Sender_t_3[s, s', t, genEvents]
        !enabledAfterStep_RDT_Sender_t_4[s, s', t, genEvents]
        !enabledAfterStep_RDT_Receiver__7[s, s', t, genEvents]
        !enabledAfterStep_RDT_Receiver__8[s, s', t, genEvents]
        !enabledAfterStep_RDT_Receiver__9[s, s', t, genEvents]
        !enabledAfterStep_RDT_Receiver__10[s, s', t, genEvents]
        !enabledAfterStep_RDT_Receiver__11[s, s', t, genEvents]
        !enabledAfterStep_RDT_Receiver__12[s, s', t, genEvents]
        !enabledAfterStep_RDT_Receiver__13[s, s', t, genEvents]
        !enabledAfterStep_RDT_Receiver__14[s, s', t, genEvents]
        !enabledAfterStep_RDT_Receiver_t_13[s, s', t, genEvents]
        !enabledAfterStep_RDT_Receiver_t_14[s, s', t, genEvents]
        !enabledAfterStep_RDT_Receiver_t_15[s, s', t, genEvents]
    }

    pred isEnabled[s:Snapshot] {
        pre_RDT_Sender_OnSendSucess_1[s]or
        pre_RDT_Sender_OnSendSucess_2[s]or
        pre_RDT_Sender_OnSendSucess_3[s]or
        pre_RDT_Sender_OnSendError_4[s]or
        pre_RDT_Sender_OnSendError_5[s]or
        pre_RDT_Sender_OnSendError_6[s]or
        pre_RDT_Sender_t_3[s]or
        pre_RDT_Sender_t_4[s]or
        pre_RDT_Receiver__7[s]or
        pre_RDT_Receiver__8[s]or
        pre_RDT_Receiver__9[s]or
        pre_RDT_Receiver__10[s]or
        pre_RDT_Receiver__11[s]or
        pre_RDT_Receiver__12[s]or
        pre_RDT_Receiver__13[s]or
        pre_RDT_Receiver__14[s]or
        pre_RDT_Receiver_t_13[s]or
        pre_RDT_Receiver_t_14[s]or
        pre_RDT_Receiver_t_15[s]
    }

    pred equals[s, s': Snapshot] {
        s'.conf = s.conf
        s'.events = s.events
        s'.taken = s.taken
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
    run path for 5 Snapshot, 4 EventLabel
        expect 1



    assert safety  {
        ctl_mc[ag[{ s: Snapshot | s.stable = True => !((RDT_Sender_ReadyResend in s.conf and RDT_Receiver_ReadyReceiveNext in s.conf))}]]
    }
    
    check safety 
        for 10 Snapshot, 4 EventLabel expect 1
    
    assert liveness  {
        ctl_mc[ag[{ (imp_ctl[
            {s: Snapshot | s.stable = True and RDT_Receiver_ReceiveError in s.conf},
            af[{ s: Snapshot | s.stable = True => (RDT_Receiver_ReceiveSuccess in s.conf)}]
        ])}]]
    }
    
    check liveness 
        for 10 Snapshot, 4 EventLabel expect 1
    

