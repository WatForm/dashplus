package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusConcatSeq extends TLAPlusBinOpMid{

	public TLAPlusConcatSeq(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo)
	{
		super(TLAPlusStrings.CONCATENATE, operandOne, operandTwo);
	}
}
