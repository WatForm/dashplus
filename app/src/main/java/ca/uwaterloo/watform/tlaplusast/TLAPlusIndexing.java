package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TLAPlusIndexing extends TLAPlusBinOp {

	public TLAPlusIndexing(TLAPlusASTNode operandOne, TLAPlusASTNode operandTwo)
	{
		super(operandOne,operandTwo);
	}

	@Override
	public List<String> toStringList() 
	{
		List<String> result = new ArrayList<>();
		result.addAll(this.getOperandOne().toStringList());
		result.add(TLAPlusStrings.SQUARE_BRACKET_OPEN);
		result.addAll(this.getOperandTwo().toStringList());
		result.add(TLAPlusStrings.SQUARE_BRACKET_CLOSE);
		return result;
	}
	
}
