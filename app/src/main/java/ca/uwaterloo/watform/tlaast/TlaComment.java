package ca.uwaterloo.watform.tlaast;

import ca.uwaterloo.watform.utils.*;

public class TlaComment extends ASTNode {

    /*

    if contents contains a newline, then:
    (* <contents> *)
    else
    \* <contents>

    */

    public final String contents;

    public TlaComment(String contents) {
        this.contents = contents;
    }

    @Override
    public void pp(PrintContext pCtx) {
        // I added this here so my change that makes pp a mandatory
        // method can build successfully. - Jack
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
        sb.append("\n" + core);
        return;
    }
}
