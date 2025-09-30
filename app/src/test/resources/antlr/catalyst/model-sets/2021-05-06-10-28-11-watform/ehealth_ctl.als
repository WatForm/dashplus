/* ehealth_ctl.als
 * Author: Amin Bandali <bandali@gnu.org>
 * Date: September 28, 2019
 *
 * Notes:
 *
 * This model is an Alloy port of the original TLA+ model created by
 * Prof. J. Ostroff at York University (Canada) and used by him in
 * teaching EECS 4312, Software Engineering Requirements.
 */

open ctlfc_path[State]

sig Patient, Medication {}

sig State {
  patients: set Patient,
  medications: set Medication,
  interactions: Medication set -> set Medication,
  prescriptions: Patient lone -> set Medication,
  in_patient: lone Patient,
  in_medication1: lone Medication,
  in_medication2: lone Medication
}

pred pre_add_patient [s: State] {
  !(s.in_patient in s.patients)
}
pred post_add_patient[s, s': State] {
  s'.patients = s.patients + s.in_patient
  s'.medications = s.medications
  s'.interactions = s.interactions
  s'.prescriptions = s.prescriptions
}
pred add_patient[s, s': State] {
  pre_add_patient[s]
  post_add_patient[s, s']
}


pred pre_add_medication[s: State] {
  !(s.in_medication1 in s.medications)
}
pred post_add_medication[s, s': State] {
  s'.medications = s.medications + s.in_medication1
  s'.patients = s.patients
  s'.interactions = s.interactions
  s'.prescriptions = s.prescriptions
}
pred add_medication[s, s': State] {
  pre_add_medication[s]
  post_add_medication[s, s']
}

pred pre_add_interaction[s: State] {
  s.in_medication1 in s.medications
  s.in_medication2 in s.medications
  s.in_medication1 != s.in_medication2
  !(s.in_medication2 in s.interactions[s.in_medication1])
  all p: s.patients |
    !((p->s.in_medication1 in s.prescriptions) and
      (p->s.in_medication2 in s.prescriptions))
}
pred post_add_interaction[s, s': State] {
  s'.interactions = s.interactions + { s.in_medication1->s.in_medication2 +
                                       s.in_medication2->s.in_medication1 }
  s'.patients = s.patients
  s'.medications = s.medications
  s'.prescriptions = s.prescriptions
}
pred add_interaction[s, s': State] {
  pre_add_interaction[s]
  post_add_interaction[s, s']
}

pred pre_add_prescription[s: State] {
  s.in_patient in s.patients
  s.in_medication1 in s.medications
  !(s.in_medication1 in s.prescriptions[s.in_patient])
  all m0: s.prescriptions[s.in_patient] | !(s.in_medication1 in s.interactions[m0] or
                                            m0 in s.interactions[s.in_medication1])
}
pred post_add_prescription[s, s': State] {
  s'.prescriptions = s.prescriptions + s.in_patient->s.in_medication1
  s'.patients = s.patients
  s'.medications = s.medications
  s'.interactions = s.interactions
}
pred add_prescription[s, s': State] {
  pre_add_prescription[s]
  post_add_prescription[s, s']
}

pred pre_remove_interaction[s: State] {
  s.in_medication1 in s.medications
  s.in_medication2 in s.medications
  s.in_medication1->s.in_medication2 in s.interactions
}
pred post_remove_interaction[s, s': State] {
  s'.interactions = s.interactions - { s.in_medication1->s.in_medication2 +
                                       s.in_medication2->s.in_medication1 }
  s'.patients = s.patients
  s'.medications = s.medications
  s'.prescriptions = s.prescriptions
}
pred remove_interaction[s, s': State] {
  pre_remove_interaction[s]
  post_remove_interaction[s, s']
}

pred pre_remove_prescription[s: State] {
  s.in_patient in s.patients
  s.in_medication1 in s.medications
  s.in_patient->s.in_medication1 in s.prescriptions
}
pred post_remove_prescription[s, s': State] {
  s'.prescriptions = s.prescriptions - s.in_patient->s.in_medication1
  s'.patients = s.patients
  s'.medications = s.medications
  s'.interactions = s.interactions
}
pred remove_prescription[s, s': State] {
  pre_remove_prescription[s]
  post_remove_prescription[s, s']
}


pred init[s: State] {
  no s.patients
  no s.medications
  no s.interactions
  no s.prescriptions
}

pred next[s, s': State] {
  add_patient[s, s'] or
  add_medication[s, s'] or
  add_interaction[s, s'] or
  add_prescription[s, s'] or
  remove_interaction[s, s'] or
  remove_prescription[s, s']
}


fact {
  all s:     State | s in initialState iff init[s]
  all s, s': State | s->s' in nextState iff next[s,s']
  all s, s': State |
    s.patients = s'.patients and
    s.medications = s'.medications and
    s.interactions = s'.interactions and
    s.prescriptions = s'.prescriptions and
    s.in_patient = s'.in_patient and
    s.in_medication1 = s'.in_medication1 and
    s.in_medication2 = s'.in_medication2
    implies s = s'
}

pred reachablity_axiom {
  all s: State | s in State.(initialState <: *nextState)
}
pred operations_axiom {
  some s, s':State | add_patient[s, s']
  some s, s':State | add_medication[s, s']
  some s, s':State | add_interaction[s, s']
  some s, s':State | add_prescription[s, s']
  some s, s':State | remove_interaction[s, s']
  some s, s':State | remove_prescription[s, s']
}
pred significance_axioms {
  reachablity_axiom
  operations_axiom
}
run significance_axioms for 1 Patient,
                            2 Medication,
                            3 State,
                            3 PathNode

pred symmetry {
  ctlfc_mc[ag[{s: State | all m1, m2: Medication |
    m1->m2 in s.interactions iff
    m2->m1 in s.interactions}]]
}

pred irreflexive {
 ctlfc_mc[ag[{s: State | all m: Medication | not m in s.interactions[m]}]]
}

pred safe_prescriptions {
  ctlfc_mc[ag[{s: State | all m1, m2: s.medications, p: s.patients |
    m2 in s.interactions[m1] =>
      !((m1 in s.prescriptions[p]) and
        (m2 in s.prescriptions[p]))}]]
}

check safety_properties {
  symmetry
  irreflexive
  safe_prescriptions
} for 3
