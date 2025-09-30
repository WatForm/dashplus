/* LibProperties.als
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

/*
Here we can find all the proprieties that we have arrived to expresse Expect
14 and 15 which are in the second File
*/

open LibSpecification

--1. A book can always be acquired by the library when it is not currently acquired.
--2. A book cannot be acquired by the library if it is already acquired.
    assert Prop1And2
{

 all b:Book,L:Lib
 {
  CanBeAcquire[L,b] <=> not (b in L.books) // 1 and 2
 }
}
check Prop1And2 for 2 Lib,8 Member,8 Book
//It has been impossible to express the prop1 in this form!!!!!!

/*assert Prop1And2
{

 all b:Book,L:Lib
 {

 some L':Lib| Acquire[b,L,L'] => not (b in L.books)
 not (b in L.books) =>some L':Lib| Acquire[b,L,L'] Can not expressed because
    we have to generate the transition graph
 }
}
check Prop1And2 for 2 Lib,8 Member,8 Book
*/

--An acquired book can be discarded only if it is neither lent nor reserved.
assert Prop3
{
 all b:Book,L:Lib
 {
    some L':Lib |Discard[b,L,L'] => no (b.(L.loan)) and no ((L.
       membersReservingOneBook.b) )
 }
}
check Prop3 for 2 Lib,8 Member,8 Book

--A person must be a member of the library in order to borrow(lend or take) a book.
assert Prop4
{
all b:Book,m:Member,L:Lib
 {
  some L':Lib|Lend[m,b,L,L'] => (b in L.books) and (m in L.members)// b and m in the Library
  some L':Lib|Take[m,b,L,L'] => (b in L.books) and (m in L.members)
 }
}
check Prop4 for 2 Lib,8 Member,8 Book

--A book can be reserved only if it has been borrowed or already reserved by another member
assert Prop5
{
 all b:Book,m:Member,L:Lib
 {
  some L':Lib|Reserve[m,b,L,L'] => (b in ((L.loan).Member)) or (some (L.
      membersReservingOneBook.b))
 }
}
check Prop5 for 2 Lib,8 Member,8 Book

--A book cannot be reserved by the member who is borrowing it.
assert Prop6
{
 all b:Book,m:Member,L:Lib
 {
  some L':Lib|Reserve[m,b,L,L'] => ((m !in b.(L.loan)))
 }
}
check Prop6 for 2 Lib,8 Member,8 Book

--A book cannot be reserved by a member who is reserving it.
assert Prop7
{
 all b:Book,m:Member,L:Lib
 {
 some L':Lib|Reserve[m,b,L,L'] => ( (m !in Int.(L.membersReservingOneBook.b))
     )
 }
}
check Prop7 for 2 Lib,8 Member,8 Book

--A book cannot be lent to a member if it is reserved
assert Prop8
{
 all b:Book,m:Member,L:Lib
 {
  some L':Lib|Lend[m,b,L,L'] => (no (L.membersReservingOneBook.b))
 }
}

check Prop8 for 2 Lib,8 Member,8 Book

--A member cannot renew a loan if the book is reserved
assert Prop9
{
 all b:Book,m:Member,L:Lib
 {
  some L':Lib|Renew[m,b,L,L'] => (no (L.membersReservingOneBook.b))
 }
}

check Prop9 for 2 Lib,8 Member,8 Book

--A member is allowed to take a reserved book only if he owns the oldest reservation
assert Prop10
{
all b:Book,m:Member,L:Lib
 {
  some L':Lib|Take[m,b,L,L'] => (L.membersReservingOneBook.b) = (0 -> m)
 }
}
check Prop10 for 2 Lib,8 Member,8 Book

--A book can be taken only if it is not borrowed
assert Prop11
{
 all b:Book,m:Member,L:Lib
 {
  some L':Lib|Take[m,b,L,L'] => not ( b in (L.loan).Member)
 }
}
check Prop11 for 2 Lib,8 Member,8 Book

--Anyone who has reserved a book can cancel the reservation at anytime before he takes it
assert Prop12
{
 all b:Book,L:Lib,m:Member
 {
  some L':Lib| Take[m,b,L,L'] => CanCancel[m,b,L']
 }
}
check Prop12 for 2 Lib,8 Member,8 Book

--A member can relinquish library membership only when all his loans have been returned and all his reservations have either been used or canceled
assert Prop13
{
 all m:Member,L:Lib
 {
  some L':Lib|Leave[m,L,L'] => (no (L.loan.m) and (m !in Int.(L.
      membersReservingOneBook.Book)))
 }
}
check Prop13 for 2 Lib,8 Member,8 Book

--A member cannot borrow more than the loan limit defined at the system level for all users
assert Prop15
{
 all L,L':Lib,m:Member,b:Book
 {
  Take[m,b,L,L'] => #(L'.loan.m)<=7
  Lend[m,b,L,L'] => #(L'.loan.m)<=7
 }
}

check Prop15 for 2 Lib, 8 Member, 8 Book
