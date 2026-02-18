package ca.uwaterloo.watform.debugcli;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DebugSimulationManager {
    private final AliasManager aliasManager = new AliasManager();
    private final ConstraintManager constraintManager = new ConstraintManager();
    private boolean initialized = false;
    private boolean diffMode = true;
    private File workingDir = new File(".");

    public AliasManager getAliasManager() {
        return aliasManager;
    }

    public ConstraintManager getConstraintManager() {
        return constraintManager;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public boolean initialize(File file, boolean isTrace) {
        initialized = true;
        if (file != null && file.getParentFile() != null) {
            workingDir = file.getParentFile();
        }
        return true;
    }

    public boolean validateConstraint(String constraint) {
        return true;
    }

    public boolean selectAlternatePath(boolean reverse) {
        return true;
    }

    public boolean isDiffMode() {
        return diffMode;
    }

    public void setDiffMode(boolean diffMode) {
        this.diffMode = diffMode;
    }

    public String getCurrentStateDiffStringFromLastCommit() {
        return "(debug) no state diff available";
    }

    public String getCurrentStateString() {
        return "(debug) no state available";
    }

    public String getCurrentStateStringForProperty(String property) {
        return "(debug) property " + property + " not available";
    }

    public String getDOTString() {
        return "digraph G {}\n";
    }

    public String getWorkingDirPath() {
        return workingDir.getPath();
    }

    public boolean performStep(int steps, List<String> constraints) {
        return true;
    }

    public String getCurrentStateDiffStringByDelta(int steps) {
        return "(debug) no state diff available";
    }

    public String getHistory(int n) {
        return "(debug) no history available";
    }

    public boolean setToInit() {
        return true;
    }

    public boolean moveToState(int identifier) {
        return true;
    }

    public void performReverseStep(int steps) {}

    public Map<String, List<String>> getScopes() {
        return Collections.emptyMap();
    }

    public List<String> getScopeForSig(String sigName) {
        return null;
    }

    public void setParsingConf(ParsingConf conf) {}

    public boolean performUntil(int limit) {
        return true;
    }

    public String getStateString(int identifier) {
        return "(debug) no state available";
    }
}
