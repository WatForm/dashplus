open util/boolean

fact {
	all a, b, c, d, e : Bool |
		(a.isTrue=>b.isTrue else c.isTrue => d.isTrue else e.isTrue) <=> (a.isTrue=>b.isTrue else (c.isTrue => d.isTrue else e.isTrue))
	! all a, b, c, d, e : Bool |
		(a.isTrue=>b.isTrue else c.isTrue => d.isTrue else e.isTrue) <=> ((a.isTrue=>b.isTrue else c.isTrue) => d.isTrue else e.isTrue)

	// mul, div, rem have same precedence
	// mul, div, rem have greater precedence than add, sub
	2 fun/rem 2 fun/mul 3 = {0}
	2 fun/rem (2 fun/mul 3) = {2}
	2 fun/mul 2 fun/rem 3 = {1}
	2 fun/mul (2 fun/rem 3) = {4}
	2 fun/div 2 fun/mul 3 = {3}
	2 fun/mul 2 fun/div 3 = {1}

	// intersection has greater precedence than 
	({1} + {2}) & {2} fun/add 1 = {3}
	1 fun/add ({1} + {2}) & {2} = {3}
	(({1} + {2}) & {2}) fun/add 1 = {3}
	({1} + {2}) & ({2} fun/add 1) = none
	
	
	
	{1} + {2} fun/add {3} = {6}
	{1} fun/add {3} + {2}= {4}+{2}

	{1} + {2} fun/sub {3} = {0}
	{1} fun/sub {3} + {2}= {-2}+{2}

	// 
	{1} + {2} fun/mul {3} = {1} + {6}
	{1} fun/mul {3} - {3}= none

	
	
}

run {} for 6 int, 6 seq
