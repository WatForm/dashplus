sig A {}
sig B {}
sig C in A {}
sig D extends B {}
sig E extends B {}

run {
	some A
} for 2 A, 2 B
check {
	some B
} for 1 A, 1 B