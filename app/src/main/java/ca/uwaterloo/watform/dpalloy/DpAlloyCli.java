package ca.uwaterloo.watform.dpalloycli;

import static ca.uwaterloo.watform.parser.AlloyParser.*;
import static ca.uwaterloo.watform.utils.CommonStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyinterface.AlloyInterface;
import ca.uwaterloo.watform.alloyinterface.Solution;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.utils.CliUtils;
import edu.mit.csail.sdg.alloy4.Err;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

@Command(
    usageHelpWidth = 120,
    name = "java -cp dp-alloy.jar",
    mixinStandardHelpOptions = true,
    version = "dp-alloy 1.0",
    header = {
      "@|cyan     ____            __    ____  __               |@",
      "@|cyan    / __ \\____ _____/ /_  / __ \\/ /_  __  _______ |@",
      "@|cyan   / / / / __ `/ __/ __ \\/ /_/ / / / / / / / ___/ |@",
      "@|cyan  / /_/ / /_/ (__  ) / // ____/ / /_/ /_/ (__  )  |@",
      "@|cyan /_____/\\__,_/____/_/_/_/   /_/\\__,_/___/____/    |@",
      ""
    },

    // Optional: Customize section headings
    optionListHeading = "%n@|bold Options:|@%n",
    parameterListHeading = "%n@|bold Parameters:|@%n")
public class DpAlloyCli implements Callable<Integer> {

  @Mixin DpAlloyCliConf cliConf = DpAlloyCliConf.INSTANCE;

  @Override
  public Integer call() {

    Boolean cmd = CliUtils.cmdPresent(cliConf.cmdIdx);
    Boolean verbose = cliConf.verbose;
    CliUtils.debug = cliConf.debug;

    // set a default value for cmd in case this arg is not given
    // cmdIdx = CliUtils.noCmdValue means no cmd value given so run all commands
    // cmdIdx = CliUtils.intArgNotPresent means no cmd so run for satisfiability only
    Integer cmdIdx =
        (cmd && CliUtils.cmdIdxUseful(cliConf.cmdIdx)) ? cliConf.cmdIdx : CliUtils.noCmdValue;

    try {
      for (String fileName : cliConf.fileNames) {
        // Main logic executed per file

        Path path = Paths.get(fileName);
        Path absolutePath = path.toAbsolutePath();
        String fullFileName = absolutePath.toString();
        String outputFileNamePrefix = fullFileName.substring(0, fullFileName.lastIndexOf("."));
        if (!Files.exists(absolutePath)) {
          dpOutput("File does not exist: " + fullFileName);
          break;
        }
        Reporter.INSTANCE.reset();
        Reporter.INSTANCE.popPath();
        Reporter.INSTANCE.pushPath(absolutePath);

        if (fullFileName.endsWith(".als")) {
          dpOutput("Input: " + fullFileName);
          AlloyModel am = alloyParseToModel(fullFileName);
          System.out.println("---");
          System.out.println("Before resolve");
          am.debug();
          am.resolve();
          System.out.println("---");
          System.out.println("After resolve");
          am.debug();
          int num_cmds_in_file = am.getNumCmds();
          if (cmdIdx < num_cmds_in_file) {
            AlloyInterface.executeCommand(am, cmdIdx);
          } else if (num_cmds_in_file == 0) {
            // if there are no commands in the file
            // and there was no cmd arg
            Solution soln = AlloyInterface.checkModelSatisfiability(am);
          } else {
            // execute all commands if no value for cmd or cmd # out of range
            for (int i = CliUtils.firstCmdIdx; i < num_cmds_in_file; i++) {
              AlloyInterface.executeCommand(am, i);
            }
          }
        }
      }

      Reporter.INSTANCE.print();
      return 0;

    } catch (Reporter.AbortSignal abortSignal) {
      // already printed Reporter if it issued an AbortSignal
      return 1;
    } catch (ImplementationError implError) {
      // Implementation Error exit code: 2
      if (cliConf.debug) implError.printStackTrace();
      else System.err.println(implError);
      return 2;
    } catch (UserOrImplError implError) {
      // bubbled up here so these are ImplementationError
      // see ErrorHandling.md
      if (cliConf.debug) implError.printStackTrace();
      else System.err.println(implError);
      return 2;
    } catch (Err e) {
      // error that comes from a call the Alloy Analyzer code base
      // probably a user error but might not be
      System.err.println("Error message from Alloy Analyzer (regarding an Alloy model)");
      System.out.println(e.getMessage());
      System.out.println("Line: " + e.pos.y);
      System.out.println("Column: " + e.pos.x);
      return 3;
    } catch (Exception e) {
      // Unexpected Error exit code: 3
      System.err.println("Unexpected error: ");
      if (cliConf.debug) e.printStackTrace();
      else System.err.println(e);
      return 4;
    }
  }

  public static void main(String[] args) throws IOException {
    int exitCode = new CommandLine(new DpAlloyCli()).execute(args);
    System.exit(exitCode);
  }
}
