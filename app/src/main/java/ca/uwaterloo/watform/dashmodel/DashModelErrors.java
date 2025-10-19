package ca.uwaterloo.watform.dashmodel;

import ca.uwaterloo.watform.utils.ErrorFatal;

public class DashModelErrors {

	// common
	public static String nameShouldNotBePrimedMsg = "Declared state/trans/event/var cannot have a primed name: ";
	public static void nameShouldNotBePrimed(String n) throws ErrorFatal{
		throw new ErrorFatal(nameShouldNotBePrimedMsg+n);
	}

	public static void notInTable(String table, String fcn, String val) throws ErrorFatal {
		throw new ErrorFatal("for function "+fcn+", index "+val+ " does not exist in "+table+ "table");
	}
	
	
	// parts of the code that should be unreachable 

	public static String ancesNotPrefixMsg = " must be a prefix of ";
	public static void ancesNotPrefix(String a, String d) throws ErrorFatal {
		throw new ErrorFatal(a + ancesNotPrefixMsg + d);
	}
	public static String chopPrefixFromFQNwithNoPrefixMsg = "chopPrefixFromFQNwithNoPrefix: ";
	public static void chopPrefixFromFQNwithNoPrefix(String s) throws ErrorFatal {
		throw new ErrorFatal(chopPrefixFromFQNwithNoPrefixMsg + s);
	}


}