package ca.uwaterloo.watform.dashast;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyArrowExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyVarExpr;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;

public final class DashVarDecls extends ASTNode implements DashStateItem {

    public final List<String> names;
    public final AlloyQtEnum mul;
    public final AlloyExpr typ;
    public final DashStrings.IntEnvKind kind;

    private static AlloyQtEnum defaultMul(Pos pos, AlloyExpr expr) {
        if (expr instanceof AlloyVarExpr) return AlloyQtEnum.ONE;
        else if (expr instanceof AlloyArrowExpr) return AlloyQtEnum.SET;
        else {
            throw DashASTImplError.missingMul(pos, expr);
        }
    }

    public DashVarDecls(
            Pos pos, List<String> n, AlloyQtEnum mul, AlloyExpr e, DashStrings.IntEnvKind k) {
        super(pos);
        assert (n != null && e != null);
        this.names = Collections.unmodifiableList(n);
        this.mul = mul;
        this.typ = e;
        this.kind = k;
        // no limits on the classes of varType
        // translator does not care what they are
        if (!AlloyQtEnum.MUL.contains(this.mul)) {
            throw new ImplementationError(
                    pos, this.getClass().getSimpleName() + ".mul must be LONE, ONE, SOME or SET. ");
        }
        reqNonNull(nullField(pos, this), this.names, this.typ, this.mul, this.kind);
    }

    public DashVarDecls(Pos pos, List<String> n, AlloyExpr e, DashStrings.IntEnvKind k) {
        super(pos);
        assert (n != null && e != null);
        this.names = Collections.unmodifiableList(n);
        this.mul = defaultMul(pos, e);
        this.typ = e;
        this.kind = k;
        // no limits on the classes of varType
        // translator does not care what they are
        if (!AlloyQtEnum.MUL.contains(this.mul)) {
            throw new ImplementationError(
                    pos, this.getClass().getSimpleName() + ".mul must be LONE, ONE, SOME or SET. ");
        }
        reqNonNull(nullField(pos, this), this.names, this.typ, this.mul, this.kind);
    }

    @Override
    public void pp(PrintContext pCtx) {
        if (kind == IntEnvKind.ENV) {
            pCtx.append(envName + SPACE);
        }
        pCtx.appendList(names, COMMA);
        pCtx.append(SPACE + COLON);
        pCtx.append(SPACE + mul);
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
}
