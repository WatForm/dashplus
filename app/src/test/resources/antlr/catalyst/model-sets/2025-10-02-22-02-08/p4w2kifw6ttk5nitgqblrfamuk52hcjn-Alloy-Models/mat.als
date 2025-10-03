module mat -- abstract view

/*
 * Verification of a distributed Laplace equation solver on a
 * rectangular domain from an implementation in Coarray Fortran.
 *
 * From the paper:
 *   Martin, J. M. (2018). Testing and Verifying Parallel Programs
 *   Using Data Refinement. In Communicating Process Architectures
 *   2018 (pp. 491-500). IOS Press.
 *
 * Authors: --------
 * Built on previous work by: --------
 *
 * Date: Mar 1 2022
 * Alloy Analyzer 6.1.0
 */

-- Original Matrix Structure from Dyer et al
sig Value {}
one sig Zero, One extends Value {}

-- Dense matrices of symbolic (atomic) values, zero and non-zeros
-- Abstract Data type (A)
sig Matrix {
  rows, cols: Int,
  vals: Int->Int->lone Value
}

pred Inv [m: Matrix] {
  m.rows >= 0
  m.cols >= 0
  m.rows = 0 <=> m.cols = 0                  
  m.vals.univ = range[m.rows]->range[m.cols]
}

-- enforce invariant as a fact as we are focusing on coarrays
fact repOk {
  all m: Matrix | Inv[m]
}

-- Adding expressions

-- Instead of building up and comparing general arithmetic
-- expressions, provide a way to record values of the four neighbors
-- in a matrix that are used to compute an average (the only type of
-- arithmetic computation being performed in a Jacobi iteration).

sig Neighbors extends Value {
  up, down, left, right: Value
}

fun neighbors[U: Matrix, i: Int, j: Int]: one Neighbors {
  { n: Neighbors |
      n.up = U.vals[plus[i, 1], j] and
      n.down = U.vals[minus[i, 1], j] and
      n.left = U.vals[i, minus[j, 1]] and
      n.right = U.vals[i, plus[j, 1]]
  }
}

-- A step in a Jacobi iteration
-- Abstract Operation (op_a)
pred JacobiStep [U, V: Matrix] {
  sameShape[U, V]
  V.vals.univ = range[V.rows]->range[V.cols] -- force neighbor objects to exist
  V.vals =
    { i: range[U.rows], j: range[U.cols], x: Value |
        let boundary = (j = 0 or j = minus[U.cols, 1] or
                          i = 0 or i = minus[U.rows, 1]) |
          x = (boundary => U.vals[i, j] else neighbors[U, i, j]) }
}

-- for keeping things simple
pred simpleMatrix [m: Matrix] {
  no i: range[m.rows], j: range[m.cols] | m.vals[i,j] in Neighbors
}

-- two matrices are equal
pred equivalent [U, V: Matrix] {
  U.rows = V.rows
  U.cols = V.cols
  U.vals = V.vals
}

-- two matrices are same dimensions
pred sameShape [U, V: Matrix] {
  U.rows = V.rows and U.cols = V.cols
}

-- index of last column of a matrix
fun lastCol [m: Matrix]: one Int {
  { i: Int | i = minus[m.cols, 1] }
}

-- index of last row of a matrix
fun lastRow [m: Matrix]: one Int {
  { i: Int | i = minus[m.rows, 1] }
}

-- the set [0, n-1]
fun range [n: Int]: set Int {
  { i: Int | 0 <= i and i < n }
}

/** Show Instances **/

pred relevant [m:Matrix] {
  m.rows >= 3 and m.cols >= 3
}

run show {
  some m,m": Matrix | relevant[m] and relevant[m"] and
	  simpleMatrix[m] and JacobiStep[m,m"]
} for 5 but 2 Matrix


/** Assertions **/

-- if U->JS->V and U->JS-> W then V and W should be the same matrix
assert JacobiDeterministic {
  all U, V, W: Matrix |
    JacobiStep[U, V] and JacobiStep[U, W]
      => equivalent[V, W]
}

check JacobiDeterministic for 4 but 3 Matrix, 10 Value

-- passes ~ 2 seconds on m1 Mac mini w/ minisat

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
