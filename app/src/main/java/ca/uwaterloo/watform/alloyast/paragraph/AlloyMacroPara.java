package ca.uwaterloo.watform.alloyast.paragraph;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.utils.*;
import ca.uwaterloo.watform.utils.Pos;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class AlloyMacroPara extends AlloyParagraph {
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
            throw new ImplementationError("block and sub cannot both be null in AlloyMacroPara. ");
        }
        if (this.block.isEmpty() && this.sub.isEmpty()) {
            throw new ImplementationError(
                    "block and sub cannot both be non-null in AlloyMacroPara. ");
        }
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
            throw new ImplementationError("block and sub cannot both be null in AlloyMacroPara. ");
        }
    }

    @Override
    public Optional<String> getName() {
        return Optional.of(this.qname.toString());
    }
}
