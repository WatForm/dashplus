package ca.uwaterloo.watform.utils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.IntConsumer;

public final class Reporter {
    public static final Reporter INSTANCE = new Reporter();

    private final List<ErrorUser> errors = new ArrayList<>();
    private final List<CommentUser> comments = new ArrayList<>();
    private boolean debugMode = false;

    public IntConsumer exitFunction = System::exit; // change this for testing purposes

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void addError(ErrorUser error) {
        errors.add(error);
    }

    public void addComment(CommentUser comment) {
        comments.add(comment);
    }

    public void reset() {
        this.errors.clear();
        this.comments.clear();
        this.exitFunction = System::exit;
    }

    public List<ErrorUser> getErrors() {
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
                System.err.println(comment.toString());
                if (this.debugMode) {
                    comment.printStackTrace();
                }
            }
        }

        if (!this.errors.isEmpty()) {
            for (ErrorUser error : this.errors) {
                System.err.println(error.toString());
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
        this.exitFunction.accept(1);
    }

    public abstract static class DiagnosticException extends RuntimeException {
        private final List<Pos> posList;
        private Optional<Path> filePath;

        public void setFilePath(Path filePath) {
            this.filePath = Optional.ofNullable(filePath);
        }

        public DiagnosticException(List<Pos> posList, Path filePath, String msg) {
            super(msg);
            this.posList = posList;
            this.filePath = Optional.ofNullable(filePath);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(
                    this.getClass().getSimpleName()
                            + ": "
                            + this.getMessage()
                            + CommonStrings.NEWLINE);
            for (Pos pos : this.posList) {
                if (Pos.UNKNOWN == pos) continue;
                sb.append(
                        CommonStrings.TAB
                                + "--> "
                                + (this.filePath.isPresent()
                                        ? this.filePath.get().toString()
                                        : "line")
                                + ":"
                                + pos.rowStart
                                + ":"
                                + pos.colStart
                                + CommonStrings.NEWLINE);
            }
            return sb.toString();
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
}
