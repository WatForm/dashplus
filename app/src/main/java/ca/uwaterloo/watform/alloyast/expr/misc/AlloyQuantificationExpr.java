package ca.uwaterloo.watform.alloyast.expr.misc;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.misc.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;

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
    }

    public AlloyQuantificationExpr(
            AlloyQuantificationExpr.Quant quant, List<AlloyDecl> decls, AlloyExpr body) {
        super();
        this.quant = quant;
        this.decls = Collections.unmodifiableList(decls);
        this.body = body;
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
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }
}
