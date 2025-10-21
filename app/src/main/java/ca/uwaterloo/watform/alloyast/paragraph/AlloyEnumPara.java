package ca.uwaterloo.watform.alloyast.paragraph;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import ca.uwaterloo.watform.utils.ASTNode;
import ca.uwaterloo.watform.utils.Pos;
import java.util.Collections;
import java.util.List;

// enumPara        : PRIVATE? ENUM name LBRACE names RBRACE;
public final class AlloyEnumPara extends AlloyParagraph {
    public final boolean isPrivate;
    public final AlloyNameExpr name;
    public final List<AlloyNameExpr> names;

    public AlloyEnumPara(
            Pos pos, boolean isPrivate, AlloyNameExpr name, List<AlloyNameExpr> names) {
        super(pos);
        this.isPrivate = isPrivate;
        this.name = name;
        this.names = Collections.unmodifiableList(names);
    }

    public AlloyEnumPara(boolean isPrivate, AlloyNameExpr name, List<AlloyNameExpr> names) {
        super();
        this.isPrivate = isPrivate;
        this.name = name;
        this.names = Collections.unmodifiableList(names);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(isPrivate ? AlloyStrings.PRIVATE + AlloyStrings.SPACE : "");
        sb.append(AlloyStrings.ENUM + AlloyStrings.SPACE);
        this.name.toString(sb, indent);
        sb.append(AlloyStrings.SPACE + AlloyStrings.LBRACE);
        ASTNode.join(sb, indent, this.names, AlloyStrings.COMMA + AlloyStrings.SPACE);
        sb.append(AlloyStrings.RBRACE);
    }
}
