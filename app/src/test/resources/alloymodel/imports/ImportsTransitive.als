// Both helpers reuse local aliases; each has its own import positions.
open import_helpers/Left as left
open import_helpers/Right as right
open util/ordering[Root]
open util/ordering[Other]

sig Root {}
sig Other {}

run {} for 2
