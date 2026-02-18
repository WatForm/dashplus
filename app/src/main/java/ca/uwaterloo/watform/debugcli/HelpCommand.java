package ca.uwaterloo.watform.debugcli;

public class HelpCommand extends DebugCommand {
    public String getName() {
        return CommandConstants.HELP_NAME;
    }

    public String getDescription() {
        return CommandConstants.HELP_DESCRIPTION;
    }

    public String[] getShorthand() {
        return CommandConstants.HELP_SHORTHAND;
    }

    public String getHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append(CommandConstants.AVAILABLE_COMMANDS_STR);
        for (DebugCommand command : DebugCommandRegistry.getAllCommands()) {
            sb.append(
                    String.format(
                            CommandConstants.COMMAND_HELP_DELIMITER,
                            command.getName(),
                            command.getDescription()));
        }
        sb.append(CommandConstants.HELP_COMMAND_END_STR);
        return sb.toString();
    }

    public void execute(String[] input, DebugSimulationManager simulationManager) {
        if (input.length < 2) {
            System.out.println(getHelp());
        } else {
            DebugCommand command = DebugCommandRegistry.commandForString(input[1]);
            if (command == DebugCommandRegistry.NOT_FOUND) {
                System.out.println(getHelp());
            } else {
                System.out.println(command.getHelp());
            }
        }
    }
}
