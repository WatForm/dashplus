package ca.uwaterloo.watform.utils;

import java.util.*;

public class UserOrImplError extends DashPlusException {

    public UserOrImplError(String msg) {
        super(msg);
    }

    public UserOrImplError(Pos pos, String msg) {
        super(pos, msg);
    }

    public UserOrImplError(List<Pos> posList, String msg) {
        super(posList, msg);
    }
}
