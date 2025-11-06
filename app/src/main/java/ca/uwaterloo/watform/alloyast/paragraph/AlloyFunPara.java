package ca.uwaterloo.watform.alloyast.paragraph;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class AlloyFunPara extends AlloyParagraph {
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
     * always print square brackets around brackets
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
        return Optional.of(this.qname.toString());
    }
}
