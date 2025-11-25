package ca.uwaterloo.watform.utils;

import ca.uwaterloo.watform.alloyast.AlloyCtorError;
import ca.uwaterloo.watform.alloyinterface.AlloyInterfaceError;
import ca.uwaterloo.watform.alloymodel.AlloyModelError;
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

    public IntConsumer exitFunction = System::exit; // change this for testing purposes

    private Reporter() {}

    public void addError(AlloyModelError alloyModelError, Path filePath) {
        errors.add(new ErrorUser((DashplusError) alloyModelError, filePath));
    }

    public void addError(AlloyCtorError alloyCtorError, Path filePath) {
        errors.add(new ErrorUser((DashplusError) alloyCtorError, filePath));
    }

    public void addError(AlloyInterfaceError alloyInterfaceError, Path filePath) {
        errors.add(new ErrorUser((DashplusError) alloyInterfaceError, filePath));
    }

    public void addError(ErrorUser error, Path filePath) {
        errors.add(error);
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
            }
        }

        if (!this.errors.isEmpty()) {
            for (ErrorUser error : this.errors) {
                System.err.println(error.toString());
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

    public abstract static class DiagnosticException extends DashplusError {
        public Optional<Path> filePath;

        public DiagnosticException(Pos pos, String message, Path filePath) {
            super(pos, message);
            this.filePath = Optional.ofNullable(filePath);
        }

        public DiagnosticException(String message, Path filePath) {
            super(message);
            this.filePath = Optional.ofNullable(filePath);
        }

        public DiagnosticException(DashplusError other, Path filePath) {
            super(other);
            this.filePath = Optional.ofNullable(filePath);
        }

        @Override
        protected String toStringPosList() {
            StringBuilder sb = new StringBuilder();
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

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.toStringMsg());
            sb.append(this.toStringPosList());
            return sb.toString();
        }
    }

    public static class CommentUser extends DiagnosticException {
        public CommentUser(String msg) {
            super(msg, null);
        }

        public CommentUser(Pos pos, String msg) {
            super(pos, msg, null);
        }

        public CommentUser(Pos pos, String msg, Path filePath) {
            super(pos, msg, filePath);
        }

        public CommentUser(String msg, Path filePath) {
            super(msg, filePath);
        }

        public CommentUser(DashplusError other, Path filePath) {
            super(other, filePath);
        }
    }

    public static class ErrorUser extends DiagnosticException {
        public ErrorUser(String msg) {
            super(msg, null);
        }

        public ErrorUser(Pos pos, String msg) {
            super(pos, msg, null);
        }

        public ErrorUser(Pos pos, String msg, Path filePath) {
            super(pos, msg, filePath);
        }

        public ErrorUser(String msg, Path filePath) {
            super(msg, filePath);
        }

        public ErrorUser(DashplusError other, Path filePath) {
            super(other, filePath);
        }
    }
}
