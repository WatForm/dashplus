package ca.uwaterloo.watform.alloyast.expr.misc;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class AlloyComprehensionExpr extends AlloyExpr {
    public final List<AlloyDecl> decls;
    public final Optional<AlloyExpr> body;

    public AlloyComprehensionExpr(Pos pos, List<AlloyDecl> decls, AlloyExpr body) {
        super(pos);
        this.decls = Collections.unmodifiableList(decls);
        this.body = Optional.ofNullable(body);
    }

    public AlloyComprehensionExpr(List<AlloyDecl> decls, AlloyExpr body) {
        super();
        this.decls = Collections.unmodifiableList(decls);
        this.body = Optional.ofNullable(body);
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
        AlloyComprehensionExpr other = (AlloyComprehensionExpr) obj;
        if (decls == null) {
            if (other.decls != null) return false;
        } else if (!decls.equals(other.decls)) return false;
        if (body == null) {
            if (other.body != null) return false;
        } else if (!body.equals(other.body)) return false;
        return true;
    }
}
