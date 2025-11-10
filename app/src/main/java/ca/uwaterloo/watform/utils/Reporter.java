package ca.uwaterloo.watform.utils;

import ca.uwaterloo.watform.alloyast.AlloyCtorError;
import ca.uwaterloo.watform.alloymodel.AlloyModelError;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntConsumer;

// todo for Jack: better formatting
public final class Reporter {
    public static final Reporter INSTANCE = new Reporter();

    public final List<ErrorUser> errors = new ArrayList<>();
    public final List<CommentUser> comments = new ArrayList<>();

    public IntConsumer exitFunction = System::exit; // change this for testing purposes

    private Reporter() {}

    public void addError(AlloyModelError alloyModelErrors) {
        errors.add(new ErrorUser(alloyModelErrors.pos, alloyModelErrors.getMessage()));
    }

    public void addError(AlloyCtorError alloyCtorErrors) {
        errors.add(new ErrorUser(alloyCtorErrors.pos, alloyCtorErrors.getMessage()));
    }

    public void addError(ErrorUser error) {
        errors.add(error);
    }

    public void addError(Pos pos, String msg) {
        errors.add(new ErrorUser(pos, msg));
    }

    public void addError(String msg) {
        errors.add(new ErrorUser(msg));
    }

    public void addComment(CommentUser warning) {
        comments.add(warning);
    }

    public void addComment(Pos pos, String msg) {
        comments.add(new CommentUser(pos, msg));
    }

    public void addComment(String msg) {
        comments.add(new CommentUser(msg));
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
            System.err.println("\nComments:");
            for (CommentUser comment : this.comments) {
                System.err.printf("[%s] %s%n", comment.pos.toString(), comment.getMessage());
            }
        }

        System.err.println("\nErrors:");
        for (ErrorUser error : this.errors) {
            System.err.printf("[%s] %s%n", error.pos.toString(), error.getMessage());
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
        public final Pos pos;

        public DiagnosticException(Pos pos, String message) {
            super(message);
            this.pos = pos;
        }

        public DiagnosticException(String message) {
            this(Pos.UNKNOWN, message);
        }
    }

    public static class CommentUser extends DiagnosticException {
        public CommentUser(Pos pos, String msg) {
            super(pos, msg);
        }

        public CommentUser(String msg) {
            super(msg);
        }
    }

    public static class ErrorUser extends DiagnosticException {
        public ErrorUser(Pos pos, String msg) {
            super(pos, msg);
        }

        public ErrorUser(String msg) {
            super(msg);
        }
    }
}
