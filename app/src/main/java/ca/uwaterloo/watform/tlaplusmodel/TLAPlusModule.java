
package ca.uwaterloo.watform.tlaplusmodel;


import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.tlaplusast.*;


public class TLAPlusModule
{
	private String name;
	public TLAPlusModule(String name)
	{
		this.name = name;
	}
	public String code()
	{
		return this.name;
	}
}