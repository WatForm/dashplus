package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusRange extends TLAPlusBinOpMid{

	public TLAPlusRange(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo)
	{
		super(TLAPlusStrings.RANGE, operandOne, operandTwo);
	}
}
