package ca.uwaterloo.watform.cli;

import static ca.uwaterloo.watform.alloyinterface.AlloyInterface.*;
import static ca.uwaterloo.watform.cli.CliError.*;
import static ca.uwaterloo.watform.parser.Parser.*;

import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloyinterface.Solution;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashtoalloy.DashToAlloy;
import ca.uwaterloo.watform.utils.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

@Command(
        usageHelpWidth = 120,
        name = "java -cp watform-dashplus.jar",
        mixinStandardHelpOptions = true,
        version = "dashplus 1.0",
        header = {
            "@|cyan     ____            __    ____  __               |@",
            "@|cyan    / __ \\____ _____/ /_  / __ \\/ /_  __  _______ |@",
            "@|cyan   / / / / __ `/ __/ __ \\/ /_/ / / / / / / / ___/ |@",
            "@|cyan  / /_/ / /_/ (__  ) / // ____/ / /_/ /_/ (__  )  |@",
            "@|cyan /_____/\\__,_/____/_/_/_/   /_/\\__,_/___/____/    |@",
            ""
        },
        footer = {
            "",
            "@|bold,underline USAGE MODES|@",
            "",
            // 1) Alloy Files
            "  @|bold 1) dashplus f.als < -cmd | -cmd=n > < -v > < -d > < -vis > |@",
            "     (execute cmd on alloy file)",
            "     @|italic DEFAULT:|@ dashplus f.als means dashplus f.als -cmd (execute all cmds)",
            "",
            // 2) Dash -> Alloy
            "  @|bold 2) dashplus f.dsh -alloy=< traces | tcmc | electrum >|@",
            "              @|bold < -cmd | -cmd=n | -write | -noCmd > < -s > < -v > < -d > < -vis > |@",
            "     (parse/translate to alloy/execute cmd(s) or write .als file in same dir)",
            "     @|italic DEFAULT:|@ dashplus f.dsh means dashplus f.dsh -alloy traces -write",
            "",
            // 3) Dash -> TLA
            "  @|bold 3) dashplus f.dsh -tla < -s > < -v > < -d > < -p > < -vis > |@",
            "     (parse/translate to tla/outputs .tla file in same dir as input file)",
            "",
            // 4) Predicate Abstraction
            "  @|bold 4) dashplus f.dsh -predAbs < -cmd | -cmd=n > < -s > < -v > < -d > < -vis > |@",
            "     (parse/pred abstraction)",
            "",
            // 5) XML Instance Check
            "  @|bold 5) dashplus f.dsh -xml=instance.xml < -s > < -v > < -d > < -vis > |@",
            "     (parse/translate to Alloy/check if instance is instance of translated Alloy)",
            "",
            // General Flags
            "@|bold,underline GENERAL FLAGS|@",
            "  @|bold -s|@   for single environmental input",
            "  @|bold -v|@   for verbose output - this adds in comments",
            "  @|bold -d|@   debug mode, where the translator writes things during translation to stdout",
            ""
        },

        // Optional: Customize section headings
        optionListHeading = "%n@|bold Options:|@%n",
        parameterListHeading = "%n@|bold Parameters:|@%n")
public class Main implements Callable<Integer> {

    @Mixin CliConf cliConf = CliConf.INSTANCE;

    @Override
    public Integer call() {

        try {
            for (String fileName : cliConf.fileNames) {
                // Main logic executed per file

                Path path = Paths.get(fileName);
                Path absolutePath = path.toAbsolutePath();

                if (!cliConf.tla && !cliConf.predAbs && !cliConf.xml) {

                    // default case
                    // translate Dash to Alloy given options
                    // or execute cmds of .als file

                    Reporter.INSTANCE.reset();
                    Reporter.INSTANCE.popPath();
                    Reporter.INSTANCE.pushPath(absolutePath);

                    if (fileName.endsWith(".als")) {
                        AlloyModel alloyModel = parseToModel(absolutePath);
                        if (cliConf.cmdIdx >= cliConf.firstCmdIdx) {
                            Solution solution = executeCommand(alloyModel, cliConf.cmdIdx);
                            System.out.println(solution.toString());
                        } else if (cliConf.cmdIdx == -1) {
                            for (int i = cliConf.firstCmdIdx;
                                    i < alloyModel.getParas(AlloyCmdPara.class).size();
                                    i++) {
                                Solution solution = executeCommand(alloyModel, i);
                                System.out.println(solution.toString());
                            }
                        } else {
                            Reporter.INSTANCE.addError(invalidParams());
                        }
                    } else if (fileName.endsWith(".dsh")) {
                        // Dash Mode
                        DashModel dm = (DashModel) parseToModel(absolutePath);
                        AlloyModel am = new DashToAlloy(dm).translate();
                        try {
                            // change the filename from .dsh to .als for output
                            String fullFileName = absolutePath.toString();
                            System.out.println("Input: " + fullFileName);
                            int lastDotIndex = fullFileName.lastIndexOf('.');
                            String nameWithoutExtension =
                                    (lastDotIndex == -1)
                                            ? fullFileName
                                            : fullFileName.substring(0, lastDotIndex);
                            String newFullFileName = nameWithoutExtension + ".als";

                            // write the .als file
                            Files.writeString(new File(newFullFileName).toPath(), am.toString());
                            System.out.println("Output: " + newFullFileName);
                        } catch (IOException e) {
                            System.out.println("An error occurred: " + e.getMessage());
                            e.printStackTrace();
                        }
                        // later add stuff about executing cmds
                        Reporter.INSTANCE.print();
                    } else {
                        Reporter.INSTANCE.addError(invalidFile(fileName));
                    }

                } else if (cliConf.tla && !cliConf.predAbs && !cliConf.xml) {
                    // TLA Mode

                } else if (!cliConf.tla && cliConf.predAbs && !cliConf.xml) {
                    // Pred Abs Mode

                } else if (!cliConf.tla && !cliConf.predAbs && cliConf.xml) {
                    // XML Mode

                } else {
                    Reporter.INSTANCE.addError(invalidParams());
                }

                Reporter.INSTANCE.exitIfHasErrors();
            }
            return 0;

            // User error exit code: 1
        } catch (Reporter.ErrorUser errorUser) {
            Reporter.INSTANCE.addError(errorUser);
            Reporter.INSTANCE.print();
            return 1;
        } catch (Reporter.AbortSignal abortSignal) {
            return 1;

        } catch (ImplementationError implementationError) {
            // Implementation Error exit code: 2
            System.err.println(implementationError);
            if (cliConf.debug) implementationError.printStackTrace();
            return 2;
        } catch (DashPlusException dashPlusError) {
            // DashPlusException bubbled up here are treated ImplementationError
            // see ErrorHandling.md
            System.err.println(dashPlusError);
            if (cliConf.debug) dashPlusError.printStackTrace();
            return 2;

        } catch (Exception e) {
            // Unexpected Error exit code: 3
            System.err.println("Unexpected error: " + e.getMessage());
            if (cliConf.debug) e.printStackTrace();
            return 3;
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
