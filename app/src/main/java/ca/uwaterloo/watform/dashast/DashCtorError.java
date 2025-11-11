package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.utils.*;

public class DashCtorError extends RuntimeException {
    public final Pos pos;

    private DashCtorError(Pos pos, String msg) {
        super(msg);
        this.pos = pos;
    }

    private DashCtorError(String msg) {
        this(Pos.UNKNOWN, msg);
    }

    // ====================================================================================
    // DashFile
    // ====================================================================================
    public static DashCtorError exactlyOneStateRoot() {
        return new DashCtorError("A dash file should contain exactly one state root. ");
    }
}
