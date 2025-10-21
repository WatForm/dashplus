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
        LONE(AlloyStrings.LONE),
        ONE(AlloyStrings.ONE),
        SOME(AlloyStrings.SOME),
        SET(AlloyStrings.SET);

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

    public AlloyDecl(
            Pos pos,
            Boolean disj1,
            List<AlloyNameExpr> names,
            Boolean disj2,
            AlloyDecl.Quant quant,
            AlloyExpr expr) {
        super(pos);
        this.disj1 = disj1;
        this.names = Collections.unmodifiableList(names);
        this.disj2 = disj2;
        this.quant = Optional.ofNullable(quant);
        this.expr = expr;
    }

    public AlloyDecl(
            Boolean disj1,
            List<AlloyNameExpr> names,
            Boolean disj2,
            AlloyDecl.Quant quant,
            AlloyExpr expr) {
        super();
        this.disj1 = disj1;
        this.names = Collections.unmodifiableList(names);
        this.disj2 = disj2;
        this.quant = Optional.ofNullable(quant);
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
