package ca.uwaterloo.watform.utils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Reporter {
    public static final Reporter INSTANCE = new Reporter();
    private Path filePath;

    private final List<DashPlusError> errors = new ArrayList<>();
    private final List<CommentUser> comments = new ArrayList<>();
    private boolean debugMode = false;

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void addError(DashPlusError error) {
        errors.add(error);
    }

    public void addComment(CommentUser comment) {
        comments.add(comment);
    }

    public void reset() {
        this.errors.clear();
        this.comments.clear();
        this.filePath = null;
    }

    public List<DashPlusError> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public List<CommentUser> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    public boolean hasComments() {
        return !this.comments.isEmpty();
    }

    public void print() {
        if (!this.comments.isEmpty()) {
            for (CommentUser comment : this.comments) {
                System.err.println(comment.toString(filePath));
                if (this.debugMode) {
                    comment.printStackTrace();
                }
            }
        }

        if (!this.errors.isEmpty()) {
            for (DashPlusError error : this.errors) {
                System.err.println(error.toString(filePath));
                if (this.debugMode) {
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

    public abstract static class DiagnosticException extends DashPlusError {
        public DiagnosticException(List<Pos> posList, Path filePath, String msg) {
            super(posList, msg);
        }
    }

    public static class CommentUser extends DiagnosticException {
        public CommentUser(List<Pos> posList, Path filePath, String msg) {
            super(posList, filePath, msg);
        }

        public CommentUser(Pos pos, String msg) {
            super(Collections.singletonList(pos), null, msg);
        }
    }

    public static class ErrorUser extends DiagnosticException {
        public ErrorUser(List<Pos> posList, Path filePath, String msg) {
            super(posList, filePath, msg);
        }

        public ErrorUser(List<Pos> posList, String msg) {
            this(posList, null, msg);
        }

        public ErrorUser(Pos pos, String msg) {
            this(Collections.singletonList(pos), null, msg);
        }

        public ErrorUser(String msg) {
            this(Collections.emptyList(), null, msg);
        }
    }

    public final class AbortSignal extends RuntimeException {}
}
