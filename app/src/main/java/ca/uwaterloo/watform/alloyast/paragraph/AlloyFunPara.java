package ca.uwaterloo.watform.alloyast.paragraph;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;
import static ca.uwaterloo.watform.utils.GeneralUtil.emptyList;
import static ca.uwaterloo.watform.utils.ImplementationError.nullField;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class AlloyFunPara extends AlloyPara {
    public final boolean isPrivate;
    public final Optional<AlloySigRefExpr> sigRef;
    public final AlloyQnameExpr qname;
    public final List<AlloyDecl> arguments;
    public final Mul mul;
    public final AlloyExpr sub;
    public final AlloyBlock block;

    public AlloyFunPara(
            Pos pos,
            boolean isPrivate,
            AlloySigRefExpr sigRef,
            AlloyQnameExpr qname,
            List<AlloyDecl> arguments,
            Mul mul,
            AlloyExpr sub,
            AlloyBlock block) {
        super(pos);
        this.isPrivate = isPrivate;
        this.sigRef = Optional.ofNullable(sigRef);
        this.qname = qname;
        this.arguments = Collections.unmodifiableList(arguments);
        this.mul = mul;
        this.sub = sub;
        this.block = block;
        reqNonNull(
                nullField(pos, this),
                this.sigRef,
                this.qname,
                this.arguments,
                this.mul,
                this.sub,
                this.block);
    }

    public AlloyFunPara(
            boolean isPrivate,
            AlloySigRefExpr sigRef,
            AlloyQnameExpr qname,
            List<AlloyDecl> arguments,
            Mul mul,
            AlloyExpr sub,
            AlloyBlock block) {
        this(Pos.UNKNOWN, isPrivate, sigRef, qname, arguments, mul, sub, block);
    }

    public AlloyFunPara(
            boolean isPrivate,
            AlloyQnameExpr qname,
            List<AlloyDecl> arguments,
            AlloyExpr sub,
            AlloyBlock block) {
        this(Pos.UNKNOWN, isPrivate, null, qname, arguments, Mul.DEFAULTSET, sub, block);
    }

    public AlloyFunPara(boolean isPrivate, AlloyQnameExpr qname, AlloyExpr sub, AlloyBlock block) {
        this(
                Pos.UNKNOWN,
                isPrivate,
                null,
                qname,
                Collections.emptyList(),
                Mul.DEFAULTSET,
                sub,
                block);
    }

   public AlloyFunPara(
            AlloyQnameExpr qname,
            AlloyBlock block) {
        this(Pos.UNKNOWN, false, null, qname, emptyList(), Mul.DEFAULTSET, null, block);
    }

    /*
     * always print square brackets around args
     */
    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(this.isPrivate ? AlloyStrings.PRIVATE + AlloyStrings.SPACE : "");
        sb.append(AlloyStrings.FUN + AlloyStrings.SPACE);
        if (!this.sigRef.isEmpty()) {
            ((AlloyVarExpr) this.sigRef.get()).toString(sb, indent);
            sb.append(AlloyStrings.DOT);
        }
        this.qname.toString(sb, indent);
        if (!this.arguments.isEmpty()) {
            sb.append(AlloyStrings.LBRACK);
            ASTNode.join(sb, indent, this.arguments, AlloyStrings.COMMA + AlloyStrings.SPACE);
            sb.append(AlloyStrings.RBRACK);
        }
        sb.append(AlloyStrings.SPACE + AlloyStrings.COLON + AlloyStrings.SPACE);
        if (this.mul != Mul.DEFAULTSET) {
            sb.append(this.mul.toString() + AlloyStrings.SPACE);
        }
        this.sub.toString(sb, indent);
        sb.append(AlloyStrings.SPACE);
        this.block.toString(sb, indent);
    }

    @Override
    public void pp(PrintContext pCtx) {
        if (isPrivate) {
            pCtx.append(PRIVATE + SPACE);
        }
        pCtx.append(FUN + SPACE);
        if (sigRef.isPresent()) {
            ((AlloyVarExpr) sigRef.get()).pp(pCtx);
            pCtx.append(DOT);
        }
        qname.pp(pCtx);
        pCtx.append(SPACE);
        if (!arguments.isEmpty()) {
            pCtx.append(LBRACK);
            pCtx.brkNoSpace();
            pCtx.appendList(arguments, COMMA);
            pCtx.brkNoSpaceNoIndent();
            pCtx.append(RBRACK + SPACE);
        }
        pCtx.append(COLON + SPACE);
        if (mul != Mul.DEFAULTSET) {
            pCtx.append(mul.toString() + SPACE);
        }
        sub.ppNewBlock(pCtx);
        pCtx.append(SPACE);
        block.pp(pCtx);
    }

    public enum Mul {
        LONE(AlloyStrings.LONE),
        ONE(AlloyStrings.ONE),
        SOME(AlloyStrings.SOME),
        SET(AlloyStrings.SET),
        DEFAULTSET("");

        public final String label;

        private Mul(String label) {
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
    public AlloyId getId() {
        return new AlloyId(
                (sigRef.isPresent() ? sigRef.get().toString() + DOT : "") + qname,
                arguments.stream().map(decl -> decl.expr.toString()).toList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.isPrivate,
                this.sigRef,
                this.qname,
                this.arguments,
                this.mul,
                this.sub,
                this.block);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyFunPara other = (AlloyFunPara) obj;
        if (isPrivate != other.isPrivate) return false;
        if (sigRef == null) {
            if (other.sigRef != null) return false;
        } else if (!sigRef.equals(other.sigRef)) return false;
        if (qname == null) {
            if (other.qname != null) return false;
        } else if (!qname.equals(other.qname)) return false;
        if (arguments == null) {
            if (other.arguments != null) return false;
        } else if (!arguments.equals(other.arguments)) return false;
        if (mul != other.mul) return false;
        if (sub == null) {
            if (other.sub != null) return false;
        } else if (!sub.equals(other.sub)) return false;
        if (block == null) {
            if (other.block != null) return false;
        } else if (!block.equals(other.block)) return false;
        return true;
    }
}
