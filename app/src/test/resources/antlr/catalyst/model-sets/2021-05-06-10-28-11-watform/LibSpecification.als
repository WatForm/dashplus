/* LibSpecification.als
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

module LibSpecification

------------------------------
-------Declaration------------
------------------------------
one sig Constants
{
 maxNbLoans : Int
}
{
 maxNbLoans = 7
}
sig Book{}
sig Member{}

sig Lib
{
 members:set Member,
 books: set Book ,
 loan: (books -> members),
 membersReservingOneBook: (seq members)->books,
 Renew: (books -> members)
}

/* =================================
   = List of no change predicates =
   = They are used in action to describe which state =
   = variables remain unchanged =
===================================*/

pred NoChangebooks[L,L':Lib]
{
 L.books =L'.books
}

pred NoChangemembers[L,L':Lib]
{
 L.members =L'.members
}

pred NoChangeloan[L,L':Lib]
{
 L.loan=L'.loan
}

pred NoChangeSeqBook[L,L':Lib]
{
 L.membersReservingOneBook= L'.membersReservingOneBook
}

pred NochangeRenew[L,L':Lib]
{
 L.Renew = L'.Renew
}

/*----------------
   Initialisation
------------------*/
pred Init [L:Lib]
{
 no L.books
 no L.members
 no L.loan
 no L.membersReservingOneBook
 no L.Renew
}

/*--------- ----------
   Acquire
-------------------*/
pred CanBeAcquire[L:Lib,b:Book]
{
 no(b & L.books) // verify that b is not in the Library
}

pred Acquire[b:Book,L,L':Lib]
{
 ----Preconditon-------
 CanBeAcquire[L,b]

 -----Postcondition-------
 L'.books = L.books + b // add the b in the set of books

 ----NoChanges-----
 NoChangemembers[L,L']
 NoChangeloan[L,L']
 NoChangeSeqBook[L,L']
 NochangeRenew[L,L']
}

/*--------- ----------
   Join
-------------------*/
pred CanJoin[m:Member,L:Lib]
{
 no (m & L.members)// m does not exist in the Library.
}

pred Join[m:Member,L,L':Lib]{
 ----Precondition-----
 CanJoin[m,L]

 -----Postcondition------
 L'.members=L.members +m// add the m in the set of members

 ------Nochanges-----
 NoChangebooks[L,L']
 NoChangeloan[L,L']
 NoChangeSeqBook[L,L']
 NochangeRenew[L,L']
}

/*--------- ----------
   LEND
-------------------*/
pred CanLend[m:Member,b:Book,L:Lib]
{
 (b in L.books) and (m in L.members) // b and m are in the Library
 (#((L.loan).m)<Constants.maxNbLoans) //maxNbLoans is the number maximum of loans authorized
 all m':Member|no((L.loan).m' & b)// b is not lent
 (no (L.membersReservingOneBook.b))// b not reserved
}

pred Lend[m:Member,b:Book,L,L':Lib]
{
 -----Precondition------------
 CanLend[m,b,L]

 ----Postcondition-------------
 L'.loan=L.loan + (b->m)

 ----Nochanges------------
 NoChangebooks[L,L']
 NoChangemembers[L,L']
 NoChangeSeqBook[L,L']
 NochangeRenew[L,L']
}

/*--------- ----------
   RESERVE
-------------------*/
pred CanReserve[m:Member,b:Book,L:Lib]
{
 (b in L.books and m in L.members ) // b and m are in the Library
 one (b & ((L.loan).Member)) or (some (L.membersReservingOneBook.b))// the book is a borrowed
  no (m & b.(L.loan)) // m is not lent
 no (Int.(L.membersReservingOneBook.b) & m) //it can't be reserved more than one Time by the same member
}

pred Reserve[m:Member,b:Book,L,L':Lib]
{
 ---- Precondition----
 CanReserve[m,b,L]

 ------PostCondition-----
 L'.membersReservingOneBook.b = L.membersReservingOneBook.b.add[m]

 -----Nochanges-------
 all b':Book - b|L'.membersReservingOneBook.b' = L.membersReservingOneBook.b'
 NoChangebooks[L,L']
 NoChangemembers[L,L']
 NoChangeloan[L,L']
 NochangeRenew[L,L']
}

/*--------- ----------
   CANCEL
-------------------*/
pred CanCancel[m:Member,b:Book,L:Lib]
{
 (b in L.books and m in L.members ) // // b and m are in the Library
  one (Int->m & (L.membersReservingOneBook.b))// b is reserved by m
}

pred Cancel[m:Member,b:Book,L,L':Lib]
{
 --------Preconditon---------------
 CanCancel[m,b,L]

 --------Postconditon------------
 L'.membersReservingOneBook.b=L.membersReservingOneBook.b.delete[
     L.membersReservingOneBook.b.indsOf[m]]// delete m from the list of reservation of b

 ------Nochanges--------
 all b':Book - b|L'.membersReservingOneBook.b' = L.membersReservingOneBook.b'
 NoChangebooks[L,L']
 NoChangemembers[L,L']
 NoChangeloan[L,L']
 NochangeRenew[L,L']
}

/*--------- ----------
   RETURN
-------------------*/
pred CanReturn[m:Member,b:Book,L:Lib]
{
 (b in L.books and m in L.members )
 one ((L.loan).m & b) // b is already lent to m
}

pred Return[m:Member,b:Book,L,L':Lib]
{
 ----Precondition-----
 CanReturn[m,b,L]

 ----PostConditon--------
 L'.loan=L.loan - (b ->m) // delete the b->m from the set of loans
 L'.Renew = L.Renew - (b -> m)// same thing

 ----Nochanges--------
 NoChangebooks[L,L']
 NoChangemembers[L,L']
 NoChangeSeqBook[L,L']


}

/*--------------------
  TAKE
----------------------*/
pred CanTake[m:Member,b:Book,L:Lib]
{
 (b in Lib.books) and (m in L.members)// b and m are in the Library
 (#((L.loan).m)<Constants.maxNbLoans) //maxNbLoans is the number maximum of lend authorized
 (L.membersReservingOneBook.b) = (0 -> m) // m is first in the list of reservation
 no (b.(L.loan)) // the book is not lent
}

pred Take[m:Member,b:Book,L,L':Lib]
{
 -----Preconditon-------
 CanTake[m,b,L]

 -----PostCondition-----
 L'.loan=L.loan + (b->m)
 L'.membersReservingOneBook.b=L'.membersReservingOneBook.b.delete[0]// delete m from the list of reservations of b

 -----Nochanges-------
 all b':Book - b|L'.membersReservingOneBook.b' = L.membersReservingOneBook.b'
 NoChangebooks[L,L']
 NoChangemembers[L,L']
 NochangeRenew[L,L']
}

/*-----------------
   LEAVE
-------------------*/
pred CanLeave[m:Member,L:Lib]
{
 m in L.members
 no (L.loan.m) // m is not in the lent list
 no( Int.(L.membersReservingOneBook.Book) & m)// m has no reseravation
}

pred Leave[m:Member,L,L':Lib]
{
 ------Preconditon-------
 CanLeave[m,L]

 ------Postconditon------
 L'.members = L.members - m

 ----Nochanges---------
 NoChangeloan[L,L']
 NochangeRenew[L,L']
 NoChangeSeqBook[L,L']
   NoChangebooks[L,L']
}

/*-----------------
   DISCARD
-------------------*/
pred CanDiscard[b:Book,L:Lib]
{
 b in L.books
 no (b.(L.loan))
 no ((L.membersReservingOneBook.b) )
}

pred Discard[b:Book,L,L':Lib]
{
 ------Precondition-------
 CanDiscard[b,L]

 ------Postconditon--------
 L'.books = L.books - b

 -----Nochanges-------
 NoChangeloan[L,L']
 NoChangeSeqBook[L,L']
   NoChangemembers[L,L']
 NochangeRenew[L,L']
}

/*--------------------
   RENEW
----------------------*/
pred CanRenew[m:Member,b:Book,L:Lib]
{
 one (b.(L.loan) & m) // b is already borrowed by m
 L.membersReservingOneBook.b.isEmpty //b has no reservation
}

pred Renew[m:Member,b:Book,L,L':Lib]
{
 ------Preconditon-------
 CanRenew[m,b,L]

 -----Postcondition--------
 L'.Renew=L.Renew ++ (b->m) // override the old b->m

 ------Nochanges-----
 NoChangebooks[L,L']
 NoChangemembers[L,L']
 NoChangeloan[L,L']
 NoChangeSeqBook[L,L']
}
