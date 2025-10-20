package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TLAPlusNot extends TLAPlusUnaryOp 
{
	public TLAPlusNot(TLAPlusASTNode operand)
	{
		super(operand);
	}

	@Override
	public List<String> toStringList() 
	{
		List<String> result = new ArrayList<>();
		result.add(TLAPlusStrings.NOT);
		result.addAll(this.getOperand().toStringList());
		return result;
	}
	
}
