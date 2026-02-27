package ca.uwaterloo.watform.cli;

import static ca.uwaterloo.watform.parser.Parser.*;

import ca.uwaterloo.watform.dashtoalloy.DashToAlloy;
import ca.uwaterloo.watform.utils.*;
import java.util.List;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public class CliConf {
    public static final CliConf INSTANCE = new CliConf();

    @Parameters(index = "0", arity = "1..*", description = "Alloy/Dash file names")
    public List<String> fileNames;

    // three possibilities:
    // -alloy=traces
    // -alloy   (value should be traces)
    // no -alloy (detected with value =nothing)
    @Option(
            names = "-alloy",
            arity = "0..1",
            defaultValue = "nothing", // value if -alloy is not on the cmd line
            fallbackValue = "traces", // value if -alloy is on the cmd line w/o a value
            description = "Translation mode: ${COMPLETION-CANDIDATES}.")
    public DashToAlloy.Options d2aOptions;

    // no default option here; there must be a string
    @Option(
            names = {"-xml"},
            arity = "1", // required to have a filename
            defaultValue =
                    "-100", // Constants.stringArgNotPresentvalue if -xml is not on the cmd line
            paramLabel = "<xmlFileName>",
            description = "check if instance is instance of (translated) Alloy")
    public String xmlFileName;

    // 0 indexed
    @Option(
            names = "-cmd",
            arity = "0..1", // Makes it optional (0 or 1 occurrence)
            defaultValue = "-100", // Constants.intArgNotPresent value if -cmd
            // is not on the cmd line
            fallbackValue =
                    "-1", // Constants.noCmdValue value if -cmd is on the cmd line w/o a value
            paramLabel = "<cmdIdx>",
            description = "Index of the command to execute (Default: execute all).")
    public int cmdIdx;

    @Option(
            names = {"-write"},
            description = "Write translated Alloy into file")
    public boolean write = false;

    @Option(
            names = {"-v", "--verbose"},
            description = "Verbose output and see comments")
    public boolean verbose = false;

    @Option(
            names = {"-d", "--debug", "-debug"},
            description = "Enable debug output.")
    public boolean debug = false;

    @Option(
            names = {"-tla"},
            description = "translate to TLA")
    public boolean tla = false;

    @Option(
            names = {"-predAbs"},
            description = "Predicate abstraction")
    public boolean predAbs = false;

    // 0 indexed
    @Option(
            names = {"-gen"},
            arity = "0..1", // Makes it optional (0 or 1 occurrence)
            defaultValue =
                    "-100", // Constants.intArgNotPresent value if -gen is not on the cmd line
            fallbackValue = "5", // value if -gen is on the cmd line w/o a value
            paramLabel = "<instanceNum>",
            description = "Generate instanceNum XML instances of Dash model (Default: 5).")
    public int instanceNum;

    @Option(
            names = {"-vis"},
            description = "create .dot file of Dash model")
    public boolean vis = false;
}
