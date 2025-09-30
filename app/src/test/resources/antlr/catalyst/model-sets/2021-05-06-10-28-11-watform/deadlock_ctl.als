/* deadlock.als -- Train scheduling deadlock avoidance model

Copyright (C) 2020 Amin Bandali <bandali@gnu.org>

deadlock.als is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License,
or (at your option) any later version.

deadlock.als is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with deadlock.als.  If not, see <https://www.gnu.org/licenses/>.

Date: January 19, 2020

Notes:

This model is an adaptation of the original model(s) presented by
Frappier et al. in~\cite{DBLP:journals/sttt/MazzantiFS18}.

@Article{DBLP:journals/sttt/MazzantiFS18,
  author    = {Franco Mazzanti and Alessio Ferrari and Giorgio Oronzo
                  Spagnolo},
  title     = {Towards formal methods diversity in railways: an
                  experience report with seven frameworks},
  year      = 2018,
  volume    = 20,
  number    = 3,
  pages     = {263-288},
  doi       = {10.1007/s10009-018-0488-3},
  url       = {https://doi.org/10.1007/s10009-018-0488-3},
  journal   = {{STTT}},
  timestamp = {Fri, 30 Nov 2018 13:26:45 +0100},
  biburl    = {https://dblp.org/rec/bib/journals/sttt/MazzantiFS18},
  bibsource = {dblp computer science bibliography, https://dblp.org}
}

TODO: add bibtex entry for this model once published. */

open ctlfc_path[State]

enum Train { t0, t1, t2, t3, t4, t5, t6, t7 }

enum Station {  s1,  s2,  s3,  s4,  s5,  s6,  s7,  s8,  s9, s10,
               s11, s12, s13, s14, s15, s16, s17, s18, s19, s20,
               s21, s22, s23, s24, s25, s26, s27 }

let indices = { 0 + 1 + 2 + 3 + 4 + 5 + 6 }
let rs = { 0 + 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 }
let LA = 7
let LB = 7

one sig Const {
  T0, T1, T2, T3, T4, T5, T6, T7: indices -> Station,
  A0, A1, A2, A3, A4, A5, A6, A7,
  B0, B1, B2, B3, B4, B5, B6, B7: indices -> { -1 + 0 + 1 }
} {
  // train missions
  T0 = { 0->s1  + 1->s9  + 2->s10 + 3->s13 + 4->s15 + 5->s20 + 6->s23 }
  T1 = { 0->s3  + 1->s9  + 2->s10 + 3->s13 + 4->s15 + 5->s20 + 6->s24 }
  T2 = { 0->s5  + 1->s27 + 2->s11 + 3->s13 + 4->s16 + 5->s20 + 6->s25 }
  T3 = { 0->s7  + 1->s27 + 2->s11 + 3->s13 + 4->s16 + 5->s20 + 6->s26 }
  T4 = { 0->s23 + 1->s22 + 2->s17 + 3->s18 + 4->s11 + 5->s9  + 6->s2 }
  T5 = { 0->s24 + 1->s22 + 2->s17 + 3->s18 + 4->s11 + 5->s9  + 6->s4 }
  T6 = { 0->s25 + 1->s22 + 2->s17 + 3->s18 + 4->s12 + 5->s27 + 6->s6 }
  T7 = { 0->s26 + 1->s22 + 2->s17 + 3->s18 + 4->s12 + 5->s27 + 6->s8 }
  // region A train constraints
  A0 = { 0->0 + 1->0 + 2->0 + 3->1  + 4->0  + 5->-1 + 6->0 }
  A1 = { 0->0 + 1->0 + 2->0 + 3->1  + 4->0  + 5->-1 + 6->0 }
  A2 = { 0->0 + 1->0 + 2->1 + 3->-1 + 4->0  + 5->1  + 6->0 }
  A3 = { 0->0 + 1->0 + 2->1 + 3->-1 + 4->0  + 5->0  + 6->0 }
  A4 = { 0->0 + 1->1 + 2->0 + 3->0  + 4->-1 + 5->0  + 6->0 }
  A5 = { 0->0 + 1->1 + 2->0 + 3->0  + 4->-1 + 5->0  + 6->0 }
  A6 = { 0->0 + 1->0 + 2->0 + 3->-1 + 4->0  + 5->0  + 6->0 }
  A7 = { 0->0 + 1->1 + 2->0 + 3->-1 + 4->0  + 5->0  + 6->0 }
  // region B train constraints
  B0 = { 0->0 + 1->0 + 2->0 + 3->1  + 4->0  + 5->-1 + 6->0 }
  B1 = { 0->0 + 1->0 + 2->0 + 3->1  + 4->0  + 5->-1 + 6->0 }
  B2 = { 0->0 + 1->0 + 2->1 + 3->-1 + 4->0  + 5->0  + 6->0 }
  B3 = { 0->0 + 1->0 + 2->1 + 3->-1 + 4->0  + 5->1  + 6->0 }
  B4 = { 0->0 + 1->1 + 2->0 + 3->0  + 4->-1 + 5->0  + 6->0 }
  B5 = { 0->0 + 1->1 + 2->0 + 3->0  + 4->-1 + 5->0  + 6->0 }
  B6 = { 0->0 + 1->1 + 2->0 + 3->-1 + 4->0  + 5->0  + 6->0 }
  B7 = { 0->0 + 1->0 + 2->0 + 3->-1 + 4->0  + 5->0  + 6->0 }
}

sig State {
  P0, P1, P2, P3, P4, P5, P6, P7: one indices,
  RA, RB: one rs,
  ct: one Train
}

pred init[s: State] {
  s.P0 = 0
  s.P1 = 0
  s.P2 = 0
  s.P3 = 0
  s.P4 = 0
  s.P5 = 0
  s.P6 = 0
  s.P7 = 0
  s.RA = 1
  s.RB = 1
}

pred choose_train[s, s': State] {
       s.ct = t0 implies s'.ct = t1
  else s.ct = t1 implies s'.ct = t2
  else s.ct = t2 implies s'.ct = t3
  else s.ct = t3 implies s'.ct = t4
  else s.ct = t4 implies s'.ct = t5
  else s.ct = t5 implies s'.ct = t6
  else s.ct = t6 implies s'.ct = t7
  else s.ct = t7 implies s'.ct = t0

  s'.RA = s.RA
  s'.RB = s.RB
  s'.P0 = s.P0
  s'.P1 = s.P1
  s'.P2 = s.P2
  s'.P3 = s.P3
  s'.P4 = s.P4
  s'.P5 = s.P5
  s'.P6 = s.P6
  s'.P7 = s.P7
}

pred pre_move_train0[s: State] {
  // t0 was chosen
  s.ct = t0
  // t0 hasn't reached end of its mission
  s.P0 < 6
  // next stop of t0 isn't occupied by any other train
  let ns = Const.T0[s.P0.plus[1]] |
    ns != Const.T1[s.P1] and
    ns != Const.T2[s.P2] and
    ns != Const.T3[s.P3] and
    ns != Const.T4[s.P4] and
    ns != Const.T5[s.P5] and
    ns != Const.T6[s.P6] and
    ns != Const.T7[s.P7]
  // progress of t0 will not saturate A or B
  s.RA.plus[Const.A0[s.P0.plus[1]]] <= LA
  s.RB.plus[Const.B0[s.P0.plus[1]]] <= LB
}
pred post_move_train0[s, s': State] {
  s'.P0 = s.P0.plus[1]
  s'.P1 = s.P1
  s'.P2 = s.P2
  s'.P3 = s.P3
  s'.P4 = s.P4
  s'.P5 = s.P5
  s'.P6 = s.P6
  s'.P7 = s.P7
  s'.RA = s.RA.plus[Const.A0[s'.P0]]
  s'.RB = s.RB.plus[Const.B0[s'.P0]]
}
pred move_train0[s, s': State] {
  pre_move_train0[s]
  post_move_train0[s, s']
}

pred pre_move_train1[s: State] {
  // t1 was chosen
  s.ct = t1
  // t1 hasn't reached end of its mission
  s.P1 < 6
  // next stop of t1 isn't occupied by any other train
  let ns = Const.T1[s.P1.plus[1]] |
    ns != Const.T0[s.P0] and
    ns != Const.T2[s.P2] and
    ns != Const.T3[s.P3] and
    ns != Const.T4[s.P4] and
    ns != Const.T5[s.P5] and
    ns != Const.T6[s.P6] and
    ns != Const.T7[s.P7]
  // progress of t1 will not saturate A or B
  s.RA.plus[Const.A1[s.P1.plus[1]]] <= LA
  s.RB.plus[Const.B1[s.P1.plus[1]]] <= LB
}
pred post_move_train1[s, s': State] {
  s'.P1 = s.P1.plus[1]
  s'.P0 = s.P0
  s'.P2 = s.P2
  s'.P3 = s.P3
  s'.P4 = s.P4
  s'.P5 = s.P5
  s'.P6 = s.P6
  s'.P7 = s.P7
  s'.RA = s.RA.plus[Const.A1[s'.P1]]
  s'.RB = s.RB.plus[Const.B1[s'.P1]]
}
pred move_train1[s, s': State] {
  pre_move_train1[s]
  post_move_train1[s, s']
}

pred pre_move_train2[s: State] {
  // t2 was chosen
  s.ct = t2
  // t2 hasn't reached end of its mission
  s.P2 < 6
  // next stop of t2 isn't occupied by any other train
  let ns = Const.T2[s.P2.plus[1]] |
    ns != Const.T0[s.P0] and
    ns != Const.T1[s.P1] and
    ns != Const.T3[s.P3] and
    ns != Const.T4[s.P4] and
    ns != Const.T5[s.P5] and
    ns != Const.T6[s.P6] and
    ns != Const.T7[s.P7]
  // progress of t2 will not saturate A or B
  s.RA.plus[Const.A2[s.P2.plus[1]]] <= LA
  s.RB.plus[Const.B2[s.P2.plus[1]]] <= LB
}
pred post_move_train2[s, s': State] {
  s'.P2 = s.P2.plus[1]
  s'.P0 = s.P0
  s'.P1 = s.P1
  s'.P3 = s.P3
  s'.P4 = s.P4
  s'.P5 = s.P5
  s'.P6 = s.P6
  s'.P7 = s.P7
  s'.RA = s.RA.plus[Const.A2[s'.P2]]
  s'.RB = s.RB.plus[Const.B2[s'.P2]]
}
pred move_train2[s, s': State] {
  pre_move_train2[s]
  post_move_train2[s, s']
}

pred pre_move_train3[s: State] {
  // t3 was chosen
  s.ct = t3
  // t3 hasn't reached end of its mission
  s.P3 < 6
  // next stop of t3 isn't occupied by any other train
  let ns = Const.T3[s.P3.plus[1]] |
    ns != Const.T0[s.P0] and
    ns != Const.T1[s.P1] and
    ns != Const.T2[s.P2] and
    ns != Const.T4[s.P4] and
    ns != Const.T5[s.P5] and
    ns != Const.T6[s.P6] and
    ns != Const.T7[s.P7]
  // progress of t3 will not saturate A or B
  s.RA.plus[Const.A3[s.P3.plus[1]]] <= LA
  s.RB.plus[Const.B3[s.P3.plus[1]]] <= LB
}
pred post_move_train3[s, s': State] {
  s'.P3 = s.P3.plus[1]
  s'.P0 = s.P0
  s'.P1 = s.P1
  s'.P2 = s.P2
  s'.P4 = s.P4
  s'.P5 = s.P5
  s'.P6 = s.P6
  s'.P7 = s.P7
  s'.RA = s.RA.plus[Const.A3[s'.P3]]
  s'.RB = s.RB.plus[Const.B3[s'.P3]]
}
pred move_train3[s, s': State] {
  pre_move_train3[s]
  post_move_train3[s, s']
}

pred pre_move_train4[s: State] {
  // t4 was chosen
  s.ct = t4
  // t4 hasn't reached end of its mission
  s.P4 < 6
  // next stop of t4 isn't occupied by any other train
  let ns = Const.T4[s.P4.plus[1]] |
    ns != Const.T0[s.P0] and
    ns != Const.T1[s.P1] and
    ns != Const.T2[s.P2] and
    ns != Const.T3[s.P3] and
    ns != Const.T5[s.P5] and
    ns != Const.T6[s.P6] and
    ns != Const.T7[s.P7]
  // progress of t4 will not saturate A or B
  s.RA.plus[Const.A4[s.P4.plus[1]]] <= LA
  s.RB.plus[Const.B4[s.P4.plus[1]]] <= LB
}
pred post_move_train4[s, s': State] {
  s'.P4 = s.P4.plus[1]
  s'.P0 = s.P0
  s'.P1 = s.P1
  s'.P2 = s.P2
  s'.P3 = s.P3
  s'.P5 = s.P5
  s'.P6 = s.P6
  s'.P7 = s.P7
  s'.RA = s.RA.plus[Const.A4[s'.P4]]
  s'.RB = s.RB.plus[Const.B4[s'.P4]]
}
pred move_train4[s, s': State] {
  pre_move_train4[s]
  post_move_train4[s, s']
}

pred pre_move_train5[s: State] {
  // t5 was chosen
  s.ct = t5
  // t5 hasn't reached end of its mission
  s.P5 < 6
  // next stop of t5 isn't occupied by any other train
  let ns = Const.T5[s.P5.plus[1]] |
    ns != Const.T0[s.P0] and
    ns != Const.T1[s.P1] and
    ns != Const.T2[s.P2] and
    ns != Const.T3[s.P3] and
    ns != Const.T4[s.P4] and
    ns != Const.T6[s.P6] and
    ns != Const.T7[s.P7]
  // progress of t5 will not saturate A or B
  s.RA.plus[Const.A5[s.P5.plus[1]]] <= LA
  s.RB.plus[Const.B5[s.P5.plus[1]]] <= LB
}
pred post_move_train5[s, s': State] {
  s'.P5 = s.P5.plus[1]
  s'.P0 = s.P0
  s'.P1 = s.P1
  s'.P2 = s.P2
  s'.P3 = s.P3
  s'.P4 = s.P4
  s'.P6 = s.P6
  s'.P7 = s.P7
  s'.RA = s.RA.plus[Const.A5[s'.P5]]
  s'.RB = s.RB.plus[Const.B5[s'.P5]]
}
pred move_train5[s, s': State] {
  pre_move_train5[s]
  post_move_train5[s, s']
}

pred pre_move_train6[s: State] {
  // t6 was chosen
  s.ct = t6
  // t6 hasn't reached end of its mission
  s.P6 < 6
  // next stop of t6 isn't occupied by any other train
  let ns = Const.T6[s.P6.plus[1]] |
    ns != Const.T0[s.P0] and
    ns != Const.T1[s.P1] and
    ns != Const.T2[s.P2] and
    ns != Const.T3[s.P3] and
    ns != Const.T4[s.P4] and
    ns != Const.T5[s.P5] and
    ns != Const.T7[s.P7]
  // progress of t6 will not saturate A or B
  s.RA.plus[Const.A6[s.P6.plus[1]]] <= LA
  s.RB.plus[Const.B6[s.P6.plus[1]]] <= LB
}
pred post_move_train6[s, s': State] {
  s'.P6 = s.P6.plus[1]
  s'.P0 = s.P0
  s'.P1 = s.P1
  s'.P2 = s.P2
  s'.P3 = s.P3
  s'.P4 = s.P4
  s'.P5 = s.P5
  s'.P7 = s.P7
  s'.RA = s.RA.plus[Const.A6[s'.P6]]
  s'.RB = s.RB.plus[Const.B6[s'.P6]]
}
pred move_train6[s, s': State] {
  pre_move_train6[s]
  post_move_train6[s, s']
}

pred pre_move_train7[s: State] {
  // t7 was chosen
  s.ct = t7
  // t7 hasn't reached end of its mission
  s.P7 < 6
  // next stop of t7 isn't occupied by any other train
  let ns = Const.T7[s.P7.plus[1]] |
    ns != Const.T0[s.P0] and
    ns != Const.T1[s.P1] and
    ns != Const.T2[s.P2] and
    ns != Const.T3[s.P3] and
    ns != Const.T4[s.P4] and
    ns != Const.T5[s.P5] and
    ns != Const.T6[s.P6]
  // progress of t7 will not saturate A or B
  s.RA.plus[Const.A7[s.P7.plus[1]]] <= LA
  s.RB.plus[Const.B7[s.P7.plus[1]]] <= LB
}
pred post_move_train7[s, s': State] {
  s'.P7 = s.P7.plus[1]
  s'.P0 = s.P0
  s'.P1 = s.P1
  s'.P2 = s.P2
  s'.P3 = s.P3
  s'.P4 = s.P4
  s'.P5 = s.P5
  s'.P6 = s.P6
  s'.RA = s.RA.plus[Const.A7[s'.P7]]
  s'.RB = s.RB.plus[Const.B7[s'.P7]]
}
pred move_train7[s, s': State] {
  pre_move_train7[s]
  post_move_train7[s, s']
}


pred next[s, s': State] {
  /*   choose_train[s, s']
  or */ move_train0[s, s']
  or move_train1[s, s']
  or move_train2[s, s']
  or move_train3[s, s']
  or move_train4[s, s']
  or move_train5[s, s']
  or move_train6[s, s']
  or move_train7[s, s']
}

fact {
  all s:     State | s in initialState iff init[s]
  all s, s': State | s->s' in nextState iff next[s, s']
  all s, s': State |
        s.P0 = s'.P0
    and s.P1 = s'.P1
    and s.P2 = s'.P2
    and s.P3 = s'.P3
    and s.P4 = s'.P4
    and s.P5 = s'.P5
    and s.P6 = s'.P6
    and s.P7 = s'.P7
    and s.RA = s'.RA
    and s.RB = s'.RB
    and s.ct = s'.ct
    implies s = s'
}

pred reachablity_axiom {
  all s: State | s in State.(initialState <: *nextState)
}
pred operations_axiom {
  some s, s': State | move_train0[s, s']
  some s, s': State | move_train1[s, s']
  some s, s': State | move_train2[s, s']
  some s, s': State | move_train3[s, s']
  some s, s': State | move_train4[s, s']
  some s, s': State | move_train5[s, s']
  some s, s': State | move_train6[s, s']
  some s, s': State | move_train7[s, s']
}
pred significance_axioms {
  reachablity_axiom
  operations_axiom
}
run significance_axioms for 10

check eventually_all_arrived {
  ctlfc_mc[ag[ef[{s: State |
    s.P0 = 6 and
    s.P1 = 6 and
    s.P2 = 6 and
    s.P3 = 6 and
    s.P4 = 6 and
    s.P5 = 6 and
    s.P6 = 6 and
    s.P7 = 6}]]]
} for 10 but 8 State // 40

// 6: 193134 vars. 6234 primary vars. 714811 clauses. 829ms.
//   No counterexample found. Assertion may be valid. 730313ms.

// 7: 237796 vars. 6428 primary vars. 883668 clauses. 961ms.
//   No counterexample found. Assertion may be valid. 5177417ms.

// 8: 286602 vars. 6624 primary vars. 1068297 clauses. 826ms.
//   No counterexample found. Assertion may be valid. 31880240ms.
