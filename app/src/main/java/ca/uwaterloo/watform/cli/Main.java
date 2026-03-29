package ca.uwaterloo.watform.cli;

import static ca.uwaterloo.watform.cli.CliError.*;
import static ca.uwaterloo.watform.parser.Parser.*;
import static ca.uwaterloo.watform.utils.CommonStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloyinterface.AlloyInterface;
import ca.uwaterloo.watform.alloyinterface.Solution;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.alloytotla.AlloyToTlaOld;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashtoalloy.DashToAlloy;
import ca.uwaterloo.watform.dashtotla.*;
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
import java.util.stream.Stream;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

// try-catch block is on outside of everything

// only other place in the code where we write files is in generating instances

/* leftover for Rocky
    if (cliConf.debug) {
        System.out.println("Entering debug CLI...");
        new DebugCli(new DebugDashSimulationManager()).run();
    }
*/

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
            // Dash -> Alloy
            "  @|bold 1) dashplus f.dsh -alloy=< traces | tcmc | electrum >|@",
            "              @|bold < -cmd | -cmd=n | -write  < -v > < -d > |@",
            "     (translate dash to alloy, execute cmd(s) or -write .als file in same dir)",
            "     @|italic DEFAULT:|@ dashplus f.dsh means dashplus f.dsh -alloy=traces ",
            "",

            // Dash -> TLA
            "  @|bold 2) dashplus f.dsh/f.als -tla < -cmd | -cmd =n > < -v > < -d > |@",
            "     (translate dash or alloy to tla)",
            "",

            // Predicate Abstraction
            "  @|bold 3) dashplus f.dsh -predAbs < -cmd | -cmd=n > < -v > < -d >  |@",
            "     (pred abstraction)",
            "",

            // Visualization
            "  @|bold 4) dashplus f.dsh -vis < -v > < -d > |@",
            "     (create a .dot file graphic of dash model)",
            "",

            // TLA XML Instance Check
            "  @|bold 5) dashplus f.dsh/f.als -xml=instance.xml <-tla> < -v > < -d > |@",
            "     (translate f to tla with additions that check if XML is instance of it)",
            "",

            // Write Dash file
            "  @|bold 6) dashplus f.dsh/f.als -write < -v > < -d > |@",
            "     (output the input dash file with expressions resolved)",
            "",

            // Alloy Files
            "  @|bold 7) dashplus f.als < -cmd | -cmd=n > < -v > < -d > |@",
            "     (execute cmd(s) of alloy file)",
            "",

            // General Flags
            "@|bold,underline GENERAL FLAGS|@",
            "  @|bold -cmd=n|@   translate/execute cmd n; if just -cmd it means execute all cmds; if not present, translate/execute with no commands",
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

        /*
        Modes                      Optional Parameters
        -alloy      .als    .dsh                    -cmd    -write  -verbose    -debug
        -tla        .als    .dsh        -xml=f.xml  -cmd            -verbose    -debug
        -xml=f.xml  .als    .dsh   -tla                             -verbose    -debug
        -predAbs            .dsh                    -cmd            -verbose    -debug
        -vis                .dsh                                    -verbose    -debug
        -write              .dsh
        */

        // flags to guide possible combinations
        Boolean alloyPresent = Constants.alloyPresent(cliConf.d2aOptions);
        Boolean tla = cliConf.tla;
        Boolean xml = Constants.xmlPresent(cliConf.xmlFileName);
        Boolean predAbs = cliConf.predAbs;
        Boolean vis = cliConf.vis;
        Boolean cmd = Constants.cmdPresent(cliConf.cmdIdx);
        Boolean write = cliConf.write;
        Boolean verbose = cliConf.verbose;
        Boolean debug = cliConf.debug;

        Boolean alsInputFile =
                someTrue(mapBy(cliConf.fileNames, f -> ((String) f).contains(".als")));

        // alloy is the default command
        Boolean translateToAlloy =
                !(tla || xml || predAbs || vis || alsInputFile); // might also have a -alloy=

        // set the default options to be traces for anything that translates to alloy
        DashToAlloy.Options d2aOptions =
                (translateToAlloy && !alloyPresent)
                        ? DashToAlloy.Options.traces
                        : cliConf.d2aOptions;

        // set a default value for cmd in case this arg is not given
        // cmdIdx = Constants.noCmdValue means no cmd value given so run all commands
        // cmdIdx = Constants.intArgNotPresent means no cmd so run for satisfiability only
        Integer cmdIdx =
                (cmd && Constants.cmdIdxUseful(cliConf.cmdIdx))
                        ? cliConf.cmdIdx
                        : Constants.noCmdValue;

        // rule out bad combinations of CLI options
        // tla and xml are okay together
        long count1 = Stream.of(alloyPresent, tla, predAbs, vis).filter(b -> b).count();
        long count2 = Stream.of(alloyPresent, xml, predAbs, vis).filter(b -> b).count();

        if (count1 >= 2) {
            Reporter.INSTANCE.addError(
                    invalidParams("-alloy, -tla, -predAbs, -vis cannot be combined"));
        } else if (count2 >= 2) {
            Reporter.INSTANCE.addError(
                    invalidParams("-alloy, -xml, -predAbs, -vis cannot be combined"));
        } else if ((alloyPresent | predAbs | vis)
                && someTrue(mapBy(cliConf.fileNames, f -> ((String) f).contains(".als")))) {
            // no alloy files for these options
            Reporter.INSTANCE.addError(
                    invalidParams("for -alloy, -predAbs, -vis only dash files can be arguments"));
        } else if (write && !translateToAlloy) {
            // write can only be used with alloy
            Reporter.INSTANCE.addError(
                    invalidParams("only -alloy can be written and input file must be .dsh"));
        } else if (xml && cliConf.fileNames.size() != 1) {
            // -xml can only have one input .dsh/.als filename
            Reporter.INSTANCE.addError(
                    invalidParams("for -xml, there can be only one input model"));
        } else if (xml && cmd) {
            Reporter.INSTANCE.addError(invalidParams("for -xml, there cannot be a command"));
        } else if (vis && cmd) {
            Reporter.INSTANCE.addError(invalidParams("for -vis, there cannot be a command"));
        }
        // stop if any errors from above check on combinations
        // Reporter.INSTANCE.exitIfHasErrors();
        if (Reporter.INSTANCE.hasErrors()) {
            Reporter.INSTANCE.print();
            return 1;
        }

        try {
            for (String fileName : cliConf.fileNames) {
                // Main logic executed per file

                Path path = Paths.get(fileName);
                Path absolutePath = path.toAbsolutePath();
                String fullFileName = absolutePath.toString();
                String outputFileNamePrefix =
                        fullFileName.substring(0, fullFileName.lastIndexOf("."));
                String tlaModuleNameGen = absolutePath.getFileName().toString();
                String tlaModuleName =
                        tlaModuleNameGen.substring(0, tlaModuleNameGen.lastIndexOf("."));
                if (!Files.exists(absolutePath)) {
                    dashOutput("File does not exist: " + fullFileName);
                    break;
                }
                Reporter.INSTANCE.reset();
                Reporter.INSTANCE.popPath();
                Reporter.INSTANCE.pushPath(absolutePath);

                if (fullFileName.endsWith(".als")) {
                    dashOutput("Input: " + fullFileName);
                    AlloyModel am = parseToModel(absolutePath);
                    if (tla && !xml) {
                        runAlloyToTla(
                                am, outputFileNamePrefix, tlaModuleName, cmdIdx, verbose, debug);
                    } else if (xml) {
                        runCheckAlloyInstanceTla(am, cliConf.xmlFileName);
                    } else {
                        runAlloy(am, cmdIdx);
                    }
                } else if (!(fullFileName.endsWith(".dsh"))) {
                    Reporter.INSTANCE.addError(invalidFile(fileName));
                } else {
                    // this is a dash file
                    dashOutput("Input: " + fullFileName);
                    DashModel dm = (DashModel) parseToModel(absolutePath);
                    if (vis) {
                        runVis(dm, outputFileNamePrefix);
                    } else if (write && !alloyPresent) {
                        runWriteResolvedDash(dm, outputFileNamePrefix);

                    } else if (xml) {
                        runCheckDashInstanceTla(dm, cliConf.xmlFileName);
                    } else {
                        if (dm.getParas(AlloyCmdPara.class).size() == 0 && cmd) {
                            dashOutputBold(
                                    "Warning: no command in input .dsh file -> using default scopes for run {}");
                        }
                        if (tla) {
                            // needs to take cmdIdx
                            runDashToTla(dm, outputFileNamePrefix, cmdIdx, verbose, debug);
                        } else if (predAbs) {
                            runPredAbs(fullFileName, dm, cmdIdx);
                        } else {
                            runDashToAlloy(dm, d2aOptions, outputFileNamePrefix, write, cmdIdx);
                        }
                    }
                }
                // Reporter.INSTANCE.exitIfHasErrors();
            }
            Reporter.INSTANCE.print();
            return 0;

        } catch (Reporter.ErrorUser errorUser) {
            // User error exit code: 1
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

    public static void main(String[] args) throws IOException {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    private static void runAlloyToTla(
            AlloyModel am,
            String outputFileNamePrefix,
            String moduleName,
            Integer cmdIdx,
            Boolean verbose,
            Boolean debug)
            throws IOException {
        // outputFileNamePrefix is the module name
        // TODO MKJ - this should take the cmd

        TlaModel tlaModel = AlloyToTlaOld.translate(am, moduleName, verbose, debug);

        String tlaFileName = outputFileNamePrefix + ".tla";
        String cfgFileName = outputFileNamePrefix + ".cfg";
        Files.writeString(fileFromString(tlaFileName), tlaModel.moduleCode());
        Files.writeString(fileFromString(cfgFileName), tlaModel.configCode());
        dashOutput("Output:\n" + tlaFileName + "\n" + cfgFileName);
    }

    private static void runCheckAlloyInstanceTla(AlloyModel am, String xmlFileName) {
        // TODO MKJ
        dashOutput("check Alloy instance in TLA not yet implemented");
    }

    private static void runAlloy(AlloyModel am, Integer cmdIdx) {
        int num_cmds_in_file = am.getParas(AlloyCmdPara.class).size();
        if (cmdIdx < num_cmds_in_file) {
            AlloyInterface.executeCommand(am, cmdIdx);
        } else if (num_cmds_in_file == 0) {
            // if there are no commands in the file
            // and there was no cmd arg
            Solution soln = AlloyInterface.checkModelSatisfiability(am);
        } else {
            // execute all commands if no value for cmd or cmd # out of range
            for (int i = Constants.firstCmdIdx; i < num_cmds_in_file; i++) {
                AlloyInterface.executeCommand(am, i);
            }
        }
    }

    private static void runVis(DashModel dm, String outputFileNamePrefix) {

        ControlStateHierarchyVisualizer visualizer = new ControlStateHierarchyVisualizer();
        String prefix = outputFileNamePrefix + "-" + ControlStateHierarchyVisualizer.DEFAULT_PREFIX;
        // TODO Rocky: could the visualization pass back a string that is output here?
        // visualizer.visualize(dm, outputDir, prefix);
        dashOutput("Visualization output: NOT YET WORKING" + prefix + ".dot");
        Reporter.INSTANCE.print();
    }

    private static void runWriteResolvedDash(DashModel dm, String outputFileNamePrefix)
            throws IOException {
        String resolvedDshFileName = outputFileNamePrefix + "-resolved.dsh";
        Files.writeString(fileFromString(resolvedDshFileName), dm.toDashFile().toString());
        dashOutput("Output:\n" + resolvedDshFileName + "\n");
    }

    private static void runCheckDashInstanceTla(DashModel dm, String xmlFileName) {
        // TODO MKJ
        dashOutput("check Dash instance in TLA not yet implemented");
    }

    private static void runDashToTla(
            DashModel dm,
            String outputFileNamePrefix,
            Integer cmdIdx,
            Boolean verbose,
            Boolean debug)
            throws IOException {

        // Mathew - drop "true" as an argument to this function b/c we no longer need the single
        // input assumption flag

        // outputFileNamePrefix is the module name
        // TODO MKJ add cmd as argument here
        TlaModel tlaModel = DashToTla.translate(dm, outputFileNamePrefix, true, verbose, debug);
        String tlaFileName = outputFileNamePrefix + ".tla";
        String cfgFileName = outputFileNamePrefix + ".cfg";
        Files.writeString(fileFromString(tlaFileName), tlaModel.moduleCode());
        Files.writeString(fileFromString(cfgFileName), tlaModel.configCode());

        dashOutput("Output:\n" + tlaFileName + "\n" + cfgFileName);
    }

    private static void runPredAbs(String fullFileName, DashModel dm, Integer cmdIdx)
            throws IOException {
        PredicateAbstraction pa;
        if (cmdIdx == Constants.noCmdValue) {
            pa = new PredicateAbstraction(dm);
        } else {
            pa = new PredicateAbstraction(dm, cmdIdx);
        }
        try {
            DashModel absModel = pa.createAbstractModel();
            dashOutput("Abstract model created.");
            dashOutput(absModel.toDashFile().toString());
        } catch (Exception e) {
            // dashOutput("Query Model:\n\n" + pa.getQueryModelString());
            String fname =
                    fullFileName.substring(0, fullFileName.length() - 4) + "-query_model_error.als";
            Files.writeString(fileFromString(fname), pa.getQueryModelString());
            dashOutput("Query Model written to: " + fname);
            Path path = Paths.get(fname).toAbsolutePath();
            AlloyModel qm = parseToModel(path);
            dashOutput("Parsed " + fname + " to AlloyModel.");
            runAlloy(qm, qm.getParas(AlloyCmdPara.class).size() - 1);
            printStackTrace();
        }
    }

    private static void runDashToAlloy(
            DashModel dm,
            DashToAlloy.Options opt,
            String outputFileNamePrefix,
            Boolean writeOnly,
            Integer cmdIdx)
            throws IOException {
        AlloyModel am = new DashToAlloy(dm, opt).translate();
        if (writeOnly) {
            String alloyFileName = outputFileNamePrefix + "-" + opt + ".als";
            Files.writeString(fileFromString(alloyFileName), am.toString());
            dashOutput("Output: " + alloyFileName);
        } else {
            // we don't need to write the file
            runAlloy(am, cmdIdx);
        }
    }

    private static Path fileFromString(String fname) {
        return new File(fname).toPath();
    }
}
