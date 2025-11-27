package ca.uwaterloo.watform.predabstraction;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyinterface.*;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.utils.*;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
        usageHelpWidth = 120,
        name = "predicate",
        mixinStandardHelpOptions = true,
        version = "dashplus 1.0",
        description = {
            "Parses a Dash/Alloy model file and execute command with predicate abstraction.",
        },
        // Optional: Customize section headings
        optionListHeading = "%n@|bold Options:|@%n",
        parameterListHeading = "%n@|bold Parameters:|@%n")
public class Main implements Callable<Integer> {
    // Picocli automatically converts the input String to a Path object
    @Parameters(index = "0", description = "The file path to the Dash/Alloy model.")
    private Path filePath;

    @Parameters(
            index = "1",
            arity = "0..1", // Makes it optional (0 or 1 occurrence)
            defaultValue = "0",
            paramLabel = "<cmdIdx>", // Makes the usage string shorter/cleaner
            description = "The command index to execute (Default: ${DEFAULT-VALUE}).")
    private int commandIndex;

    @Option(
            names = {"-d", "--debug"},
            description = "Enable debug output.")
    private boolean debug = false;

    @Override
    public Integer call() {
        try {
            // Main logic
            Reporter.INSTANCE.setDebugMode(debug);
            Solution solution =
                    AlloyInterface.executeCommand(
                            ParserUtil.parseToModel(filePath), this.commandIndex);
            System.out.println(solution.toString());

            AlloyModel alloyModel = new AlloyModel();
            CEValidation.addSigs(alloyModel, solution);

            System.out.println(alloyModel);

            return 0;
        } catch (Exception e) {
            // if a Reporter.ErrorUser has propagated here
            // then either
            // 1) need to catch it and add to Reporter before it get's here
            // 2) intentionally not caught and it is treated as an implementation error
            System.err.println("Unexpected error: " + e.getMessage());
            if (debug) e.printStackTrace();
            return 1;
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
