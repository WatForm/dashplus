package ca.uwaterloo.watform.alloyast.paragraph;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;
import static ca.uwaterloo.watform.utils.ImplementationError.nullField;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
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
        sub.pp(pCtx);
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
    public Optional<String> getName() {
        StringBuilder sb = new StringBuilder();
        if (!this.sigRef.isEmpty()) {
            ((AlloyVarExpr) this.sigRef.get()).toString(sb, 0);
            sb.append(AlloyStrings.DOT);
        }
        this.qname.toString(sb, 0);
        if (!this.arguments.isEmpty()) {
            sb.append(AlloyStrings.LBRACK);
            ASTNode.join(sb, 0, this.arguments, AlloyStrings.COMMA + AlloyStrings.SPACE);
            sb.append(AlloyStrings.RBRACK);
        }
        return Optional.of(sb.toString());
    }
}
