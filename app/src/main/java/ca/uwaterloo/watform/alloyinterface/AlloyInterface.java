package ca.uwaterloo.watform.alloyinterface;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.ast.Command;
import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.parser.CompUtil;
import edu.mit.csail.sdg.translator.A4Options;
import edu.mit.csail.sdg.translator.A4Solution;
import edu.mit.csail.sdg.translator.TranslateAlloyToKodkod;

public class AlloyInterface {
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

    public static A4Solution executeCommand(String alloyCode, int cmdnum) {
        CompModule alloy = parse(alloyCode);
        A4Reporter rep = new A4Reporter();
        Command cmd = alloy.getAllCommands().get(cmdnum);
        A4Solution ans =
                TranslateAlloyToKodkod.execute_command(
                        rep, alloy.getAllReachableSigs(), cmd, new A4Options());
        return ans;
    }

    public static void executeCommand(String alloyCode, int cmdnum, String XMLfileName) {
        A4Solution ans = executeCommand(alloyCode, cmdnum);
        ans.writeXML(XMLfileName);
    }

    public static Solution executeCommand(AlloyModel am, int cmdnum) {
        String alloyCode = am.toString();
        A4Solution ans = executeCommand(alloyCode, cmdnum);
        return new Solution(ans);
    }

    public static Solution executeCommand(AlloyModel am) {
        return AlloyInterface.executeCommand(am, 0);
    }
}
