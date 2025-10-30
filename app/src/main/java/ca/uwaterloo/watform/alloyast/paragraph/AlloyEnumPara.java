package ca.uwaterloo.watform.alloyast.paragraph;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.utils.ASTNode;
import ca.uwaterloo.watform.utils.Pos;
import java.util.Collections;
import java.util.List;

// enumPara        : PRIVATE? ENUM qname LBRACE qnames RBRACE;
public final class AlloyEnumPara extends AlloyParagraph {
    public final boolean isPrivate;
    public final AlloyQnameExpr qname;
    public final List<AlloyQnameExpr> qnames;

    public AlloyEnumPara(
            Pos pos, boolean isPrivate, AlloyQnameExpr qname, List<AlloyQnameExpr> qnames) {
        super(pos);
        this.isPrivate = isPrivate;
        this.qname = qname;
        this.qnames = Collections.unmodifiableList(qnames);
    }

    public AlloyEnumPara(boolean isPrivate, AlloyQnameExpr qname, List<AlloyQnameExpr> qnames) {
        super();
        this.isPrivate = isPrivate;
        this.qname = qname;
        this.qnames = Collections.unmodifiableList(qnames);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(isPrivate ? AlloyStrings.PRIVATE + AlloyStrings.SPACE : "");
        sb.append(AlloyStrings.ENUM + AlloyStrings.SPACE);
        this.qname.toString(sb, indent);
        sb.append(AlloyStrings.SPACE + AlloyStrings.LBRACE);
        ASTNode.join(sb, indent, this.qnames, AlloyStrings.COMMA + AlloyStrings.SPACE);
        sb.append(AlloyStrings.RBRACE);
    }
}
