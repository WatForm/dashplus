
open util/steps[Snapshot]
open util/ordering[Snapshot]
open util/boolean

// Snapshot definition
    sig Snapshot extends BaseSnapshot {
        stable: one Bool,
        Environment_p : lone Patient,
        Environment_m1 : lone Medication,
        Environment_m2 : lone Medication,
        EHealthSystem_medications : set Medication,
        EHealthSystem_patients : set Patient,
        EHealthSystem_prescriptions : Patient -> set Medication,
        EHealthSystem_interactions : Medication -> set Medication
    }

/***************************** STATE SPACE ************************************/
    abstract sig SystemState extends StateLabel {}
    one sig Environment extends SystemState {}
    one sig EHealthSystem extends SystemState {}

/*************************** TRANSITIONS SPACE ********************************/
    one sig EHealthSystem_add_patient extends TransitionLabel {}
    one sig EHealthSystem_add_medication extends TransitionLabel {}
    one sig EHealthSystem_add_interaction extends TransitionLabel {}
    one sig EHealthSystem_add_prescription extends TransitionLabel {}
    one sig EHealthSystem_remove_interaction extends TransitionLabel {}
    one sig EHealthSystem_remove_prescription extends TransitionLabel {}

    // Transition EHealthSystem_add_patient
    pred pre_EHealthSystem_add_patient[s:Snapshot] {
        EHealthSystem in s.conf
        !(((s.Environment_p) in (s.EHealthSystem_patients)))
    }

    pred pos_EHealthSystem_add_patient[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - EHealthSystem + {
            EHealthSystem
        }
        sPrime.EHealthSystem_medications = s.EHealthSystem_medications
        sPrime.EHealthSystem_prescriptions = s.EHealthSystem_prescriptions
        sPrime.EHealthSystem_interactions = s.EHealthSystem_interactions
        (sPrime.EHealthSystem_patients) = (s.EHealthSystem_patients) + (s.Environment_p)
    
        testIfNextStable[s, sPrime, {none}, EHealthSystem_add_patient] => {
            sPrime.stable = True
        } else {
            sPrime.stable = False
            sPrime.Environment_p = s.Environment_p
            sPrime.Environment_m1 = s.Environment_m1
            sPrime.Environment_m2 = s.Environment_m2
        }
    }

    pred EHealthSystem_add_patient[s, sPrime: Snapshot] {
        pre_EHealthSystem_add_patient[s]
        pos_EHealthSystem_add_patient[s, sPrime]
        semantics_EHealthSystem_add_patient[s, sPrime]
    }

    pred enabledAfterStep_EHealthSystem_add_patient[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        EHealthSystem in s.conf
        !(((_s.Environment_p) in (s.EHealthSystem_patients)))
        _s.stable = True => {
            no t & {
                EHealthSystem_add_prescription + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        } else {
            no {_s.taken + t} & {
                EHealthSystem_add_prescription + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        }
    }
    pred semantics_EHealthSystem_add_patient[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = EHealthSystem_add_patient
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + EHealthSystem_add_patient
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                EHealthSystem_add_prescription + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        }
    }
    // Transition EHealthSystem_add_medication
    pred pre_EHealthSystem_add_medication[s:Snapshot] {
        EHealthSystem in s.conf
        !(((s.Environment_m1) in (s.EHealthSystem_medications)))
    }

    pred pos_EHealthSystem_add_medication[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - EHealthSystem + {
            EHealthSystem
        }
        sPrime.EHealthSystem_patients = s.EHealthSystem_patients
        sPrime.EHealthSystem_prescriptions = s.EHealthSystem_prescriptions
        sPrime.EHealthSystem_interactions = s.EHealthSystem_interactions
        (sPrime.EHealthSystem_medications) = (s.EHealthSystem_medications) + (s.Environment_m1)
    
        testIfNextStable[s, sPrime, {none}, EHealthSystem_add_medication] => {
            sPrime.stable = True
        } else {
            sPrime.stable = False
            sPrime.Environment_p = s.Environment_p
            sPrime.Environment_m1 = s.Environment_m1
            sPrime.Environment_m2 = s.Environment_m2
        }
    }

    pred EHealthSystem_add_medication[s, sPrime: Snapshot] {
        pre_EHealthSystem_add_medication[s]
        pos_EHealthSystem_add_medication[s, sPrime]
        semantics_EHealthSystem_add_medication[s, sPrime]
    }

    pred enabledAfterStep_EHealthSystem_add_medication[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        EHealthSystem in s.conf
        !(((_s.Environment_m1) in (s.EHealthSystem_medications)))
        _s.stable = True => {
            no t & {
                EHealthSystem_add_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        } else {
            no {_s.taken + t} & {
                EHealthSystem_add_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        }
    }
    pred semantics_EHealthSystem_add_medication[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = EHealthSystem_add_medication
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + EHealthSystem_add_medication
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                EHealthSystem_add_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        }
    }
    // Transition EHealthSystem_add_interaction
    pred pre_EHealthSystem_add_interaction[s:Snapshot] {
        EHealthSystem in s.conf
        {
            (s.Environment_m1) != (s.Environment_m2)
            (!(((s.Environment_m1) -> (s.Environment_m2) in (s.EHealthSystem_interactions))) and !(((s.Environment_m2) -> (s.Environment_m1) in (s.EHealthSystem_interactions))))
            (s.Environment_m1) in (s.EHealthSystem_medications)
            (s.Environment_m2) in (s.EHealthSystem_medications)
        }
    }

    pred pos_EHealthSystem_add_interaction[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - EHealthSystem + {
            EHealthSystem
        }
        sPrime.EHealthSystem_medications = s.EHealthSystem_medications
        sPrime.EHealthSystem_patients = s.EHealthSystem_patients
        sPrime.EHealthSystem_prescriptions = s.EHealthSystem_prescriptions
        (sPrime.EHealthSystem_interactions) = (s.EHealthSystem_interactions) + {
            (s.Environment_m1) -> (s.Environment_m2) + (s.Environment_m2) -> (s.Environment_m1)
        }
    
        testIfNextStable[s, sPrime, {none}, EHealthSystem_add_interaction] => {
            sPrime.stable = True
        } else {
            sPrime.stable = False
            sPrime.Environment_p = s.Environment_p
            sPrime.Environment_m1 = s.Environment_m1
            sPrime.Environment_m2 = s.Environment_m2
        }
    }

    pred EHealthSystem_add_interaction[s, sPrime: Snapshot] {
        pre_EHealthSystem_add_interaction[s]
        pos_EHealthSystem_add_interaction[s, sPrime]
        semantics_EHealthSystem_add_interaction[s, sPrime]
    }

    pred enabledAfterStep_EHealthSystem_add_interaction[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        EHealthSystem in s.conf
        {
            (_s.Environment_m1) != (_s.Environment_m2)
            (!(((_s.Environment_m1) -> (_s.Environment_m2) in (s.EHealthSystem_interactions))) and !(((_s.Environment_m2) -> (_s.Environment_m1) in (s.EHealthSystem_interactions))))
            (_s.Environment_m1) in (s.EHealthSystem_medications)
            (_s.Environment_m2) in (s.EHealthSystem_medications)
        }
        _s.stable = True => {
            no t & {
                EHealthSystem_add_prescription + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        } else {
            no {_s.taken + t} & {
                EHealthSystem_add_prescription + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        }
    }
    pred semantics_EHealthSystem_add_interaction[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = EHealthSystem_add_interaction
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + EHealthSystem_add_interaction
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                EHealthSystem_add_prescription + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        }
    }
    // Transition EHealthSystem_add_prescription
    pred pre_EHealthSystem_add_prescription[s:Snapshot] {
        EHealthSystem in s.conf
        {
            (s.Environment_p) in (s.EHealthSystem_patients)
            !(((s.Environment_p) -> (s.Environment_m1) in (s.EHealthSystem_prescriptions)))
            all x : (s.Environment_p).(s.EHealthSystem_prescriptions)
             | !(((s.Environment_m1) -> x in (s.EHealthSystem_interactions)))
        }
    }

    pred pos_EHealthSystem_add_prescription[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - EHealthSystem + {
            EHealthSystem
        }
        sPrime.EHealthSystem_medications = s.EHealthSystem_medications
        sPrime.EHealthSystem_patients = s.EHealthSystem_patients
        sPrime.EHealthSystem_interactions = s.EHealthSystem_interactions
        (sPrime.EHealthSystem_prescriptions) = (s.EHealthSystem_prescriptions) + (s.Environment_p) -> (s.Environment_m1)
    
        testIfNextStable[s, sPrime, {none}, EHealthSystem_add_prescription] => {
            sPrime.stable = True
        } else {
            sPrime.stable = False
            sPrime.Environment_p = s.Environment_p
            sPrime.Environment_m1 = s.Environment_m1
            sPrime.Environment_m2 = s.Environment_m2
        }
    }

    pred EHealthSystem_add_prescription[s, sPrime: Snapshot] {
        pre_EHealthSystem_add_prescription[s]
        pos_EHealthSystem_add_prescription[s, sPrime]
        semantics_EHealthSystem_add_prescription[s, sPrime]
    }

    pred enabledAfterStep_EHealthSystem_add_prescription[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        EHealthSystem in s.conf
        {
            (_s.Environment_p) in (s.EHealthSystem_patients)
            !(((_s.Environment_p) -> (_s.Environment_m1) in (s.EHealthSystem_prescriptions)))
            all x : (_s.Environment_p).(s.EHealthSystem_prescriptions)
             | !(((_s.Environment_m1) -> x in (s.EHealthSystem_interactions)))
        }
        _s.stable = True => {
            no t & {
                EHealthSystem_add_prescription + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        } else {
            no {_s.taken + t} & {
                EHealthSystem_add_prescription + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        }
    }
    pred semantics_EHealthSystem_add_prescription[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = EHealthSystem_add_prescription
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + EHealthSystem_add_prescription
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                EHealthSystem_add_prescription + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        }
    }
    // Transition EHealthSystem_remove_interaction
    pred pre_EHealthSystem_remove_interaction[s:Snapshot] {
        EHealthSystem in s.conf
        {
            (s.Environment_m1) in (s.EHealthSystem_medications)
            (s.Environment_m2) in (s.EHealthSystem_medications)
            (s.Environment_m1) -> (s.Environment_m2) in (s.EHealthSystem_interactions)
        }
    }

    pred pos_EHealthSystem_remove_interaction[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - EHealthSystem + {
            EHealthSystem
        }
        sPrime.EHealthSystem_medications = s.EHealthSystem_medications
        sPrime.EHealthSystem_patients = s.EHealthSystem_patients
        sPrime.EHealthSystem_prescriptions = s.EHealthSystem_prescriptions
        (sPrime.EHealthSystem_interactions) = (s.EHealthSystem_interactions) - {
            (s.Environment_m1) -> (s.Environment_m2) + (s.Environment_m2) -> (s.Environment_m1)
        }
    
        testIfNextStable[s, sPrime, {none}, EHealthSystem_remove_interaction] => {
            sPrime.stable = True
        } else {
            sPrime.stable = False
            sPrime.Environment_p = s.Environment_p
            sPrime.Environment_m1 = s.Environment_m1
            sPrime.Environment_m2 = s.Environment_m2
        }
    }

    pred EHealthSystem_remove_interaction[s, sPrime: Snapshot] {
        pre_EHealthSystem_remove_interaction[s]
        pos_EHealthSystem_remove_interaction[s, sPrime]
        semantics_EHealthSystem_remove_interaction[s, sPrime]
    }

    pred enabledAfterStep_EHealthSystem_remove_interaction[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        EHealthSystem in s.conf
        {
            (_s.Environment_m1) in (s.EHealthSystem_medications)
            (_s.Environment_m2) in (s.EHealthSystem_medications)
            (_s.Environment_m1) -> (_s.Environment_m2) in (s.EHealthSystem_interactions)
        }
        _s.stable = True => {
            no t & {
                EHealthSystem_add_prescription + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        } else {
            no {_s.taken + t} & {
                EHealthSystem_add_prescription + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        }
    }
    pred semantics_EHealthSystem_remove_interaction[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = EHealthSystem_remove_interaction
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + EHealthSystem_remove_interaction
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                EHealthSystem_add_prescription + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        }
    }
    // Transition EHealthSystem_remove_prescription
    pred pre_EHealthSystem_remove_prescription[s:Snapshot] {
        EHealthSystem in s.conf
        {
            (s.Environment_p) in (s.EHealthSystem_patients)
            (s.Environment_m1) in (s.EHealthSystem_medications)
            (s.Environment_p) -> (s.Environment_m1) in (s.EHealthSystem_prescriptions)
        }
    }

    pred pos_EHealthSystem_remove_prescription[s, sPrime:Snapshot] {
        sPrime.conf = s.conf - EHealthSystem + {
            EHealthSystem
        }
        sPrime.EHealthSystem_medications = s.EHealthSystem_medications
        sPrime.EHealthSystem_patients = s.EHealthSystem_patients
        sPrime.EHealthSystem_interactions = s.EHealthSystem_interactions
        (sPrime.EHealthSystem_prescriptions) = (s.EHealthSystem_prescriptions) - (s.Environment_p) -> (s.Environment_m1)
    
        testIfNextStable[s, sPrime, {none}, EHealthSystem_remove_prescription] => {
            sPrime.stable = True
        } else {
            sPrime.stable = False
            sPrime.Environment_p = s.Environment_p
            sPrime.Environment_m1 = s.Environment_m1
            sPrime.Environment_m2 = s.Environment_m2
        }
    }

    pred EHealthSystem_remove_prescription[s, sPrime: Snapshot] {
        pre_EHealthSystem_remove_prescription[s]
        pos_EHealthSystem_remove_prescription[s, sPrime]
        semantics_EHealthSystem_remove_prescription[s, sPrime]
    }

    pred enabledAfterStep_EHealthSystem_remove_prescription[_s, s: Snapshot, t: TransitionLabel, genEvents: set InternalEvent] {
        // Preconditions
        EHealthSystem in s.conf
        {
            (_s.Environment_p) in (s.EHealthSystem_patients)
            (_s.Environment_m1) in (s.EHealthSystem_medications)
            (_s.Environment_p) -> (_s.Environment_m1) in (s.EHealthSystem_prescriptions)
        }
        _s.stable = True => {
            no t & {
                EHealthSystem_add_prescription + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        } else {
            no {_s.taken + t} & {
                EHealthSystem_add_prescription + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        }
    }
    pred semantics_EHealthSystem_remove_prescription[s, sPrime: Snapshot] {
        (s.stable = True) => {
            // SINGLE semantics
            sPrime.taken = EHealthSystem_remove_prescription
        } else {
            // SINGLE semantics
            sPrime.taken = s.taken + EHealthSystem_remove_prescription
            // Bigstep "TAKE_ONE" semantics
            no s.taken & {
                EHealthSystem_add_prescription + 
                EHealthSystem_remove_prescription + 
                EHealthSystem_add_medication + 
                EHealthSystem_remove_interaction + 
                EHealthSystem_add_patient + 
                EHealthSystem_add_interaction
            }
        }
    }
/****************************** INITIAL CONDITIONS ****************************/
    pred init[s: Snapshot] {
        s.conf = {
            Environment + 
            EHealthSystem
        }
        no s.taken
        s.stable = True
        // Model specific constraints
        no (s.EHealthSystem_medications)
            no (s.EHealthSystem_prescriptions)
            no (s.EHealthSystem_patients)
            no (s.EHealthSystem_interactions)
    }


/***************************** MODEL DEFINITION *******************************/
    pred operation[s, sPrime: Snapshot] {
        EHealthSystem_add_patient[s, sPrime] or
        EHealthSystem_add_medication[s, sPrime] or
        EHealthSystem_add_interaction[s, sPrime] or
        EHealthSystem_add_prescription[s, sPrime] or
        EHealthSystem_remove_interaction[s, sPrime] or
        EHealthSystem_remove_prescription[s, sPrime]
    }

    pred small_step[s, sPrime: Snapshot] {
        operation[s, sPrime]
    }

    pred testIfNextStable[s, sPrime: Snapshot, genEvents: set InternalEvent, t:TransitionLabel] {
        !enabledAfterStep_EHealthSystem_add_patient[s, sPrime, t, genEvents]
        !enabledAfterStep_EHealthSystem_add_medication[s, sPrime, t, genEvents]
        !enabledAfterStep_EHealthSystem_add_interaction[s, sPrime, t, genEvents]
        !enabledAfterStep_EHealthSystem_add_prescription[s, sPrime, t, genEvents]
        !enabledAfterStep_EHealthSystem_remove_interaction[s, sPrime, t, genEvents]
        !enabledAfterStep_EHealthSystem_remove_prescription[s, sPrime, t, genEvents]
    }

    pred isEnabled[s:Snapshot] {
        pre_EHealthSystem_add_patient[s]or
        pre_EHealthSystem_add_medication[s]or
        pre_EHealthSystem_add_interaction[s]or
        pre_EHealthSystem_add_prescription[s]or
        pre_EHealthSystem_remove_interaction[s]or
        pre_EHealthSystem_remove_prescription[s]
    }

    pred equals[s, sPrime: Snapshot] {
        sPrime.conf = s.conf
        sPrime.taken = s.taken
        // Model specific declarations
        sPrime.Environment_p = s.Environment_p
        sPrime.Environment_m1 = s.Environment_m1
        sPrime.Environment_m2 = s.Environment_m2
        sPrime.EHealthSystem_medications = s.EHealthSystem_medications
        sPrime.EHealthSystem_patients = s.EHealthSystem_patients
        sPrime.EHealthSystem_prescriptions = s.EHealthSystem_prescriptions
        sPrime.EHealthSystem_interactions = s.EHealthSystem_interactions
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
    run path for 5 Snapshot, 0 EventLabel,
        3 Patient,
        3 Medication
        expect 1

/****************************** INVARIANTS ************************************/
    fact EHealthSystem_symmetry {
        all s: Snapshot | EHealthSystem in s.conf => {
            all m1, m2 : Medication
                 | m1 -> m2 in (s.EHealthSystem_interactions) iff m2 -> m1 in (s.EHealthSystem_interactions)
        }
    }
    
    fact EHealthSystem_no_self_interaction {
        all s: Snapshot | EHealthSystem in s.conf => {
            all m : (s.EHealthSystem_medications)
                 | !((m -> m in (s.EHealthSystem_interactions)))
        }
    }
    
    fact EHealthSystem_safe_prescriptions {
        all s: Snapshot | EHealthSystem in s.conf => {
            all m1, m2 : (s.EHealthSystem_medications),
                p : (s.EHealthSystem_patients)
                 | m1 -> m2 in (s.EHealthSystem_interactions) => !(((p -> m1 in (s.EHealthSystem_prescriptions)) and (p -> m2 in (s.EHealthSystem_prescriptions))))
        }
    }
    


    sig Patient {}
    
    sig Medication {}
    
    
    run operation for 8 Snapshot, 0 EventLabel, 3 Patient, 4 Medication
    

