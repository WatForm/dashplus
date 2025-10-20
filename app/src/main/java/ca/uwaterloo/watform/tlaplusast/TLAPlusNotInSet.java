package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusNotInSet extends TLAPlusBinOpMid{

	public TLAPlusNotInSet(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo)
	{
		super(TLAPlusStrings.SET_NOT_IN, operandOne, operandTwo);
	}
}
