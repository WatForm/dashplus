package ca.uwaterloo.watform.tlaplusast;

import java.util.List;

public class TLAPlusSet extends TLAPlusNaryOp {

	public TLAPlusSet(List<TLAPlusASTNode> children) 
	{
		super(
			TLAPlusStrings.SET_START, 
			TLAPlusStrings.SET_END, 
			TLAPlusStrings.COMMA, 
		children);
	}
	
}
