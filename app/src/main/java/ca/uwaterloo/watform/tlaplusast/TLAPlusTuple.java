package ca.uwaterloo.watform.tlaplusast;

import java.util.List;

public class TLAPlusTuple extends TLAPlusNaryOp {

	public TLAPlusTuple(List<TLAPlusASTNode> children) 
	{
		super(
			TLAPlusStrings.TUPLE_OPEN, 
			TLAPlusStrings.TUPLE_CLOSE, 
			TLAPlusStrings.COMMA, 
		children);
	}
	
}
