package ca.uwaterloo.watform.alloyast.paragraph;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.misc.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class AlloyPredPara extends AlloyParagraph {
    public final boolean isPrivate;
    public final Optional<AlloySigRefExpr> sigRef;
    public final AlloyNameExpr name;
    public final boolean hasBrack;
    public final boolean hasParen;
    public final List<AlloyDecl> arguments;
    public final AlloyBlock block;

    public AlloyPredPara(
            Pos pos,
            boolean isPrivate,
            AlloySigRefExpr sigRef,
            AlloyNameExpr name,
            boolean hasBrack,
            boolean hasParen,
            List<AlloyDecl> arguments,
            AlloyBlock block) {
        super(pos);
        this.isPrivate = isPrivate;
        this.sigRef = Optional.ofNullable(sigRef);
        this.name = name;
        this.hasBrack = hasBrack;
        this.hasParen = hasParen;
        this.arguments = Collections.unmodifiableList(arguments);
        this.block = block;
    }

    public AlloyPredPara(
            boolean isPrivate,
            AlloySigRefExpr sigRef,
            AlloyNameExpr name,
            boolean hasBrack,
            boolean hasParen,
            List<AlloyDecl> arguments,
            AlloyBlock block) {
        this(Pos.UNKNOWN, isPrivate, sigRef, name, hasBrack, hasParen, arguments, block);
    }

    public AlloyPredPara(
            boolean isPrivate,
            AlloyNameExpr name,
            boolean hasBrack,
            boolean hasParen,
            List<AlloyDecl> arguments,
            AlloyBlock block) {
        this(Pos.UNKNOWN, isPrivate, null, name, hasBrack, hasParen, arguments, block);
    }

    public AlloyPredPara(boolean isPrivate, AlloyNameExpr name, AlloyBlock block) {
        this(Pos.UNKNOWN, isPrivate, null, name, false, false, Collections.emptyList(), block);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(this.isPrivate ? AlloyStrings.PRIVATE + AlloyStrings.SPACE : "");
        sb.append(AlloyStrings.PRED + AlloyStrings.SPACE);
        if (!this.sigRef.isEmpty()) {
            ((AlloyVarExpr) this.sigRef.get()).toString(sb, indent);
            sb.append(AlloyStrings.DOT);
        }
        this.name.toString(sb, indent);
        if (this.hasBrack) {
            sb.append(AlloyStrings.LBRACK);
            ASTNode.join(sb, indent, this.arguments, AlloyStrings.COMMA + AlloyStrings.SPACE);
            sb.append(AlloyStrings.RBRACK);
        } else if (this.hasParen) {
            sb.append(AlloyStrings.LPAREN);
            ASTNode.join(sb, indent, this.arguments, AlloyStrings.COMMA + AlloyStrings.SPACE);
            sb.append(AlloyStrings.RPAREN);
        }
        sb.append(AlloyStrings.SPACE);
        this.block.toString(sb, indent);
    }
}
