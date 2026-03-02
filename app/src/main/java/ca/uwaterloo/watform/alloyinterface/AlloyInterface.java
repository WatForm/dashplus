/*
    Because Solution is a class (and A4Solution is a class inside our Solution class, only one solution can exist at any time, thus
    getting a list of Solutions is not an option.  We can iterate
    soln.next() and writeXML right away but we cannot get a list of
    satisfying solutions by iterating soln.next() because it will just
    be a list of the same objects.
*/

package ca.uwaterloo.watform.alloyinterface;

import static ca.uwaterloo.watform.utils.CommonStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.cli.Constants;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.XMLNode;
import edu.mit.csail.sdg.ast.Command;
import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.parser.CompUtil;
import edu.mit.csail.sdg.translator.A4Options;
import edu.mit.csail.sdg.translator.A4Solution;
import edu.mit.csail.sdg.translator.A4SolutionReader;
import edu.mit.csail.sdg.translator.TranslateAlloyToKodkod;
import java.io.File;

public class AlloyInterface {

    public static CompModule parse(String alloyCode) throws Err {
        return CompUtil.parseEverything_fromString(new A4Reporter(), alloyCode);
    }

    private static CompModule toAlloy(AlloyModel am) throws Err {
        return parse(am.toString());
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
        dashOutput("Executing cmd " + String.valueOf(cmdnum) + ": " + cmd.toString());
        // turn off kodkod stuff going to screen
        System.setProperty("org.slf4j.simpleLogger.log.kodkod.engine.config", "warn");
        A4Solution ans =
                TranslateAlloyToKodkod.execute_command(
                        rep, alloy.getAllReachableSigs(), cmd, new A4Options());

        dashOutput("Solution is : " + (ans.satisfiable() ? "SAT" : "UNSAT"));
        return new Solution(ans, alloy);
    }

    public static Solution executeCommand(AlloyModel am, int cmdnum) {
        // assumes this is a valid cmd or NOCMD
        String alloyCode = am.toString();
        if (cmdnum == Constants.noCmdValue) {
            return checkModelSatisfiability(am);
        } else {
            return AlloyInterface.executeCommand(alloyCode, cmdnum);
        }
    }

    public static Solution checkModelSatisfiability(AlloyModel am) {
        // translate to Alloy without any commands ("false" arg to toString below)
        // and ask it to execute cmd 0
        // in converting Alloy to Kodkod, it will add a run {}
        String alloyCode = am.toStringNoCmds();
        return AlloyInterface.executeCommand(alloyCode, 0);
    }

    /*
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
    */

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

        Solution soln = executeCommand(am, cmdNum);
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
        return writeInstancesToXML(am, Constants.noCmdValue, instanceFileName, maxInstances);
    }

    public static Solution readXMLInstance(String xmlFileName) {
        try {
            A4Solution sol = A4SolutionReader.read(null, new XMLNode(new File(xmlFileName)));
            return new Solution(sol, null);
        } catch (Err e) {
            // TODO: clean this up
            System.err.println("Alloy error:");
            System.err.println(e.toString());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error:");
            e.printStackTrace();
            return null;
        }
    }

    /*
        Return true if cmdIdx in AlloyModel am is true in the instance found
        within the file xmlFileName
    */
    /*
    public static boolean runCheckInstance(AlloyModel am, String xmlFileName, Integer cmdIdx)
            throws IOException {

        A4Options opt = new A4Options(); // use the default ones
        A4Reporter rep = new A4Reporter();

        Path path = Paths.get(xmlFileName);
        Path absolutePath = path.toAbsolutePath();
        if (!Files.exists(absolutePath)) {
            dashOutput("File does not exist: " + xmlFileName);
            return false;
        }
        String fullFileName = absolutePath.toString();

        // soln from the xml file
        A4Solution soln = A4SolutionReader.read(null, new XMLNode(new File(xmlFileName)));

        // cm is the CompModule for AlloyModel am
        CompModule cm = toAlloy(am);

        // cmd in cm that we want to check is true in soln
        // note: cmd.formula contains that facts of the model that cmd is from
        // plus the cmd itself.
        Command cmd = cm.getAllCommands().get(cmdIdx);

        // check the cm and the soln have the same set of sig/field names
        // we assume there are no duplicates within the cm or soln individually
        Set<String> cmAllNames = new HashSet<String>();
        for (Sig s : cm.getAllReachableSigs()) {
            cmAllNames.add(s.label);
            for (Sig.Field f : s.getFields()) {
                cmAllNames.add(f.label);
            }
        }
        Set<String> solnAllNames = new HashSet<String>();
        Instance instance = soln.debugExtractKInstance();
        for (Relation r : instance.relations()) {
            solnAllNames.add(r.name());
        }

        if (!(cmAllNames).equals(solnAllNames)) {
            dashOutput("Solution and Alloy model do not have same set of signatures/fields.");
            return false;
        }

        // check the soln and the cmd have compatible scopes
        ScopeComputer scoper = ScopeComputer.compute(rep, opt, cm.getAllReachableSigs(), cmd).b;

        // we don't need to check for sizes of fields
        // because they can't be given scopes in a cmd
        Integer sSizeInSoln, sScopeInCmd;
        for (Sig s : cm.getAllReachableSigs()) {
            // s must exist in both because we checked above
            sSizeInSoln = instance.tuples(s.label).size();
            sScopeInCmd = scoper.sig2scope(s);
            if (scoper.isExact(s) && sSizeInSoln != sScopeInCmd
                    || !scoper.isExact(s) && sSizeInSoln > sScopeInCmd) {
                dashOutput("Solution and Alloy model do not have compatible scopes.");
                return false;
            }
        }

        // create a TranslateAlloyToKodkod instance
        // soln and cmd are useless args for our purposes
        TranslateAlloyToKodkod tr = new TranslateAlloyToKodkod(soln, cmd);

        // makeFacts create a formula within tr that is the
        // per sig facts from the model of cmd +
        // the facts of the mode +
        // the formula of the cmd itself
        tr.makeFacts(cmd.formula);

        // a frame is an A4solution
        // this is the formula that includes the cmd + facts + per sig facts
        Formula f = tr.frame.getFullFormula();

        // before running evalModel() we have to call solve
        // this doesn't actually call the solver or do any solving,
        // but it sets the "solved" state to true
        // inside soln (which is necessary for evaluating)
        // soln.solve(A4Reporter.NOP, null, 0) call doesn't touch the
        // formulas inside the A4Solution and just creates an evaluator
        // out of the sig/field instances, which is what we want,
        // so there shouldn't be any effect from having the extra formulas in it.
        // (Not to be confused with the other overload of solve)
        soln.solve(A4Reporter.NOP, null, 0);

        return soln.evalModel(cmd, opt);
    }
    */
}
