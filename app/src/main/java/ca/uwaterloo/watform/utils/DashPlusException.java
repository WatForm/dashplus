package ca.uwaterloo.watform.utils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

// Extensions of this class are errors that can either be Reporter.Error or
// Implementation Error, depending on where these errors occur. See
// ErrorHandling.md for more This class is primarily for the convenience of
// keeping a List<Pos>
public abstract class DashPlusException extends RuntimeException {
    public final List<Pos> posList;

    public DashPlusException(List<Pos> posList, String msg) {
        super(msg);
        this.posList = posList;
    }

    public DashPlusException(Pos pos, String msg) {
        this(Collections.singletonList(pos), msg);
    }

    public DashPlusException(String msg) {
        this(Collections.unmodifiableList(Collections.emptyList()), msg);
    }

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
