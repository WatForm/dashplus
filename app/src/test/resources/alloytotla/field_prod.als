sig X {}
sig Y {}
sig Z {}
sig U {}
sig V {}
sig W {}

sig A {
	f : X
	g : Y -> Z
	p : f
	q : W -> f
	r : f -> W
	s : W -> g
	t : g -> W
	i : U -> f -> V
	j : U -> g -> V
	k : i -> j
}