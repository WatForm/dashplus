package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusOr extends TLAPlusBinOpMid{

	public TLAPlusOr(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo)
	{
		super(TLAPlusStrings.OR, operandOne, operandTwo);
	}
}
