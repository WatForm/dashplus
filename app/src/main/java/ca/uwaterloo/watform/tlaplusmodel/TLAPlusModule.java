
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
		String head = 
		TLAPlusStrings.HEAD_DELIMITER 
		+ TLAPlusStrings.SPACE 
		+ this.name
		+ TLAPlusStrings.SPACE
		+ TLAPlusStrings.HEAD_DELIMITER;

		String body = 
		TLAPlusStrings.BODY_DELIMITER
		+ "\n"
		+ TLAPlusStrings.BODY_DELIMITER;

		String answer = head + "\n" + body;

		return answer;
	}
}