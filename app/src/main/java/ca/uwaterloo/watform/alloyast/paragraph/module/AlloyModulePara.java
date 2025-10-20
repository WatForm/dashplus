package ca.uwaterloo.watform.alloyast.paragraph.module;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;

public final class AlloyModulePara extends AlloyParagraph {
    public static class AlloyModuleArg extends AlloyASTNode {
        public final boolean isExactly;
        public final AlloyNameExpr name;

        public AlloyModuleArg(Pos pos, boolean isExactly, AlloyNameExpr name) {
            super(pos);
            this.isExactly = isExactly;
            this.name = name;
        }

        public void toString(StringBuilder sb, int indent) {
            if (isExactly) {
                sb.append(AlloyStrings.EXACTLY);
                sb.append(AlloyStrings.SPACE);
            }
            this.name.toString(sb, indent);
        }
    }

    public final AlloyQnameExpr qname;
    public final List<AlloyModuleArg> moduleArgs;

    public AlloyModulePara(Pos pos, AlloyQnameExpr moduleName, List<AlloyModuleArg> moduleArgs) {
        super(pos);
        this.qname = moduleName;
        this.moduleArgs = Collections.unmodifiableList(moduleArgs);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(AlloyStrings.MODULE);
        sb.append(AlloyStrings.SPACE);
        this.qname.toString(sb, indent);
        sb.append(AlloyStrings.SPACE);
        if (this.moduleArgs.size() != 0) {
            sb.append(AlloyStrings.LBRACK);
            ASTNode.join(sb, indent, this.moduleArgs, AlloyStrings.COMMA + AlloyStrings.SPACE);
            sb.append(AlloyStrings.RBRACK);
        }
    }
}
