sig A {}
abstract sig X {}

sig B in A {}
sig C in A+X {}



sig Y extends X {}
sig Z extends X {}


run {} for exactly 2 A