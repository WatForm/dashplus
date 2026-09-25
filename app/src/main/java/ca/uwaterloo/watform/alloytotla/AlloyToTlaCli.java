package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.dashast.DashParser.*;
import static ca.uwaterloo.watform.parser.AlloyParser.alloyParseToModel;
import static ca.uwaterloo.watform.utils.CommonStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.alloytotla.BaseA2T.Optimization;
import ca.uwaterloo.watform.alloytotla.BaseA2T.Scheme;
import ca.uwaterloo.watform.utils.*;
import edu.mit.csail.sdg.alloy4.Err;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

@Command(
    usageHelpWidth = 120,
    name = "java -jar alloytotla.jar",
    mixinStandardHelpOptions = true,
    version = "alloytotla 1.0",
    header = {
      "@|cyan     ____            __    ____  __               |@",
      "@|cyan    / __ \\____ _____/ /_  / __ \\/ /_  __  _______ |@",
      "@|cyan   / / / / __ `/ __/ __ \\/ /_/ / / / / / / / ___/ |@",
      "@|cyan  / /_/ / /_/ (__  ) / // ____/ / /_/ /_/ (__  )  |@",
      "@|cyan /_____/\\__,_/____/_/_/_/   /_/\\__,_/___/____/    |@",
      ""
    },
    footer = {},
    // Optional: Customize section headings
    optionListHeading = "%n@|bold Options:|@%n",
    parameterListHeading = "%n@|bold Parameters:|@%n")
public class AlloyToTlaCli implements Callable<Integer> {

  public static void main(String[] args) throws IOException {
    int exitCode = new CommandLine(new AlloyToTlaCli()).execute(args);
    System.exit(exitCode);
  }

  @Mixin AlloyToTlaCliConf cliConf = AlloyToTlaCliConf.INSTANCE;

  public void perFile(String fileName, AlloyToTlaCliConf CliConf) throws Exception {

    Integer cmdIdx = cliConf.cmdIdx;
    Scheme scheme = cliConf.scheme == 1 ? Scheme.INVARIANT_COMMAND : Scheme.INIT_COMMAND;
    Boolean verbose = cliConf.verbose;
    Boolean debug = cliConf.debug;
    Optimization optimization =
        new Optimization(
            cliConf.optimizeSyntactic,
            cliConf.optimizeSemantic,
            cliConf.optimizeScopeExact,
            cliConf.optimizeOneSig);

    Path absolutePath = Paths.get(fileName).toAbsolutePath();

    String t = absolutePath.getFileName().toString();
    String baseName = t.substring(0, t.lastIndexOf("."));
    Path tlaFilePath = absolutePath.getParent().resolve(baseName + ".tla");
    Path cfgFilePath = absolutePath.getParent().resolve(baseName + ".cfg");

    if (!Files.exists(absolutePath)) {
      dpOutput("File does not exist: " + absolutePath.toString());
      return;
    }

    Reporter.INSTANCE.reset();

    if (absolutePath.toString().endsWith(".als")) {
      AlloyModel alloyModel = alloyParseToModel(absolutePath.toString());
      alloyModel.resolve();
      AlloyToTla translator = new AlloyToTla(alloyModel, scheme, optimization, verbose, debug);
      var tlaModel = translator.translate(baseName, cmdIdx);

      Files.writeString(tlaFilePath, tlaModel.moduleCode());
      Files.writeString(cfgFilePath, tlaModel.configCode());
    }
  }

  @Override
  public Integer call() {

    try {
      for (String fileName : cliConf.fileNames) {
        // Main logic executed per file

        perFile(fileName, cliConf);
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
}
