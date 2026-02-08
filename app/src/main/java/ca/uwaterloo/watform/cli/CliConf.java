package ca.uwaterloo.watform.cli;

import static ca.uwaterloo.watform.parser.Parser.*;

import ca.uwaterloo.watform.utils.*;
import java.util.List;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public class CliConf {
    public static final CliConf INSTANCE = new CliConf();

    public enum AlloyMode {
        traces,
        tcmc,
        electrum
    }

    @Parameters(index = "0", arity = "1..*", description = "Alloy/Dash file names")
    public List<String> fileNames;

    @Option(
            names = "-alloy",
            description = "Translation mode: ${COMPLETION-CANDIDATES}.",
            defaultValue = "traces")
    public AlloyMode alloyMode;

    // 0 indexed
    //
    @Option(
            names = "-cmd",
            arity = "0..1", // Makes it optional (0 or 1 occurrence)
            defaultValue = "-1",
            paramLabel = "<cmdIdx>",
            description = "Index of the command to execute (Default: execute all).")
    public int cmdIdx;

    public final int firstCmdIdx = 0;

    @Option(
            names = {"-noCmd"},
            description = "Check satisfiability without commands")
    public boolean noCmd = false;

    @Option(
            names = {"-write"},
            description = "Write translated Alloy into file")
    public boolean write = false;

    @Option(
            names = {"-s", "--single"},
            description = "Single environmental input")
    public boolean single = false;

    @Option(
            names = {"-v", "--verbose"},
            description = "Verbose output and see comments")
    public boolean verbose = false;

    @Option(
            names = {"-d", "--debug"},
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

    @Option(
            names = {"-xml"},
            description =
                    "translate to Alloy & check if instance is " + "instance of translated Alloy")
    public boolean xml = false;

    @Option(
            names = {"-vis"},
            description = "use visualization tool")
    public boolean vis = false;

    @Option(
            names = {"-visualize", "--visualize"},
            description = "visualize Dash models")
    public boolean visualize = false;
}
