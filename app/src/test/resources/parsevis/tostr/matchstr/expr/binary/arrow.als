fact {
	a -> lone b
	a -> one b
	a -> some b
	a -> set b
	a -> b
}

fact {
	a lone -> lone b
	a lone -> one b
	a lone -> some b
	a lone -> set b
	a lone -> b
}

fact {
	a one -> lone b
	a one -> one b
	a one -> some b
	a one -> set b
	a one -> b
}

fact {
	a some -> lone b
	a some -> one b
	a some -> some b
	a some -> set b
	a some -> b
}

fact {
	a set -> lone b
	a set -> one b
	a set -> some b
	a set -> set b
	a set -> b
}




fact {
	a -> lone let b=c | b
	a -> one let b=c | b
	a -> some let b=c | b
	a -> set let b=c | b
	a -> let b=c | b
}

fact {
	a lone -> lone let b=c | b
	a lone -> one let b=c | b
	a lone -> some let b=c | b
	a lone -> set let b=c | b
	a lone -> let b=c | b
}

fact {
	a one -> lone let b=c | b
	a one -> one let b=c | b
	a one -> some let b=c | b
	a one -> set let b=c | b
	a one -> let b=c | b
}

fact {
	a some -> lone let b=c | b
	a some -> one let b=c | b
	a some -> some let b=c | b
	a some -> set let b=c | b
	a some -> let b=c | b
}

fact {
	a set -> lone let b=c | b
	a set -> one let b=c | b
	a set -> some let b=c | b
	a set -> set let b=c | b
	a set -> let b=c | b
}

