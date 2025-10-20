package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusForAll extends TLAPlusQuantifier
{
	public TLAPlusForAll(TLAPlusVariable v, TLAPlusASTNode set, TLAPlusASTNode exp)
	{
		super(v,set,exp,TLAPlusStrings.FOR_ALL);
	}
}
