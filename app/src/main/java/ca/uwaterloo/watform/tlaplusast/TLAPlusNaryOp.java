package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public abstract class TLAPlusNaryOp extends TLAPlusExp
{
	private String start;
	private String end;
	private String separator;
	public TLAPlusNaryOp(String start, String end, String separator, List<TLAPlusASTNode> children)
	{
		this.start = start;
		this.end = end;
		this.separator = separator;
		this.children = children;
	}
	@Override
	public List<String> toStringList()
	{
		int n = this.children.size();
		List<String> result = new ArrayList<>();
		
		result.add(this.start);
		if(n!=0)
		{
			for(int i=0;i<n;i++)
			{
				result.addAll(this.children.get(i).toStringList());
				if(i!=n-1)result.add(this.separator);
			}
		}
		result.add(this.end);
		
		return result;
	}
	
}
