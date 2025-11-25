package ca.uwaterloo.watform.tlaplusast;

import ca.uwaterloo.watform.utils.ASTNode;

public class TLAPlusComment extends ASTNode {

    public final String contents;

    public TLAPlusComment(String contents) {
        this.contents = contents;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        String core = "";
        if (this.contents.contains("\n"))
            core =
                    TLAPlusStrings.MULTI_COMMENT_START
                            + TLAPlusStrings.SPACE
                            + this.contents
                            + TLAPlusStrings.SPACE
                            + TLAPlusStrings.MULTI_COMMENT_END;
        else core = TLAPlusStrings.SINGLE_COMMENT + TLAPlusStrings.SPACE + this.contents;
        sb.append(core);
        return;
    }
}
