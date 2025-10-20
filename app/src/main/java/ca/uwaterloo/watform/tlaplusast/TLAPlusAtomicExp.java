package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

class TLAPlusAtomicExp extends TLAPlusExp
{
	private final String value;

	protected TLAPlusAtomicExp(String value)
	{
		this.value = value;
		this.children = new ArrayList<>();
	}

	@Override
	public List<String> toStringList() {
		List<String> l = new ArrayList<>();
		l.add(this.value);
		return l;
	}
	
}
