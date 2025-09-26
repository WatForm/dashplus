private one sig Ord {
   First: set elem,
   Next: elem -> elem
} {
   pred/totalOrder[elem,First,Next]
}

sig A {}

one sig a, b, c extends A {}

pred showDisj {
    -- This expression represents all 3-tuples of disjoint atoms
    disj[a, b, c]
}

run showDisj for 3 A

