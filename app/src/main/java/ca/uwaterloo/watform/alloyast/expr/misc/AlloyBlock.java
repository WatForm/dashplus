package ca.uwaterloo.watform.alloyast.expr.misc;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;

public final class AlloyBlock extends AlloyExpr {
    public final List<AlloyExpr> exprs;

    public AlloyBlock(Pos pos, List<AlloyExpr> exprs) {
        super(pos);
        this.exprs = Collections.unmodifiableList(exprs);
    }

    public AlloyBlock(Pos pos, AlloyExpr expr) {
        super(pos);
        this.exprs = Collections.unmodifiableList(Collections.singletonList(expr));
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        String tabs = "";
        for (int i = 0; i < indent; i++) {
            tabs += AlloyStrings.TAB;
        }

        sb.append(AlloyStrings.LBRACE);

        for (AlloyExpr expr : this.exprs) {
            sb.append(AlloyStrings.NEWLINE + tabs + AlloyStrings.TAB);
            expr.toString(sb, indent + 1);
        }
        sb.append(AlloyStrings.NEWLINE);

        sb.append(tabs);
        sb.append(AlloyStrings.RBRACE);
    }
}
