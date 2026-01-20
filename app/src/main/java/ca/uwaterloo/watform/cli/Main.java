package ca.uwaterloo.watform.cli;

import static ca.uwaterloo.watform.parser.Parser.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashtoalloy.DashToAlloy;
import ca.uwaterloo.watform.utils.*;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

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
        description = {
            "Parse Dash/Alloy model files and execute command.",
        },
        footer = {
            "",
            "@|bold,underline OTHER TOOLS IN THIS JAR:|@",
            "  This JAR contains the full Dash+ suite. You can invoke other " + "tools",
            "  by specifying their class name:",
            "",
            "  @|yellow,bold [TLA]|@ ",
            "    java -cp watform-dashplus.jar " + "ca.uwaterloo.watform.dashtotla.Main <args>",
            "",
            "  @|yellow,bold [Predicate Abstraction]|@   ",
            "    java -cp watform-dashplus.jar "
                    + "ca.uwaterloo.watform.predabstraction.Main <args>",
            "",
        },

        // Optional: Customize section headings
        optionListHeading = "%n@|bold Options:|@%n",
        parameterListHeading = "%n@|bold Parameters:|@%n")
public class Main implements Callable<Integer> {
    @Option(
            names = "-cmd",
            arity = "0..1", // Makes it optional (0 or 1 occurrence)
            defaultValue = "0",
            paramLabel = "<cmdIdx>",
            description = "Index of the command to execute (Default: ${DEFAULT-VALUE}).")
    private int commandIndex;

    @Parameters(index = "0", arity = "1..*", description = "Paths to Alloy/Dash files")
    private List<Path> inputPaths;

    @Option(
            names = {"-d", "--debug"},
            description = "Enable debug output.")
    private boolean debug = false;

    @Override
    public Integer call() {
        try {
            // Main logic
            Reporter.INSTANCE.setDebugMode(debug);
            for (Path filePath : inputPaths) {
                Reporter.INSTANCE.reset();
                Reporter.INSTANCE.setFilePath(filePath);
                DashModel dm = (DashModel) parseToModel(filePath);
                // tmp debugging start
                //
                // tmp debugging end
                // Solution solution =
                //        AlloyInterface.executeCommand(parseToModel(path), this.commandIndex);
                // System.out.println(solution.toString());
                Reporter.INSTANCE.print();
                // System.out.println("translation to Alloy ----");
                // need to output this to a file
                System.out.println(new DashToAlloy(dm).translate());
            }
            return 0;

            // User error exit code: 1
        } catch (Reporter.ErrorUser errorUser) {
            Reporter.INSTANCE.addError(errorUser);
            Reporter.INSTANCE.print();
            return 1;
        } catch (Reporter.AbortSignal abortSignal) {
            return 1;

            // Implementation Error exit code: 2
        } catch (ImplementationError implementationError) {
            System.err.println(implementationError);
            if (debug) implementationError.printStackTrace();
            return 2;
            // DashPlusException bubbled up here are treated ImplementationError
            // see ErrorHandling.md
        } catch (DashPlusException dashPlusError) {
            System.err.println(dashPlusError);
            if (debug) dashPlusError.printStackTrace();
            return 2;

            // Unexpected Error exit code: 3
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            if (debug) e.printStackTrace();
            return 3;
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
