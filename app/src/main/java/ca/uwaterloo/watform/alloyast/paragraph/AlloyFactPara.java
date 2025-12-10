package ca.uwaterloo.watform.alloyast.paragraph;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyStrLiteralExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.Optional;

public final class AlloyFactPara extends AlloyPara {
    public final AlloyBlock block;
    // mutually exclusive fields
    public final Optional<AlloyQnameExpr> qname;
    public final Optional<AlloyStrLiteralExpr> strLit;

    private AlloyFactPara(
            Pos pos, AlloyQnameExpr qname, AlloyStrLiteralExpr strLit, AlloyBlock block) {
        super(pos);
        this.qname = Optional.ofNullable(qname);
        this.strLit = Optional.ofNullable(strLit);
        this.block = block;
    }

    public AlloyFactPara(Pos pos, AlloyQnameExpr qname, AlloyBlock block) {
        this(pos, qname, null, block);
    }

    public AlloyFactPara(AlloyQnameExpr qname, AlloyBlock block) {
        this(Pos.UNKNOWN, qname, null, block);
    }

    public AlloyFactPara(Pos pos, AlloyStrLiteralExpr strLit, AlloyBlock block) {
        this(pos, null, strLit, block);
    }

    public AlloyFactPara(AlloyStrLiteralExpr strLit, AlloyBlock block) {
        this(Pos.UNKNOWN, null, strLit, block);
    }

    public AlloyFactPara(Pos pos, AlloyBlock block) {
        this(pos, null, null, block);
    }

    public AlloyFactPara(AlloyBlock block) {
        this(Pos.UNKNOWN, null, null, block);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        String factName = "";
        if (!this.qname.isEmpty()) {
            factName = this.qname.get().toString() + " ";
        } else if (!this.strLit.isEmpty()) {
            factName = this.strLit.get().toString() + " ";
        }
        sb.append(AlloyStrings.FACT + AlloyStrings.SPACE + factName);
        this.block.toString(sb, indent);
    }

    @Override
    public Optional<String> getName() {
        if (this.qname.isPresent()) {
            return Optional.of(this.qname.get().toString());
        } else if (this.strLit.isPresent()) {
            return Optional.of(this.strLit.get().toString());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void pp(PrintContext pCtx) {
        String factName = "";
        if (!this.qname.isEmpty()) {
            factName = this.qname.get().toString() + " ";
        } else if (!this.strLit.isEmpty()) {
            factName = this.strLit.get().toString() + " ";
        }

        pCtx.append(FACT + SPACE + factName);
        this.block.pp(pCtx);
    }
}
