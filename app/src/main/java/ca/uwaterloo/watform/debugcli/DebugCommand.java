package ca.uwaterloo.watform.debugcli;

public abstract class DebugCommand {
    private static final String[] SHORTHAND = {};

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getHelp();

    public abstract void execute(String[] input, DebugSimulationManager simulationManager);

    public String[] getShorthand() {
        return SHORTHAND;
    }

    public boolean requiresFile() {
        return false;
    }
}
