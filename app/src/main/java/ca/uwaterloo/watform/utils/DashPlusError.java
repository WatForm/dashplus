package ca.uwaterloo.watform.utils;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// Extensions of this class are errors that can either be Reporter.Error or
// Implementation Error, depending on where these errors occur. See
// ErrorHandling.md for more This class is primarily for the convenience of
// keeping a List<Pos>
public abstract class DashPlusError extends RuntimeException {
    public final List<Pos> posList;
    public Optional<Path> filePath;

    public void setFilePath(Path filePath) {
        this.filePath = Optional.ofNullable(filePath);
    }

    public DashPlusError(List<Pos> posList, Path filePath, String msg) {
        super(msg);
        this.filePath = Optional.ofNullable(filePath);
        this.posList = posList;
    }

    public DashPlusError(List<Pos> posList, String msg) {
        this(posList, null, msg);
    }

    public DashPlusError(Pos pos, String msg) {
        this(Collections.singletonList(pos), null, msg);
    }

    public DashPlusError(String msg) {
        this(Collections.unmodifiableList(Collections.emptyList()), null, msg);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(
                this.getClass().getSimpleName() + ": " + this.getMessage() + CommonStrings.NEWLINE);
        for (Pos pos : this.posList) {
            if (Pos.UNKNOWN == pos) continue;
            sb.append(
                    CommonStrings.TAB
                            + "--> "
                            + (this.filePath.isPresent() ? this.filePath.get().toString() : "line")
                            + ":"
                            + pos.rowStart
                            + ":"
                            + pos.colStart
                            + CommonStrings.NEWLINE);
        }
        return sb.toString();
    }
}
