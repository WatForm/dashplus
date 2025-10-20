package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusAdd extends TLAPlusBinOpMid{

	public TLAPlusAdd(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo)
	{
		super(TLAPlusStrings.PLUS, operandOne, operandTwo);
	}
}
