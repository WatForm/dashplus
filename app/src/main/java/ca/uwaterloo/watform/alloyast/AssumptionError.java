package ca.uwaterloo.watform.alloyast;

import ca.uwaterloo.watform.utils.*;
import java.util.List;

public class AssumptionError extends UserOrImplError {

    private AssumptionError(String msg) {
        super(msg);
    }

    private AssumptionError(List<Pos> posList, String msg) {
        super(posList, msg);
    }

    private AssumptionError(Pos pos, String msg) {
        super(pos, msg);
    }

    public static AssumptionError cantHaveMultipleCmdDecls(Pos p, String s) {
        return new AssumptionError(p, "Can't have multiple cmds within cmd: " + s);
    }
}
