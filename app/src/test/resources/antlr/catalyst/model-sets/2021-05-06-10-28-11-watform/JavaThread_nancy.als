
open util/steps[Snapshot]
open util/ordering[Snapshot]

// Snapshot definition
    sig Snapshot extends BaseSnapshot {
        events: set EventLabel,
    }

/***************************** STATE SPACE ************************************/
    abstract sig SystemState extends StateLabel {}
    abstract sig ThreadStates extends SystemState {}
    one sig ThreadStates_New extends ThreadStates {}
    abstract sig ThreadStates_Runnable extends ThreadStates {}
    one sig ThreadStates_Runnable_Ready extends ThreadStates_Runnable {}
    one sig ThreadStates_Runnable_Running extends ThreadStates_Runnable {}
    one sig ThreadStates_Terminated extends ThreadStates {}
    one sig ThreadStates_TimedWaiting extends ThreadStates {}
    one sig ThreadStates_Waiting extends ThreadStates {}
    one sig ThreadStates_Blocked extends ThreadStates {}

/***************************** EVENTS SPACE ***********************************/
    one sig ThreadStates_ThreadStart extends EnvironmentEvent {}
    one sig ThreadStates_ThreadYield extends EnvironmentEvent {}
    one sig ThreadStates_ThreadTerminated extends EnvironmentEvent {}
    one sig ThreadStates_ThreadJoin extends EnvironmentEvent {}
    one sig ThreadStates_ThreadJoinTimeout extends EnvironmentEvent {}
    one sig ThreadStates_ThreadSleepTime extends EnvironmentEvent {}
    one sig ThreadStates_ThreadSleepTimeElapsed extends EnvironmentEvent {}
    one sig ThreadStates_ObjectWaitTimeOut extends EnvironmentEvent {}
    one sig ThreadStates_ObjectWait extends EnvironmentEvent {}
    one sig ThreadStates_ObjectNotifyAll extends EnvironmentEvent {}
    one sig ThreadStates_ObjectNotify extends EnvironmentEvent {}
    one sig ThreadStates_LockSupportParkUntil extends EnvironmentEvent {}
    one sig ThreadStates_LockSupportParkNanos extends EnvironmentEvent {}
    one sig ThreadStates_LockSupportPark extends EnvironmentEvent {}
    one sig ThreadStates_SchedulerSelected extends EnvironmentEvent {}
    one sig ThreadStates_SchedulerSuspended extends EnvironmentEvent {}
    one sig ThreadStates_WaitForLockToEnterSynchro extends EnvironmentEvent {}
    one sig ThreadStates_WaitForLockToReEnterSynchro extends EnvironmentEvent {}
    one sig ThreadStates_MonitorLockAcquired extends EnvironmentEvent {}

/*************************** TRANSITIONS SPACE ********************************/
    one sig ThreadStates_t_2 extends TransitionLabel {}
    one sig ThreadStates_t_3 extends TransitionLabel {}
    one sig ThreadStates_t_4 extends TransitionLabel {}
    one sig ThreadStates_t_5 extends TransitionLabel {}
    one sig ThreadStates_t_6 extends TransitionLabel {}
    one sig ThreadStates_t_7 extends TransitionLabel {}
    one sig ThreadStates_t_8 extends TransitionLabel {}
    one sig ThreadStates_t_9 extends TransitionLabel {}
    one sig ThreadStates_t_10 extends TransitionLabel {}
    one sig ThreadStates_t_11 extends TransitionLabel {}
    one sig ThreadStates_t_14 extends TransitionLabel {}
    one sig ThreadStates_t_15 extends TransitionLabel {}
    one sig ThreadStates_New_t_16 extends TransitionLabel {}
    one sig ThreadStates_Runnable_t_17 extends TransitionLabel {}
    one sig ThreadStates_Runnable_t_18 extends TransitionLabel {}
    one sig ThreadStates_Runnable_t_19 extends TransitionLabel {}
    one sig ThreadStates__1 extends TransitionLabel {}
    one sig ThreadStates__2 extends TransitionLabel {}
    one sig ThreadStates__3 extends TransitionLabel {}
    one sig ThreadStates__4 extends TransitionLabel {}
    one sig ThreadStates__5 extends TransitionLabel {}
    one sig ThreadStates__6 extends TransitionLabel {}
    one sig ThreadStates__7 extends TransitionLabel {}
    one sig ThreadStates__8 extends TransitionLabel {}

    // Transition ThreadStates_t_2
    pred pre_ThreadStates_t_2[s:Snapshot] {
        (some ThreadStates_Runnable & s.conf)
        ThreadStates_ThreadSleepTime in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates_t_2[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Runnable + {
            ThreadStates_TimedWaiting
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates_t_2[s, s': Snapshot] {
        pre_ThreadStates_t_2[s]
        pos_ThreadStates_t_2[s, s']
        semantics_ThreadStates_t_2[s, s']
    }
    pred semantics_ThreadStates_t_2[s, s': Snapshot] {
        s'.taken = ThreadStates_t_2
        // Priority "SOURCE-PARENT" semantics
        !pre_ThreadStates_Runnable_t_17[s]
        !pre_ThreadStates_Runnable_t_18[s]
        !pre_ThreadStates_Runnable_t_19[s]
    }
    // Transition ThreadStates_t_3
    pred pre_ThreadStates_t_3[s:Snapshot] {
        (some ThreadStates_Runnable & s.conf)
        ThreadStates_ObjectWaitTimeOut in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates_t_3[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Runnable + {
            ThreadStates_TimedWaiting
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates_t_3[s, s': Snapshot] {
        pre_ThreadStates_t_3[s]
        pos_ThreadStates_t_3[s, s']
        semantics_ThreadStates_t_3[s, s']
    }
    pred semantics_ThreadStates_t_3[s, s': Snapshot] {
        s'.taken = ThreadStates_t_3
        // Priority "SOURCE-PARENT" semantics
        !pre_ThreadStates_Runnable_t_17[s]
        !pre_ThreadStates_Runnable_t_18[s]
        !pre_ThreadStates_Runnable_t_19[s]
    }
    // Transition ThreadStates_t_4
    pred pre_ThreadStates_t_4[s:Snapshot] {
        (some ThreadStates_Runnable & s.conf)
        ThreadStates_ThreadJoinTimeout in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates_t_4[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Runnable + {
            ThreadStates_TimedWaiting
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates_t_4[s, s': Snapshot] {
        pre_ThreadStates_t_4[s]
        pos_ThreadStates_t_4[s, s']
        semantics_ThreadStates_t_4[s, s']
    }
    pred semantics_ThreadStates_t_4[s, s': Snapshot] {
        s'.taken = ThreadStates_t_4
        // Priority "SOURCE-PARENT" semantics
        !pre_ThreadStates_Runnable_t_17[s]
        !pre_ThreadStates_Runnable_t_18[s]
        !pre_ThreadStates_Runnable_t_19[s]
    }
    // Transition ThreadStates_t_5
    pred pre_ThreadStates_t_5[s:Snapshot] {
        (some ThreadStates_Runnable & s.conf)
        ThreadStates_LockSupportParkNanos in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates_t_5[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Runnable + {
            ThreadStates_TimedWaiting
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates_t_5[s, s': Snapshot] {
        pre_ThreadStates_t_5[s]
        pos_ThreadStates_t_5[s, s']
        semantics_ThreadStates_t_5[s, s']
    }
    pred semantics_ThreadStates_t_5[s, s': Snapshot] {
        s'.taken = ThreadStates_t_5
        // Priority "SOURCE-PARENT" semantics
        !pre_ThreadStates_Runnable_t_17[s]
        !pre_ThreadStates_Runnable_t_18[s]
        !pre_ThreadStates_Runnable_t_19[s]
    }
    // Transition ThreadStates_t_6
    pred pre_ThreadStates_t_6[s:Snapshot] {
        (some ThreadStates_Runnable & s.conf)
        ThreadStates_LockSupportParkUntil in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates_t_6[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Runnable + {
            ThreadStates_TimedWaiting
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates_t_6[s, s': Snapshot] {
        pre_ThreadStates_t_6[s]
        pos_ThreadStates_t_6[s, s']
        semantics_ThreadStates_t_6[s, s']
    }
    pred semantics_ThreadStates_t_6[s, s': Snapshot] {
        s'.taken = ThreadStates_t_6
        // Priority "SOURCE-PARENT" semantics
        !pre_ThreadStates_Runnable_t_17[s]
        !pre_ThreadStates_Runnable_t_18[s]
        !pre_ThreadStates_Runnable_t_19[s]
    }
    // Transition ThreadStates_t_7
    pred pre_ThreadStates_t_7[s:Snapshot] {
        (some ThreadStates_Runnable & s.conf)
        ThreadStates_ObjectWait in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates_t_7[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Runnable + {
            ThreadStates_Waiting
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates_t_7[s, s': Snapshot] {
        pre_ThreadStates_t_7[s]
        pos_ThreadStates_t_7[s, s']
        semantics_ThreadStates_t_7[s, s']
    }
    pred semantics_ThreadStates_t_7[s, s': Snapshot] {
        s'.taken = ThreadStates_t_7
        // Priority "SOURCE-PARENT" semantics
        !pre_ThreadStates_Runnable_t_17[s]
        !pre_ThreadStates_Runnable_t_18[s]
        !pre_ThreadStates_Runnable_t_19[s]
    }
    // Transition ThreadStates_t_8
    pred pre_ThreadStates_t_8[s:Snapshot] {
        (some ThreadStates_Runnable & s.conf)
        ThreadStates_ThreadJoin in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates_t_8[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Runnable + {
            ThreadStates_Waiting
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates_t_8[s, s': Snapshot] {
        pre_ThreadStates_t_8[s]
        pos_ThreadStates_t_8[s, s']
        semantics_ThreadStates_t_8[s, s']
    }
    pred semantics_ThreadStates_t_8[s, s': Snapshot] {
        s'.taken = ThreadStates_t_8
        // Priority "SOURCE-PARENT" semantics
        !pre_ThreadStates_Runnable_t_17[s]
        !pre_ThreadStates_Runnable_t_18[s]
        !pre_ThreadStates_Runnable_t_19[s]
    }
    // Transition ThreadStates_t_9
    pred pre_ThreadStates_t_9[s:Snapshot] {
        (some ThreadStates_Runnable & s.conf)
        ThreadStates_LockSupportPark in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates_t_9[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Runnable + {
            ThreadStates_Waiting
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates_t_9[s, s': Snapshot] {
        pre_ThreadStates_t_9[s]
        pos_ThreadStates_t_9[s, s']
        semantics_ThreadStates_t_9[s, s']
    }
    pred semantics_ThreadStates_t_9[s, s': Snapshot] {
        s'.taken = ThreadStates_t_9
        // Priority "SOURCE-PARENT" semantics
        !pre_ThreadStates_Runnable_t_17[s]
        !pre_ThreadStates_Runnable_t_18[s]
        !pre_ThreadStates_Runnable_t_19[s]
    }
    // Transition ThreadStates_t_10
    pred pre_ThreadStates_t_10[s:Snapshot] {
        (some ThreadStates_Runnable & s.conf)
        ThreadStates_WaitForLockToEnterSynchro in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates_t_10[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Runnable + {
            ThreadStates_Blocked
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates_t_10[s, s': Snapshot] {
        pre_ThreadStates_t_10[s]
        pos_ThreadStates_t_10[s, s']
        semantics_ThreadStates_t_10[s, s']
    }
    pred semantics_ThreadStates_t_10[s, s': Snapshot] {
        s'.taken = ThreadStates_t_10
        // Priority "SOURCE-PARENT" semantics
        !pre_ThreadStates_Runnable_t_17[s]
        !pre_ThreadStates_Runnable_t_18[s]
        !pre_ThreadStates_Runnable_t_19[s]
    }
    // Transition ThreadStates_t_11
    pred pre_ThreadStates_t_11[s:Snapshot] {
        (some ThreadStates_Runnable & s.conf)
        ThreadStates_WaitForLockToReEnterSynchro in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates_t_11[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Runnable + {
            ThreadStates_Blocked
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates_t_11[s, s': Snapshot] {
        pre_ThreadStates_t_11[s]
        pos_ThreadStates_t_11[s, s']
        semantics_ThreadStates_t_11[s, s']
    }
    pred semantics_ThreadStates_t_11[s, s': Snapshot] {
        s'.taken = ThreadStates_t_11
        // Priority "SOURCE-PARENT" semantics
        !pre_ThreadStates_Runnable_t_17[s]
        !pre_ThreadStates_Runnable_t_18[s]
        !pre_ThreadStates_Runnable_t_19[s]
    }
    // Transition ThreadStates_t_14
    pred pre_ThreadStates_t_14[s:Snapshot] {
        ThreadStates_Blocked in s.conf
        ThreadStates_MonitorLockAcquired in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates_t_14[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Blocked + {
            ThreadStates_Runnable_Ready
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates_t_14[s, s': Snapshot] {
        pre_ThreadStates_t_14[s]
        pos_ThreadStates_t_14[s, s']
        semantics_ThreadStates_t_14[s, s']
    }
    pred semantics_ThreadStates_t_14[s, s': Snapshot] {
        s'.taken = ThreadStates_t_14
    }
    // Transition ThreadStates_t_15
    pred pre_ThreadStates_t_15[s:Snapshot] {
        ThreadStates_TimedWaiting in s.conf
        ThreadStates_ThreadSleepTimeElapsed in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates_t_15[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_TimedWaiting + {
            ThreadStates_Runnable_Ready
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates_t_15[s, s': Snapshot] {
        pre_ThreadStates_t_15[s]
        pos_ThreadStates_t_15[s, s']
        semantics_ThreadStates_t_15[s, s']
    }
    pred semantics_ThreadStates_t_15[s, s': Snapshot] {
        s'.taken = ThreadStates_t_15
    }
    // Transition ThreadStates_New_t_16
    pred pre_ThreadStates_New_t_16[s:Snapshot] {
        ThreadStates_New in s.conf
        ThreadStates_ThreadStart in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates_New_t_16[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_New + {
            ThreadStates_Runnable_Ready
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates_New_t_16[s, s': Snapshot] {
        pre_ThreadStates_New_t_16[s]
        pos_ThreadStates_New_t_16[s, s']
        semantics_ThreadStates_New_t_16[s, s']
    }
    pred semantics_ThreadStates_New_t_16[s, s': Snapshot] {
        s'.taken = ThreadStates_New_t_16
    }
    // Transition ThreadStates_Runnable_t_17
    pred pre_ThreadStates_Runnable_t_17[s:Snapshot] {
        ThreadStates_Runnable_Ready in s.conf
        ThreadStates_SchedulerSelected in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates_Runnable_t_17[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Runnable_Ready + {
            ThreadStates_Runnable_Running
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates_Runnable_t_17[s, s': Snapshot] {
        pre_ThreadStates_Runnable_t_17[s]
        pos_ThreadStates_Runnable_t_17[s, s']
        semantics_ThreadStates_Runnable_t_17[s, s']
    }
    pred semantics_ThreadStates_Runnable_t_17[s, s': Snapshot] {
        s'.taken = ThreadStates_Runnable_t_17
    }
    // Transition ThreadStates_Runnable_t_18
    pred pre_ThreadStates_Runnable_t_18[s:Snapshot] {
        ThreadStates_Runnable_Running in s.conf
        ThreadStates_ThreadYield in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates_Runnable_t_18[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Runnable_Running + {
            ThreadStates_Runnable_Ready
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates_Runnable_t_18[s, s': Snapshot] {
        pre_ThreadStates_Runnable_t_18[s]
        pos_ThreadStates_Runnable_t_18[s, s']
        semantics_ThreadStates_Runnable_t_18[s, s']
    }
    pred semantics_ThreadStates_Runnable_t_18[s, s': Snapshot] {
        s'.taken = ThreadStates_Runnable_t_18
    }
    // Transition ThreadStates_Runnable_t_19
    pred pre_ThreadStates_Runnable_t_19[s:Snapshot] {
        ThreadStates_Runnable_Running in s.conf
        ThreadStates_SchedulerSuspended in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates_Runnable_t_19[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Runnable_Running + {
            ThreadStates_Runnable_Ready
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates_Runnable_t_19[s, s': Snapshot] {
        pre_ThreadStates_Runnable_t_19[s]
        pos_ThreadStates_Runnable_t_19[s, s']
        semantics_ThreadStates_Runnable_t_19[s, s']
    }
    pred semantics_ThreadStates_Runnable_t_19[s, s': Snapshot] {
        s'.taken = ThreadStates_Runnable_t_19
    }
    // Transition ThreadStates__1
    pred pre_ThreadStates__1[s:Snapshot] {
        (some ThreadStates_Runnable & s.conf)
        ThreadStates_ThreadTerminated in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates__1[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Runnable + {
            ThreadStates_Terminated
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates__1[s, s': Snapshot] {
        pre_ThreadStates__1[s]
        pos_ThreadStates__1[s, s']
        semantics_ThreadStates__1[s, s']
    }
    pred semantics_ThreadStates__1[s, s': Snapshot] {
        s'.taken = ThreadStates__1
        // Priority "SOURCE-PARENT" semantics
        !pre_ThreadStates_Runnable_t_17[s]
        !pre_ThreadStates_Runnable_t_18[s]
        !pre_ThreadStates_Runnable_t_19[s]
    }
    // Transition ThreadStates__2
    pred pre_ThreadStates__2[s:Snapshot] {
        ThreadStates_TimedWaiting in s.conf
        ThreadStates_ThreadTerminated in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates__2[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_TimedWaiting + {
            ThreadStates_Terminated
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates__2[s, s': Snapshot] {
        pre_ThreadStates__2[s]
        pos_ThreadStates__2[s, s']
        semantics_ThreadStates__2[s, s']
    }
    pred semantics_ThreadStates__2[s, s': Snapshot] {
        s'.taken = ThreadStates__2
    }
    // Transition ThreadStates__3
    pred pre_ThreadStates__3[s:Snapshot] {
        ThreadStates_Waiting in s.conf
        ThreadStates_ThreadTerminated in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates__3[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Waiting + {
            ThreadStates_Terminated
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates__3[s, s': Snapshot] {
        pre_ThreadStates__3[s]
        pos_ThreadStates__3[s, s']
        semantics_ThreadStates__3[s, s']
    }
    pred semantics_ThreadStates__3[s, s': Snapshot] {
        s'.taken = ThreadStates__3
    }
    // Transition ThreadStates__4
    pred pre_ThreadStates__4[s:Snapshot] {
        ThreadStates_Blocked in s.conf
        ThreadStates_ThreadTerminated in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates__4[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Blocked + {
            ThreadStates_Terminated
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates__4[s, s': Snapshot] {
        pre_ThreadStates__4[s]
        pos_ThreadStates__4[s, s']
        semantics_ThreadStates__4[s, s']
    }
    pred semantics_ThreadStates__4[s, s': Snapshot] {
        s'.taken = ThreadStates__4
    }
    // Transition ThreadStates__5
    pred pre_ThreadStates__5[s:Snapshot] {
        ThreadStates_TimedWaiting in s.conf
        ThreadStates_ObjectNotify in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates__5[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_TimedWaiting + {
            ThreadStates_Blocked
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates__5[s, s': Snapshot] {
        pre_ThreadStates__5[s]
        pos_ThreadStates__5[s, s']
        semantics_ThreadStates__5[s, s']
    }
    pred semantics_ThreadStates__5[s, s': Snapshot] {
        s'.taken = ThreadStates__5
    }
    // Transition ThreadStates__6
    pred pre_ThreadStates__6[s:Snapshot] {
        ThreadStates_Waiting in s.conf
        ThreadStates_ObjectNotify in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates__6[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Waiting + {
            ThreadStates_Blocked
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates__6[s, s': Snapshot] {
        pre_ThreadStates__6[s]
        pos_ThreadStates__6[s, s']
        semantics_ThreadStates__6[s, s']
    }
    pred semantics_ThreadStates__6[s, s': Snapshot] {
        s'.taken = ThreadStates__6
    }
    // Transition ThreadStates__7
    pred pre_ThreadStates__7[s:Snapshot] {
        ThreadStates_TimedWaiting in s.conf
        ThreadStates_ObjectNotifyAll in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates__7[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_TimedWaiting + {
            ThreadStates_Blocked
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates__7[s, s': Snapshot] {
        pre_ThreadStates__7[s]
        pos_ThreadStates__7[s, s']
        semantics_ThreadStates__7[s, s']
    }
    pred semantics_ThreadStates__7[s, s': Snapshot] {
        s'.taken = ThreadStates__7
    }
    // Transition ThreadStates__8
    pred pre_ThreadStates__8[s:Snapshot] {
        ThreadStates_Waiting in s.conf
        ThreadStates_ObjectNotifyAll in (s.events & EnvironmentEvent)
    }

    pred pos_ThreadStates__8[s, s':Snapshot] {
        s'.conf = s.conf - ThreadStates_Waiting + {
            ThreadStates_Blocked
        }
        no ((s'.events & InternalEvent) )
    }

    pred ThreadStates__8[s, s': Snapshot] {
        pre_ThreadStates__8[s]
        pos_ThreadStates__8[s, s']
        semantics_ThreadStates__8[s, s']
    }
    pred semantics_ThreadStates__8[s, s': Snapshot] {
        s'.taken = ThreadStates__8
    }
/****************************** INITIAL CONDITIONS ****************************/
    pred init[s: Snapshot] {
        s.conf = {
            ThreadStates_New
        }
        no s.taken
        no s.events & InternalEvent
    }


/***************************** MODEL DEFINITION *******************************/
    pred operation[s, s': Snapshot] {
        ThreadStates_t_2[s, s'] or
        ThreadStates_t_3[s, s'] or
        ThreadStates_t_4[s, s'] or
        ThreadStates_t_5[s, s'] or
        ThreadStates_t_6[s, s'] or
        ThreadStates_t_7[s, s'] or
        ThreadStates_t_8[s, s'] or
        ThreadStates_t_9[s, s'] or
        ThreadStates_t_10[s, s'] or
        ThreadStates_t_11[s, s'] or
        ThreadStates_t_14[s, s'] or
        ThreadStates_t_15[s, s'] or
        ThreadStates_New_t_16[s, s'] or
        ThreadStates_Runnable_t_17[s, s'] or
        ThreadStates_Runnable_t_18[s, s'] or
        ThreadStates_Runnable_t_19[s, s'] or
        ThreadStates__1[s, s'] or
        ThreadStates__2[s, s'] or
        ThreadStates__3[s, s'] or
        ThreadStates__4[s, s'] or
        ThreadStates__5[s, s'] or
        ThreadStates__6[s, s'] or
        ThreadStates__7[s, s'] or
        ThreadStates__8[s, s']
    }

    pred small_step[s, s': Snapshot] {
        operation[s, s']
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
        path
    }

    pred path {
        all s:Snapshot, s': s.next | operation[s, s']
        init[first]
    }
    run path for 5 Snapshot, 19 EventLabel
        expect 1




