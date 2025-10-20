package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TLAPlusUnchanged extends TLAPlusUnaryOp 
{
	public TLAPlusUnchanged(TLAPlusASTNode operand)
	{
		super(operand);
	}

	@Override
	public List<String> toStringList() 
	{
		List<String> result = new ArrayList<>();
		result.add(TLAPlusStrings.UNCHANGED);
		result.addAll(this.getOperand().toStringList());
		return result;
	}
	
}