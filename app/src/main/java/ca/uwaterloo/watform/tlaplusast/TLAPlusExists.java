package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusExists extends TLAPlusQuantifier
{
	public TLAPlusExists(TLAPlusVariable v, TLAPlusASTNode set, TLAPlusASTNode exp)
	{
		super(v,set,exp,TLAPlusStrings.EXISTS);
	}
}
