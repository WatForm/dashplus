package ca.uwaterloo.watform.alloyast.expr.misc;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.nullField;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyASTImplError;
import ca.uwaterloo.watform.alloyast.AlloyCtorError;
import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class AlloyDecl extends AlloyExpr {
    public final boolean isVar;
    public final boolean isPrivate;
    public final boolean isDisj1;
    public final List<AlloyQnameExpr> qnames;
    public final boolean isDisj2;
    public final AlloyQtEnum mul;
    public final AlloyExpr expr;

    private static AlloyQtEnum defaultMul(Pos pos, AlloyExpr expr) {
        if (expr instanceof AlloyVarExpr) return AlloyQtEnum.ONE;
        else if (expr instanceof AlloyArrowExpr) return AlloyQtEnum.SET;
        else
            // @53 this is not throwing an error msg with a pos
            throw AlloyASTImplError.invalidAlloyQtEnum(
                    pos, expr.toString() + " must be given a multiplicity explicitly");
    }

    private static AlloyQtEnum defaultMul(AlloyExpr expr) {
        if (expr instanceof AlloyVarExpr) return AlloyQtEnum.ONE;
        else if (expr instanceof AlloyArrowExpr) return AlloyQtEnum.SET;
        else {
            throw AlloyASTImplError.invalidAlloyQtEnum(
                    expr.toString() + " must be given a multiplicity explicitly");
        }
    }

    public AlloyDecl(
            Pos pos,
            boolean isVar,
            boolean isPrivate,
            boolean isDisj1,
            List<AlloyQnameExpr> qnames,
            boolean isDisj2,
            AlloyQtEnum mul,
            AlloyExpr expr) {
        super(pos);
        this.isVar = isVar;
        this.isPrivate = isPrivate;
        this.isDisj1 = isDisj1;
        this.qnames = Collections.unmodifiableList(qnames);
        this.isDisj2 = isDisj2;
        this.mul = mul;
        this.expr = expr;
        if (this.mul == AlloyQtEnum.EXACTLY) {
            if (isVar || isDisj1 || isDisj2) {
                throw AlloyCtorError.declExactlyCannotHaveDisj(pos);
            }
        }
        reqNonNull(nullField(pos, this), this.qnames, this.mul, this.expr);
        if (!AlloyQtEnum.MUL.contains(this.mul) && this.mul != AlloyQtEnum.EXACTLY) {
            throw AlloyASTImplError.invalidAlloyQtEnum(
                    pos,
                    this.getClass().getSimpleName()
                            + ".mul must be LONE, ONE, SOME, SET or EXACTLY. ");
        }
    }

    public AlloyDecl(
            Pos pos,
            boolean isVar,
            boolean isPrivate,
            boolean isDisj1,
            List<AlloyQnameExpr> qnames,
            boolean isDisj2,
            AlloyExpr expr) {
        this(Pos.UNKNOWN, isVar, isPrivate, isDisj1, qnames, isDisj2, defaultMul(pos, expr), expr);
    }

    public AlloyDecl(
            boolean isVar,
            boolean isPrivate,
            boolean isDisj1,
            List<AlloyQnameExpr> qnames,
            boolean isDisj2,
            AlloyQtEnum mul,
            AlloyExpr expr) {
        this(Pos.UNKNOWN, isVar, isPrivate, isDisj1, qnames, isDisj2, mul, expr);
    }

    public AlloyDecl(List<AlloyQnameExpr> qnames, AlloyExpr expr) {
        this(Pos.UNKNOWN, false, false, false, qnames, false, AlloyQtEnum.ONE, expr);
    }

    public AlloyDecl(AlloyQnameExpr qname, AlloyExpr expr) {
        this(
                Pos.UNKNOWN,
                false,
                false,
                false,
                Collections.singletonList(qname),
                false,
                defaultMul(expr),
                expr);
    }

    public AlloyDecl(AlloyQnameExpr qname, AlloyQtEnum mul, AlloyExpr expr) {
        this(Pos.UNKNOWN, false, false, false, Collections.singletonList(qname), false, mul, expr);
    }

    public AlloyDecl(String qname, String expr) {
        AlloyQnameExpr e = new AlloyQnameExpr(expr);
        this(
                Pos.UNKNOWN,
                false,
                false,
                false,
                Collections.singletonList(new AlloyQnameExpr(qname)),
                false,
                defaultMul(e),
                e);
    }

    public AlloyDecl(String qname, AlloyExpr expr) {
        this(
                Pos.UNKNOWN,
                false,
                false,
                false,
                Collections.singletonList(new AlloyQnameExpr(qname)),
                false,
                defaultMul(expr),
                expr);
    }

    public AlloyDecl(String qname, AlloyQtEnum mul, AlloyExpr expr) {
        this(
                Pos.UNKNOWN,
                false,
                false,
                false,
                Collections.singletonList(new AlloyQnameExpr(qname)),
                false,
                mul,
                expr);
    }

    public Optional<String> getName() {
        if (this.qnames.size() > 1) {
            throw ImplementationError.methodShouldNotBeCalled(
                    this.pos,
                    "AlloyDecl.getName. This should not be called because the "
                            + "decl doesn't have a single name, but multiple. "
                            + "See AlloyDecl.expand(). ");
        }
        return Optional.of(this.qnames.get(0).toString());
    }

    public List<AlloyDecl> expand() {
        if (1 == this.qnames.size()) {
            return Collections.singletonList(this);
        }
        List<AlloyDecl> expandedLi = new ArrayList<>();
        for (AlloyQnameExpr qname : this.qnames) {
            expandedLi.add(
                    new AlloyDecl(
                            this.pos,
                            this.isVar,
                            this.isPrivate,
                            this.isDisj1,
                            Collections.singletonList(qname),
                            this.isDisj2,
                            this.mul,
                            this.expr));
        }
        return expandedLi;
    }

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.append(this.isVar ? VAR + SPACE : "");
        pCtx.append(this.isPrivate ? PRIVATE + SPACE : "");
        pCtx.append((this.isDisj1 ? DISJ + SPACE : ""));
        pCtx.appendList(this.qnames, COMMA);
        pCtx.append(SPACE);
        if (this.mul == AlloyQtEnum.EXACTLY) {
            pCtx.append(EQUAL);
            pCtx.brk();
        } else {
            pCtx.append(COLON);
            pCtx.brk();
            if (this.isDisj2) {
                pCtx.append(DISJ + SPACE);
            }
            pCtx.append(this.mul.toString() + SPACE);
        }
        this.expr.pp(pCtx);
    }

    public AlloyDecl withExpr(AlloyExpr newExpr) {
        return new AlloyDecl(pos, isVar, isPrivate, isDisj1, qnames, isDisj2, mul, newExpr);
    }

    @Override
    public <T> T accept(AlloyExprVis<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.isVar,
                this.isPrivate,
                this.isDisj1,
                this.qnames,
                this.isDisj2,
                this.mul,
                this.expr);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyDecl other = (AlloyDecl) obj;
        if (isVar != other.isVar) return false;
        if (isPrivate != other.isPrivate) return false;
        if (isDisj1 != other.isDisj1) return false;
        if (qnames == null) {
            if (other.qnames != null) return false;
        } else if (!qnames.equals(other.qnames)) return false;
        if (isDisj2 != other.isDisj2) return false;
        if (mul == null) {
            if (other.mul != null) return false;
        } else if (!mul.equals(other.mul)) return false;
        if (expr == null) {
            if (other.expr != null) return false;
        } else if (!expr.equals(other.expr)) return false;
        return true;
    }

    @Override
    public int getPrec() {
        return AlloyExpr.NO_PAREN;
    }
}
