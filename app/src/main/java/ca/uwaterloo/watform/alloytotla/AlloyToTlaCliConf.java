package ca.uwaterloo.watform.alloytotla;

import java.util.List;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public class AlloyToTlaCliConf {
  public static final AlloyToTlaCliConf INSTANCE = new AlloyToTlaCliConf();

  @Parameters(index = "0", arity = "1..*", description = "Alloy file names")
  public List<String> fileNames;

  @Option(
      names = "-cmd",
      arity = "0..1", // Makes it optional (0 or 1 occurrence)
      defaultValue = "0", // 0-indexed
      fallbackValue = "0",
      paramLabel = "<cmdIdx>",
      description = "Index of the command to execute (-cmd w/o index means run first command).")
  public int cmdIdx;

  @Option(
      names = "-scheme",
      arity = "0..1", // Makes it optional (0 or 1 occurrence)
      defaultValue = "0",
      fallbackValue = "0",
      paramLabel = "<scheme>",
      description =
          "Translation scheme to use:\n0 - (default) Command in Init\n1 - Command as Invariant")
  public int scheme;

  @Option(
      names = {"-v", "--verbose", "-verbose"},
      description = "Verbose output.")
  public boolean verbose = false;

  @Option(
      names = {"-d", "--debug", "-debug"},
      description = "Print stack traces from exceptions.")
  public boolean debug = false;

  @Option(
      names = {"-osx", "--optimize-syntactic"},
      description = "Syntactic Tree Shaking")
  public boolean optimizeSyntactic = false;

  @Option(
      names = {"-osm", "--optimize-semantic"},
      description = "Semantic Tree Shaking")
  public boolean optimizeSemantic = false;

  @Option(
      names = {"-oos", "--optimize-one-sig"},
      description = "Semantic Tree Shaking")
  public boolean optimizeOneSig = false;

  @Option(
      names = {"-ose", "--optimize-scope-exact"},
      description = "Symmetry Breaking for Exact Scopes")
  public boolean optimizeScopeExact = false;
}
