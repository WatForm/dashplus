package ca.uwaterloo.watform.alloyast.paragraph.module;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/*
* this the following to import to avoid long names
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara.*;
*/
public final class AlloyModulePara extends AlloyPara {
    public static class AlloyModuleArg extends AlloyASTNode {
        public final boolean isExactly;
        public final AlloyQnameExpr qname;

        public AlloyModuleArg(Pos pos, boolean isExactly, AlloyQnameExpr qname) {
            super(pos);
            this.isExactly = isExactly;
            this.qname = qname;
        }

        public AlloyModuleArg(boolean isExactly, AlloyQnameExpr qname) {
            this(Pos.UNKNOWN, isExactly, qname);
        }

        @Override
        public void toString(StringBuilder sb, int indent) {
            if (isExactly) {
                sb.append(AlloyStrings.EXACTLY);
                sb.append(AlloyStrings.SPACE);
            }
            this.qname.toString(sb, indent);
        }

        @Override
        public void pp(PrintContext pCtx) {
            if (isExactly) {
                pCtx.append(EXACTLY + SPACE);
            }
            this.qname.pp(pCtx);
        }
    }

    public final AlloyQnameExpr qname;
    public final List<AlloyModuleArg> moduleArgs;

    public AlloyModulePara(Pos pos, AlloyQnameExpr moduleName, List<AlloyModuleArg> moduleArgs) {
        super(pos);
        this.qname = moduleName;
        this.moduleArgs = Collections.unmodifiableList(moduleArgs);
    }

    public AlloyModulePara(AlloyQnameExpr moduleName, List<AlloyModuleArg> moduleArgs) {
        super();
        this.qname = moduleName;
        this.moduleArgs = Collections.unmodifiableList(moduleArgs);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        sb.append(AlloyStrings.MODULE);
        sb.append(AlloyStrings.SPACE);
        this.qname.toString(sb, indent);
        if (this.moduleArgs.size() != 0) {
            sb.append(AlloyStrings.LBRACK);
            ASTNode.join(sb, indent, this.moduleArgs, AlloyStrings.COMMA + AlloyStrings.SPACE);
            sb.append(AlloyStrings.RBRACK);
        }
    }

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.append(MODULE + SPACE);
        this.qname.pp(pCtx);
        if (!this.moduleArgs.isEmpty()) {
            pCtx.append(LBRACK);
            pCtx.brkNoSpace();
            pCtx.appendList(this.moduleArgs, COMMA);
            pCtx.brkNoSpaceNoIndent();
            pCtx.append(RBRACK);
        }
    }

    @Override
    public Optional<String> getName() {
        return Optional.of(this.qname.toString());
    }
}
