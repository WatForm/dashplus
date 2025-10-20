package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusAnd extends TLAPlusBinOpMid{

	public TLAPlusAnd(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo)
	{
		super(TLAPlusStrings.AND, operandOne, operandTwo);
	}
}
