/*
    Because Solution is a class (and A4Solution is a class inside our Solution class, only one solution can exist at any time, thus
    getting a list of Solutions is not an option.  We can iterate
    soln.next() and writeXML right away but we cannot get a list of
    satisfying solutions by iterating soln.next() because it will just
    be a list of the same objects.
*/

package ca.uwaterloo.watform.alloyinterface;

import static ca.uwaterloo.watform.alloyinterface.Instance.*;
import static ca.uwaterloo.watform.alloyinterface.Solution.*;
import static ca.uwaterloo.watform.utils.CommonStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.utils.CliUtils;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.ast.Command;
import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.parser.CompUtil;
import edu.mit.csail.sdg.translator.A4Options;
import edu.mit.csail.sdg.translator.A4Solution;
import edu.mit.csail.sdg.translator.TranslateAlloyToKodkod;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class AlloyInterface {

  public static CompModule parse(String alloyCode) throws Err {
    return CompUtil.parseEverything_fromString(new A4Reporter(), alloyCode);
  }

  public static CompModule parseFromFile(String fullFileName) throws Err {
    return CompUtil.parseEverything_fromFile(new A4Reporter(), null, fullFileName);
  }

  private static CompModule toAlloy(AlloyModel am) throws Err {
    return parse(am.toString());
  }

  // testing function
  public static Boolean canParse(String alloyCode) {
    try {
      parse(alloyCode);
      return true;
    } catch (Err e) {
      return false;
    }
  }

  private static Solution executeCommand(String alloyCode, String fullFileName, int cmdnum) {

    // create a tmp filename
    String baseName =
        fullFileName.endsWith(".als")
            ? fullFileName.substring(0, fullFileName.length() - 4)
            : fullFileName;
    Path tmpPath = Path.of(baseName + "-tmp.als");

    if (!Files.exists(tmpPath)) {
      // write to tmp file in correct place
      // so imports in alloyCode work correctly
      try {
        Files.writeString(tmpPath, alloyCode);
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(4);
      }
      CompModule alloy = null;
      try {
        // this will put in a cmd 0: run {} if there are no other cmds
        alloy = parseFromFile(tmpPath.toString());
        Files.deleteIfExists(tmpPath);
      } catch (Err e) {
        System.out.println("Leaving problematic tmp file: " + tmpPath.toString());
        // Files.deleteIfExists(tmpPath);
        throw e;
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(4);
      }
      A4Reporter rep = new A4Reporter();
      // TODO: no cmd at that position
      Integer numCmds = alloy.getAllCommands().size();
      if (cmdnum > numCmds) {
        throw AlloyInterfaceImplError.cmdNumOutOfRange(cmdnum);
      } else {
        Command cmd = alloy.getAllCommands().get(cmdnum);
        dpOutput("Executing cmd " + String.valueOf(cmdnum) + ": " + cmd.toString());
        // turn off kodkod stuff going to screen
        System.setProperty("org.slf4j.simpleLogger.log.kodkod.engine.config", "warn");
        A4Solution ans =
            TranslateAlloyToKodkod.execute_command(
                rep, alloy.getAllReachableSigs(), cmd, new A4Options());
        dpOutput("Solution is : " + (ans.satisfiable() ? "SAT" : "UNSAT"));
        if (ans.satisfiable()) {
          StringWriter sw = new StringWriter();
          PrintWriter pw = new PrintWriter(sw);
          ans.writeXML(pw, alloy.getAllFunc(), Collections.emptyMap());
          pw.flush();
          String xml = sw.toString();
          // System.out.println(xml);
          return SatSolution(new Instance(xml));
        } else {
          return UnsatSolution();
        }
      }
    } else {
      throw AlloyInterfaceError.tmpFileExists(tmpPath.toString());
    }
  }

  public static Solution executeCommand(AlloyModel am, int cmdnum) {
    // assumes this is a valid cmd or NOCMD
    if (cmdnum >= am.getNumCmds()) {
      throw AlloyInterfaceImplError.cmdNumOutOfRange(cmdnum);
    } else if (cmdnum == CliUtils.noCmdValue) {
      return checkModelSatisfiability(am);
    } else {
      String alloyCode = am.toString();
      return AlloyInterface.executeCommand(alloyCode, am.fullFileName, cmdnum);
    }
  }

  public static Solution checkModelSatisfiability(AlloyModel am) {
    // translate to Alloy without any commands ("false" arg to toString below)
    // and ask it to execute cmd 0
    // in converting Alloy to Kodkod, it will add a run {}
    String alloyCode = am.toStringNoCmds();
    return AlloyInterface.executeCommand(alloyCode, am.fullFileName, 0);
  }
}
