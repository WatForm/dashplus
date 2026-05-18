package ca.uwaterloo.watform.utils;

import java.nio.file.Path;
import java.util.*;
import java.util.Collections;

public abstract class DashPlusException extends RuntimeException {
    public final List<Pos> posList;

    public DashPlusException(String msg) {
        this(Collections.unmodifiableList(Collections.emptyList()), msg);
    }

    public DashPlusException(Pos pos, String msg) {
        this(Collections.singletonList(pos), msg);
    }

    public DashPlusException(List<Pos> posList, String msg) {
        super(msg);
        this.posList = posList;
    }

    // these could go to the Reporter if they are a user error
    public String toString(Path filePath) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                this.getClass().getSimpleName() + ": " + this.getMessage() + CommonStrings.NEWLINE);
        for (Pos pos : this.posList) {
            if (Pos.UNKNOWN == pos) continue;
            sb.append(
                    CommonStrings.TAB
                            + "--> "
                            + (null != filePath ? filePath.toString() : "line")
                            + ":"
                            + pos.rowStart
                            + ":"
                            + pos.colStart
                            + CommonStrings.NEWLINE);
        }
        return sb.toString();
    }
}
