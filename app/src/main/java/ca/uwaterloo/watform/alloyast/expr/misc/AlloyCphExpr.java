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
import java.util.Optional;

// AlloyComprehensionExpr
public final class AlloyCphExpr extends AlloyExpr {
    public final List<AlloyDecl> decls;
    public final Optional<AlloyExpr> body;

    public AlloyCphExpr(Pos pos, List<AlloyDecl> decls, AlloyExpr body) {
        super(pos);
        this.decls = Collections.unmodifiableList(decls);
        this.body = Optional.ofNullable(body);
        reqNonNull(nullField(pos, this), this.decls, this.body);
    }

    public AlloyCphExpr(List<AlloyDecl> decls, AlloyExpr body) {
        this(Pos.UNKNOWN, decls, body);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(AlloyStrings.LBRACE);
        ASTNode.join(sb, indent, this.decls, AlloyStrings.COMMA + AlloyStrings.SPACE);
        if (!this.body.isEmpty()) {
            sb.append(AlloyStrings.SPACE);
            sb.append(AlloyStrings.BAR);
            sb.append(AlloyStrings.SPACE);
            this.body.get().toString(sb, indent);
        }
        sb.append(AlloyStrings.RBRACE);
    }

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.append(LBRACE);
        pCtx.brkNoSpace();
        pCtx.begin(); // begin of block
        pCtx.appendList(this.decls, COMMA);
        if (this.body.isPresent()) {
            pCtx.append(SPACE);
            pCtx.append(BAR);
            pCtx.brk();
            this.body.get().ppNewBlock(pCtx);
            pCtx.end(); // end of block
            pCtx.brkNoSpaceNoIndent();
        } else {
            pCtx.end(); // end of block
            pCtx.brkNoSpaceNoIndent();
        }
        pCtx.append(RBRACE);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.decls, this.body);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyCphExpr other = (AlloyCphExpr) obj;
        if (decls == null) {
            if (other.decls != null) return false;
        } else if (!decls.equals(other.decls)) return false;
        if (body == null) {
            if (other.body != null) return false;
        } else if (!body.equals(other.body)) return false;
        return true;
    }
}
