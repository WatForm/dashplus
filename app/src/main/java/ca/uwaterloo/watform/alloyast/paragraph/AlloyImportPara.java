package ca.uwaterloo.watform.alloyast.paragraph;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;
import static ca.uwaterloo.watform.utils.ImplementationError.nullField;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloySigRefExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class AlloyImportPara extends AlloyPara {
    public final boolean isPrivate;
    public final AlloyQnameExpr qname;
    public final List<AlloySigRefExpr> sigRefs;
    public final Optional<AlloyQnameExpr> asQname;

    public AlloyImportPara(
            Pos pos,
            boolean isPrivate,
            AlloyQnameExpr qname,
            List<AlloySigRefExpr> sigRefs,
            AlloyQnameExpr asQname) {
        super(pos);
        this.isPrivate = isPrivate;
        this.qname = qname;
        this.sigRefs = Collections.unmodifiableList(sigRefs);
        this.asQname = Optional.ofNullable(asQname);
        reqNonNull(nullField(pos, this), this.qname, this.sigRefs, this.asQname);
    }

    public AlloyImportPara(
            boolean isPrivate,
            AlloyQnameExpr qname,
            List<AlloySigRefExpr> sigRefs,
            AlloyQnameExpr asQname) {
        this(Pos.UNKNOWN, isPrivate, qname, sigRefs, asQname);
    }

    /*
     * If no sigRefs, then don't print []
     */
    @Override
    public void toString(StringBuilder sb, int indent) {
        if (isPrivate) {
            sb.append(AlloyStrings.PRIVATE);
            sb.append(AlloyStrings.SPACE);
        }
        sb.append(AlloyStrings.OPEN);
        sb.append(AlloyStrings.SPACE);
        this.qname.toString(sb, indent);
        if (!sigRefs.isEmpty()) {
            sb.append(AlloyStrings.LBRACK);
            ASTNode.join(sb, indent, this.sigRefs, AlloyStrings.COMMA + AlloyStrings.SPACE);
            sb.append(AlloyStrings.RBRACK);
        }
        if (!this.asQname.isEmpty()) {
            sb.append(AlloyStrings.SPACE);
            sb.append(AlloyStrings.AS);
            sb.append(AlloyStrings.SPACE);
            this.asQname.get().toString(sb, indent);
        }
    }

    @Override
    public void pp(PrintContext pCtx) {
        if (isPrivate) {
            pCtx.append(PRIVATE + SPACE);
        }
        pCtx.append(OPEN + SPACE);
        qname.pp(pCtx);
        pCtx.append(SPACE);
        if (!sigRefs.isEmpty()) {
            pCtx.append(LBRACK);
            pCtx.brkNoSpace();
            pCtx.appendList(sigRefs, COMMA);
            pCtx.brkNoSpaceNoIndent();
            pCtx.append(RBRACK + SPACE);
        }
        if (this.asQname.isPresent()) {
            pCtx.append(AS + SPACE);
            this.asQname.get().pp(pCtx);
        }
    }

    /*
     * The name of a import should include the arguments;
     * open util/ordering[Time] as to
     * open util/ordering[Key] as ko
     * are not the same
     */
    @Override
    public Optional<String> getName() {
        StringBuilder sb = new StringBuilder();
        this.qname.toString(sb, 0);
        if (!sigRefs.isEmpty()) {
            sb.append(AlloyStrings.LBRACK);
            ASTNode.join(sb, 0, this.sigRefs, AlloyStrings.COMMA + AlloyStrings.SPACE);
            sb.append(AlloyStrings.RBRACK);
        }

        return Optional.of(sb.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.isPrivate, this.qname, this.sigRefs, this.asQname);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyImportPara other = (AlloyImportPara) obj;
        if (isPrivate != other.isPrivate) return false;
        if (qname == null) {
            if (other.qname != null) return false;
        } else if (!qname.equals(other.qname)) return false;
        if (sigRefs == null) {
            if (other.sigRefs != null) return false;
        } else if (!sigRefs.equals(other.sigRefs)) return false;
        if (asQname == null) {
            if (other.asQname != null) return false;
        } else if (!asQname.equals(other.asQname)) return false;
        return true;
    }
}
