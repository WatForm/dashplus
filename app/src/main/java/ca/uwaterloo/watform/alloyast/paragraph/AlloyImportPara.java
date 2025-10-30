package ca.uwaterloo.watform.alloyast.paragraph;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloySigRefExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class AlloyImportPara extends AlloyParagraph {
    public final boolean isPrivate;
    public final AlloyQnameExpr qname;
    public final boolean hasBrackets;
    public final List<AlloySigRefExpr> sigRefs;
    public final Optional<AlloyQnameExpr> asQname;

    public AlloyImportPara(
            Pos pos,
            boolean isPrivate,
            AlloyQnameExpr qname,
            boolean hasBrackets,
            List<AlloySigRefExpr> sigRefs,
            AlloyQnameExpr asQname) {
        super(pos);
        this.isPrivate = isPrivate;
        this.qname = qname;
        this.hasBrackets = hasBrackets;
        this.sigRefs = Collections.unmodifiableList(sigRefs);
        this.asQname = Optional.ofNullable(asQname);
    }

    public AlloyImportPara(
            boolean isPrivate,
            AlloyQnameExpr qname,
            boolean hasBrackets,
            List<AlloySigRefExpr> sigRefs,
            AlloyQnameExpr asQname) {
        this(Pos.UNKNOWN, isPrivate, qname, hasBrackets, sigRefs, asQname);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        if (isPrivate) {
            sb.append(AlloyStrings.PRIVATE);
            sb.append(AlloyStrings.SPACE);
        }
        sb.append(AlloyStrings.OPEN);
        sb.append(AlloyStrings.SPACE);
        this.qname.toString(sb, indent);
        if (this.hasBrackets) {
            sb.append(AlloyStrings.LBRACK);
            if (!sigRefs.isEmpty()) {
                ASTNode.join(sb, indent, this.sigRefs, AlloyStrings.COMMA + AlloyStrings.SPACE);
            }
            sb.append(AlloyStrings.RBRACK);
        }
        if (!this.asQname.isEmpty()) {
            sb.append(AlloyStrings.SPACE);
            sb.append(AlloyStrings.AS);
            sb.append(AlloyStrings.SPACE);
            this.asQname.get().toString(sb, indent);
        }
    }
}
