package ca.uwaterloo.watform.alloyast.misc;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyASTNode;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class AlloyDecl extends AlloyASTNode {
    public final boolean disj1;
    public final List<AlloyNameExpr> names;
    public final boolean disj2;
    public final Optional<Quant> quant;
    public final AlloyExpr expr;

    public enum Quant {
        /** lone a,b:x | formula */
        LONE(AlloyStrings.LONE),
        /** one a,b:x | formula */
        ONE(AlloyStrings.ONE),
        /** some a,b:x | formula */
        SOME(AlloyStrings.SOME),
        /** set a,b:x | formula */
        SET(AlloyStrings.SET);
        // Alloy has Comprehension here too, but that's made into a separate
        // class here

        public final String label;

        private Quant(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        /** Returns the human readable label for this operator */
        @Override
        public final String toString() {
            return label;
        }
    }

    public AlloyDecl(
            Pos pos,
            Boolean disj1,
            List<AlloyNameExpr> names,
            Boolean disj2,
            Optional<AlloyDecl.Quant> quant,
            AlloyExpr expr) {
        super(pos);
        this.disj1 = disj1;
        this.names = Collections.unmodifiableList(names);
        this.disj2 = disj2;
        this.quant = quant;
        this.expr = expr;
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append((this.disj1 ? "disj " : ""));
        ASTNode.join(sb, indent, this.names, AlloyStrings.COMMA + AlloyStrings.SPACE);
        sb.append(AlloyStrings.COLON);
        sb.append((this.disj2 ? "disj " : ""));
        sb.append(this.quant.map(q -> q.toString() + " ").orElse(""));
        this.expr.toString(sb, indent);
    }
}
