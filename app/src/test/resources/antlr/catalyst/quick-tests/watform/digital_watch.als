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

open util/ordering [State] as StateOrdering

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
pred post_light_off_light_on[s, sPrime: State] {
  sPrime.light = true
  sPrime.display = s.display
  sPrime.pressed = s.pressed
  sPrime.waited_2_min = s.waited_2_min
  sPrime.waited_2_sec = s.waited_2_sec
}
pred light_off_light_on[s, sPrime: State] {
  pre_light_off_light_on[s]
  post_light_off_light_on[s, sPrime]
}

pred pre_light_on_light_off[s: State] {
  s.light = true
  s.pressed[b] = false
}
pred post_light_on_light_off[s, sPrime: State] {
  sPrime.light = false
  sPrime.display = s.display
  sPrime.pressed = s.pressed
  sPrime.waited_2_min = s.waited_2_min
  sPrime.waited_2_sec = s.waited_2_sec
}
pred light_on_light_off[s, sPrime: State] {
  pre_light_on_light_off[s]
  post_light_on_light_off[s, sPrime]
}
// </Light>

// <Time>
pred pre_time_show_date[s: State] {
  s.display = Time
  s.pressed[d] = true
}
pred post_time_show_date[s, sPrime: State] {
  sPrime.display = Date
  sPrime.light = s.light
  sPrime.pressed = s.pressed
  sPrime.waited_2_min = s.waited_2_min
  sPrime.waited_2_sec = s.waited_2_sec
}
pred time_show_date[s, sPrime: State] {
  pre_time_show_date[s]
  post_time_show_date[s, sPrime]
}

pred pre_time_try_update[s: State] {
  s.display = Time
  s.pressed[c] = true
}
pred post_time_try_update[s, sPrime: State] {
  sPrime.display = Wait
  sPrime.light = s.light
  sPrime.pressed = s.pressed
  sPrime.waited_2_min = s.waited_2_min
  sPrime.waited_2_sec = s.waited_2_sec
}
pred time_try_update[s, sPrime: State] {
  pre_time_try_update[s]
  post_time_try_update[s, sPrime]
}

pred pre_time_go2alarm1[s: State] {
  s.display = Time
  s.pressed[a] = true
}
pred post_time_go2alarm1[s, sPrime: State] {
  sPrime.display = Alarm1
  sPrime.light = s.light
  sPrime.pressed = s.pressed
  sPrime.waited_2_min = s.waited_2_min
  sPrime.waited_2_sec = s.waited_2_sec
}
pred time_go2alarm1[s, sPrime: State] {
  pre_time_go2alarm1[s]
  post_time_go2alarm1[s, sPrime]
}
// </Time>

// <Date>
pred pre_date_show_time[s: State] {
  s.display = Date
  s.pressed[d] = true
}
pred post_date_show_time[s, sPrime: State] {
  sPrime.display = Time
  sPrime.light = s.light
  sPrime.pressed = s.pressed
  sPrime.waited_2_min = s.waited_2_min
  sPrime.waited_2_sec = s.waited_2_sec
}
pred date_show_time[s, sPrime: State] {
  pre_date_show_time[s]
  post_date_show_time[s, sPrime]
}

pred pre_date_return_to_time[s: State] {
  s.display = Date
  s.waited_2_min = true
}
pred post_date_return_to_time[s, sPrime: State] {
  sPrime.display = Time
  sPrime.light = s.light
  sPrime.pressed = s.pressed
  sPrime.waited_2_min = s.waited_2_min
  sPrime.waited_2_sec = s.waited_2_sec
}
pred date_return_to_time[s, sPrime: State] {
  pre_date_return_to_time[s]
  post_date_return_to_time[s, sPrime]
}
// </Date>

// <Wait>
pred pre_wait_show_time[s: State] {
  s.display = Wait
  s.pressed[c] = false
}
pred post_wait_show_time[s, sPrime: State] {
  sPrime.display = Time
  sPrime.light = s.light
  sPrime.pressed = s.pressed
  sPrime.waited_2_min = s.waited_2_min
  sPrime.waited_2_sec = s.waited_2_sec
}
pred wait_show_time[s, sPrime: State] {
  pre_wait_show_time[s]
  post_wait_show_time[s, sPrime]
}

pred pre_wait_show_update[s: State] {
  s.display = Wait
  s.waited_2_sec = true
}
pred post_wait_show_update[s, sPrime: State] {
  sPrime.display = Update
  sPrime.light = s.light
  sPrime.pressed = s.pressed
  sPrime.waited_2_min = s.waited_2_min
  sPrime.waited_2_sec = s.waited_2_sec
}
pred wait_show_update[s, sPrime: State] {
  pre_wait_show_update[s]
  post_wait_show_update[s, sPrime]
}
// </Wait>

// <Update>
pred pre_update_show_time[s: State] {
  s.display = Update
  s.pressed[b] = true
}
pred post_update_show_time[s, sPrime: State] {
  sPrime.display = Time
  sPrime.light = s.light
  sPrime.pressed = s.pressed
  sPrime.waited_2_min = s.waited_2_min
  sPrime.waited_2_sec = s.waited_2_sec
}
pred update_show_time[s, sPrime: State] {
  pre_update_show_time[s]
  post_update_show_time[s, sPrime]
}
// </Update>

// <Alarm1>
pred pre_alarm1_go2alarm2[s: State] {
  s.display = Alarm1
  s.pressed[a] = true
}
pred post_alarm1_go2alarm2[s, sPrime: State] {
  sPrime.display = Alarm2
  sPrime.light = s.light
  sPrime.pressed = s.pressed
  sPrime.waited_2_min = s.waited_2_min
  sPrime.waited_2_sec = s.waited_2_sec
}
pred alarm1_go2alarm2[s, sPrime: State] {
  pre_alarm1_go2alarm2[s]
  post_alarm1_go2alarm2[s, sPrime]
}
// </Alarm1>

// <Alarm2>
pred pre_alarm2_go2chime[s: State] {
  s.display = Alarm2
  s.pressed[a] = true
}
pred post_alarm2_go2chime[s, sPrime: State] {
  sPrime.display = Chime
  sPrime.light = s.light
  sPrime.pressed = s.pressed
  sPrime.waited_2_min = s.waited_2_min
  sPrime.waited_2_sec = s.waited_2_sec
}
pred alarm2_go2chime[s, sPrime: State] {
  pre_alarm2_go2chime[s]
  post_alarm2_go2chime[s, sPrime]
}
// </Alarm2>

// <Chime>
pred pre_chime_go2Stopwatch[s: State] {
  s.display = Chime
  s.pressed[a] = true
}
pred post_chime_go2Stopwatch[s, sPrime: State] {
  sPrime.display = StopWatch
  sPrime.light = s.light
  sPrime.pressed = s.pressed
  sPrime.waited_2_min = s.waited_2_min
  sPrime.waited_2_sec = s.waited_2_sec
}
pred chime_go2Stopwatch[s, sPrime: State] {
  pre_chime_go2Stopwatch[s]
  post_chime_go2Stopwatch[s, sPrime]
}
// </Chime>

// <StopWatch>
pred pre_Stopwatch_go2time[s: State] {
  s.display = StopWatch
  s.pressed[a] = true
}
pred post_Stopwatch_go2time[s, sPrime: State] {
  sPrime.display = Time
  sPrime.light = s.light
  sPrime.pressed = s.pressed
  sPrime.waited_2_min = s.waited_2_min
  sPrime.waited_2_sec = s.waited_2_sec
}
pred Stopwatch_go2time[s, sPrime: State] {
  pre_Stopwatch_go2time[s]
  post_Stopwatch_go2time[s, sPrime]
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

pred next[s, sPrime: State] {
     light_off_light_on[s, sPrime]
  or light_on_light_off[s, sPrime]
  or time_show_date[s, sPrime]
  or time_try_update[s, sPrime]
  or time_go2alarm1[s, sPrime]
  or date_show_time[s, sPrime]
  or date_return_to_time[s, sPrime]
  or wait_show_time[s, sPrime]
  or wait_show_update[s, sPrime]
  or update_show_time[s, sPrime]
  or alarm1_go2alarm2[s, sPrime]
  or alarm2_go2chime[s, sPrime]
  or chime_go2Stopwatch[s, sPrime]
  or Stopwatch_go2time[s, sPrime]
}


fact traces {
  init[StateOrdering/first]
  all s: State-StateOrdering/last |
    let sPrime = s.StateOrdering/next |
      next[s, sPrime]
}


check eventually_time {
  all s: State-StateOrdering/last |
      s.pressed[a] = true implies some sPrime: s.*StateOrdering/next |
        sPrime.display = Time
} for 10
