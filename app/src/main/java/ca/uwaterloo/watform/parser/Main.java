package ca.uwaterloo.watform.parser;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyinterface.*;
import ca.uwaterloo.watform.utils.*;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
        usageHelpWidth = 120,
        name = "dashplus",
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
            "Parses a Dash/Alloy model file and execute command.",
        },
        footer = {
            "",
            "@|bold,underline OTHER TOOLS IN THIS JAR:|@",
            "  This JAR contains the full Dash+ suite. You can invoke other " + "tools",
            "  by specifying their class name:",
            "",
            "  @|yellow,bold [TLA]|@ ",
            "    java -cp watform-dashplus.jar " + "ca.uwaterloo.watform.dashtotlaplus.Main <args>",
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
            Solution instance =
                    AlloyInterface.executeCommand(
                            ParserUtil.parseToModel(filePath), this.commandIndex);
            System.out.println(instance.toString());
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
