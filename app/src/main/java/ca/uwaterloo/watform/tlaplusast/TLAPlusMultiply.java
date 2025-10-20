package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusMultiply extends TLAPlusBinOpMid{

	public TLAPlusMultiply(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo)
	{
		super(TLAPlusStrings.TIMES, operandOne, operandTwo);
	}
}
