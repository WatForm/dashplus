package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusLesser extends TLAPlusBinOpMid{

	public TLAPlusLesser(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo)
	{
		super(TLAPlusStrings.LESSER_THAN, operandOne, operandTwo);
	}
}
