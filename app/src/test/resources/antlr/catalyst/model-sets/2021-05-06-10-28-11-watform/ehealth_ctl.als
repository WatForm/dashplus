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
pred post_add_patient[s, sPrime: State] {
  sPrime.patients = s.patients + s.in_patient
  sPrime.medications = s.medications
  sPrime.interactions = s.interactions
  sPrime.prescriptions = s.prescriptions
}
pred add_patient[s, sPrime: State] {
  pre_add_patient[s]
  post_add_patient[s, sPrime]
}


pred pre_add_medication[s: State] {
  !(s.in_medication1 in s.medications)
}
pred post_add_medication[s, sPrime: State] {
  sPrime.medications = s.medications + s.in_medication1
  sPrime.patients = s.patients
  sPrime.interactions = s.interactions
  sPrime.prescriptions = s.prescriptions
}
pred add_medication[s, sPrime: State] {
  pre_add_medication[s]
  post_add_medication[s, sPrime]
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
pred post_add_interaction[s, sPrime: State] {
  sPrime.interactions = s.interactions + { s.in_medication1->s.in_medication2 +
                                       s.in_medication2->s.in_medication1 }
  sPrime.patients = s.patients
  sPrime.medications = s.medications
  sPrime.prescriptions = s.prescriptions
}
pred add_interaction[s, sPrime: State] {
  pre_add_interaction[s]
  post_add_interaction[s, sPrime]
}

pred pre_add_prescription[s: State] {
  s.in_patient in s.patients
  s.in_medication1 in s.medications
  !(s.in_medication1 in s.prescriptions[s.in_patient])
  all m0: s.prescriptions[s.in_patient] | !(s.in_medication1 in s.interactions[m0] or
                                            m0 in s.interactions[s.in_medication1])
}
pred post_add_prescription[s, sPrime: State] {
  sPrime.prescriptions = s.prescriptions + s.in_patient->s.in_medication1
  sPrime.patients = s.patients
  sPrime.medications = s.medications
  sPrime.interactions = s.interactions
}
pred add_prescription[s, sPrime: State] {
  pre_add_prescription[s]
  post_add_prescription[s, sPrime]
}

pred pre_remove_interaction[s: State] {
  s.in_medication1 in s.medications
  s.in_medication2 in s.medications
  s.in_medication1->s.in_medication2 in s.interactions
}
pred post_remove_interaction[s, sPrime: State] {
  sPrime.interactions = s.interactions - { s.in_medication1->s.in_medication2 +
                                       s.in_medication2->s.in_medication1 }
  sPrime.patients = s.patients
  sPrime.medications = s.medications
  sPrime.prescriptions = s.prescriptions
}
pred remove_interaction[s, sPrime: State] {
  pre_remove_interaction[s]
  post_remove_interaction[s, sPrime]
}

pred pre_remove_prescription[s: State] {
  s.in_patient in s.patients
  s.in_medication1 in s.medications
  s.in_patient->s.in_medication1 in s.prescriptions
}
pred post_remove_prescription[s, sPrime: State] {
  sPrime.prescriptions = s.prescriptions - s.in_patient->s.in_medication1
  sPrime.patients = s.patients
  sPrime.medications = s.medications
  sPrime.interactions = s.interactions
}
pred remove_prescription[s, sPrime: State] {
  pre_remove_prescription[s]
  post_remove_prescription[s, sPrime]
}


pred init[s: State] {
  no s.patients
  no s.medications
  no s.interactions
  no s.prescriptions
}

pred next[s, sPrime: State] {
  add_patient[s, sPrime] or
  add_medication[s, sPrime] or
  add_interaction[s, sPrime] or
  add_prescription[s, sPrime] or
  remove_interaction[s, sPrime] or
  remove_prescription[s, sPrime]
}


fact {
  all s:     State | s in initialState iff init[s]
  all s, sPrime: State | s->sPrime in nextState iff next[s,sPrime]
  all s, sPrime: State |
    s.patients = sPrime.patients and
    s.medications = sPrime.medications and
    s.interactions = sPrime.interactions and
    s.prescriptions = sPrime.prescriptions and
    s.in_patient = sPrime.in_patient and
    s.in_medication1 = sPrime.in_medication1 and
    s.in_medication2 = sPrime.in_medication2
    implies s = sPrime
}

pred reachablity_axiom {
  all s: State | s in State.(initialState <: *nextState)
}
pred operations_axiom {
  some s, sPrime:State | add_patient[s, sPrime]
  some s, sPrime:State | add_medication[s, sPrime]
  some s, sPrime:State | add_interaction[s, sPrime]
  some s, sPrime:State | add_prescription[s, sPrime]
  some s, sPrime:State | remove_interaction[s, sPrime]
  some s, sPrime:State | remove_prescription[s, sPrime]
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
