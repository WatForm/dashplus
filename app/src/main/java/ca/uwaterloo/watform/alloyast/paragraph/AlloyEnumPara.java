package ca.uwaterloo.watform.alloyast.paragraph;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;
import static ca.uwaterloo.watform.utils.ImplementationError.nullField;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.utils.ASTNode;
import ca.uwaterloo.watform.utils.Pos;
import ca.uwaterloo.watform.utils.PrintContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
        reqNonNull(nullField(pos, this), this.qname, this.qnames);
    }

    public AlloyEnumPara(boolean isPrivate, AlloyQnameExpr qname, List<AlloyQnameExpr> qnames) {
        this(Pos.UNKNOWN, isPrivate, qname, qnames);
    }

    public AlloyEnumPara(AlloyQnameExpr qname, List<AlloyQnameExpr> qnames) {
        this(Pos.UNKNOWN, false, qname, qnames);
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
    public AlloyId getId() {
        return new AlloyId(qname.toString());
    }

    public List<AlloyId> getAllIds() {
        List<AlloyId> li = new ArrayList<>();
        li.add(getId());
        for (AlloyQnameExpr qname : qnames) {
            li.add(new AlloyId(qname.toString()));
        }
        return li;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.isPrivate, this.qname, this.qnames);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyEnumPara other = (AlloyEnumPara) obj;
        if (isPrivate != other.isPrivate) return false;
        if (qname == null) {
            if (other.qname != null) return false;
        } else if (!qname.equals(other.qname)) return false;
        if (qnames == null) {
            if (other.qnames != null) return false;
        } else if (!qnames.equals(other.qnames)) return false;
        return true;
    }
}
