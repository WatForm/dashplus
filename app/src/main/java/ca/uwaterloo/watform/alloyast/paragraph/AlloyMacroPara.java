package ca.uwaterloo.watform.alloyast.paragraph;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;
import static ca.uwaterloo.watform.utils.ImplementationError.nullField;

import ca.uwaterloo.watform.alloyast.AlloyASTImplError;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class AlloyMacroPara extends AlloyPara {
    public final boolean isPrivate;
    public final AlloyQnameExpr qname;
    public final List<AlloyQnameExpr> qnames;
    // mutually exclusive fields
    public final Optional<AlloyBlock> block;
    public final Optional<AlloyExpr> sub;

    private AlloyMacroPara(
            Pos pos,
            boolean isPrivate,
            AlloyQnameExpr qname,
            List<AlloyQnameExpr> qnames,
            AlloyBlock block,
            AlloyExpr sub) {
        super(pos);
        this.isPrivate = isPrivate;
        this.qname = qname;
        this.qnames = Collections.unmodifiableList(qnames);
        this.block = Optional.ofNullable(block);
        this.sub = Optional.ofNullable(sub);
        if (!this.block.isEmpty() && !this.sub.isEmpty()) {
            throw AlloyASTImplError.xorFields(pos, "block", "sub", "AlloyMacroPara");
        }
        if (this.block.isEmpty() && this.sub.isEmpty()) {
            throw AlloyASTImplError.xorFields(pos, "block", "sub", "AlloyMacroPara");
        }
        reqNonNull(nullField(pos, this), this.qname, this.qnames, this.block, this.sub);
    }

    public AlloyMacroPara(
            Pos pos,
            boolean isPrivate,
            AlloyQnameExpr qname,
            List<AlloyQnameExpr> qnames,
            AlloyBlock block) {
        this(pos, isPrivate, qname, qnames, block, null);
    }

    public AlloyMacroPara(
            boolean isPrivate,
            AlloyQnameExpr qname,
            List<AlloyQnameExpr> qnames,
            AlloyBlock block) {
        this(Pos.UNKNOWN, isPrivate, qname, qnames, block, null);
    }

    public AlloyMacroPara(
            Pos pos,
            boolean isPrivate,
            AlloyQnameExpr qname,
            List<AlloyQnameExpr> qnames,
            AlloyExpr sub) {
        this(pos, isPrivate, qname, qnames, null, sub);
    }

    public AlloyMacroPara(
            boolean isPrivate, AlloyQnameExpr qname, List<AlloyQnameExpr> qnames, AlloyExpr sub) {
        this(Pos.UNKNOWN, isPrivate, qname, qnames, null, sub);
    }

    /*
     * Always use square brackets around arguments
     */
    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(this.isPrivate ? AlloyStrings.PRIVATE + AlloyStrings.SPACE : "");
        sb.append(AlloyStrings.LET + AlloyStrings.SPACE);
        this.qname.toString(sb, indent);

        if (!this.qnames.isEmpty()) {
            sb.append(AlloyStrings.LBRACK);
            ASTNode.join(sb, indent, this.qnames, AlloyStrings.COMMA + AlloyStrings.SPACE);
            sb.append(AlloyStrings.RBRACK);
        }

        sb.append(AlloyStrings.SPACE);

        if (!this.block.isEmpty()) {
            this.block.get().toString(sb, indent);
        } else if (!this.sub.isEmpty()) {
            sb.append(AlloyStrings.EQUAL);
            sb.append(AlloyStrings.SPACE);
            this.sub.get().toString(sb, indent);
        } else {
            throw AlloyASTImplError.xorFields(pos, "block", "sub", "AlloyMacroPara");
        }
    }

    @Override
    public void pp(PrintContext pCtx) {
        if (isPrivate) {
            pCtx.append(PRIVATE + SPACE);
        }
        pCtx.append(LET + SPACE);
        qname.pp(pCtx);
        pCtx.append(SPACE);
        if (!qnames.isEmpty()) {
            pCtx.append(LBRACK);
            pCtx.brkNoSpace();
            pCtx.appendList(qnames, COMMA);
            pCtx.brkNoSpaceNoIndent();
            pCtx.append(RBRACK + SPACE);
        }
        if (this.block.isPresent()) {
            this.block.get().pp(pCtx);
        } else if (this.sub.isPresent()) {
            pCtx.append(EQUAL);
            pCtx.brk();
            this.sub.get().pp(pCtx);
        } else {
            throw AlloyASTImplError.xorFields(pos, "block", "sub", "AlloyMacroPara");
        }
    }

    @Override
    public Optional<String> getName() {
        return Optional.of(this.qname.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.isPrivate, this.qname, this.qnames, this.block, this.sub);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyMacroPara other = (AlloyMacroPara) obj;
        if (isPrivate != other.isPrivate) return false;
        if (qname == null) {
            if (other.qname != null) return false;
        } else if (!qname.equals(other.qname)) return false;
        if (qnames == null) {
            if (other.qnames != null) return false;
        } else if (!qnames.equals(other.qnames)) return false;
        if (block == null) {
            if (other.block != null) return false;
        } else if (!block.equals(other.block)) return false;
        if (sub == null) {
            if (other.sub != null) return false;
        } else if (!sub.equals(other.sub)) return false;
        return true;
    }
}
