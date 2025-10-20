package ca.uwaterloo.watform.dashmodel;

import ca.uwaterloo.watform.utils.ErrorUser;
import ca.uwaterloo.watform.utils.ErrorFatal;
import ca.uwaterloo.watform.utils.Pos;

public class DashModelErrors {

	// errors discovered in initializing DashModel phase

	public static String nameShouldNotBePrimedMsg = "Declared state/trans/event/var cannot have a primed name: ";
	public static void nameShouldNotBePrimed(String n) throws ErrorFatal{
		throw new ErrorUser(nameShouldNotBePrimedMsg+n);
	}

	public static String onlyOneStateMsg = "Dash model can only have one 'state' section";
	public static void onlyOneState(Pos o) throws ErrorFatal {
		throw new ErrorUser(o,onlyOneStateMsg);
	}
	public static String nameCantBeFQNMsg = "When declared, name cannot have slash: ";
	public static void nameCantBeFQN(Pos o, String name) throws ErrorFatal {
		throw new ErrorUser(o,nameCantBeFQNMsg+name);
	}

	public static String dupNameMsg = "Duplicate names: ";
	public static void dupNames(Pos o, String dups) throws ErrorFatal {
		throw new ErrorUser(o, dupNameMsg + dups);
	}

	// errors discovered in resolveExpr DashModel phase

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