package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.utils.*;

public final class AlloyModelError extends RuntimeException {
    public final Pos pos;

    private AlloyModelError(Pos pos, String msg) {
        super(msg);
        this.pos = pos;
    }

    private AlloyModelError(String msg) {
        this(Pos.UNKNOWN, msg);
    }

    public static AlloyModelError duplicateName(Pos pos1, Pos pos2) {
        return new AlloyModelError(
                pos1, "A duplicated name is found: " + pos1.toString() + ", " + pos2.toString());
    }

    public static AlloyModelError paragraphDNE(String name) {
        return new AlloyModelError("The paragraph with " + name + " does not exist. ");
    }
}
