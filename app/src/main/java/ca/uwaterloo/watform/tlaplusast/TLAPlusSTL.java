package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TLAPlusSTL extends TLAPlusASTNode { // not an expression, cannot be embedded inside an expression

	public static enum LIBRARIES
	{
		STL_FiniteSets,
		STL_Naturals,
		STL_Integers,
		STL_Sequences
	};
	private LIBRARIES library;
	public TLAPlusSTL(LIBRARIES library)
	{
		this.library = library;
	}

	@Override
	public List<String> toStringList()
	{
		String s = "Unknown";
		if(this.library == LIBRARIES.STL_FiniteSets)s = TLAPlusStrings.FINITE_SETS;
		else if(this.library == LIBRARIES.STL_Naturals)s = TLAPlusStrings.NATURALS;
		else if(this.library == LIBRARIES.STL_Integers)s = TLAPlusStrings.INTEGERS;
		else if(this.library == LIBRARIES.STL_Sequences)s = TLAPlusStrings.SEQUENCES;
		
		List<String> t = new ArrayList<String>();
		t.add(s);
		return t;
	}
	
}
