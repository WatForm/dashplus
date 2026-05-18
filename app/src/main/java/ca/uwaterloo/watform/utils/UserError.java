package ca.uwaterloo.watform.utils;

import java.util.*;

public class UserError extends DashPlusException {

    public UserError(String msg) {
        super(msg);
    }

    public UserError(Pos pos, String msg) {
        super(pos, msg);
    }

    public UserError(List<Pos> posList, String msg) {
        super(posList, msg);
    }
}
