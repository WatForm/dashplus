package ca.uwaterloo.watform.debugcli;

public class DebugDashSimulationManager extends DebugSimulationManager {
    public String isTransition(String label) {
        return label == null ? "" : label;
    }

    public boolean forceTransition(String transitionName, int limit) {
        return true;
    }
}
