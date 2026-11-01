// Repeated opens share their instance but still occupy import positions.
open util/boolean
open util/boolean
open util/ordering[A]
open util/ordering[A]
open util/ordering[B]

sig A {}
sig B {}

run {} for exactly 2 A, exactly 2 B
