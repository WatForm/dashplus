// Explicit aliases and unrelated imports also occupy import positions.
open util/ordering[A]
open util/boolean as bool
open util/ordering[B] as named
open util/ordering[C]
open util/ordering[D]

sig A {}
sig B {}
sig C {}
sig D {}

pred endpoints {
    some ordering/first
    some named/last
}

run endpoints for exactly 2 A, exactly 2 B, exactly 2 C, exactly 2 D
