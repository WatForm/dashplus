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

// macroPara       : PRIVATE? LET qname ( LBRACK qnames? RBRACK )? (block | (EQUAL
// expr1)) 				| PRIVATE? LET qname ( LPAREN qnames? RPAREN )? (block |
// (EQUAL expr1))
public final class AlloyMacroPara extends AlloyParagraph {
    public final boolean isPrivate;
    public final AlloyQnameExpr qname;
    public final List<AlloyQnameExpr> qnames;
    // mutually exclusive fields
    public final boolean hasBrack;
    public final boolean hasParen;
    // mutually exclusive fields
    public final Optional<AlloyBlock> block;
    public final Optional<AlloyExpr> sub;

    private AlloyMacroPara(
            Pos pos,
            boolean isPrivate,
            AlloyQnameExpr qname,
            List<AlloyQnameExpr> qnames,
            boolean hasBrack,
            boolean hasParen,
            AlloyBlock block,
            AlloyExpr sub) {
        super(pos);
        this.isPrivate = isPrivate;
        this.qname = qname;
        this.qnames = Collections.unmodifiableList(qnames);
        this.hasBrack = hasBrack;
        this.hasParen = hasParen;
        this.block = Optional.ofNullable(block);
        this.sub = Optional.ofNullable(sub);
        if (hasBrack && hasParen) {
            throw new ImplementationError(
                    "hasBrack and hasParen cannot both be true " + "in AlloyMacroPara. ");
        }
        if (!hasBrack && !hasParen && !qnames.isEmpty()) {
            throw new ImplementationError(
                    "Need to have either hasBrack or hasParen "
                            + "if qnames is not empty in AlloyMacroPara. ");
        }
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
            boolean hasBrack,
            boolean hasParen,
            AlloyBlock block) {
        this(pos, isPrivate, qname, qnames, hasBrack, hasParen, block, null);
    }

    public AlloyMacroPara(
            boolean isPrivate,
            AlloyQnameExpr qname,
            List<AlloyQnameExpr> qnames,
            boolean hasBrack,
            boolean hasParen,
            AlloyBlock block) {
        this(Pos.UNKNOWN, isPrivate, qname, qnames, hasBrack, hasParen, block, null);
    }

    public AlloyMacroPara(
            Pos pos,
            boolean isPrivate,
            AlloyQnameExpr qname,
            List<AlloyQnameExpr> qnames,
            boolean hasBrack,
            boolean hasParen,
            AlloyExpr sub) {
        this(pos, isPrivate, qname, qnames, hasBrack, hasParen, null, sub);
    }

    public AlloyMacroPara(
            boolean isPrivate,
            AlloyQnameExpr qname,
            List<AlloyQnameExpr> qnames,
            boolean hasBrack,
            boolean hasParen,
            AlloyExpr sub) {
        this(Pos.UNKNOWN, isPrivate, qname, qnames, hasBrack, hasParen, null, sub);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(this.isPrivate ? AlloyStrings.PRIVATE + AlloyStrings.SPACE : "");
        sb.append(AlloyStrings.LET + AlloyStrings.SPACE);
        this.qname.toString(sb, indent);

        if (this.hasBrack) {
            sb.append(AlloyStrings.LBRACK);
            ASTNode.join(sb, indent, this.qnames, AlloyStrings.COMMA + AlloyStrings.SPACE);
            sb.append(AlloyStrings.RBRACK);
        } else if (this.hasParen) {
            sb.append(AlloyStrings.LPAREN);
            ASTNode.join(sb, indent, this.qnames, AlloyStrings.COMMA + AlloyStrings.SPACE);
            sb.append(AlloyStrings.RPAREN);
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
}
