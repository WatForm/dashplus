package ca.uwaterloo.watform.cli;

import static ca.uwaterloo.watform.alloyinterface.AlloyInterface.*;
import static ca.uwaterloo.watform.cli.CliError.*;
import static ca.uwaterloo.watform.parser.Parser.*;
import static ca.uwaterloo.watform.utils.CommonStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloyinterface.Solution;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.alloytotla.AlloyToTla;
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
            // Alloy Files
            "  @|bold 1) dashplus f.als < -cmd | -cmd=n > < -v > < -d > |@",
            "     (execute cmd n on alloy file)",
            "     @|italic DEFAULT:|@ dashplus f.als means dashplus f.als -cmd (execute all cmds)",
            "",
            // Alloy Files
            "  @|bold 2) dashplus f.als -tla < -v > < -d > |@",
            "     (translate alloy to tla)",
            "",

            // Dash -> Alloy
            "  @|bold 3) dashplus f.dsh -alloy=< traces | tcmc | electrum >|@",
            "              @|bold < -cmd | -cmd=n | -write  < -v > < -d > |@",
            "     (translate to alloy, execute cmd(s) or -write .als file in same dir)",
            "     @|italic DEFAULT:|@ dashplus f.dsh means dashplus f.dsh -alloy=traces ",
            "",
            // Dash -> TLA
            "  @|bold 4) dashplus f.dsh/.als -tla < -v > < -d > |@",
            "     (translate to tla)",
            "",
            // Predicate Abstraction
            "  @|bold 5) dashplus f.dsh -predAbs < -cmd | -cmd=n > < -v > < -d >  |@",
            "     (pred abstraction)",
            "",
            // XML Instance Check
            "  @|bold 6) dashplus f.dsh/.als -xml=instance.xml < -v > < -d > |@",
            "     (check if instance is instance of (translated) alloy)",
            "",
            // Visualization
            "  @|bold 7) dashplus f.dsh -vis < -v > < -d > |@",
            "     (create a .dot file graphic of dash model)",
            "",
            // Generate XML Instances
            "  @|bold 8) dashplus f.dsh/.als -gen=n < -v > < -d > |@",
            "     (generate n instances in XML of (translated) model (default=5))",
            "",
            // General Flags
            "@|bold,underline GENERAL FLAGS|@",
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

        // flags to guide possible combinations
        Boolean alloyPresent = Constants.alloyPresent(cliConf.d2aOptions);
        Boolean tla = cliConf.tla;
        Boolean xml = Constants.xmlPresent(cliConf.xmlFileName);
        Boolean predAbs = cliConf.predAbs;
        Boolean vis = cliConf.vis;
        Boolean gen = Constants.genPresent(cliConf.instanceNum);
        Boolean cmd = Constants.cmdPresent(cliConf.cmdIdx);
        Boolean write = cliConf.write;

        Boolean alsInputFile =
                someTrue(mapBy(cliConf.fileNames, f -> ((String) f).contains(".als")));
        // alloy is the default command
        Boolean alloy =
                !(tla || xml || predAbs || vis || gen || alsInputFile); // might also have a -alloy=

        // set the default options to be traces for anything that translates to alloy
        DashToAlloy.Options d2aOptions =
                ((alloy || gen || xml) && !alloyPresent)
                        ? DashToAlloy.Options.traces
                        : cliConf.d2aOptions;
        // set a default value for cmd in case this arg is not given
        Integer cmdIdx =
                (cmd && Constants.cmdIdxUseful(cliConf.cmdIdx))
                        ? cliConf.cmdIdx
                        : Constants.noCmdValue;

        // rule out bad combinations of CLI options
        long count1 = Stream.of(alloyPresent, tla, predAbs, vis).filter(b -> b).count();
        long count2 = Stream.of(gen, tla, predAbs, vis).filter(b -> b).count();
        long count3 = Stream.of(xml, predAbs, vis).filter(b -> b).count();

        if (count1 >= 2) {
            // mutually exclusive: alloy, tla, alloy, predAbs, vis (alloy is default if others
            // aren't present)
            Reporter.INSTANCE.addError(
                    invalidParams("-tla, -alloy, -vis, -predAbs cannot be combined"));
        } else if (count2 >= 2) {
            // mutually exclusive: gen, tla, alloy, predAbs, vis (alloy is default if others
            // aren't present)
            Reporter.INSTANCE.addError(
                    invalidParams("-tla, -gen, -vis, -predAbs cannot be combined"));
        } else if (count3 >= 2) {
            // mutually exclusive: xml, predAbs, vis (alloy is default if others
            // aren't present)
            Reporter.INSTANCE.addError(invalidParams("-xml, -vis, -predAbs cannot be combined"));
        } else if ((alloyPresent | predAbs | vis)
                && someTrue(mapBy(cliConf.fileNames, f -> ((String) f).contains(".als")))) {
            // no alloy files for these options
            Reporter.INSTANCE.addError(
                    invalidParams("for -alloy, -predAbs, -vis only dash files can be arguments"));
        } else if (tla && cmd) {
            // tla takes no cmds
            Reporter.INSTANCE.addError(invalidParams("-tla takes no cmd argument"));
        } else if (write && !alloy) {
            // write can only be used with alloy
            Reporter.INSTANCE.addError(
                    invalidParams("only -alloy can be written and input file must be .dsh"));
        } else if (xml && cliConf.fileNames.size() != 1) {
            // -xml can only have one input .dsh/.als filename
            Reporter.INSTANCE.addError(invalidParams("for -xml, there can be only one input file"));
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

                Reporter.INSTANCE.reset();
                Reporter.INSTANCE.popPath();
                Reporter.INSTANCE.pushPath(absolutePath);

                if (fullFileName.endsWith(".als")) {
                    dashOutput("Input: " + fullFileName);
                    AlloyModel am = parseToModel(absolutePath);
                    if (tla) {
                        runAlloyToTla(am, outputFileNamePrefix, cliConf.verbose, cliConf.debug);
                    } else if (gen) {
                        runGenInstances(am, cmdIdx, outputFileNamePrefix, cliConf.instanceNum);
                    } else if (xml) {
                        runCheckInstanceAlloy(am, cliConf.xmlFileName);
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
                    } else if (tla) {
                        runDashToTla(dm, outputFileNamePrefix, cliConf.verbose, cliConf.debug);
                    } else if (predAbs) {
                        runPredAbs(dm, cmdIdx);
                    } else if (gen) {
                        AlloyModel am = new DashToAlloy(dm, d2aOptions).translate();
                        // same function as used for Alloy file above
                        runGenInstances(
                                am,
                                cmdIdx,
                                outputFileNamePrefix + "-" + d2aOptions,
                                cliConf.instanceNum);
                    } else if (xml) {
                        if (alloyPresent || !tla) {
                            AlloyModel am = new DashToAlloy(dm, d2aOptions).translate();
                            runCheckInstanceAlloy(am, cliConf.xmlFileName);
                        } else if (tla) {
                            // Mathew
                            runCheckInstanceTla(cliConf.xmlFileName);
                        }
                    } else {
                        runDashToAlloy(dm, d2aOptions, outputFileNamePrefix, write, cmdIdx);
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

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    private static void runAlloy(AlloyModel am, Integer cmdIdx) {
        int num_cmds_in_file = am.getParas(AlloyCmdPara.class).size();
        if (cmdIdx < num_cmds_in_file) {
            executeCommand(am, cmdIdx);
        } else if (num_cmds_in_file == 0) {
            // if there are no commands in the file
            // and there was no cmd arg
            // TODO: look for default .ver cmd in directory!
            Solution soln = checkModelSatisfiability(am);
        } else {
            // execute all commands
            for (int i = Constants.firstCmdIdx; i < num_cmds_in_file; i++) {
                executeCommand(am, i);
            }
        }
    }

    private static void runAlloyToTla(
            AlloyModel am, String outputFileNamePrefix, Boolean verbose, Boolean debug)
            throws IOException {
        // outputFileNamePrefix is the module name
        TlaModel tlaModel = AlloyToTla.translate(am, outputFileNamePrefix, verbose, debug);

        String tlaFileName = outputFileNamePrefix + ".tla";
        String cfgFileName = outputFileNamePrefix + ".cfg";
        Files.writeString(fileFromString(tlaFileName), tlaModel.moduleCode());
        Files.writeString(fileFromString(cfgFileName), tlaModel.configCode());
        dashOutput("Output:\n" + tlaFileName + "\n" + cfgFileName);
    }

    private static void runVis(DashModel dm, String outputFileNamePrefix) {

        ControlStateHierarchyVisualizer visualizer = new ControlStateHierarchyVisualizer();
        String prefix = outputFileNamePrefix + "-" + ControlStateHierarchyVisualizer.DEFAULT_PREFIX;
        // Rocky: could the visualization pass back a string that is output here?
        // visualizer.visualize(dm, outputDir, prefix);
        dashOutput("Visualization output: " + prefix + ".dot");
        Reporter.INSTANCE.print();
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

    private static void runDashToTla(
            DashModel dm, String outputFileNamePrefix, Boolean verbose, Boolean debug)
            throws IOException {

        // Mathew - drop "true" as an argument to this function b/c we no longer need the single
        // input assumption flag

        // outputFileNamePrefix is the module name
        TlaModel tlaModel = DashToTla.translate(dm, outputFileNamePrefix, true, verbose, debug);
        String tlaFileName = outputFileNamePrefix + ".tla";
        String cfgFileName = outputFileNamePrefix + ".cfg";
        Files.writeString(fileFromString(tlaFileName), tlaModel.moduleCode());
        Files.writeString(fileFromString(cfgFileName), tlaModel.configCode());

        dashOutput("Output:\n" + tlaFileName + "\n" + cfgFileName);
    }

    private static void runPredAbs(DashModel dm, Integer cmdIdx) {
        PredicateAbstraction pa;
        if (cmdIdx == Constants.noCmdValue) {
            pa = new PredicateAbstraction(dm);
        } else {
            pa = new PredicateAbstraction(dm, cmdIdx);
        }
        DashModel absModel = pa.createAbstractModel();
        dashOutput("Abstract model created.");
    }

    private static void runGenInstances(
            AlloyModel am, Integer cmdIdx, String outputFileNamePrefix, Integer numInstances) {
        // executes and writes numInstances instances of model with cmd cmdIdx
        int count = writeInstancesToXML(am, cmdIdx, outputFileNamePrefix, numInstances);
        dashOutput("Wrote " + String.valueOf(count) + " instance(s).");
    }

    private static void runCheckInstanceAlloy(AlloyModel am, String xmlFileName) {
        dashOutput("check instance in Alloy not yet implemented");
    }

    private static void runCheckInstanceTla(String xmlFileName) {
        dashOutput("check instance in TLA not yet implemented");
    }

    private static Path fileFromString(String fname) {
        return new File(fname).toPath();
    }
}
