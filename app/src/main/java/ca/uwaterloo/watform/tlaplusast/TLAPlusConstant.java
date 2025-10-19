package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public class TLAPlusConstant extends TLAPlusExp 
{
	private String name;
	public TLAPlusConstant(String name)
	{
		this.name = name;
	}

	@Override
	public List<String> toStringList() 
	{
		List<String> t = new ArrayList<String>();
		t.add(this.name);
		return t;
	}
	
}
