package ca.uwaterloo.watform.alloyast.expr.misc;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.*;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class AlloyLetExpr extends AlloyExpr {
    public final List<AlloyLetAsn> asns;
    public final AlloyExpr body;

    public AlloyLetExpr(Pos pos, List<AlloyLetAsn> asns, AlloyExpr body) {
        super(pos);
        this.asns = Collections.unmodifiableList(asns);
        this.body = body;
        reqNonNull(nullField(pos, this), this.asns, this.body);
    }

    public AlloyLetExpr(List<AlloyLetAsn> asns, AlloyExpr body) {
        this(Pos.UNKNOWN, asns, body);
    }

    public AlloyLetExpr(Pos pos, AlloyLetAsn asn, AlloyExpr body) {
        this(pos, Collections.singletonList(asn), body);
    }

    public AlloyLetExpr(AlloyLetAsn asn, AlloyExpr body) {
        this(Pos.UNKNOWN, Collections.singletonList(asn), body);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(AlloyStrings.LET);
        sb.append(AlloyStrings.SPACE);
        ASTNode.join(sb, indent, this.asns, AlloyStrings.COMMA + AlloyStrings.SPACE);
        sb.append(AlloyStrings.SPACE);
        sb.append(AlloyStrings.BAR);
        sb.append(AlloyStrings.SPACE);
        this.body.toString(sb, indent);
    }

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.append(LET + SPACE);
        pCtx.appendList(this.asns, COMMA);
        pCtx.append(SPACE + BAR);
        pCtx.brk();
        pCtx.appendChild(this, this.body, true);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    public AlloyLetExpr rebuild(AlloyExpr body) {
        return new AlloyLetExpr(this.pos, this.asns, body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.asns, this.body);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyLetExpr other = (AlloyLetExpr) obj;
        if (asns == null) {
            if (other.asns != null) return false;
        } else if (!asns.equals(other.asns)) return false;
        if (body == null) {
            if (other.body != null) return false;
        } else if (!body.equals(other.body)) return false;
        return true;
    }

    @Override
    public int getPrec() {
        return AlloyExpr.LET;
    }

    public static final class AlloyLetAsn extends AlloyASTNode {
        public final AlloyQnameExpr qname;
        public final AlloyExpr expr;

        public AlloyLetAsn(Pos pos, AlloyQnameExpr qname, AlloyExpr expr) {
            super(pos);
            this.qname = qname;
            this.expr = expr;
            reqNonNull(nullField(pos, this), this.qname, this.expr);
        }

        public AlloyLetAsn(AlloyQnameExpr qname, AlloyExpr expr) {
            this(Pos.UNKNOWN, qname, expr);
        }

        public AlloyQnameExpr getName() {
            return this.qname;
        }

        public AlloyExpr getExpr() {
            return expr;
        }

        @Override
        public void toString(StringBuilder sb, int indent) {
            sb.append(this.qname.toString());
            sb.append(AlloyStrings.EQUAL);
            expr.toString(sb, indent);
        }

        @Override
        public void pp(PrintContext pCtx) {
            this.qname.pp(pCtx);
            pCtx.append(SPACE + EQUAL);
            pCtx.brk();
            this.expr.pp(pCtx);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.qname, this.expr);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            AlloyLetAsn other = (AlloyLetAsn) obj;
            if (qname == null) {
                if (other.qname != null) return false;
            } else if (!qname.equals(other.qname)) return false;
            if (expr == null) {
                if (other.expr != null) return false;
            } else if (!expr.equals(other.expr)) return false;
            return true;
        }
    }
}
