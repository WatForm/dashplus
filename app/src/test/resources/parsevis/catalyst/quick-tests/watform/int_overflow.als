let rs = { 0 + 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 }

sig State {
  RA: one rs,
}

/*fact {
  some s: State | s.RA = 9
}*/

run {}
