package ca.uwaterloo.watform.debugcli;

import java.util.ArrayList;
import java.util.List;

public class ConstraintManager {
    private final List<String> constraints = new ArrayList<>();

    public void addConstraint(String constraint) {
        constraints.add(constraint);
    }

    public boolean removeConstraint(int index) {
        if (index < 0 || index >= constraints.size()) {
            return false;
        }
        constraints.remove(index);
        return true;
    }

    public String getFormattedConstraints() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < constraints.size(); i++) {
            sb.append(i).append(": ").append(constraints.get(i)).append("\n");
        }
        return sb.toString();
    }

    public void clearConstraints() {
        constraints.clear();
    }
}
