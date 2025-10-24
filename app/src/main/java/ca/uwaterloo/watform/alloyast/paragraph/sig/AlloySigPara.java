package ca.uwaterloo.watform.alloyast.paragraph.sig;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyNameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloySigRefExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyVarExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyParagraph;
import ca.uwaterloo.watform.utils.ASTNode;
import ca.uwaterloo.watform.utils.Pos;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class AlloySigPara extends AlloyParagraph {
    public final List<Qual> quals;
    public final List<AlloyNameExpr> names;
    public final Optional<Rel> rel;
    public final List<AlloyDecl> decls;
    public final Optional<AlloyBlock> block;

    public AlloySigPara(
            Pos pos,
            List<Qual> quals,
            List<AlloyNameExpr> names,
            Rel rel,
            List<AlloyDecl> decls,
            AlloyBlock block) {
        super(pos);
        this.quals = Collections.unmodifiableList(quals);
        this.names = Collections.unmodifiableList(names);
        this.rel = Optional.ofNullable(rel);
        this.decls = decls;
        this.block = Optional.ofNullable(block);
    }

    public AlloySigPara(
            List<Qual> quals,
            List<AlloyNameExpr> names,
            Rel rel,
            List<AlloyDecl> decls,
            AlloyBlock block) {
        this(Pos.UNKNOWN, quals, names, rel, decls, block);
    }

    public AlloySigPara(
            List<AlloyNameExpr> names, Rel rel, List<AlloyDecl> decls, AlloyBlock block) {
        this(Pos.UNKNOWN, Collections.emptyList(), names, rel, decls, block);
    }

    public AlloySigPara(List<AlloyNameExpr> names, List<AlloyDecl> decls, AlloyBlock block) {
        this(Pos.UNKNOWN, Collections.emptyList(), names, null, decls, block);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        // cannot use ASTNode.join here b/c Qual is not ASTNode; will fail dynamic cast
        // consider changing these Enum to an object and extend ASTNode
        for (Qual qual : this.quals) {
            sb.append(qual.toString() + AlloyStrings.SPACE);
        }
        sb.append(AlloyStrings.SIG);
        sb.append(AlloyStrings.SPACE);
        ASTNode.join(sb, indent, this.names, AlloyStrings.COMMA + AlloyStrings.SPACE);
        sb.append(AlloyStrings.SPACE);
        if (!this.rel.isEmpty()) {
            ((ASTNode) this.rel.get()).toString(sb, indent);
            sb.append(AlloyStrings.SPACE);
        }
        sb.append(AlloyStrings.LBRACE + AlloyStrings.NEWLINE + AlloyStrings.TAB.repeat(indent + 1));
        ASTNode.join(
                sb,
                indent + 1,
                this.decls,
                AlloyStrings.COMMA
                        + AlloyStrings.SPACE
                        + AlloyStrings.NEWLINE
                        + AlloyStrings.TAB.repeat(indent + 1));
        sb.append(AlloyStrings.NEWLINE + AlloyStrings.RBRACE);
        if (!this.block.isEmpty()) {
            sb.append(AlloyStrings.SPACE);
            this.block.get().toString(sb, indent);
            sb.append(AlloyStrings.SPACE);
        }
    }

    public enum Qual {
        VAR(AlloyStrings.VAR),
        ABSTRACT(AlloyStrings.ABSTRACT),
        PRIVATE(AlloyStrings.PRIVATE),
        LONE(AlloyStrings.LONE),
        ONE(AlloyStrings.ONE),
        SOME(AlloyStrings.SOME);

        public final String label;

        private Qual(String label) {
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

    public sealed interface Rel permits Extends, In, Equal {}

    public static final class Extends extends ASTNode implements Rel {
        public final AlloySigRefExpr sigRef;

        public Extends(Pos pos, AlloySigRefExpr sigRef) {
            super(pos);
            this.sigRef = sigRef;
        }

        public Extends(AlloySigRefExpr sigRef) {
            super();
            this.sigRef = sigRef;
        }

        @Override
        public void toString(StringBuilder sb, int indent) {
            sb.append(AlloyStrings.EXTENDS + AlloyStrings.SPACE);
            ((AlloyVarExpr) this.sigRef).toString(sb, indent);
        }
    }

    public static final class In extends ASTNode implements Rel {
        public final List<AlloySigRefExpr> sigRefs;

        public In(Pos pos, List<AlloySigRefExpr> sigRefs) {
            super(pos);
            this.sigRefs = Collections.unmodifiableList(sigRefs);
        }

        public In(List<AlloySigRefExpr> sigRefs) {
            super();
            this.sigRefs = Collections.unmodifiableList(sigRefs);
        }

        @Override
        public void toString(StringBuilder sb, int indent) {
            sb.append(AlloyStrings.IN + AlloyStrings.SPACE);
            ASTNode.join(
                    sb,
                    indent,
                    this.sigRefs,
                    AlloyStrings.SPACE + AlloyStrings.PLUS + AlloyStrings.SPACE);
        }
    }

    public static final class Equal extends ASTNode implements Rel {
        public final List<AlloySigRefExpr> sigRefs;

        public Equal(Pos pos, List<AlloySigRefExpr> sigRefs) {
            super(pos);
            this.sigRefs = Collections.unmodifiableList(sigRefs);
        }

        public Equal(List<AlloySigRefExpr> sigRefs) {
            super();
            this.sigRefs = Collections.unmodifiableList(sigRefs);
        }

        @Override
        public void toString(StringBuilder sb, int indent) {
            sb.append(AlloyStrings.EQUAL + AlloyStrings.SPACE);
            ASTNode.join(
                    sb,
                    indent,
                    this.sigRefs,
                    AlloyStrings.SPACE + AlloyStrings.PLUS + AlloyStrings.SPACE);
        }
    }
}
