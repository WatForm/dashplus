package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusStringLiteral extends TLAPlusAtom{

	public TLAPlusStringLiteral(String s)
	{
		super(TLAPlusStrings.STRING_START+s+TLAPlusStrings.STRING_END);
	}
	
}
