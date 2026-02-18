package ca.uwaterloo.watform.debugcli;

public class QuitCommand extends DebugCommand {
    public String getName() {
        return CommandConstants.QUIT_NAME;
    }

    public String getDescription() {
        return CommandConstants.QUIT_DESCRIPTION;
    }

    public String getHelp() {
        return CommandConstants.QUIT_HELP;
    }

    public String[] getShorthand() {
        return CommandConstants.QUIT_SHORTHAND;
    }

    public void execute(String[] input, DebugSimulationManager simulationManager) {
        if (!simulationManager.isInitialized()) {
            System.exit(0);
        }
        System.out.print(CommandConstants.QUIT_USER_PROMPT);
        String s = System.console() != null ? System.console().readLine() : "";
        for (String accepted : CommandConstants.QUIT_ACCEPTED_RESPONSES) {
            if (s.equals(accepted)) {
                System.exit(0);
            }
        }
    }
}
