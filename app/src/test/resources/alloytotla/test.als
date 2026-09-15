open input1 as X
open input2 as Y

sig A {
 f : A -> A
}

sig B extends A {}
sig C in A+B {}
abstract sig D {}
sig E extends D {}
sig F extends D {}


run {} for exactly 2 A