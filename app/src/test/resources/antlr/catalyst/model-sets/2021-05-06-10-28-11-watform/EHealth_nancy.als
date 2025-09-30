
open util/steps[Snapshot]
open util/ordering[Snapshot]

// Snapshot definition
    sig Snapshot extends BaseSnapshot {
        EHealthSystem_in_p : lone Patient,
        EHealthSystem_in_m1 : lone Medication,
        EHealthSystem_medications : set Medication,
        EHealthSystem_patients : set Patient,
        EHealthSystem_prescriptions : Patient -> set Medication,
        EHealthSystem_interactions : Medication -> set Medication,
        EHealthSystem_in_m2 : lone Medication
    }

/***************************** STATE SPACE ************************************/
    abstract sig SystemState extends StateLabel {}
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
        !(((s.EHealthSystem_in_p) in (s.EHealthSystem_patients)))
    }

    pred pos_EHealthSystem_add_patient[s, s':Snapshot] {
        s'.conf = s.conf - EHealthSystem + {
            EHealthSystem
        }
        s'.EHealthSystem_medications = s.EHealthSystem_medications
        s'.EHealthSystem_prescriptions = s.EHealthSystem_prescriptions
        s'.EHealthSystem_interactions = s.EHealthSystem_interactions
        (s'.EHealthSystem_patients) = (s.EHealthSystem_patients) + (s.EHealthSystem_in_p)
    }

    pred EHealthSystem_add_patient[s, s': Snapshot] {
        pre_EHealthSystem_add_patient[s]
        pos_EHealthSystem_add_patient[s, s']
        semantics_EHealthSystem_add_patient[s, s']
    }
    pred semantics_EHealthSystem_add_patient[s, s': Snapshot] {
        s'.taken = EHealthSystem_add_patient
    }
    // Transition EHealthSystem_add_medication
    pred pre_EHealthSystem_add_medication[s:Snapshot] {
        EHealthSystem in s.conf
        !(((s.EHealthSystem_in_m1) in (s.EHealthSystem_medications)))
    }

    pred pos_EHealthSystem_add_medication[s, s':Snapshot] {
        s'.conf = s.conf - EHealthSystem + {
            EHealthSystem
        }
        s'.EHealthSystem_patients = s.EHealthSystem_patients
        s'.EHealthSystem_prescriptions = s.EHealthSystem_prescriptions
        s'.EHealthSystem_interactions = s.EHealthSystem_interactions
        (s'.EHealthSystem_medications) = (s.EHealthSystem_medications) + (s.EHealthSystem_in_m1)
    }

    pred EHealthSystem_add_medication[s, s': Snapshot] {
        pre_EHealthSystem_add_medication[s]
        pos_EHealthSystem_add_medication[s, s']
        semantics_EHealthSystem_add_medication[s, s']
    }
    pred semantics_EHealthSystem_add_medication[s, s': Snapshot] {
        s'.taken = EHealthSystem_add_medication
    }
    // Transition EHealthSystem_add_interaction
    pred pre_EHealthSystem_add_interaction[s:Snapshot] {
        EHealthSystem in s.conf
        {
            (s.EHealthSystem_in_m1) != (s.EHealthSystem_in_m2)
            (!(((s.EHealthSystem_in_m1) -> (s.EHealthSystem_in_m2) in (s.EHealthSystem_interactions))) and !(((s.EHealthSystem_in_m2) -> (s.EHealthSystem_in_m1) in (s.EHealthSystem_interactions))))
            (s.EHealthSystem_in_m1) in (s.EHealthSystem_medications)
            (s.EHealthSystem_in_m2) in (s.EHealthSystem_medications)
        }
    }

    pred pos_EHealthSystem_add_interaction[s, s':Snapshot] {
        s'.conf = s.conf - EHealthSystem + {
            EHealthSystem
        }
        s'.EHealthSystem_medications = s.EHealthSystem_medications
        s'.EHealthSystem_patients = s.EHealthSystem_patients
        s'.EHealthSystem_prescriptions = s.EHealthSystem_prescriptions
        (s'.EHealthSystem_interactions) = (s.EHealthSystem_interactions) + {
            (s.EHealthSystem_in_m1) -> (s.EHealthSystem_in_m2) + (s.EHealthSystem_in_m2) -> (s.EHealthSystem_in_m1)
        }
    }

    pred EHealthSystem_add_interaction[s, s': Snapshot] {
        pre_EHealthSystem_add_interaction[s]
        pos_EHealthSystem_add_interaction[s, s']
        semantics_EHealthSystem_add_interaction[s, s']
    }
    pred semantics_EHealthSystem_add_interaction[s, s': Snapshot] {
        s'.taken = EHealthSystem_add_interaction
    }
    // Transition EHealthSystem_add_prescription
    pred pre_EHealthSystem_add_prescription[s:Snapshot] {
        EHealthSystem in s.conf
        {
            (s.EHealthSystem_in_p) in (s.EHealthSystem_patients)
            !(((s.EHealthSystem_in_p) -> (s.EHealthSystem_in_m1) in (s.EHealthSystem_prescriptions)))
            all x : (s.EHealthSystem_in_p).(s.EHealthSystem_prescriptions)
             | !(((s.EHealthSystem_in_m1) -> x in (s.EHealthSystem_interactions)))
        }
    }

    pred pos_EHealthSystem_add_prescription[s, s':Snapshot] {
        s'.conf = s.conf - EHealthSystem + {
            EHealthSystem
        }
        s'.EHealthSystem_medications = s.EHealthSystem_medications
        s'.EHealthSystem_patients = s.EHealthSystem_patients
        s'.EHealthSystem_interactions = s.EHealthSystem_interactions
        (s'.EHealthSystem_prescriptions) = (s.EHealthSystem_prescriptions) + (s.EHealthSystem_in_p) -> (s.EHealthSystem_in_m1)
    }

    pred EHealthSystem_add_prescription[s, s': Snapshot] {
        pre_EHealthSystem_add_prescription[s]
        pos_EHealthSystem_add_prescription[s, s']
        semantics_EHealthSystem_add_prescription[s, s']
    }
    pred semantics_EHealthSystem_add_prescription[s, s': Snapshot] {
        s'.taken = EHealthSystem_add_prescription
    }
    // Transition EHealthSystem_remove_interaction
    pred pre_EHealthSystem_remove_interaction[s:Snapshot] {
        EHealthSystem in s.conf
        {
            (s.EHealthSystem_in_m1) in (s.EHealthSystem_medications)
            (s.EHealthSystem_in_m2) in (s.EHealthSystem_medications)
            (s.EHealthSystem_in_m1) -> (s.EHealthSystem_in_m2) in (s.EHealthSystem_interactions)
        }
    }

    pred pos_EHealthSystem_remove_interaction[s, s':Snapshot] {
        s'.conf = s.conf - EHealthSystem + {
            EHealthSystem
        }
        s'.EHealthSystem_medications = s.EHealthSystem_medications
        s'.EHealthSystem_patients = s.EHealthSystem_patients
        s'.EHealthSystem_prescriptions = s.EHealthSystem_prescriptions
        (s'.EHealthSystem_interactions) = (s.EHealthSystem_interactions) - {
            (s.EHealthSystem_in_m1) -> (s.EHealthSystem_in_m2) + (s.EHealthSystem_in_m2) -> (s.EHealthSystem_in_m1)
        }
    }

    pred EHealthSystem_remove_interaction[s, s': Snapshot] {
        pre_EHealthSystem_remove_interaction[s]
        pos_EHealthSystem_remove_interaction[s, s']
        semantics_EHealthSystem_remove_interaction[s, s']
    }
    pred semantics_EHealthSystem_remove_interaction[s, s': Snapshot] {
        s'.taken = EHealthSystem_remove_interaction
    }
    // Transition EHealthSystem_remove_prescription
    pred pre_EHealthSystem_remove_prescription[s:Snapshot] {
        EHealthSystem in s.conf
        {
            (s.EHealthSystem_in_p) in (s.EHealthSystem_patients)
            (s.EHealthSystem_in_m1) in (s.EHealthSystem_medications)
            (s.EHealthSystem_in_p) -> (s.EHealthSystem_in_m1) in (s.EHealthSystem_prescriptions)
        }
    }

    pred pos_EHealthSystem_remove_prescription[s, s':Snapshot] {
        s'.conf = s.conf - EHealthSystem + {
            EHealthSystem
        }
        s'.EHealthSystem_medications = s.EHealthSystem_medications
        s'.EHealthSystem_patients = s.EHealthSystem_patients
        s'.EHealthSystem_interactions = s.EHealthSystem_interactions
        (s'.EHealthSystem_prescriptions) = (s.EHealthSystem_prescriptions) - (s.EHealthSystem_in_p) -> (s.EHealthSystem_in_m1)
    }

    pred EHealthSystem_remove_prescription[s, s': Snapshot] {
        pre_EHealthSystem_remove_prescription[s]
        pos_EHealthSystem_remove_prescription[s, s']
        semantics_EHealthSystem_remove_prescription[s, s']
    }
    pred semantics_EHealthSystem_remove_prescription[s, s': Snapshot] {
        s'.taken = EHealthSystem_remove_prescription
    }
/****************************** INITIAL CONDITIONS ****************************/
    pred init[s: Snapshot] {
        s.conf = {
            EHealthSystem
        }
        no s.taken
        // Model specific constraints
        no (s.EHealthSystem_medications)
            no (s.EHealthSystem_prescriptions)
            no (s.EHealthSystem_patients)
            no (s.EHealthSystem_interactions)
    }


/***************************** MODEL DEFINITION *******************************/
    pred operation[s, s': Snapshot] {
        EHealthSystem_add_patient[s, s'] or
        EHealthSystem_add_medication[s, s'] or
        EHealthSystem_add_interaction[s, s'] or
        EHealthSystem_add_prescription[s, s'] or
        EHealthSystem_remove_interaction[s, s'] or
        EHealthSystem_remove_prescription[s, s']
    }

    pred small_step[s, s': Snapshot] {
        operation[s, s']
    }

    pred equals[s, s': Snapshot] {
        s'.conf = s.conf
        s'.taken = s.taken
        // Model specific declarations
        s'.EHealthSystem_in_p = s.EHealthSystem_in_p
        s'.EHealthSystem_in_m1 = s.EHealthSystem_in_m1
        s'.EHealthSystem_medications = s.EHealthSystem_medications
        s'.EHealthSystem_patients = s.EHealthSystem_patients
        s'.EHealthSystem_prescriptions = s.EHealthSystem_prescriptions
        s'.EHealthSystem_interactions = s.EHealthSystem_interactions
        s'.EHealthSystem_in_m2 = s.EHealthSystem_in_m2
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
    

