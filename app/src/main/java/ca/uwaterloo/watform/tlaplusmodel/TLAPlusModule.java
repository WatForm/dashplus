
package ca.uwaterloo.watform.tlaplusmodel;


import ca.uwaterloo.watform.utils.*;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.watform.tlaplusast.*;


public class TLAPlusModule
{
	private String name;
	private List<TLAPlusConstant> constants;
	private List<TLAPlusVariable> variables;
	private List<TLAPlusSTL> extended_libraries;
	public TLAPlusModule(String name)
	{
		this.name = name;
		this.constants = new ArrayList<>();
		this.variables = new ArrayList<>();
		this.extended_libraries = new ArrayList<>();
	}

	public void addSTL(TLAPlusSTL stl)
	{
		this.extended_libraries.add(stl);
	}
	public void addVariable(TLAPlusVariable v)
	{
		this.variables.add(v);
	}
	public void addConstant(TLAPlusConstant c)
	{
		this.constants.add(c);
	}


	private String stringHead()
	{
		return TLAPlusStrings.HEAD_DELIMITER 
		+ TLAPlusStrings.SPACE 
		+ this.name
		+ TLAPlusStrings.SPACE
		+ TLAPlusStrings.HEAD_DELIMITER;
	}
	private static String simpleBuilder(String initial, List<? extends TLAPlusASTNode> l)
	{
		StringBuilder sb = new StringBuilder(initial);
		int n = l.size();
		if(n==0)return ""; // no need for the line if nothing exists
		for(int i = 0; i < n;i++)
		{
			sb.append(" "+l.get(i).toStringList().get(0));
			if(i < n-1)sb.append(TLAPlusStrings.COMMA);
		}
		return sb.toString();
	}
	private String stringBody()
	{
		String doubleSpace = "\n\n";
		return TLAPlusStrings.BODY_DELIMITER
		+ doubleSpace
		+ TLAPlusModule.simpleBuilder(TLAPlusStrings.EXTENDS, this.extended_libraries)
		+ doubleSpace
		+ TLAPlusModule.simpleBuilder(TLAPlusStrings.CONSTANTS, this.constants)
		+ doubleSpace
		+ TLAPlusModule.simpleBuilder(TLAPlusStrings.VARIABLES, this.variables)
		+ doubleSpace
		+ TLAPlusStrings.BODY_DELIMITER;
	}
	public String code()
	{
		return this.stringHead() + "\n" + this.stringBody();
	}
}