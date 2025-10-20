package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusLesserEquals extends TLAPlusBinOpMid{

	public TLAPlusLesserEquals(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo)
	{
		super(TLAPlusStrings.LESSER_THAN_EQUALS, operandOne, operandTwo);
	}
}
