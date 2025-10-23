package ca.uwaterloo.watform.alloyast.paragraph.command;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNumExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyScopableExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyVarExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyParagraph;
import ca.uwaterloo.watform.utils.ASTNode;
import ca.uwaterloo.watform.utils.ErrorFatal;
import ca.uwaterloo.watform.utils.Pos;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class AlloyCmdPara extends AlloyParagraph {

    public final List<CommandDecl> cmdDecls;

    public AlloyCmdPara(Pos pos, List<CommandDecl> cmdDecls) {
        super(pos);
        this.cmdDecls = Collections.unmodifiableList(cmdDecls);
    }

    public AlloyCmdPara(List<CommandDecl> cmdDecls) {
        super();
        this.cmdDecls = Collections.unmodifiableList(cmdDecls);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        ASTNode.join(
                sb,
                indent,
                this.cmdDecls,
                AlloyStrings.SPACE + AlloyStrings.RFATARROW + AlloyStrings.SPACE);
    }

    public static final class CommandDecl extends ASTNode {
        public final CmdType cmdType;
        public final Optional<AlloyQnameExpr> declQname;

        // mutually exclusive fields, has exactly one
        public final Optional<AlloyQnameExpr> invoQname;
        public final Optional<AlloyBlock> constrBlock;

        public final Optional<Scope> scope;
        public final Optional<AlloyNumExpr> number;

        public CommandDecl(
                Pos pos,
                CmdType cmdType,
                AlloyQnameExpr declQname,
                AlloyQnameExpr invoQname,
                AlloyBlock constrBlock,
                Scope scope,
                AlloyNumExpr number) {
            super(pos);
            this.cmdType = cmdType;
            this.declQname = Optional.ofNullable(declQname);
            this.invoQname = Optional.ofNullable(invoQname);
            this.constrBlock = Optional.ofNullable(constrBlock);
            this.scope = Optional.ofNullable(scope);
            this.number = Optional.ofNullable(number);
            if (!this.invoQname.isEmpty() && !this.constrBlock.isEmpty()) {
                throw new ErrorFatal(
                        "invoQname and constrBlock cannot both be non-null in "
                                + "AlloyCmdPara.CommandDecl. ");
            }
            if (this.invoQname.isEmpty() && this.constrBlock.isEmpty()) {
                throw new ErrorFatal(
                        "invoQname and constrBlock cannot both be null in "
                                + "AlloyCmdPara.CommandDecl. ");
            }
        }

        public CommandDecl(
                CmdType cmdType,
                AlloyQnameExpr declQname,
                AlloyQnameExpr invoQname,
                AlloyBlock constrBlock,
                Scope scope,
                AlloyNumExpr number) {
            this(Pos.UNKNOWN, cmdType, declQname, invoQname, constrBlock, scope, number);
        }

        public CommandDecl(
                CmdType cmdType, AlloyQnameExpr invoQname, AlloyBlock constrBlock, Scope scope) {
            this(Pos.UNKNOWN, cmdType, null, invoQname, constrBlock, scope, null);
        }

        @Override
        public void toString(StringBuilder sb, int indent) {
            sb.append(this.cmdType.toString());
            sb.append(AlloyStrings.SPACE);
            sb.append(this.declQname.map(q -> q.toString() + AlloyStrings.SPACE).orElse(""));
            if (!this.invoQname.isEmpty()) {
                this.invoQname.get().toString(sb, indent);
            } else if (!this.constrBlock.isEmpty()) {
                this.constrBlock.get().toString(sb, indent);
            } else {
                throw new ErrorFatal(
                        "invoQname and constrBlock cannot both be null in "
                                + "AlloyCmdPara.CommandDecl. ");
            }
            sb.append(AlloyStrings.SPACE);
            if (!this.scope.isEmpty()) {
                this.scope.get().toString(sb, indent);
                sb.append(AlloyStrings.SPACE);
            }
            if (!this.number.isEmpty()) {
                sb.append(AlloyStrings.EXPECT);
                this.number.get().toString(sb, indent);
            }
        }

        public enum CmdType {
            CHECK(AlloyStrings.CHECK),
            RUN(AlloyStrings.RUN);

            public final String label;

            private CmdType(String label) {
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

        public static final class Scope extends ASTNode {
            public final Optional<AlloyNumExpr> num;
            public final List<Typescope> typescopes;

            public Scope(Pos pos, AlloyNumExpr num, List<Typescope> typescopes) {
                super(pos);
                this.num = Optional.ofNullable(num);
                this.typescopes = Collections.unmodifiableList(typescopes);
                if (this.num.isEmpty() && this.typescopes.isEmpty()) {
                    throw new ErrorFatal(
                            pos,
                            "num and typescopes cannot both by empty in "
                                    + "AlloyCmdPara.CommandDecl.Scope. ");
                }
            }

            public Scope(AlloyNumExpr num, List<Typescope> typescopes) {
                this(Pos.UNKNOWN, num, typescopes);
            }

            @Override
            public void toString(StringBuilder sb, int indent) {
                sb.append(AlloyStrings.FOR + AlloyStrings.SPACE);
                if (!this.num.isEmpty()) {
                    sb.append(this.num.get().toString());
                    if (!this.typescopes.isEmpty()) {
                        sb.append(AlloyStrings.SPACE + AlloyStrings.BUT + AlloyStrings.SPACE);
                        ASTNode.join(
                                sb,
                                indent,
                                this.typescopes,
                                AlloyStrings.COMMA + AlloyStrings.SPACE);
                    }
                } else if (!this.typescopes.isEmpty()) {
                    ASTNode.join(
                            sb, indent, this.typescopes, AlloyStrings.COMMA + AlloyStrings.SPACE);
                } else {
                    throw new ErrorFatal(
                            pos,
                            "num and typescopes cannot both by empty in "
                                    + "AlloyCmdPara.CommandDecl.Scope. ");
                }
            }

            public static final class Typescope extends ASTNode {
                public final boolean isExactly;
                public final AlloyNumExpr start;
                public final AlloyNumExpr end;
                public final AlloyNumExpr increment;
                public final AlloyScopableExpr scopableExpr;

                public Typescope(
                        Pos pos,
                        boolean isExactly,
                        AlloyNumExpr start,
                        AlloyNumExpr end,
                        AlloyNumExpr increment,
                        AlloyScopableExpr scopableExpr) {
                    super(pos);
                    this.isExactly = isExactly;
                    this.start = start;
                    this.end = end;
                    this.increment = increment;
                    this.scopableExpr = scopableExpr;
                }

                public Typescope(
                        boolean isExactly,
                        AlloyNumExpr start,
                        AlloyNumExpr end,
                        AlloyNumExpr increment,
                        AlloyScopableExpr scopableExpr) {
                    this.isExactly = isExactly;
                    this.start = start;
                    this.end = end;
                    this.increment = increment;
                    this.scopableExpr = scopableExpr;
                }

                @Override
                public void toString(StringBuilder sb, int indent) {
                    sb.append(isExactly ? AlloyStrings.EXACTLY + AlloyStrings.SPACE : "");
                    this.start.toString(sb, indent);
                    sb.append(AlloyStrings.DOT + AlloyStrings.DOT);
                    this.end.toString(sb, indent);
                    sb.append(AlloyStrings.SPACE + AlloyStrings.COLON + AlloyStrings.SPACE);
                    this.increment.toString(sb, indent);
                    sb.append(AlloyStrings.SPACE);
                    ((AlloyVarExpr) this.scopableExpr).toString(sb, indent);
                }
            }
        }
    }
}
