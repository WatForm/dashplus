package ca.uwaterloo.watform.alloytotla;


import java.util.List;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public class AlloyToTlaCliConf {
  public static final AlloyToTlaCliConf INSTANCE = new AlloyToTlaCliConf();

  @Parameters(index = "0", arity = "1..*", description = "Dash file names")
  public List<String> fileNames;

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
      names = {"-v", "--verbose", "-verbose"},
      description = "Verbose output.")
  public boolean verbose = false;

  @Option(
      names = {"-d", "--debug", "-debug"},
      description = "Print stack traces from exceptions.")
  public boolean debug = false;
}

