module coa -- concrete view
open mat

/*
 * Verification of a distributed Laplace equation solver on a
 * rectangular domain from an implementation in Coarray Fortran.
 *
 * From the paper:
 *   Martin, J. M. (2018). Testing and Verifying Parallel Programs
 *   Using Data Refinement. In Communicating Process Architectures
 *   2018 (pp. 491-500). IOS Press.
 *
 * Authors: -------
 * Built on previous work by:--------
 *
 * Date: Mar 1 2022
 * Alloy Analyzer 6.1.0
 */

-- a coarray is a sequence of images (in our case matrices)
-- Concrete data type
sig Coarray {
  mseq: seq Matrix
}

-- concrete data invariant, enforces shape and overlap
-- two columns on either side of an interface must be the same.  These
-- overlapping strips allow each step to be performed in phases: a
-- computation phase and then a communication phase.
pred Inv [c: Coarray] {
  all m1, m2: c.mseq.elems | sameShape[m1,m2]
  all i: allRows[c], j: allCols[c], q, p: c.mseq.inds |
    let Lc = lastCol[c] {
      -- if q is left of p, last column of q is the second column of p
      q = minus[p, 1] and j = Lc =>
        c.mseq[q].vals[i, j] = c.mseq[p].vals[i, 1]
      -- if q is right of p, first column of q is next to last column of p
      q = plus[p, 1] and j = 0 =>
        c.mseq[q].vals[i, j] = c.mseq[p].vals[i, minus[Lc, 1]]
    } 
}

-- abstraction relation (alpha)
pred alpha [c: Coarray, m: Matrix] {
  totRows[c, m.rows]
  totCols[c, m.cols]
  all i: range[m.rows], j: range[m.cols] {
    -- 1st column of m is the 1st column of the 1st image of c
    j = 0 => m.vals[i, j] = c.mseq[0].vals[i, 0]

    -- last column of m is the last column of the last image of c
    j = lastCol[m] =>
      let mi = sub[#c.mseq, 1],      -- matrix index
          ci = lastCol[c.mseq[mi]] | -- column index
        m.vals[i, j] = c.mseq[mi].vals[i, ci]

    -- mapping of middle image columns
    j != 0 and j != lastCol[m] =>
      let mi = div[sub[j, 1], sub[c.mseq[0].cols, 2]],
          ci = add[1, rem[sub[j, 1], sub[c.mseq[0].cols, 2]]] |
        m.vals[i, j] = c.mseq[mi].vals[i, ci] }
}

-- number of rows (same as for each image)
pred totRows [c: Coarray, r: Int] {
  //some c
  let i = #c.mseq |
    r = (i > 0 => c.mseq[0].rows else 0)
}

-- number of columns resulting from the combination of coarray images
pred totCols [c: Coarray, m: Int] {
  //some c
  let i = #c.mseq |
    rel[m, i > 0 => c.mseq[0].cols else 0, i]
}

// rel: relationship between the number of matrix and coarray columns and
//      the number of images
// m: number of columns in the abstract matrix
// c: number of columns in each coarray matrix
// i: number of images

pred rel [m, c, i: Int] {
  m >= 0 and c >= 0 and i >= 0
  m = 0 => c = 0 and i = 0
  m > 0 and m < 4 => c = m and i = 1
  m >= 4 => m = add[mul[i, sub[c, 2]], 2] // rearranged to minimize int size
}

-- transition predicate for one iteration of parallel Jacobi method
-- (predicate (2) from the Martin paper)
-- Concrete Operation (op_c)

pred JacobiStep [u, v: Coarray] {
  sameShape[u, v]
  all i:allRows[u], j:allCols[u], q, p: u.mseq.inds |
    let Lc = lastCol[u], Lr = lastRow[u] {
     -- Constituent matrices must be valid
      Inv[v.mseq[q]]
      -- start with simple matrices for now
      simpleMatrix[u.mseq[q]]

      -- COMPUTATION PHASE
      -- internal region: set non-boundary elements in v to neighbors in u
      -- top and bottom rows (except first and last cols):
      --   copy into v the elements from u
      q = p and j != 0 and j != Lc =>
       	 (i != 0 and i != Lr =>
	          v.mseq[q].vals[i, j] = neighbors[u.mseq[q], i, j]
	            else v.mseq[q].vals[i, j] = u.mseq[q].vals[i, j])
      -- first column of first image: copy into v the elements from u
      v.mseq[0].vals[i, 0] = u.mseq[0].vals[i, 0]
      -- last column of last image: copy into v the elements from u
      q = u.mseq.lastIdx => v.mseq[q].vals[i, Lc] = u.mseq[q].vals[i, Lc]

      -- COMMUNICATION PHASE
      -- if q is left of p, set last column of q to the second column of p
      q = minus[p, 1] and j = Lc =>
        v.mseq[q].vals[i, j] = v.mseq[p].vals[i, 1]
      -- if q is right of p, set first column of q to next to last column of p
      q = plus[p, 1] and j = 0 =>
        v.mseq[q].vals[i, j] = v.mseq[p].vals[i, minus[Lc, 1]]
    }
}

-- to iterate through cols of a CoArray image
fun allCols[c:Coarray]: set Int {
  range[c.mseq[0].cols]
}

-- to iterate through rows of a CoArray image
fun allRows[c:Coarray]: set Int {
  range[c.mseq[0].rows]
}

-- if there are multiple coarrays, they must have the same number of images
fact numImages {
  all c1, c2: Coarray | #c1.mseq = #c2.mseq
}

-- index of last column of a coarray image
fun lastCol [c: Coarray]: one Int {
  { i: Int | i = minus[c.mseq[0].cols, 1] }
}

-- index of last row of a coarray image
fun lastRow [c:Coarray]: one Int {
  { i: Int | i = minus[c.mseq[0].rows, 1] }
}

-- if two coarrays are equivalent
pred equivalent [u, v: Coarray] {
  sameShape[u, v]
  all i: u.mseq.inds | equivalent[u.mseq[i], v.mseq[i]]
}

-- if two coarrays are the same shape
pred sameShape [u, v: Coarray] {
  all i: range[#u.mseq] |
    u.mseq[i].rows = v.mseq[i].rows and
	 u.mseq[i].cols = v.mseq[i].cols
}

/** Show Instances **/

-- only relevant CoArray / Matrix combos
pred relevant [u: Coarray, U: Matrix] {
  #u.mseq >= 2  and U.cols >= 3 and U.rows >= 3
}

-- only relevant CoArrays 
pred relevant [u: Coarray] {
  #u.mseq >= 2 
  u.mseq[0].rows >= 3
  u.mseq[0].cols >= 3
}

pred show {
  all m: Matrix| Inv[m]
  all c: Coarray | Inv[c]
  some U, V: Matrix, u, v: Coarray |
    relevant[v, V] and Inv[u] and alpha[u,U] and alpha[v,V] and
      simpleMatrix[U] and JacobiStep[u, v] and JacobiStep[U, V]
}

run show for 2 Coarray, 7 Matrix, 4 Value

/** Assertions **/

-- if u satisfies the invariant and u->js->v 
-- then v must satisfy the invariant
assert preservesInv {
  all u, v: Coarray |
    relevant[u] and -- <- need to winnow down and eventually remove
      Inv[u] and JacobiStep[u, v] => Inv[v]
}

check preservesInv for 2 Coarray, 13 Matrix, 4 Int, 10 Value
-- passes ~8.5 minutes on m1 mac mini w/ minisat
-- can reduce time significantly by decreasing matrix/value scope

-- if u->js-> v and u->js->w
-- then v and w should be the same
assert JacobiDeterministic2 {
  all u, v, w: Coarray |
    relevant[u] and -- <- need to winnow down and eventually remove
      Inv[u] and JacobiStep[u, v] and JacobiStep[u, w] => equivalent[v, w]
}

check JacobiDeterministic2 for 3 Coarray, 15 Matrix, 4 Int, 10 Value
-- passes ~ 12 minutes on m1 mac mini w/ minisat
-- can reduce time significantly by decreasing matrix/value scope

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

