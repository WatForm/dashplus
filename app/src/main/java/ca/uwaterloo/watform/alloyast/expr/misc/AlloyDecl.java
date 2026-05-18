package ca.uwaterloo.watform.alloyast.expr.misc;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.nullField;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

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
    public final Optional<AlloyQtEnum> mul;
    public final AlloyExpr expr;

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
        this.mul = Optional.ofNullable(mul);
        this.expr = expr;
        if (this.mul.orElse(null) == AlloyQtEnum.EXACTLY) {
            if (isVar || isDisj1 || isDisj2) {
                throw AlloyCtorError.declExactlyCannotHaveDisj(pos);
            }
        }
        reqNonNull(nullField(pos, this), this.qnames, this.mul, this.expr);
        // System.out.println(expr);
        // System.out.println(expr.getClass());
        // System.out.println(this.mul);
        if (qnames.isEmpty())
            // must be non-empty
            // would be unreachable from parser
            throw AlloyCtorError.emptyAlloyDeclNames(pos);
        // with x: seq A, mul field is empty
        if (isSeq(this.expr) && !this.mul.isEmpty()) {
            throw AlloyCtorError.seqWithMul(pos, this.expr.toString());
        }
        if (!this.mul.isEmpty()
                && !AlloyQtEnum.MUL.contains(this.mul.orElse(null))
                && this.mul.orElse(null) != AlloyQtEnum.EXACTLY
                && this.mul.orElse(null) != AlloyQtEnum.SEQ) {
            throw AlloyCtorError.invalidAlloyQtEnum(
                    pos,
                    this.getClass().getSimpleName()
                            + ".mul must be LONE, ONE, SOME, SET, SEQ, or EXACTLY. ");
        }
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

    public AlloyDecl(List<AlloyQnameExpr> qnames, AlloyQtEnum mul, AlloyExpr expr) {
        this(Pos.UNKNOWN, false, false, false, qnames, false, mul, expr);
    }

    public AlloyDecl(AlloyQnameExpr qname, AlloyQtEnum mul, AlloyExpr expr) {
        this(Pos.UNKNOWN, false, false, false, Collections.singletonList(qname), false, mul, expr);
    }

    public AlloyDecl(AlloyQnameExpr qname, AlloyExpr expr) {
        this(Pos.UNKNOWN, false, false, false, Collections.singletonList(qname), false, null, expr);
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

    public String getName() {
        if (this.qnames.size() > 1) {
            throw ImplementationError.methodShouldNotBeCalled(
                    this.pos,
                    "AlloyDecl.getName. This should not be called because the "
                            + "decl doesn't have a single name, but multiple. "
                            + "See AlloyDecl.expand(). ");
        }
        return this.qnames.get(0).toString();
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
                            this.mul.orElse(null),
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
        if (this.mul.orElse(null) == AlloyQtEnum.EXACTLY) {
            pCtx.append(EQUAL);
            pCtx.brk();
        } else {
            pCtx.append(COLON);
            pCtx.brk();
            if (this.isDisj2) {
                pCtx.append(DISJ + SPACE);
            }
            this.mul.ifPresent(v -> pCtx.append(v.toString() + SPACE));
        }
        this.expr.pp(pCtx);
    }

    public AlloyDecl rebuild(AlloyExpr newExpr) {
        return new AlloyDecl(
                this.pos,
                this.isVar,
                this.isPrivate,
                this.isDisj1,
                this.qnames,
                this.isDisj2,
                this.mul.orElse(null),
                newExpr);
    }

    public AlloyDecl rebuild(AlloyQtEnum mul, AlloyExpr newExpr) {
        return new AlloyDecl(
                this.pos,
                this.isVar,
                this.isPrivate,
                this.isDisj1,
                this.qnames,
                this.isDisj2,
                mul,
                newExpr);
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
        if (this.mul != other.mul) return false;
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
