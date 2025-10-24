package ca.uwaterloo.watform.alloyast.expr.misc;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.misc.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
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
}
