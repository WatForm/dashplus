package ca.uwaterloo.watform.debugcli;

public class NotFoundCommand extends DebugCommand {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public void execute(String[] input, DebugSimulationManager simulationManager) {
        System.out.printf(CommandConstants.UNDEFINED_COMMAND, input[0]);
    }
}
