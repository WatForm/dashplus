package ca.uwaterloo.watform.utils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Reporter {
    public static final Reporter INSTANCE = new Reporter();
    private Path filePath;

    private final List<DashPlusException> errors = new ArrayList<>();
    private final List<WarningUser> warnings = new ArrayList<>();
    private final List<CommentUser> comments = new ArrayList<>();
    private boolean debugMode = false;

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void addError(DashPlusException error) {
        errors.add(error);
    }

    public void addWarning(WarningUser warning) {
        warnings.add(warning);
    }

    public void addComment(CommentUser comment) {
        comments.add(comment);
    }

    public void reset() {
        this.errors.clear();
        this.warnings.clear();
        this.comments.clear();
        this.filePath = null;
    }

    public List<DashPlusException> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public List<WarningUser> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }

    public List<CommentUser> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    public boolean hasComments() {
        return !comments.isEmpty();
    }

    public void print() {
        if (!comments.isEmpty()) {
            for (CommentUser comment : comments) {
                System.err.println(comment.toString(filePath));
                if (debugMode) {
                    comment.printStackTrace();
                }
            }
        }

        if (!warnings.isEmpty()) {
            for (WarningUser warning : warnings) {
                System.err.println(warning.toString(filePath));
                if (debugMode) {
                    warning.printStackTrace();
                }
            }
        }

        if (!errors.isEmpty()) {
            for (DashPlusException error : errors) {
                System.err.println(error.toString(filePath));
                if (debugMode) {
                    error.printStackTrace();
                }
            }
        }
    }

    public void exitIfHasErrors() {
        if (!this.hasErrors()) {
            return;
        }
        this.print();
        throw new AbortSignal();
    }

    public abstract static class DiagnosticException extends DashPlusException {
        public DiagnosticException(List<Pos> posList, String msg) {
            super(posList, msg);
        }
    }

    public static class CommentUser extends DiagnosticException {
        public CommentUser(List<Pos> posList, String msg) {
            super(posList, msg);
        }

        public CommentUser(Pos pos, String msg) {
            super(Collections.singletonList(pos), msg);
        }
    }

    public static class WarningUser extends DiagnosticException {
        public WarningUser(List<Pos> posList, String msg) {
            super(posList, msg);
        }

        public WarningUser(Pos pos, String msg) {
            super(Collections.singletonList(pos), msg);
        }
    }

    public static class ErrorUser extends DiagnosticException {
        public ErrorUser(List<Pos> posList, String msg) {
            super(posList, msg);
        }

        public ErrorUser(Pos pos, String msg) {
            this(Collections.singletonList(pos), msg);
        }

        public ErrorUser(String msg) {
            this(Collections.emptyList(), msg);
        }
    }

    public final class AbortSignal extends RuntimeException {}
}
