package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusSTL extends TLAPlusExp {

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
	public String toString()
	{
		String s = "Unknown";
		if(this.library == LIBRARIES.STL_FiniteSets)s = TLAPlusStrings.FINITE_SETS;
		else if(this.library == LIBRARIES.STL_Naturals)s = TLAPlusStrings.NATURALS;
		else if(this.library == LIBRARIES.STL_Integers)s = TLAPlusStrings.INTEGERS;
		else if(this.library == LIBRARIES.STL_Sequences)s = TLAPlusStrings.SEQUENCES;
		return s;
	}
	
}
