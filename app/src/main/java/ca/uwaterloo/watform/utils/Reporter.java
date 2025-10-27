package ca.uwaterloo.watform.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Reporter {
    public static final Reporter INSTANCE = new Reporter();

    public final List<ErrorUser> errors = new ArrayList<>();
    public final List<CommentUser> comments = new ArrayList<>();

    private Reporter() {}

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
}
