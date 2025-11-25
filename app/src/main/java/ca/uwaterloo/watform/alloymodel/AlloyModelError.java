package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.utils.*;

public final class AlloyModelError extends DashplusError {
    private AlloyModelError(Pos pos, String msg) {
        super(pos, msg);
    }

    private AlloyModelError(String msg) {
        super(msg);
    }

    public static AlloyModelError duplicateName(Pos pos1, Pos pos2) {
        return new AlloyModelError(
                pos1, "A duplicated name is found: " + pos1.toString() + ", " + pos2.toString());
    }

    public static AlloyModelError paragraphDNE(String name) {
        return new AlloyModelError("The paragraph with " + name + " does not exist. ");
    }
}
