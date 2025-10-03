module correct -- correctness proof obligations
open mat
open coa

/*
 * Verification of a distributed Laplace equation solver on a
 * rectangular domain from an implementation in Coarray Fortran.
 *
 * From the paper:
 *   Martin, J. M. (2018). Testing and Verifying Parallel Programs
 *   Using Data Refinement. In Communicating Process Architectures
 *   2018 (pp. 491-500). IOS Press.
 *
 * Authors: -----
 * Built on previous work by: --------
 *
 * Date: Mar 1 2022
 * Alloy Analyzer 6.1.0
 */

-- constrain alpha through invariants

fact invariants {
  all m: Matrix| Inv[m]
  all c: Coarray | Inv[c]
 }


-- keep sizes small and avoid empty matrix
fact sizes {
  all m: Matrix| m.cols <= 10 and m.rows <= 4 
  all m: Matrix| m.cols > 0 and m.rows > 0
}

--- small coarrays for avoiding integer overflow issues
-- at this scope, only 4 Int is needed

pred small[c:Coarray] {
	all m:c.mseq.elems | m.cols <= 3
	#c.mseq <= 5
}

-- at this scope, 5 Int is needed
-- checks take much longer ( ~20 seconds -> ~10 minutes)

pred small2[c:Coarray] {
	all m:c.mseq.elems | m.cols <= 5
	#c.mseq <= 4
}

-------------------------------------------------
------------------- is alpha[con,abs] functional?

check isFunctional { all a1,a2: Matrix, c:Coarray | 
						(alpha[c,a1] and alpha [c,a2] and small[c]) =>
						equivalent[a1,a2] } for
	 1 Coarray, 7 Matrix, 10 Value, 0 P, 5 seq, 0 P2, 4 Int

-- only passes if we restrict the coarray size otherwise overflow issues
-- with small and 4 Int : ~ 22 seconds on m1 mac mini w/ minisat

check isFunctional2 { all a1,a2: Matrix, c:Coarray | 
						(alpha[c,a1] and alpha [c,a2] and small2[c]) =>
						equivalent[a1,a2] } for
	 1 Coarray, 7 Matrix, 10 Value, 0 P, 5 seq, 0 P2, 5 Int

-- with small2 and 5 Int : ~ 9 minutes on m1 mac mini w/ minisat

run isFunctional3 { some a1,a2:Matrix, c:Coarray |
					alpha[c,a1] and alpha[c,a2] and not equivalent[a1,a2]} for
	1 Coarray, 7 Matrix, 10 Value, 0 P, 5 seq, 0 P2

-- This passes even without restricting the coarray ( no instances)
-- 4 Int : ~ 23 seconds on m1 mac mini w/ minisat

-------------------------------------------------
------------------- is alpha[con] -> abs total? 
--------------------aka Correspondence from De Roever

sig P {
	con:Coarray,
	abs: lone Matrix }

check Correspondence { all p:P | alpha[p.con,p.abs] => some p.abs} for
	1 Coarray, 7 Matrix, 10 Value, 1 P, 5 seq, 0 P2, 4 Int
-- 4 Int :passes ~ 20 seconds on m1 mac mini w/ minisat
-- 5 Int :passes ~ 8 minutes on m1 mac mini w/ minisat

-------------------------------------------------
------------------- is alpha[abs] -> con total? 
--------------------aka Adequacy from De Roever

sig P2 {
	con: lone Coarray,
	abs: Matrix }{
    some con => #con.mseq>1} // not interested in base case w/ 1 Image

check adequacy { all p:P2 | alpha[p.con,p.abs] =>  some p.con} for
	3 Coarray, 7 Matrix, 10 Value, 5 seq, 1 P2, 0 P, 4 Int

-- 4 Int: passes ~ 54 seconds on m1 mac mini w/ minisat


-------------------------------------------------
------------------- correctness

check correctness { all c,c":Coarray, a,a":Matrix |
					(alpha[c,a] and alpha[c",a"] and JacobiStep[c,c"] 
					and small[c] and small[c"]) => JacobiStep[a,a"] } for
	2 Coarray, 14 Matrix, 4 Value, 0 P, 0 P2, 4 Int

-- Only passes if we restrict the Coarray size otherwise overflow issues
-- With 4 Int: ~ 40 seconds on m1 mac mini w/ minisat


check correctness2 { all c,c":Coarray, a,a":Matrix |
					(alpha[c,a] and alpha[c",a"] and JacobiStep[c,c"] 
					and small2[c] and small2[c"]) => JacobiStep[a,a"] } for
	2 Coarray, 14 Matrix, 4 Value, 0 P, 0 P2, 5 Int

-- With 5 Int: passes ~ 45 minutes on m1 mac min w/ minisat

run correctness3 { some c,c":Coarray, a,a":Matrix |
					alpha[c,a] and alpha[c",a"] and JacobiStep[c,c"]
 					and not JacobiStep[a,a"] 
} for 2 Coarray, 14 Matrix, 4 Value, 0 P, 0 P2

-- This passes even without restricting the coarray ( no instances)
-- 4 Int : ~ 40 seconds on m1 mac mini w/ minisat


/*
default scope is 3 (except for integers, which have a default bitwidth of 4)

max integer for "n Int" = 2^(n-1) - 1

  n   max   min
 --   ---   ---
 10   511  -512
  9   255  -256
  8   127  -128
  7    63   -64
  6    31   -32
  5    15   -16
  4     7    -8   <- default
  3     3    -4
  2     1    -2
*/




																		
