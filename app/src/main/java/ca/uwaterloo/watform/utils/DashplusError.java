package ca.uwaterloo.watform.utils;

import java.util.Collections;
import java.util.List;

public abstract class DashplusError extends RuntimeException {
    public final List<Pos> posList;

    protected DashplusError(String msg) {
        super(msg);
        this.posList = Collections.unmodifiableList(Collections.singletonList(Pos.UNKNOWN));
    }

    protected DashplusError(Pos pos, String msg) {
        super(msg);
        this.posList = Collections.unmodifiableList(Collections.singletonList(pos));
    }

    protected DashplusError(List<Pos> posList, String msg) {
        super(msg);
        this.posList = posList;
    }

    protected DashplusError(DashplusError other) {
        this(other.posList, other.getMessage());
    }

    protected String toStringMsg() {
        return this.getClass().getSimpleName() + ": " + this.getMessage() + CommonStrings.NEWLINE;
    }

    protected String toStringPosList() {
        StringBuilder sb = new StringBuilder();
        for (Pos pos : this.posList) {
            if (Pos.UNKNOWN == pos) continue;
            sb.append(
                    CommonStrings.TAB
                            + "--> "
                            + pos.rowStart
                            + ":"
                            + pos.colStart
                            + CommonStrings.NEWLINE);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.toStringMsg());
        sb.append(this.toStringPosList());
        return sb.toString();
    }
}
