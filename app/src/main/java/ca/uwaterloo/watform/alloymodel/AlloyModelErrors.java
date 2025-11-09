package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.utils.*;

public final class AlloyModelErrors extends Reporter.ErrorUser {
    public AlloyModelErrors(Pos pos, String msg) {
        super(pos, msg);
    }

    public AlloyModelErrors(String msg) {
        this(Pos.UNKNOWN, msg);
    }

    public static AlloyModelErrors duplicateName(Pos pos1, Pos pos2) {
        return new AlloyModelErrors(
                pos1, "A duplicated name is found: " + pos1.toString() + ", " + pos2.toString());
    }

    public static AlloyModelErrors paragraphDNE(String name) {
        return new AlloyModelErrors("The paragraph with " + name + " does not exist. ");
    }
}
