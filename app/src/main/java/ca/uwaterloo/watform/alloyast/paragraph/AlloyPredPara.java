package ca.uwaterloo.watform.alloyast.paragraph;

import ca.uwaterloo.watform.alloyast.AlloyASTImplError;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class AlloyPredPara extends AlloyParagraph {
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
        this.qname =
                GeneralUtil.requireNonNull(
                        qname, AlloyASTImplError.nullOrBlankField(pos, "AlloyPredPara.qname"));
        this.arguments =
                Collections.unmodifiableList(
                        GeneralUtil.requireNonNull(
                                arguments,
                                AlloyASTImplError.nullOrBlankField(
                                        pos, "AlloyPredPara.arguments")));
        this.block =
                GeneralUtil.requireNonNull(
                        block, AlloyASTImplError.nullOrBlankField(pos, "AlloyPredPara.block"));
        GeneralUtil.requireNonNull(
                this.qname.toString(),
                AlloyASTImplError.nullOrBlankField(pos, "AlloyPredPara.qname"));
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
    public Optional<String> getName() {
        assert (null != this.qname
                && !this.qname.toString().isBlank()); // this check is done already in ctor
        return Optional.of(this.qname.toString());
    }
}
