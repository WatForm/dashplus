/* ehealth.als -- Electronic Health system model

Copyright (C) 2019 Amin Bandali <bandali@gnu.org>

ehealth.als is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License,
or (at your option) any later version.

ehealth.als is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with ehealth.als.  If not, see <https://www.gnu.org/licenses/>.

Date: September 28, 2019

Notes:

This model is an Alloy port of the original TLA+
model~\cite{ostroff2017} created by Prof. J. Ostroff at
York University (Canada) and used by him in teaching EECS 4312,
Software Engineering Requirements.

@techreport{ostroff2017,
  author      = {Jonathan S. Ostroff},
  title       = {Validating Software via Abstract State Specifications},
  year        = {2017},
  number      = {EECS-2017-02},
  institution = {York University},
  url         = {http://www.eecs.yorku.ca/research/techreports/2017/?abstract=EECS-2017-02}
}

TODO: add bibtex entry for this model once published. */

open util/ordering [State] as StateOrdering

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

pred pre_add_patient[s: State] {
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
  !(s.in_medication1->s.in_medication2 in s.interactions)
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
  !(s.in_patient->s.in_medication1 in s.prescriptions)
  all m0: s.prescriptions[s.in_patient] | !(s.in_medication1->m0 in s.interactions)
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
     add_patient[s, s']
  or add_medication[s, s']
  or add_interaction[s, s']
  or remove_interaction[s, s']
  or add_prescription[s, s']
  or remove_prescription[s, s']
}

fact traces {
  init[StateOrdering/first]
  all s: State-StateOrdering/last |
    let s' = s.StateOrdering/next |
      next[s, s']
}

pred symmetry {
  all s: State, m1, m2: Medication |
    m1->m2 in s.interactions iff m2->m1 in s.interactions
}

pred irreflexive {
  all s: State, m: Medication | s.interactions[m] != m
}

pred safe_prescriptions {
  all s: State, m1, m2: Medication, p: Patient |
    m1->m2 in s.interactions =>
      !((p->m1 in s.prescriptions) and (p->m2 in s.prescriptions))
}

check properties {
  symmetry
  irreflexive
  safe_prescriptions
} for 3
