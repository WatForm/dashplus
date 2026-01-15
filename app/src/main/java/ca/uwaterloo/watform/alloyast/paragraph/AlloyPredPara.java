package ca.uwaterloo.watform.alloyast.paragraph;

import static ca.uwaterloo.watform.alloyast.AlloyASTImplError.nullField;
import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class AlloyPredPara extends AlloyPara {
    public final boolean isPrivate;
    public final Optional<AlloySigRefExpr> sigRef;
    public final AlloyQnameExpr qname;
    public final List<AlloyDecl> arguments;
    public final AlloyBlock block;

    public AlloyPredPara(
            Pos pos,
            boolean isPrivate,
            AlloySigRefExpr sigRef,
            AlloyQnameExpr qname,
            List<AlloyDecl> arguments,
            AlloyBlock block) {
        super(pos);
        this.isPrivate = isPrivate;
        this.sigRef = Optional.ofNullable(sigRef);
        this.qname = qname;
        this.arguments = arguments;
        this.block = block;
        reqNonNull(nullField(pos, this), this.sigRef, this.qname, this.arguments, this.block);
    }

    public AlloyPredPara(
            boolean isPrivate,
            AlloySigRefExpr sigRef,
            AlloyQnameExpr qname,
            List<AlloyDecl> arguments,
            AlloyBlock block) {
        this(Pos.UNKNOWN, isPrivate, sigRef, qname, arguments, block);
    }

    public AlloyPredPara(
            boolean isPrivate, AlloyQnameExpr qname, List<AlloyDecl> arguments, AlloyBlock block) {
        this(Pos.UNKNOWN, isPrivate, null, qname, arguments, block);
    }

    public AlloyPredPara(boolean isPrivate, AlloyQnameExpr qname, AlloyBlock block) {
        this(Pos.UNKNOWN, isPrivate, null, qname, Collections.emptyList(), block);
    }

    public AlloyPredPara(AlloyQnameExpr qname, List<AlloyDecl> arguments, AlloyBlock block) {
        this(Pos.UNKNOWN, false, null, qname, arguments, block);
    }

    public AlloyPredPara(AlloyQnameExpr qname, AlloyBlock block) {
        this(Pos.UNKNOWN, false, null, qname, Collections.emptyList(), block);
    }

    public AlloyPredPara(String qname, AlloyBlock block) {
        this(Pos.UNKNOWN, false, null, new AlloyQnameExpr(qname), Collections.emptyList(), block);
    }

    /*
     * Always use square brackets around arguments
     */
    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(this.isPrivate ? AlloyStrings.PRIVATE + AlloyStrings.SPACE : "");
        sb.append(AlloyStrings.PRED + AlloyStrings.SPACE);
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
        sb.append(AlloyStrings.SPACE);
        this.block.toString(sb, indent);
    }

    @Override
    public void pp(PrintContext pCtx) {
        if (isPrivate) {
            pCtx.append(PRIVATE + SPACE);
        }
        pCtx.append(PRED + SPACE);
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
        block.pp(pCtx);
    }

    @Override
    public AlloyId getId() {
        return new AlloyId(
                (sigRef.isPresent() ? sigRef.get().toString() + DOT : "") + qname,
                arguments.stream().map(decl -> decl.expr.toString()).toList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.isPrivate, this.sigRef, this.qname, this.arguments, this.block);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyPredPara other = (AlloyPredPara) obj;
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
        if (block == null) {
            if (other.block != null) return false;
        } else if (!block.equals(other.block)) return false;
        return true;
    }
}
