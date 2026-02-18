package ca.uwaterloo.watform.cli;

import static ca.uwaterloo.watform.alloyinterface.AlloyInterface.*;
import static ca.uwaterloo.watform.cli.CliError.*;
import static ca.uwaterloo.watform.parser.Parser.*;

import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloyinterface.Solution;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.alloytotla.AlloyToTla;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashtoalloy.DashToAlloy;
import ca.uwaterloo.watform.dashtotla.*;
import ca.uwaterloo.watform.debugcli.DebugCli;
import ca.uwaterloo.watform.debugcli.DebugDashSimulationManager;
import ca.uwaterloo.watform.predabstraction.PredicateAbstraction;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.visualization.ControlStateHierarchyVisualizer;
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
            "              @|bold < -cmd | -cmd=n | -write | -noCmd | -visualize > < -s > < -v > < -d > < -vis > |@",
            "     (parse/translate to alloy/execute cmd(s) or write .als file in same dir)",
            "     @|italic DEFAULT:|@ dashplus f.dsh means dashplus f.dsh -alloy traces -write",
            "",
            // 3) Dash -> TLA
            "  @|bold 3) dashplus f.dsh -tla < -s > < -v > < -d > |@",
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

                Reporter.INSTANCE.reset();
                Reporter.INSTANCE.popPath();
                Reporter.INSTANCE.pushPath(absolutePath);

                if (!cliConf.tla && !cliConf.predAbs && !cliConf.xml) {

                    // default case
                    // translate Dash to Alloy given options
                    // or execute cmds of .als file

                    if (fileName.endsWith(".als")) {
                        System.out.println("Parsing model: " + absolutePath);
                        AlloyModel alloyModel = parseToModel(absolutePath);
                        int num_cmds_in_file = alloyModel.getParas(AlloyCmdPara.class).size();
                        if (num_cmds_in_file == 0 && cliConf.cmdIdx == -1) {
                            // if there are no commands in the file
                            // and there was no cmd arg
                            System.out.println("Executing satisfiability check");
                            Solution soln = checkModelSatisfiability(alloyModel);
                            System.out.println("Satisfiable: " + String.valueOf(soln.isSat()));
                        } else if (cliConf.cmdIdx >= cliConf.firstCmdIdx) {
                            System.out.println(
                                    "Executing command: " + String.valueOf(cliConf.cmdIdx));
                            System.out.println(executeCommandToString(alloyModel, cliConf.cmdIdx));
                        } else if (cliConf.cmdIdx == -1) {
                            // execute all commands
                            for (int i = cliConf.firstCmdIdx; i < num_cmds_in_file; i++) {
                                System.out.println("Executing command: " + String.valueOf(i));
                                System.out.println(executeCommandToString(alloyModel, i));
                            }
                        } else {
                            Reporter.INSTANCE.addError(invalidParams());
                        }
                    } else if (fileName.endsWith(".dsh")) {
                        // Dash Mode
                        DashModel dm = (DashModel) parseToModel(absolutePath);

                        if (cliConf.visualize) {
                            Path outputDir =
                                    path.getParent() == null ? Paths.get(".") : path.getParent();
                            ControlStateHierarchyVisualizer visualizer =
                                    new ControlStateHierarchyVisualizer();
                            String prefix =
                                    path.getFileName().toString()
                                            + "-"
                                            + ControlStateHierarchyVisualizer.DEFAULT_PREFIX;
                            visualizer.visualize(dm, outputDir, prefix);
                            System.out.println(
                                    "Visualization output: " + outputDir.resolve(prefix + ".dot"));
                        }

                        AlloyModel am = new DashToAlloy(dm).translate();

                        // change the filename from .dsh to .als for output
                        String fullFileName = absolutePath.toString();
                        System.out.println("Input: " + fullFileName);
                        int lastDotIndex = fullFileName.lastIndexOf('.');
                        String nameWithoutExtension =
                                (lastDotIndex == -1)
                                        ? fullFileName
                                        : fullFileName.substring(0, lastDotIndex);
                        String nameWithoutExtensionWithMethod =
                                nameWithoutExtension + "-" + cliConf.alloyMode;
                        String newFullFileName = nameWithoutExtensionWithMethod + ".als";
                        try {
                            // write the .als file
                            Files.writeString(new File(newFullFileName).toPath(), am.toString());
                            System.out.println("Output: " + newFullFileName);

                        } catch (IOException e) {
                            System.out.println("An error occurred: " + e.getMessage());
                            e.printStackTrace();
                        }
                        // executes and writes 5 instances of model with cmd run {}
                        int count = writeInstancesToXML(am, nameWithoutExtensionWithMethod, 5);
                        System.out.println("Wrote " + String.valueOf(count) + " instance(s).");

                        // later add output about executing cmds
                        Reporter.INSTANCE.print();

                        if (cliConf.debug) {
                            System.out.println("Entering debug CLI...");
                            new DebugCli(new DebugDashSimulationManager()).run();
                        }
                    } else {
                        Reporter.INSTANCE.addError(invalidFile(fileName));
                    }

                } else if (cliConf.tla && !cliConf.predAbs && !cliConf.xml) {

                    if (fileName.endsWith(".dsh") || fileName.endsWith(".als")) {

                        try {

                            String onlyFileName = path.getFileName().toString();
                            String moduleName =
                                    onlyFileName.substring(0, onlyFileName.lastIndexOf("."));

                            if (fileName.endsWith(".dsh")) {
                                DashModel dashModel = (DashModel) parseToModel(absolutePath);
                                TlaModel tlaModel =
                                        DashToTla.translate(
                                                dashModel,
                                                moduleName,
                                                cliConf.single,
                                                cliConf.verbose,
                                                cliConf.debug);
                            }

                            TlaModel tlaModel =
                                    fileName.endsWith(".dsh")
                                            ? DashToTla.translate(
                                                    (DashModel) parseToModel(absolutePath),
                                                    moduleName,
                                                    cliConf.single,
                                                    cliConf.verbose,
                                                    cliConf.debug)
                                            : AlloyToTla.translate(
                                                    (AlloyModel) parseToModel(absolutePath),
                                                    moduleName,
                                                    cliConf.verbose,
                                                    cliConf.debug);

                            Path tlaOutPath = path.getParent().resolve(moduleName + ".tla");
                            Path cfgOutPath = path.getParent().resolve(moduleName + ".cfg");
                            // write the .tla and .cfg file
                            Files.writeString(tlaOutPath, tlaModel.moduleCode());
                            Files.writeString(cfgOutPath, tlaModel.configCode());

                            System.out.println(
                                    "Output:\n"
                                            + tlaOutPath.toString()
                                            + "\n"
                                            + cfgOutPath.toString());
                        } catch (IOException e) {
                            System.out.println("An error occurred: " + e.getMessage());
                            e.printStackTrace();
                        }

                    } else {
                        Reporter.INSTANCE.addError(invalidFile(fileName));
                    }

                } else if (!cliConf.tla && cliConf.predAbs && !cliConf.xml) {
                    // Pred Abs Mode
                    if (fileName.endsWith(".dsh")) {
                        try {
                            DashModel dm = (DashModel) parseToModel(absolutePath);
                            PredicateAbstraction pa;
                            if (cliConf.noCmd) {
                                pa = new PredicateAbstraction(dm);
                            } else {
                                pa = new PredicateAbstraction(dm, cliConf.cmdIdx);
                            }
                            DashModel absModel = pa.createAbstractModel();
                            System.out.println("Abstract model created.");
                        } catch (Exception e) {
                            System.out.println("An error occurred: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

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
