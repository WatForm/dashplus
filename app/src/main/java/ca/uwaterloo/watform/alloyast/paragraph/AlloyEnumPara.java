package ca.uwaterloo.watform.alloyast.paragraph;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.utils.ASTNode;
import ca.uwaterloo.watform.utils.Pos;
import ca.uwaterloo.watform.utils.PrintContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class AlloyEnumPara extends AlloyPara {
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

    public AlloyEnumPara(AlloyQnameExpr qname, List<AlloyQnameExpr> qnames) {
        this(false, qname, qnames);
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

    @Override
    public void pp(PrintContext pCtx) {
        if (isPrivate) {
            pCtx.append(PRIVATE + SPACE);
        }
        pCtx.append(ENUM + SPACE);
        this.qname.pp(pCtx);
        pCtx.append(SPACE + LBRACE);
        pCtx.brkNoSpace();
        pCtx.appendList(this.qnames, COMMA);
        pCtx.brkNoSpaceNoIndent();
        pCtx.append(RBRACE);
    }

    @Override
    public Optional<String> getName() {
        return Optional.of(this.qname.toString());
    }
}
