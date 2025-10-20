package ca.uwaterloo.watform.alloyinterface;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.parser.CompUtil;

public class AlloyUtils {
    public static CompModule parse(String alloyCode) throws Err {
        return CompUtil.parseEverything_fromString(new A4Reporter(), alloyCode);
    }

    public static Boolean canParse(String alloyCode) {
        try {
            parse(alloyCode);
            return true;
        } catch (Err e) {
            return false;
        }
    }
}
