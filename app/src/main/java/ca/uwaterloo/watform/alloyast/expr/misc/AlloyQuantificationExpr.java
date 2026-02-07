package ca.uwaterloo.watform.alloyast.expr.misc;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.nullField;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class AlloyQuantificationExpr extends AlloyExpr {
    public final Quant quant;
    public final List<AlloyDecl> decls;
    public final AlloyExpr body;

    public AlloyQuantificationExpr(
            Pos pos, AlloyQuantificationExpr.Quant quant, List<AlloyDecl> decls, AlloyExpr body) {
        super(pos);
        this.quant = quant;
        this.decls = Collections.unmodifiableList(decls);
        this.body = body;
        reqNonNull(nullField(pos, this), this.quant, this.decls, this.body);
    }

    public AlloyQuantificationExpr(
            AlloyQuantificationExpr.Quant quant, List<AlloyDecl> decls, AlloyExpr body) {
        this(Pos.UNKNOWN, quant, decls, body);
    }

    public enum Quant {
        /** all a,b:x | formula */
        ALL(AlloyStrings.ALL),
        /** no a,b:x | formula */
        NO(AlloyStrings.NO),
        /** lone a,b:x | formula */
        LONE(AlloyStrings.LONE),
        /** one a,b:x | formula */
        ONE(AlloyStrings.ONE),
        /** some a,b:x | formula */
        SOME(AlloyStrings.SOME),
        /** sum a,b:x | intExpression */
        SUM(AlloyStrings.SUM);
        // Alloy has Comprehension here too, but that's made into a separate
        // class here

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
    public void toString(StringBuilder sb, int indent) {
        sb.append(this.quant.toString());
        sb.append(AlloyStrings.SPACE);
        ASTNode.join(sb, indent, this.decls, AlloyStrings.COMMA + AlloyStrings.SPACE);
        sb.append(AlloyStrings.SPACE);
        sb.append(AlloyStrings.BAR);
        sb.append(AlloyStrings.SPACE);
        this.body.toString(sb, indent);
    }

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.append(this.quant.toString() + SPACE);
        if (!this.decls.isEmpty()) {
            pCtx.appendList(this.decls, COMMA);
            pCtx.append(SPACE);
        }
        pCtx.append(BAR);
        pCtx.brk();
        this.body.ppNewBlock(pCtx);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.quant, this.decls, this.body);
    }

    public AlloyQuantificationExpr rebuild(List<AlloyDecl> decls, AlloyExpr body) {
        return new AlloyQuantificationExpr(this.quant, decls, body);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyQuantificationExpr other = (AlloyQuantificationExpr) obj;
        if (quant != other.quant) return false;
        if (decls == null) {
            if (other.decls != null) return false;
        } else if (!decls.equals(other.decls)) return false;
        if (body == null) {
            if (other.body != null) return false;
        } else if (!body.equals(other.body)) return false;
        return true;
    }

    @Override
    public int getPrec() {
        return AlloyExpr.QUANTIFICATION;
    }
}
