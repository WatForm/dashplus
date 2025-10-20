package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusGreater extends TLAPlusBinOpMid{

	public TLAPlusGreater(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo)
	{
		super(TLAPlusStrings.GREATER_THAN, operandOne, operandTwo);
	}
}
