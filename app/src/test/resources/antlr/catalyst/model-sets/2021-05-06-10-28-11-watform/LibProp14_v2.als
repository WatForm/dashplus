/* LibProp14_v2.als
 * Authors: Frappier et al.
 * Date: 2010
 *
 * Note: this model was extracted
 * from~\cite{DBLP:conf/icfem/FrappierFCCO10} by Frappier et al.
 *
 * To cite this model, you can use:
 *
  @InProceedings{DBLP:conf/icfem/FrappierFCCO10,
    author    = {Marc Frappier and Beno{\^{\i}}t Fraikin and Romain
                    Chossart and Rapha{\"{e}}l Chane{-}Yack{-}Fa and
                    Mohammed Ouenzar},
    title     = {Comparison of Model Checking Tools for Information
                    Systems},
    year      = 2010,
    booktitle = {Formal Methods and Software Engineering - 12th
                    International Conference on Formal Engineering
                    Methods, {ICFEM} 2010, Shanghai, China, November
                    17-19, 2010. Proceedings},
    pages     = {581-596},
    doi       = {10.1007/978-3-642-16901-4\_38},
    url       = {https://doi.org/10.1007/978-3-642-16901-4\_38},
    crossref  = {DBLP:conf/icfem/2010},
    timestamp = {Tue, 14 May 2019 10:00:50 +0200},
    biburl    = {https://dblp.org/rec/bib/conf/icfem/FrappierFCCO10},
    bibsource = {dblp computer science bibliography, https://dblp.org}
  } */

open LibSpecification
open util/ordering[Lib] as LibOrd
/*
Specification of property 14 using traces. A fact is used
to define the states from which the property must be
satisfied (ie, the trace does not start from the initial
state of the library.

*/


pred BuggyLeave[L,L':Lib]{L=L'}//Just For Test

pred LCR[m:Member,L,L' : Lib]
{
 some b:Book |
   Cancel[m,b,L,L']
   or Return[m,b,L,L']
 //For test switch Leave and BuggyLeave
   or Leave[m,L,L']
 //or BuggyLeave[L,L']
}

pred TransLCR[m:Member]
{
 all l : Lib-LibOrd/last |
   LCR[m,l,l.LibOrd/next]
}

pred OrCanLCR[L:Lib,m:Member]
{
 CanLeave[m,L]
 or some b:Book|CanCancel[m,b,L]
 or some b :Book|CanReturn[m,b,L]
}

-------------- Property 14 as a run --------------------------
pred NegProp14 {

Prop14StartLib[LibOrd/first] // start from a valid library state

  some m : LibOrd/first.members |

               TransLCR[m]
           and
             m in LibOrd/last.members
           and
             (
           not OrCanLCR[LibOrd/last,m] // at max : LCR can't be applyed
             )
}

// Property 14 is checked by executing a run using a fact which negates property 14
// m members, b books => at most l loans and b-l membersReservingOneBook
// hence, need l returns + (b-l) cancels + 1 leave =>
// b+1 actions =>
// trace of length b+2
run NegProp14 for 10 but 8 Book, 8 Member
run NegProp14 for 8 but 6 Book, 6 Member
run BuggyLeave for 5
run {} for 3 but 2 Lib

-------------------- Property 14 using a check --------------------------
/*
This states property 14 as an assert and uses a check to verify it.
*/
assert Prop14 {

  all m : LibOrd/first.members |
    (
    Prop14StartLib[LibOrd/first]
  and
    TransLCR[m]
   )
  =>
  (
     (not m in LibOrd/last.members)
   or
     ( // case when the sequence is incomplete
        OrCanLCR[LibOrd/last,m]
     )
  )
}

//check {not NegProp14}for 8 but 6 Book, 6 Member
//check Prop14 for 8 but 6 Book, 6 Member
check Prop14 for 10 but 8 Book, 8 Member

-------------Define what are the valid first library state of a trace -------------
pred Prop14StartLib[L:Lib]
{

 all m: Member| (#((L.loan).m)<=Constants.maxNbLoans)

-- The book can not be reserved if it's not loaned or reserved
 all b : Book|
    some (L.membersReservingOneBook.b)
    =>
    ((b in L.loan.Member) or ((#L.membersReservingOneBook.b) > 0))

-- the Member can not reserve and lend the same book
 all m : Member,b : Book|
    (m in b.(L.loan) => not (m in Int.(L.membersReservingOneBook.b)))

--the same thing
 all m : Member,b : Book|
    (m in Int.(L.membersReservingOneBook.b)) => not (m in b.(L.loan))

-- the member can not Renew a book and he has not lend this book
 all b : Book,m : Member|
    (b in L.Renew.m) =>b in (L.loan.m)//7

-- books -> lone members
 all b : Book|
    ((b in L.books) and (b in L.loan.Member)) => one (b ->Member & (L.loan))
 all b : Book|
    ((b in L.books) and (b in L.Renew.Member)) => one (b ->Member & (L.Renew))
//it defines the valid sequences of reservations for a book.
 all m:Member,b:Book |
  (m in Int.(Lib.membersReservingOneBook.b))
=>
 one (Int->m & (Lib.membersReservingOneBook.b))

}
