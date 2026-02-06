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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    // keep these private; dashplus should be working from AlloyModel
    // and DashModel
    // assumption: cmdnum exists
    private static Solution executeCommand(String alloyCode, int cmdnum) {
        System.out.println("Command: " + String.valueOf(cmdnum));
        CompModule alloy = parse(alloyCode);
        A4Reporter rep = new A4Reporter();
        Command cmd = alloy.getAllCommands().get(cmdnum);
        System.out.println(cmd);
        A4Solution ans =
                TranslateAlloyToKodkod.execute_command(
                        rep, alloy.getAllReachableSigs(), cmd, new A4Options());
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
        String alloyCode = am.toString(false);
        return AlloyInterface.executeCommand(alloyCode, 0);
    }

    // returns a String
    public static String executeCommandToString(AlloyModel am, int cmdnum) {
        Solution soln = executeCommand(am, cmdnum);
        // print cmd; must exist or would have thrown error in line above
        Optional<AlloyCmdPara> cmd = am.getCmdNum(cmdnum);
        String result = cmd.map(Object::toString).orElse("");
        // print solution: might be unsat
        return result + "\n" + soln.toString();
    }

    public static Solution executeCommand(AlloyModel am) {
        return AlloyInterface.executeCommand(am, 0);
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
        List<Solution> solnList = getInstances(am, cmdNum, maxInstances);
        for (int i = 0; i < solnList.size(); i++) {
            solnList.get(i).writeXML(instanceFileName + String.valueOf(i) + ".xml");
        }
        return solnList.size();
    }

    public static List<Solution> getInstances(AlloyModel am, Integer cmdNum, Integer maxInstances) {

        assert (cmdNum >= 0);
        // at this point we don't know if it is satisfiable
        List<Solution> solnList = new ArrayList<Solution>();
        Solution soln = executeCommand(am, cmdNum);
        int c;
        for (c = 0; c < maxInstances; c++) {
            if (!(soln.isSat() && c <= maxInstances)) break;
            int j = c + 1;
            solnList.add(soln);
            soln.next();
        }
        return solnList;
    }
}
