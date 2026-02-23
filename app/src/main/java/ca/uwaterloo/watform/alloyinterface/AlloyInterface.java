/*
    Because Solution is a class (and A4Solution is a class inside our Solution class, only one solution can exist at any time, thus
    getting a list of Solutions is not an option.  We can iterate
    soln.next() and writeXML right away but we cannot get a list of
    satisfying solutions by iterating soln.next() because it will just
    be a list of the same objects.
*/

package ca.uwaterloo.watform.alloyinterface;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.utils.ImplementationError;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.ast.Command;
import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.parser.CompUtil;
import edu.mit.csail.sdg.translator.A4Options;
import edu.mit.csail.sdg.translator.A4Solution;
import edu.mit.csail.sdg.translator.TranslateAlloyToKodkod;
import java.util.Optional;

public class AlloyInterface {

    public static final int NOCMD = -1;

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

    // keep these private; dashplus should be working from AlloyModel
    // and DashModel
    // assumption: cmdnum exists
    private static Solution executeCommand(String alloyCode, int cmdnum) {
        // this will put in a cmd 0: run {} if there are no other cmds
        CompModule alloy = parse(alloyCode);
        A4Reporter rep = new A4Reporter();
        Command cmd = alloy.getAllCommands().get(cmdnum);
        blue("Executing cmd " + String.valueOf(cmdnum) + ": " + cmd.toString());
        A4Solution ans =
                TranslateAlloyToKodkod.execute_command(
                        rep, alloy.getAllReachableSigs(), cmd, new A4Options());
        blue("Solution is : " + (ans.satisfiable() ? "SAT" : "UNSAT"));
        return new Solution(ans, alloy);
    }

    public static Solution executeCommand(AlloyModel am, int cmdnum) {
        String alloyCode = am.toString();
        Optional<AlloyCmdPara> cmd = am.getCmdNum(cmdnum);
        if (cmd.isEmpty()) {
            printStackTrace();
            throw ImplementationError.shouldNotReach();
        }
        return AlloyInterface.executeCommand(alloyCode, cmdnum);
    }

    public static Solution checkModelSatisfiability(AlloyModel am) {
        // translate to Alloy without any commands ("false" arg to toString below)
        // and ask it to execute cmd 0
        // in converting Alloy to Kodkod, it will add a run {}
        String alloyCode = am.toStringNoCmds();
        return AlloyInterface.executeCommand(alloyCode, 0);
    }

    // returns a String
    public static String executeCommandToString(AlloyModel am, int cmdnum) {
        Solution soln = executeCommand(am, cmdnum);
        // print cmd; must exist or would have thrown error in line above
        Optional<AlloyCmdPara> cmd = am.getCmdNum(cmdnum);
        String result = cmd.map(Object::toString).orElse("");
        // print solution: might be unsat
        if (soln.isSat()) return result + "\nSATISFIABLE\n" + soln.toString();
        else return result + "\nUNSATISFIABLE\n";
    }

    /*
        write up to maxInstances of satisfying solutions of
        AlloyModel am (run {} cmd) to files called
        instanceFileName1, instanceFilename2, etc.
        Returns how many solutions are written (0 if unsat)
    */
    public static Integer writeInstanceToXML(
            AlloyModel am, Integer cmdNum, String instanceFileName // should not include .xml at end
            ) {
        return writeInstancesToXML(am, cmdNum, instanceFileName, 1);
    }

    // returns number of instances written to file(s)
    public static Integer writeInstancesToXML(
            AlloyModel am,
            Integer cmdNum,
            String instanceFileName, // should not include .xml at end
            Integer maxInstances) {
        assert (!instanceFileName.contains(".xml"));
        Solution soln =
                (cmdNum == NOCMD) ? checkModelSatisfiability(am) : executeCommand(am, cmdNum);
        int c;
        for (c = 0; c < maxInstances; c++) {
            // c is how many instances that we have written
            if (!(soln.isSat() && c < maxInstances)) break;
            soln.writeXML(instanceFileName + String.valueOf(c + 1) + ".xml");
            soln.next();
        }
        return c;
    }

    // returns number of instances written to file(s)
    public static Integer writeInstancesToXML(
            AlloyModel am,
            String instanceFileName, // should not include .xml at end
            Integer maxInstances) {
        return writeInstancesToXML(am, NOCMD, instanceFileName, maxInstances);
    }
}
