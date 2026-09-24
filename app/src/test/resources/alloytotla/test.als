sig A {}
sig B {}

run {
	some A
} for 2 A, 2 B
check {
	some B
} for 1 A, 1 B