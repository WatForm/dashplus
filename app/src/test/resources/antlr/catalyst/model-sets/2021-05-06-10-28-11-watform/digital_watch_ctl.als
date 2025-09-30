/* digital_watch.als -- Digital watch model

Copyright (C) 2019 Amin Bandali <bandali@gnu.org>

digital_watch.als is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License,
or (at your option) any later version.

digital_watch.als is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with digital_watch.als.  If not, see <https://www.gnu.org/licenses/>.

Date: December 17, 2019

Notes:

This model is an adaptation of the digital watch model presented by
Harel in~\cite{DBLP:journals/scp/Harel87}.

@Article{DBLP:journals/scp/Harel87,
  author    = {David Harel},
  title     = {Statecharts: {A} Visual Formalism for Complex Systems},
  year      = 1987,
  volume    = 8,
  number    = 3,
  pages     = {231-274},
  doi       = {10.1016/0167-6423(87)90035-9},
  url       = {https://doi.org/10.1016/0167-6423(87)90035-9},
  journal   = {Sci. Comput. Program.},
  timestamp = {Wed, 14 Nov 2018 10:21:28 +0100},
  biburl    = {https://dblp.org/rec/bib/journals/scp/Harel87},
  bibsource = {dblp computer science bibliography, https://dblp.org}
}

TODO: add bibtex entry for this model once published. */

open ctlfc_path[State]

abstract sig Key, Display, Bool {}
one sig a, b, c, d extends Key {}
one sig Time, Date, Wait, Update,
        Alarm1, Alarm2, Chime, StopWatch
        extends Display {}
one sig false, true extends Bool {}

sig State {
  light: Bool,
  display: Display,
  pressed: Key one -> one Bool,
  waited_2_min: Bool,
  waited_2_sec: Bool
}

// <Light>
pred pre_light_off_light_on[s: State] {
  s.light = false
  s.pressed[b] = true
}
pred post_light_off_light_on[s, s': State] {
  s'.light = true
  s'.display = s.display
  s'.pressed = s.pressed
  s'.waited_2_min = s.waited_2_min
  s'.waited_2_sec = s.waited_2_sec
}
pred light_off_light_on[s, s': State] {
  pre_light_off_light_on[s]
  post_light_off_light_on[s, s']
}

pred pre_light_on_light_off[s: State] {
  s.light = true
  s.pressed[b] = false
}
pred post_light_on_light_off[s, s': State] {
  s'.light = false
  s'.display = s.display
  s'.pressed = s.pressed
  s'.waited_2_min = s.waited_2_min
  s'.waited_2_sec = s.waited_2_sec
}
pred light_on_light_off[s, s': State] {
  pre_light_on_light_off[s]
  post_light_on_light_off[s, s']
}
// </Light>

// <Time>
pred pre_time_show_date[s: State] {
  s.display = Time
  s.pressed[d] = true
}
pred post_time_show_date[s, s': State] {
  s'.display = Date
  s'.light = s.light
  s'.pressed = s.pressed
  s'.waited_2_min = s.waited_2_min
  s'.waited_2_sec = s.waited_2_sec
}
pred time_show_date[s, s': State] {
  pre_time_show_date[s]
  post_time_show_date[s, s']
}

pred pre_time_try_update[s: State] {
  s.display = Time
  s.pressed[c] = true
}
pred post_time_try_update[s, s': State] {
  s'.display = Wait
  s'.light = s.light
  s'.pressed = s.pressed
  s'.waited_2_min = s.waited_2_min
  s'.waited_2_sec = s.waited_2_sec
}
pred time_try_update[s, s': State] {
  pre_time_try_update[s]
  post_time_try_update[s, s']
}

pred pre_time_go2alarm1[s: State] {
  s.display = Time
  s.pressed[a] = true
}
pred post_time_go2alarm1[s, s': State] {
  s'.display = Alarm1
  s'.light = s.light
  s'.pressed = s.pressed
  s'.waited_2_min = s.waited_2_min
  s'.waited_2_sec = s.waited_2_sec
}
pred time_go2alarm1[s, s': State] {
  pre_time_go2alarm1[s]
  post_time_go2alarm1[s, s']
}
// </Time>

// <Date>
pred pre_date_show_time[s: State] {
  s.display = Date
  s.pressed[d] = true
}
pred post_date_show_time[s, s': State] {
  s'.display = Time
  s'.light = s.light
  s'.pressed = s.pressed
  s'.waited_2_min = s.waited_2_min
  s'.waited_2_sec = s.waited_2_sec
}
pred date_show_time[s, s': State] {
  pre_date_show_time[s]
  post_date_show_time[s, s']
}

pred pre_date_return_to_time[s: State] {
  s.display = Date
  s.waited_2_min = true
}
pred post_date_return_to_time[s, s': State] {
  s'.display = Time
  s'.light = s.light
  s'.pressed = s.pressed
  s'.waited_2_min = s.waited_2_min
  s'.waited_2_sec = s.waited_2_sec
}
pred date_return_to_time[s, s': State] {
  pre_date_return_to_time[s]
  post_date_return_to_time[s, s']
}
// </Date>

// <Wait>
pred pre_wait_show_time[s: State] {
  s.display = Wait
  s.pressed[c] = false
}
pred post_wait_show_time[s, s': State] {
  s'.display = Time
  s'.light = s.light
  s'.pressed = s.pressed
  s'.waited_2_min = s.waited_2_min
  s'.waited_2_sec = s.waited_2_sec
}
pred wait_show_time[s, s': State] {
  pre_wait_show_time[s]
  post_wait_show_time[s, s']
}

pred pre_wait_show_update[s: State] {
  s.display = Wait
  s.waited_2_sec = true
}
pred post_wait_show_update[s, s': State] {
  s'.display = Update
  s'.light = s.light
  s'.pressed = s.pressed
  s'.waited_2_min = s.waited_2_min
  s'.waited_2_sec = s.waited_2_sec
}
pred wait_show_update[s, s': State] {
  pre_wait_show_update[s]
  post_wait_show_update[s, s']
}
// </Wait>

// <Update>
pred pre_update_show_time[s: State] {
  s.display = Update
  s.pressed[b] = true
}
pred post_update_show_time[s, s': State] {
  s'.display = Time
  s'.light = s.light
  s'.pressed = s.pressed
  s'.waited_2_min = s.waited_2_min
  s'.waited_2_sec = s.waited_2_sec
}
pred update_show_time[s, s': State] {
  pre_update_show_time[s]
  post_update_show_time[s, s']
}
// </Update>

// <Alarm1>
pred pre_alarm1_go2alarm2[s: State] {
  s.display = Alarm1
  s.pressed[a] = true
}
pred post_alarm1_go2alarm2[s, s': State] {
  s'.display = Alarm2
  s'.light = s.light
  s'.pressed = s.pressed
  s'.waited_2_min = s.waited_2_min
  s'.waited_2_sec = s.waited_2_sec
}
pred alarm1_go2alarm2[s, s': State] {
  pre_alarm1_go2alarm2[s]
  post_alarm1_go2alarm2[s, s']
}
// </Alarm1>

// <Alarm2>
pred pre_alarm2_go2chime[s: State] {
  s.display = Alarm2
  s.pressed[a] = true
}
pred post_alarm2_go2chime[s, s': State] {
  s'.display = Chime
  s'.light = s.light
  s'.pressed = s.pressed
  s'.waited_2_min = s.waited_2_min
  s'.waited_2_sec = s.waited_2_sec
}
pred alarm2_go2chime[s, s': State] {
  pre_alarm2_go2chime[s]
  post_alarm2_go2chime[s, s']
}
// </Alarm2>

// <Chime>
pred pre_chime_go2Stopwatch[s: State] {
  s.display = Chime
  s.pressed[a] = true
}
pred post_chime_go2Stopwatch[s, s': State] {
  s'.display = StopWatch
  s'.light = s.light
  s'.pressed = s.pressed
  s'.waited_2_min = s.waited_2_min
  s'.waited_2_sec = s.waited_2_sec
}
pred chime_go2Stopwatch[s, s': State] {
  pre_chime_go2Stopwatch[s]
  post_chime_go2Stopwatch[s, s']
}
// </Chime>

// <StopWatch>
pred pre_Stopwatch_go2time[s: State] {
  s.display = StopWatch
  s.pressed[a] = true
}
pred post_Stopwatch_go2time[s, s': State] {
  s'.display = Time
  s'.light = s.light
  s'.pressed = s.pressed
  s'.waited_2_min = s.waited_2_min
  s'.waited_2_sec = s.waited_2_sec
}
pred Stopwatch_go2time[s, s': State] {
  pre_Stopwatch_go2time[s]
  post_Stopwatch_go2time[s, s']
}
// </StopWatch>

// <Alarms_Beep>
// </Alarms_Beep>


pred init[s: State] {
  s.light = false
  s.display = Time
  all k: Key | s.pressed[k] = false
  s.waited_2_min = false
  s.waited_2_sec = false
}

pred next[s, s': State] {
     light_off_light_on[s, s']
  or light_on_light_off[s, s']
  or time_show_date[s, s']
  or time_try_update[s, s']
  or time_go2alarm1[s, s']
  or date_show_time[s, s']
  or date_return_to_time[s, s']
  or wait_show_time[s, s']
  or wait_show_update[s, s']
  or update_show_time[s, s']
  or alarm1_go2alarm2[s, s']
  or alarm2_go2chime[s, s']
  or chime_go2Stopwatch[s, s']
  or Stopwatch_go2time[s, s']
}


fact {
  all s:     State | s in initialState iff init[s]
  all s, s': State | s->s' in nextState iff next[s, s']
  all s, s': State |
        s.light = s'.light
    and s.display = s'.display
    and s.pressed = s'.pressed
    and s.waited_2_min = s'.waited_2_min
    and s.waited_2_sec = s'.waited_2_sec
    implies s = s'
}

pred reachablity_axiom {
  all s: State | s in State.(initialState <: *nextState)
}
pred operations_axiom {
  some s, s': State | light_off_light_on[s, s']
  some s, s': State | light_on_light_off[s, s']
  some s, s': State | time_show_date[s, s']
  some s, s': State | time_try_update[s, s']
  some s, s': State | time_go2alarm1[s, s']
  some s, s': State | date_show_time[s, s']
  some s, s': State | date_return_to_time[s, s']
  some s, s': State | wait_show_time[s, s']
  some s, s': State | wait_show_update[s, s']
  some s, s': State | update_show_time[s, s']
  some s, s': State | alarm1_go2alarm2[s, s']
  some s, s': State | alarm2_go2chime[s, s']
  some s, s': State | chime_go2Stopwatch[s, s']
  some s, s': State | Stopwatch_go2time[s, s']
}
pred significance_axioms {
  reachablity_axiom
  operations_axiom
}
run significance_axioms for 10

check eventually_time {
  ctlfc_mc[ag[ef[{s: State | s.pressed[a] = true implies
    some s': s.*nextState | s'.display = Time}]]]
} for 10
