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
}
