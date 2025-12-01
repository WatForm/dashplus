package ca.uwaterloo.watform.tlaplusast;

import ca.uwaterloo.watform.utils.ASTNode;

public class TlaComment extends ASTNode {

    public final String contents;

    public TlaComment(String contents) {
        this.contents = contents;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        String core = "";
        if (this.contents.contains("\n"))
            core =
                    TlaStrings.MULTI_COMMENT_START
                            + TlaStrings.SPACE
                            + this.contents
                            + TlaStrings.SPACE
                            + TlaStrings.MULTI_COMMENT_END;
        else core = TlaStrings.SINGLE_COMMENT + TlaStrings.SPACE + this.contents;
        sb.append(core);
        return;
    }
}
