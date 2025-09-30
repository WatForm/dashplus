
open util/steps[Snapshot]
open util/ordering[Snapshot]

// Snapshot definition
    sig Snapshot extends BaseSnapshot {
        events: set EventLabel,
    }

/***************************** STATE SPACE ************************************/
    abstract sig SystemState extends StateLabel {}
    abstract sig SnapshotUI extends SystemState {}
    one sig SnapshotUI_LoginPage extends SnapshotUI {}
    abstract sig SnapshotUI_Snapshot extends SnapshotUI {}
    abstract sig SnapshotUI_Snapshot_Reports extends SnapshotUI_Snapshot {}
    one sig SnapshotUI_Snapshot_Reports_Summary extends SnapshotUI_Snapshot_Reports {}
    one sig SnapshotUI_Snapshot_Reports_Students extends SnapshotUI_Snapshot_Reports {}
    one sig SnapshotUI_Snapshot_Reports_Standards extends SnapshotUI_Snapshot_Reports {}
    one sig SnapshotUI_Snapshot_Answers extends SnapshotUI_Snapshot {}

/***************************** EVENTS SPACE ***********************************/
    one sig SnapshotUI_login extends EnvironmentEvent {}
    one sig SnapshotUI_logout extends EnvironmentEvent {}
    one sig SnapshotUI_summary extends EnvironmentEvent {}
    one sig SnapshotUI_students extends EnvironmentEvent {}
    one sig SnapshotUI_close extends EnvironmentEvent {}
    one sig SnapshotUI_answer extends EnvironmentEvent {}
    one sig SnapshotUI_standards extends EnvironmentEvent {}

/*************************** TRANSITIONS SPACE ********************************/
    one sig SnapshotUI_LoginPage_Login extends TransitionLabel {}
    one sig SnapshotUI_Snapshot_Logout extends TransitionLabel {}
    one sig SnapshotUI_Snapshot_Reports_SeeSummary extends TransitionLabel {}
    one sig SnapshotUI_Snapshot_Reports_SeeStudents extends TransitionLabel {}
    one sig SnapshotUI_Snapshot_Reports_SeeStandards extends TransitionLabel {}
    one sig SnapshotUI_Snapshot_Reports_Students_SeeAnswers extends TransitionLabel {}
    one sig SnapshotUI_Snapshot_Answers_SeeStudents extends TransitionLabel {}

    // Transition SnapshotUI_LoginPage_Login
    pred pre_SnapshotUI_LoginPage_Login[s:Snapshot] {
        SnapshotUI_LoginPage in s.conf
        SnapshotUI_login in (s.events & EnvironmentEvent)
    }

    pred pos_SnapshotUI_LoginPage_Login[s, s':Snapshot] {
        s'.conf = s.conf - SnapshotUI_LoginPage + {
            SnapshotUI_Snapshot_Reports_Summary
        }
        no ((s'.events & InternalEvent) )
    }

    pred SnapshotUI_LoginPage_Login[s, s': Snapshot] {
        pre_SnapshotUI_LoginPage_Login[s]
        pos_SnapshotUI_LoginPage_Login[s, s']
        semantics_SnapshotUI_LoginPage_Login[s, s']
    }
    pred semantics_SnapshotUI_LoginPage_Login[s, s': Snapshot] {
        s'.taken = SnapshotUI_LoginPage_Login
    }
    // Transition SnapshotUI_Snapshot_Logout
    pred pre_SnapshotUI_Snapshot_Logout[s:Snapshot] {
        (some SnapshotUI_Snapshot & s.conf)
        SnapshotUI_logout in (s.events & EnvironmentEvent)
    }

    pred pos_SnapshotUI_Snapshot_Logout[s, s':Snapshot] {
        s'.conf = s.conf - SnapshotUI_Snapshot + {
            SnapshotUI_LoginPage
        }
        no ((s'.events & InternalEvent) )
    }

    pred SnapshotUI_Snapshot_Logout[s, s': Snapshot] {
        pre_SnapshotUI_Snapshot_Logout[s]
        pos_SnapshotUI_Snapshot_Logout[s, s']
        semantics_SnapshotUI_Snapshot_Logout[s, s']
    }
    pred semantics_SnapshotUI_Snapshot_Logout[s, s': Snapshot] {
        s'.taken = SnapshotUI_Snapshot_Logout
        // Priority "SOURCE-PARENT" semantics
        !pre_SnapshotUI_Snapshot_Reports_SeeStandards[s]
        !pre_SnapshotUI_Snapshot_Reports_Students_SeeAnswers[s]
        !pre_SnapshotUI_Snapshot_Reports_SeeStudents[s]
        !pre_SnapshotUI_Snapshot_Reports_SeeSummary[s]
        !pre_SnapshotUI_Snapshot_Answers_SeeStudents[s]
    }
    // Transition SnapshotUI_Snapshot_Reports_SeeSummary
    pred pre_SnapshotUI_Snapshot_Reports_SeeSummary[s:Snapshot] {
        (some SnapshotUI_Snapshot_Reports & s.conf)
        SnapshotUI_summary in (s.events & EnvironmentEvent)
    }

    pred pos_SnapshotUI_Snapshot_Reports_SeeSummary[s, s':Snapshot] {
        s'.conf = s.conf - SnapshotUI_Snapshot_Reports + {
            SnapshotUI_Snapshot_Reports_Summary
        }
        no ((s'.events & InternalEvent) )
    }

    pred SnapshotUI_Snapshot_Reports_SeeSummary[s, s': Snapshot] {
        pre_SnapshotUI_Snapshot_Reports_SeeSummary[s]
        pos_SnapshotUI_Snapshot_Reports_SeeSummary[s, s']
        semantics_SnapshotUI_Snapshot_Reports_SeeSummary[s, s']
    }
    pred semantics_SnapshotUI_Snapshot_Reports_SeeSummary[s, s': Snapshot] {
        s'.taken = SnapshotUI_Snapshot_Reports_SeeSummary
        // Priority "SOURCE-PARENT" semantics
        !pre_SnapshotUI_Snapshot_Reports_Students_SeeAnswers[s]
    }
    // Transition SnapshotUI_Snapshot_Reports_SeeStudents
    pred pre_SnapshotUI_Snapshot_Reports_SeeStudents[s:Snapshot] {
        (some SnapshotUI_Snapshot_Reports & s.conf)
        SnapshotUI_students in (s.events & EnvironmentEvent)
    }

    pred pos_SnapshotUI_Snapshot_Reports_SeeStudents[s, s':Snapshot] {
        s'.conf = s.conf - SnapshotUI_Snapshot_Reports + {
            SnapshotUI_Snapshot_Reports_Students
        }
        no ((s'.events & InternalEvent) )
    }

    pred SnapshotUI_Snapshot_Reports_SeeStudents[s, s': Snapshot] {
        pre_SnapshotUI_Snapshot_Reports_SeeStudents[s]
        pos_SnapshotUI_Snapshot_Reports_SeeStudents[s, s']
        semantics_SnapshotUI_Snapshot_Reports_SeeStudents[s, s']
    }
    pred semantics_SnapshotUI_Snapshot_Reports_SeeStudents[s, s': Snapshot] {
        s'.taken = SnapshotUI_Snapshot_Reports_SeeStudents
        // Priority "SOURCE-PARENT" semantics
        !pre_SnapshotUI_Snapshot_Reports_Students_SeeAnswers[s]
    }
    // Transition SnapshotUI_Snapshot_Reports_SeeStandards
    pred pre_SnapshotUI_Snapshot_Reports_SeeStandards[s:Snapshot] {
        (some SnapshotUI_Snapshot_Reports & s.conf)
        SnapshotUI_standards in (s.events & EnvironmentEvent)
    }

    pred pos_SnapshotUI_Snapshot_Reports_SeeStandards[s, s':Snapshot] {
        s'.conf = s.conf - SnapshotUI_Snapshot_Reports + {
            SnapshotUI_Snapshot_Reports_Standards
        }
        no ((s'.events & InternalEvent) )
    }

    pred SnapshotUI_Snapshot_Reports_SeeStandards[s, s': Snapshot] {
        pre_SnapshotUI_Snapshot_Reports_SeeStandards[s]
        pos_SnapshotUI_Snapshot_Reports_SeeStandards[s, s']
        semantics_SnapshotUI_Snapshot_Reports_SeeStandards[s, s']
    }
    pred semantics_SnapshotUI_Snapshot_Reports_SeeStandards[s, s': Snapshot] {
        s'.taken = SnapshotUI_Snapshot_Reports_SeeStandards
        // Priority "SOURCE-PARENT" semantics
        !pre_SnapshotUI_Snapshot_Reports_Students_SeeAnswers[s]
    }
    // Transition SnapshotUI_Snapshot_Reports_Students_SeeAnswers
    pred pre_SnapshotUI_Snapshot_Reports_Students_SeeAnswers[s:Snapshot] {
        SnapshotUI_Snapshot_Reports_Students in s.conf
        SnapshotUI_answer in (s.events & EnvironmentEvent)
    }

    pred pos_SnapshotUI_Snapshot_Reports_Students_SeeAnswers[s, s':Snapshot] {
        s'.conf = s.conf - SnapshotUI_Snapshot_Reports + {
            SnapshotUI_Snapshot_Answers
        }
        no ((s'.events & InternalEvent) )
    }

    pred SnapshotUI_Snapshot_Reports_Students_SeeAnswers[s, s': Snapshot] {
        pre_SnapshotUI_Snapshot_Reports_Students_SeeAnswers[s]
        pos_SnapshotUI_Snapshot_Reports_Students_SeeAnswers[s, s']
        semantics_SnapshotUI_Snapshot_Reports_Students_SeeAnswers[s, s']
    }
    pred semantics_SnapshotUI_Snapshot_Reports_Students_SeeAnswers[s, s': Snapshot] {
        s'.taken = SnapshotUI_Snapshot_Reports_Students_SeeAnswers
    }
    // Transition SnapshotUI_Snapshot_Answers_SeeStudents
    pred pre_SnapshotUI_Snapshot_Answers_SeeStudents[s:Snapshot] {
        SnapshotUI_Snapshot_Answers in s.conf
        SnapshotUI_close in (s.events & EnvironmentEvent)
    }

    pred pos_SnapshotUI_Snapshot_Answers_SeeStudents[s, s':Snapshot] {
        s'.conf = s.conf - SnapshotUI_Snapshot_Answers + {
            SnapshotUI_Snapshot_Reports_Students
        }
        no ((s'.events & InternalEvent) )
    }

    pred SnapshotUI_Snapshot_Answers_SeeStudents[s, s': Snapshot] {
        pre_SnapshotUI_Snapshot_Answers_SeeStudents[s]
        pos_SnapshotUI_Snapshot_Answers_SeeStudents[s, s']
        semantics_SnapshotUI_Snapshot_Answers_SeeStudents[s, s']
    }
    pred semantics_SnapshotUI_Snapshot_Answers_SeeStudents[s, s': Snapshot] {
        s'.taken = SnapshotUI_Snapshot_Answers_SeeStudents
    }
/****************************** INITIAL CONDITIONS ****************************/
    pred init[s: Snapshot] {
        s.conf = {
            SnapshotUI_LoginPage
        }
        no s.taken
        no s.events & InternalEvent
    }


/***************************** MODEL DEFINITION *******************************/
    pred operation[s, s': Snapshot] {
        SnapshotUI_LoginPage_Login[s, s'] or
        SnapshotUI_Snapshot_Logout[s, s'] or
        SnapshotUI_Snapshot_Reports_SeeSummary[s, s'] or
        SnapshotUI_Snapshot_Reports_SeeStudents[s, s'] or
        SnapshotUI_Snapshot_Reports_SeeStandards[s, s'] or
        SnapshotUI_Snapshot_Reports_Students_SeeAnswers[s, s'] or
        SnapshotUI_Snapshot_Answers_SeeStudents[s, s']
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
    run path for 5 Snapshot, 7 EventLabel
        expect 1



    assert noSkippingStudentsReport  {
        ctl_mc[ag[{ (imp_ctl[
            (ex[{ s: Snapshot | SnapshotUI_Snapshot_Answers in s.conf}]),
            {s: Snapshot | SnapshotUI_Snapshot_Reports_Students in s.conf}
        ])}]]
    }
    
    check noSkippingStudentsReport 
        for 10 expect 0
    
    pred logOutLogIn  {
        ctl_mc[ef[{ (and_ctl[
            ex[{ s: Snapshot | (SnapshotUI_LoginPage in s.conf)}],
            ex[{  (ex[{ s: Snapshot | (some x : SnapshotUI_Snapshot
             | x in s.conf)}])}]
        ])}]]
    }
    
    run logOutLogIn 
        for 6 expect 1
    

