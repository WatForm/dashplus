package ca.uwaterloo.watform.dashtoalloy;

import java.util.List;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public class DashToAlloyCliConf {
  public static final DashToAlloyCliConf INSTANCE = new DashToAlloyCliConf();

  @Parameters(index = "0", arity = "1..*", description = "Dash file names")
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

  // 0 indexed
  @Option(
      names = "-cmd",
      arity = "0..1", // Makes it optional (0 or 1 occurrence)
      defaultValue = "-100", // Constants.intArgNotPresent value if -cmd
      // is not on the cmd line
      fallbackValue = "-1", // Constants.noCmdValue value if -cmd is on the cmd line w/o a value
      paramLabel = "<cmdIdx>",
      description = "Index of the command to execute (-cmd w/o index means execute all).")
  public int cmdIdx;

  @Option(
      names = {"-write"},
      description = "Write translated Alloy into file")
  public boolean write = false;

  @Option(
      names = {"-v", "--verbose"},
      description = "Verbose output.")
  public boolean verbose = false;

  @Option(
      names = {"-d", "--debug", "-debug"},
      description = "Print stack traces from exceptions.")
  public boolean debug = false;
}
