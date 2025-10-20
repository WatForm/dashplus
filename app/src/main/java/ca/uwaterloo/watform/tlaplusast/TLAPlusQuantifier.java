package ca.uwaterloo.watform.tlaplusast;

import java.util.ArrayList;
import java.util.List;

public abstract class TLAPlusQuantifier extends TLAPlusExp {

	private String symbol;

	public TLAPlusQuantifier(TLAPlusVariable v, TLAPlusASTNode set, TLAPlusASTNode exp, String symbol)
	{
		this.children = new ArrayList<>();
		this.children.add(v);
		this.children.add(set);
		this.children.add(exp);
		this.symbol = symbol;
	}


	@Override
	public List<String> toStringList() 
	{

		List<String> result = new ArrayList<>();
		result.add(this.symbol);
		result.addAll(this.children.get(0).toStringList());
		result.add(TLAPlusStrings.SET_IN);
		result.addAll(this.children.get(1).toStringList());
		result.add(TLAPlusStrings.COLON);
		result.addAll(this.children.get(2).toStringList());
		return result;
	}
	
}
