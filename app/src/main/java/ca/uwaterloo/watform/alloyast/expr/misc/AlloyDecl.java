package ca.uwaterloo.watform.alloyast.expr.misc;

import ca.uwaterloo.watform.alloyast.AlloyCtorError;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class AlloyDecl extends AlloyExpr {
    public final boolean isVar;
    public final boolean isPrivate;
    public final boolean isDisj1;
    public final List<AlloyQnameExpr> qnames;
    public final boolean isDisj2;
    public final Optional<Quant> quant;
    public final AlloyExpr expr;

    public AlloyDecl(
            Pos pos,
            boolean isVar,
            boolean isPrivate,
            boolean isDisj1,
            List<AlloyQnameExpr> qnames,
            boolean isDisj2,
            AlloyDecl.Quant quant,
            AlloyExpr expr) {
        super(pos);
        this.isVar = isVar;
        this.isPrivate = isPrivate;
        this.isDisj1 = isDisj1;
        this.qnames = Collections.unmodifiableList(qnames);
        this.isDisj2 = isDisj2;
        this.quant = Optional.ofNullable(quant);
        this.expr = expr;
        if (!this.quant.isEmpty() && this.quant.get() == Quant.EXACTLY) {
            if (isVar || isDisj1 || isDisj2) {
                throw AlloyCtorError.declExactlyCannotHaveDisj(pos);
            }
        }
    }

    public AlloyDecl(
            boolean isVar,
            boolean isPrivate,
            boolean isDisj1,
            List<AlloyQnameExpr> qnames,
            boolean isDisj2,
            AlloyDecl.Quant quant,
            AlloyExpr expr) {
        this(Pos.UNKNOWN, isVar, isPrivate, isDisj1, qnames, isDisj2, quant, expr);
    }

    public AlloyDecl(AlloyQnameExpr qname, AlloyExpr expr) {
        this(
                Pos.UNKNOWN,
                false,
                false,
                false,
                Collections.singletonList(qname),
                false,
                Quant.SET,
                expr);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(this.isVar ? AlloyStrings.VAR + AlloyStrings.SPACE : "");
        sb.append(this.isPrivate ? AlloyStrings.PRIVATE + AlloyStrings.SPACE : "");
        sb.append((this.isDisj1 ? AlloyStrings.DISJ + AlloyStrings.SPACE : ""));
        ASTNode.join(sb, indent, this.qnames, AlloyStrings.COMMA + AlloyStrings.SPACE);
        if (!this.quant.isEmpty() && this.quant.get() == Quant.EXACTLY) {
            sb.append(AlloyStrings.EQUAL);
        } else {
            sb.append(AlloyStrings.COLON);
            sb.append((this.isDisj2 ? AlloyStrings.DISJ + AlloyStrings.SPACE : ""));
            sb.append(this.quant.map(q -> q.toString() + AlloyStrings.SPACE).orElse(""));
        }
        this.expr.toString(sb, indent);
    }

    public AlloyDecl withExpr(AlloyExpr newExpr) {
        return new AlloyDecl(
                pos, isVar, isPrivate, isDisj1, qnames, isDisj2, quant.orElse(null), newExpr);
    }

    public enum Quant {
        LONE(AlloyStrings.LONE),
        ONE(AlloyStrings.ONE),
        SOME(AlloyStrings.SOME),
        SET(AlloyStrings.SET),
        EXACTLY(AlloyStrings.EXACTLY);

        public final String label;

        private Quant(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public final String toString() {
            return label;
        }
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
