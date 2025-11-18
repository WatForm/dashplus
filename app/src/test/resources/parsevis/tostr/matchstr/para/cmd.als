run predA

check predA 

run predA => run predB => run predC

run cmdDeclName predA 

run cmdDeclName { someExpr } 

run cmdDeclName predA for 1

run cmdDeclName predA for 1 .. someQname

run cmdDeclName predA for 1 .. 9 someQname

run cmdDeclName predA for 1 .. 9 : 2 someQname

run cmdDeclName predA for 1 .. 9 : 2 someQname, 1 .. 9 : 2 someQname

run cmdDeclName predA for exactly 1 .. 9 : 2 someQname, exactly 1 .. 9 : 2 someQname

run cmdDeclName predA for exactly 1 .. 9 : 2 String

run cmdDeclName predA for exactly 1 .. 9 : 1 steps

