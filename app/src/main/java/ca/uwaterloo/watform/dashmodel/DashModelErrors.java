package ca.uwaterloo.watform.dashmodel;

import ca.uwaterloo.watform.utils.ErrorFatal;

public class DashModelErrors {

	// common
	public static String nameShouldNotBePrimedMsg = "Declared state/trans/event/var cannot have a primed name: ";
	public static void nameShouldNotBePrimed(String n) throws ErrorFatal{
		throw new ErrorFatal(nameShouldNotBePrimedMsg+n);
	}

	// PredTable specific
	public static void predDoesNotExist(String s1, String n) throws ErrorFatal {
		throw new ErrorFatal("for function "+s1+", var "+n+ " does not exist in pred table");
	}

	// EventTable specific
	public static void eventTableEventNotFound(String m, String efqn) throws ErrorFatal{
		throw new ErrorFatal("eventTableEventNotFound: "+m+" "+efqn);
	}

	// parts of the code that should be unreachable -------------

	public static String ancesNotPrefixMsg = " must be a prefix of ";
	public static void ancesNotPrefix(String a, String d) throws ErrorFatal {
		throw new ErrorFatal(a + ancesNotPrefixMsg + d);
	}
	public static String chopPrefixFromFQNwithNoPrefixMsg = "chopPrefixFromFQNwithNoPrefix: ";
	public static void chopPrefixFromFQNwithNoPrefix(String s) throws ErrorFatal {
		throw new ErrorFatal(chopPrefixFromFQNwithNoPrefixMsg + s);
	}
}