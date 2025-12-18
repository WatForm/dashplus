package ca.uwaterloo.watform.alloyast.paragraph.command;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/*
* Use the following to import this class to avoid long names
	import ca.uwaterloo.watform.alloyast.paragraph.command.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara.CommandDecl.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara.CommandDecl.Scope.*;
*/
public final class AlloyCmdPara extends AlloyPara {

    public final List<CommandDecl> cmdDecls;

    public AlloyCmdPara(Pos pos, List<CommandDecl> cmdDecls) {
        super(pos);
        this.cmdDecls = Collections.unmodifiableList(cmdDecls);
        reqNonNull(nullField(pos, this), this.cmdDecls);
    }

    public AlloyCmdPara(List<CommandDecl> cmdDecls) {
        this(Pos.UNKNOWN, cmdDecls);
    }

    public AlloyCmdPara(CommandDecl cmdDecl) {
        this(Pos.UNKNOWN, Collections.singletonList(cmdDecl));
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        ASTNode.join(
                sb,
                indent,
                this.cmdDecls,
                AlloyStrings.SPACE + AlloyStrings.RFATARROW + AlloyStrings.SPACE);
    }

    @Override
    public void pp(PrintContext pCtx) {
        pCtx.appendList(this.cmdDecls, SPACE + RFATARROW + SPACE);
    }

    /*
     * This is used to uniquely identity paragraphs,
     * but AlloyCmdPara are allowed to have the same name.
     * So return nothing here
     */
    @Override
    public Optional<String> getName() {
        return Optional.empty();
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
                throw AlloyASTImplError.xorFields(
                        pos, "invoQname", "constrBlock", "AlloyCmdPara.CommandDecl");
            }
            if (this.invoQname.isEmpty() && this.constrBlock.isEmpty()) {
                throw AlloyASTImplError.xorFields(
                        pos, "invoQname", "constrBlock", "AlloyCmdPara.CommandDecl");
            }
            reqNonNull(
                    nullField(pos, this),
                    this.cmdType,
                    this.declQname,
                    this.invoQname,
                    this.constrBlock,
                    this.scope,
                    this.number);
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

        public CommandDecl(
                CmdType cmdType,
                AlloyQnameExpr declQname,
                AlloyQnameExpr invoQname,
                AlloyBlock constrBlock,
                Scope scope) {
            this(Pos.UNKNOWN, cmdType, declQname, invoQname, constrBlock, scope, null);
        }

        public CommandDecl(CmdType cmdType, AlloyQnameExpr invoQname, Scope scope) {
            this(Pos.UNKNOWN, cmdType, null, invoQname, null, scope, null);
        }

        public CommandDecl(CmdType cmdType, AlloyBlock constrBlock, Scope scope) {
            this(Pos.UNKNOWN, cmdType, null, null, constrBlock, scope, null);
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
                throw AlloyASTImplError.xorFields(
                        pos, "invoQname", "constrBlock", "AlloyCmdPara.CommandDecl");
            }
            sb.append(AlloyStrings.SPACE);
            if (!this.scope.isEmpty()) {
                this.scope.get().toString(sb, indent);
                sb.append(AlloyStrings.SPACE);
            }
            if (!this.number.isEmpty()) {
                sb.append(AlloyStrings.EXPECT + AlloyStrings.SPACE);
                this.number.get().toString(sb, indent);
            }
        }

        @Override
        public void pp(PrintContext pCtx) {
            pCtx.append(this.cmdType.toString() + SPACE);
            if (this.declQname.isPresent()) {
                this.declQname.get().pp(pCtx);
                pCtx.append(SPACE);
            }
            if (this.invoQname.isPresent()) {
                this.invoQname.get().pp(pCtx);
            } else if (this.constrBlock.isPresent()) {
                this.constrBlock.get().pp(pCtx);
            } else {
                throw AlloyASTImplError.xorFields(
                        pos, "invoQname", "constrBlock", "AlloyCmdPara.CommandDecl");
            }
            pCtx.brk();
            if (this.scope.isPresent()) {
                this.scope.get().pp(pCtx);
                pCtx.append(SPACE);
            }
            if (this.number.isPresent()) {
                pCtx.append(AlloyStrings.EXPECT + AlloyStrings.SPACE);
                this.number.get().pp(pCtx);
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
                    throw AlloyASTImplError.bothNull(
                            pos, "num", "typescopes", "AlloyCmdPara.CommandDecl.Scope");
                }
                reqNonNull(nullField(pos, this), this.num, this.typescopes);
            }

            public Scope(AlloyNumExpr num, List<Typescope> typescopes) {
                this(Pos.UNKNOWN, num, typescopes);
            }

            public Scope(List<Typescope> typescopes) {
                this(Pos.UNKNOWN, null, typescopes);
            }

            public Scope(Typescope typescope) {
                this(Pos.UNKNOWN, null, Collections.singletonList(typescope));
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
                    throw AlloyASTImplError.bothNull(
                            pos, "num", "typescopes", "AlloyCmdPara.CommandDecl.Scope");
                }
            }

            public void pp(PrintContext pCtx) {
                pCtx.append(FOR + SPACE);
                if (!this.num.isEmpty()) {
                    this.num.get().pp(pCtx);
                    if (!this.typescopes.isEmpty()) {
                        pCtx.append(AlloyStrings.SPACE + AlloyStrings.BUT + AlloyStrings.SPACE);
                        pCtx.appendList(this.typescopes, COMMA);
                    }
                } else if (!this.typescopes.isEmpty()) {
                    pCtx.appendList(this.typescopes, COMMA);
                } else {
                    throw AlloyASTImplError.bothNull(
                            pos, "num", "typescopes", "AlloyCmdPara.CommandDecl.Scope");
                }
            }

            // The fields only reflect the syntax
            // In the constructor, do not try to infer the fields from the arguments given
            // For example, if start is equal to end, don't make isExactly true
            public static final class Typescope extends ASTNode {
                public final boolean isExactly;
                public final AlloyNumExpr start;
                public final boolean hasDotDot;
                public final Optional<AlloyNumExpr> end;
                public final Optional<AlloyNumExpr> increment;
                public final AlloyScopableExpr scopableExpr;

                public Typescope(
                        Pos pos,
                        boolean isExactly,
                        AlloyNumExpr start,
                        boolean hasDotDot,
                        AlloyNumExpr end,
                        AlloyNumExpr increment,
                        AlloyScopableExpr scopableExpr) {
                    super(pos);
                    this.isExactly = isExactly;
                    this.start = start;
                    this.hasDotDot = hasDotDot;
                    this.end = Optional.ofNullable(end);
                    this.increment = Optional.ofNullable(increment);
                    this.scopableExpr = scopableExpr;
                    if (this.end.isPresent() && !this.hasDotDot) {
                        throw AlloyCtorError.endWithoutDotDot(pos);
                    }
                    if (this.scopableExpr instanceof AlloySigIntExpr
                            || this.scopableExpr instanceof AlloyIntExpr
                            || this.scopableExpr instanceof AlloySeqExpr) {
                        if (this.end.isPresent() && this.end.get().value > this.start.value) {
                            throw AlloyCtorError.growingScope(pos, scopableExpr);
                        }
                        if (isExactly) {
                            throw AlloyCtorError.redundantExactly(pos);
                        }
                    }

                    if (this.start.value < 0
                            || (this.end.isPresent() && this.end.get().value < 0)) {
                        throw AlloyCtorError.cmdNegScop(pos);
                    }

                    if (this.end.isPresent() && this.end.get().value < this.start.value) {
                        throw AlloyCtorError.cmdDecreasingScope(pos);
                    }

                    if (this.increment.isPresent() && this.increment.get().value < 1) {
                        throw AlloyCtorError.cmdInvalidIncrement(pos);
                    }

                    if ((this.scopableExpr instanceof AlloySigIntExpr
                                    || this.scopableExpr instanceof AlloyIntExpr)
                            && this.start.value > 30) {
                        throw AlloyCtorError.cmdBitwidthTooBig(pos);
                    }

                    reqNonNull(
                            nullField(pos, this),
                            this.start,
                            this.end,
                            this.increment,
                            this.scopableExpr);
                }

                public Typescope(
                        boolean isExactly,
                        int start,
                        boolean hasDotDot,
                        int end,
                        int increment,
                        AlloyScopableExpr scopableExpr) {
                    this(
                            Pos.UNKNOWN,
                            isExactly,
                            new AlloyNumExpr(start),
                            hasDotDot,
                            new AlloyNumExpr(end),
                            new AlloyNumExpr(increment),
                            scopableExpr);
                }

                public Typescope(
                        boolean isExactly,
                        AlloyNumExpr start,
                        boolean hasDotDot,
                        AlloyNumExpr end,
                        AlloyNumExpr increment,
                        AlloyScopableExpr scopableExpr) {
                    this(Pos.UNKNOWN, isExactly, start, hasDotDot, end, increment, scopableExpr);
                }

                public Typescope(
                        boolean isExactly, int start, int end, int increment, String name) {
                    this(
                            Pos.UNKNOWN,
                            isExactly,
                            new AlloyNumExpr(start),
                            true,
                            new AlloyNumExpr(end),
                            new AlloyNumExpr(increment),
                            new AlloyQnameExpr(name));
                }

                @Override
                public void toString(StringBuilder sb, int indent) {
                    sb.append(isExactly ? AlloyStrings.EXACTLY + AlloyStrings.SPACE : "");
                    this.start.toString(sb, indent);
                    if (!(this.scopableExpr instanceof AlloySigIntExpr)
                            && !(this.scopableExpr instanceof AlloyIntExpr)
                            && !(this.scopableExpr instanceof AlloySeqExpr)) {
                        if (this.hasDotDot) {
                            sb.append(AlloyStrings.DOT + AlloyStrings.DOT);
                        }
                        if (this.end.isPresent()) {
                            this.end.get().toString(sb, indent);
                        }
                        if (this.increment.isPresent()) {
                            sb.append(AlloyStrings.SPACE + AlloyStrings.COLON + AlloyStrings.SPACE);
                            this.increment.get().toString(sb, indent);
                        }
                    }
                    sb.append(AlloyStrings.SPACE);
                    ((AlloyVarExpr) this.scopableExpr).toString(sb, indent);
                }

                @Override
                public void pp(PrintContext pCtx) {
                    if (isExactly) {
                        pCtx.append(EXACTLY + SPACE);
                    }
                    this.start.pp(pCtx);
                    if (!(this.scopableExpr instanceof AlloySigIntExpr)
                            && !(this.scopableExpr instanceof AlloyIntExpr)
                            && !(this.scopableExpr instanceof AlloySeqExpr)) {
                        if (this.hasDotDot) {
                            pCtx.append(DOT + DOT);
                        }
                        if (this.end.isPresent()) {
                            this.end.get().pp(pCtx);
                        }
                        if (this.increment.isPresent()) {
                            pCtx.append(SPACE + COLON + SPACE);
                            this.increment.get().pp(pCtx);
                        }
                    }
                    pCtx.append(SPACE);
                    ((AlloyVarExpr) this.scopableExpr).pp(pCtx);
                }
            }
        }
    }
}
