sig Animal {}          // Top-level type signature
sig Plant {}           // Top-level type signature

sig Dog extends Animal {} // Type signature
sig Cat extends Animal {} // Type signature

sig F{}

sig Pet in Dog+Cat {
	f: F
} // Subset signature
sig Stray in Animal {
	f: F
}    // Subset signature
sig Weed in Plant {
	f: F
}      // Subset signature

/*
Two overlapping signatures cannot have
two fields with the same name "f":

1) one is in sig "this/Stray"
line 13, column 2,
filename=/Users/qiyuechen/25F/dash/Untitled 1.als

2) the other is in sig "this/Pet"
line 10, column 2,
filename=/Users/qiyuechen/25F/dash/Untitled 1.als
*/

