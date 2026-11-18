// The implicit integer import is position 1. The second ordering is open$4.
open util/boolean
open util/ordering[A]
open util/ordering[B]
open util/ordering[C]

sig A {}
sig B {}
sig C {}

run {} for exactly 2 A, exactly 2 B, exactly 2 C
