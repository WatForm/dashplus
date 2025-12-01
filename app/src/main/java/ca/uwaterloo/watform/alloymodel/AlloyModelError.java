package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.utils.*;
import java.util.List;

public final class AlloyModelError extends DashPlusError {
    private AlloyModelError(List<Pos> posList, String msg) {
        super(posList, msg);
    }

    private AlloyModelError(Pos pos, String msg) {
        super(pos, msg);
    }

    private AlloyModelError(String msg) {
        super(msg);
    }

    public static AlloyModelError duplicateName(Pos pos1, Pos pos2) {
        return new AlloyModelError(List.of(pos1, pos2), "A duplicated name is found. ");
    }

    public static AlloyModelError paragraphDNE(String name) {
        return new AlloyModelError("The paragraph with " + name + " does not exist. ");
    }
}
