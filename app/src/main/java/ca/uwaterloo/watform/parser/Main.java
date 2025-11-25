package ca.uwaterloo.watform.parser;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyinterface.*;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.utils.Reporter.ErrorUser;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
        name = "dashplus",
        mixinStandardHelpOptions = true,
        version = "dashplus 1.0",
        description = "Parses a Dash/Alloy file and executes commands.")
public class Main implements Callable<Integer> {
    // Picocli automatically converts the input String to a Path object
    @Parameters(index = "0", description = "The file path to the Dash/Alloy model.")
    private Path filePath;

    @Option(
            names = {"-d", "--debug"},
            description = "Enable debug output.")
    private boolean debug = false;

    @Override
    public Integer call() {
        try {
            // Main logic
            Solution instance = AlloyInterface.executeCommand(ParserUtil.parseToModel(filePath), 0);
            System.out.println(instance.toString());
            return 0;
        } catch (ErrorUser errorUser) {
            Reporter.INSTANCE.addError(errorUser, filePath);
            if (debug) errorUser.printStackTrace();
            Reporter.INSTANCE.print();
            return 1;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            if (debug) e.printStackTrace();
            return 2;
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
