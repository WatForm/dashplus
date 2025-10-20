package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusSubsetEq extends TLAPlusBinOpMid{

	public TLAPlusSubsetEq(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo)
	{
		super(TLAPlusStrings.SET_SUBSET_EQ, operandOne, operandTwo);
	}
}
