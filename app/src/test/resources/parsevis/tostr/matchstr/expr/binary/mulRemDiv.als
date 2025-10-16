fact {
	a fun/mul b
	a fun/rem b
	a fun/div b
}

fact {
	a fun/mul let c=b | c
	a fun/rem let c=b | c
	a fun/div let c=b | c
}
