package ca.uwaterloo.watform.tlaplusast;

public class TLAPlusConstant extends TLAPlusExp {

	private String name;
	public TLAPlusConstant(String name)
	{
		this.name = name;
	}
	@Override
	public String toString() {
		return this.name;
	}
	
}
