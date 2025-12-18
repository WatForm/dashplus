package ca.uwaterloo.watform.alloyast.paragraph;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;
import static ca.uwaterloo.watform.utils.ImplementationError.nullField;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyStrLiteralExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.Optional;

public final class AlloyAssertPara extends AlloyPara {
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
        reqNonNull(nullField(pos, this), this.block, this.qname, this.strLit);
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

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.append(ASSERT + SPACE);
        if (this.qname.isPresent()) {
            this.qname.get().pp(pCtx);
            pCtx.append(SPACE);
        } else if (this.strLit.isPresent()) {
            this.strLit.get().pp(pCtx);
            pCtx.append(SPACE);
        }
        this.block.pp(pCtx);
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
}
