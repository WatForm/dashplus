package ca.uwaterloo.watform.alloyast.paragraph;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyStrLiteralExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.Optional;

public final class AlloyAssertPara extends AlloyParagraph {
    public final AlloyBlock block;
    // mutually exclusive fields
    public final Optional<AlloyQnameExpr> qname;
    public final Optional<AlloyStrLiteralExpr> strLit;

    private AlloyAssertPara(
            Pos pos, AlloyQnameExpr qname, AlloyStrLiteralExpr strLit, AlloyBlock block) {
        super(pos);
        this.qname = Optional.ofNullable(qname);
        this.strLit = Optional.ofNullable(strLit);
        this.block = block;
    }

    public AlloyAssertPara(Pos pos, AlloyQnameExpr qname, AlloyBlock block) {
        this(pos, qname, null, block);
    }

    public AlloyAssertPara(AlloyQnameExpr qname, AlloyBlock block) {
        this(Pos.UNKNOWN, qname, null, block);
    }

    public AlloyAssertPara(Pos pos, AlloyStrLiteralExpr strLit, AlloyBlock block) {
        this(pos, null, strLit, block);
    }

    public AlloyAssertPara(AlloyStrLiteralExpr strLit, AlloyBlock block) {
        this(Pos.UNKNOWN, null, strLit, block);
    }

    public AlloyAssertPara(Pos pos, AlloyBlock block) {
        this(pos, null, null, block);
    }

    public AlloyAssertPara(AlloyBlock block) {
        this(Pos.UNKNOWN, null, null, block);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        String assertionName = "";
        if (!this.qname.isEmpty()) {
            assertionName = this.qname.get().toString() + " ";
        } else if (!this.strLit.isEmpty()) {
            assertionName = this.strLit.get().toString() + " ";
        }
        sb.append(AlloyStrings.ASSERT + AlloyStrings.SPACE + assertionName);
        this.block.toString(sb, indent);
    }
}
