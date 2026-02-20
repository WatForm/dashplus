package ca.uwaterloo.watform.dashast;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;

public final class DashVarDecls extends ASTNode implements DashStateItem {

    public final List<String> names;
    public final AlloyExpr typ;
    public final DashStrings.IntEnvKind kind;
    public final Quant quant;

    public DashVarDecls(
            Pos pos, List<String> n, Quant quant, AlloyExpr e, DashStrings.IntEnvKind k) {
        super(pos);
        assert (n != null && e != null);
        this.names = Collections.unmodifiableList(n);
        this.quant = quant;
        this.typ = e;
        this.kind = k;
    }

    @Override
    public void pp(PrintContext pCtx) {
        if (kind == IntEnvKind.ENV) {
            pCtx.append(envName + SPACE);
        }
        pCtx.appendList(names, COMMA);
        pCtx.append(SPACE + COLON);
        pCtx.append(SPACE + quant);
        pCtx.brk();
        typ.ppNewBlock(pCtx);
    }

    public List<String> getNames() {
        return names;
    }

    public AlloyExpr getTyp() {
        return typ;
    }

    public DashStrings.IntEnvKind getKind() {
        return kind;
    }

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
}
