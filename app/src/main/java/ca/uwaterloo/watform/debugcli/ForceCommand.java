package ca.uwaterloo.watform.debugcli;

public class ForceCommand extends DebugCommand {
    public String getName() {
        return CommandConstants.Force_NAME;
    }

    public String getDescription() {
        return CommandConstants.Force_DESCRIPTION;
    }

    public String getHelp() {
        return CommandConstants.Force_HELP;
    }

    public String[] getShorthand() {
        return CommandConstants.Force_SHORTHAND;
    }

    public void execute(String[] input, DebugSimulationManager simulationManager) {
        if (!(simulationManager instanceof DebugDashSimulationManager)) {
            System.out.println("Dash simulation manager required.");
            return;
        }
        DebugDashSimulationManager dashSimulationManager =
                (DebugDashSimulationManager) simulationManager;

        if (input.length == 1) {
            System.out.println(CommandConstants.Force_HELP);
            return;
        }
        if (input.length == 2) {
            String transitionName = input[1];
            if (dashSimulationManager.isTransition(transitionName).equals("")) {
                System.out.println("transition does not exist.");
                return;
            }
            dashSimulationManager.forceTransition(
                    dashSimulationManager.isTransition(transitionName), 10);
        }
        if (input.length == 3) {
            String transitionName = input[1];
            Integer maxSteps = Integer.parseInt(input[2]);
            if (dashSimulationManager.isTransition(transitionName).equals("")) {
                System.out.println("transition does not exist.");
                return;
            }
            dashSimulationManager.forceTransition(
                    dashSimulationManager.isTransition(transitionName), maxSteps);
        }
    }
}
