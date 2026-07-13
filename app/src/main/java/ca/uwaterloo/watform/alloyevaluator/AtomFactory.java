package ca.uwaterloo.watform.alloyevaluator;

import ca.uwaterloo.watform.alloyevaluator.OverflowAtom.OverflowDirection;
import ca.uwaterloo.watform.utils.Reporter;
import ca.uwaterloo.watform.utils.Reporter.WarningUser;
import java.util.ArrayList;

public class AtomFactory {
    private final int min;
    private final int max;
    private final Reporter reporter;

    public AtomFactory(int min, int max) {
        this.min = min;
        this.max = max;
        this.reporter = Reporter.INSTANCE;
    }

    public Atom createAtom(String label) {
        try {
            return createAtom(Integer.parseInt(label));
        } catch (NumberFormatException e) {
            return new LabelAtom(label);
        }
    }

    public Atom createAtom(int value) {
        if (value > max) return createAtom(OverflowDirection.OVERFLOW_UP);
        if (value < min) return createAtom(OverflowDirection.OVERFLOW_DOWN);
        return new IntegerAtom(value);
    }

    public Atom createAtom(OverflowDirection direction) {
        reporter.addWarning(
                new WarningUser(new ArrayList<>(), "Created overflowing atom: " + direction));
        return new OverflowAtom(direction);
    }
}
