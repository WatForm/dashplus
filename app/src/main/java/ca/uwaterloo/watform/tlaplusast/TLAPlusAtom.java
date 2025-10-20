package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

abstract class TLAPlusAtom extends TLAPlusASTNode
{
	private final String value;

	protected TLAPlusAtom(String value)
	{
		this.value = value;
	}

	@Override
	public List<String> toStringList() {
		List<String> l = new ArrayList<>();
		l.add(this.value);
		return l;
	}
	
}
