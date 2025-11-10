package ca.uwaterloo.watform.alloyast.paragraph.sig;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloySigRefExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyVarExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyParagraph;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class AlloySigPara extends AlloyParagraph {
    public final List<Qual> quals;
    public final List<AlloyQnameExpr> qnames;
    public final Optional<Rel> rel;
    public final List<AlloyDecl> decls; // sig's fields
    public final Optional<AlloyBlock> block;

    public AlloySigPara(
            Pos pos,
            List<Qual> quals,
            List<AlloyQnameExpr> qnames,
            Rel rel,
            List<AlloyDecl> decls,
            AlloyBlock block) {
        super(pos);
        this.quals = Collections.unmodifiableList(quals);
        this.qnames = Collections.unmodifiableList(qnames);
        this.rel = Optional.ofNullable(rel);
        this.decls = decls;
        this.block = Optional.ofNullable(block);
    }

    public AlloySigPara(
            List<Qual> quals,
            List<AlloyQnameExpr> qnames,
            Rel rel,
            List<AlloyDecl> decls,
            AlloyBlock block) {
        this(Pos.UNKNOWN, quals, qnames, rel, decls, block);
    }

    public AlloySigPara(
            List<AlloyQnameExpr> qnames, Rel rel, List<AlloyDecl> decls, AlloyBlock block) {
        this(Pos.UNKNOWN, Collections.emptyList(), qnames, rel, decls, block);
    }

    public AlloySigPara(List<AlloyQnameExpr> qnames, List<AlloyDecl> decls, AlloyBlock block) {
        this(Pos.UNKNOWN, Collections.emptyList(), qnames, null, decls, block);
    }

    public AlloySigPara(AlloyQnameExpr qname, List<AlloyDecl> decls, AlloyBlock block) {
        this(
                Pos.UNKNOWN,
                Collections.emptyList(),
                Collections.singletonList(qname),
                null,
                decls,
                block);
    }

    public AlloySigPara(List<AlloyQnameExpr> qnames, AlloyBlock block) {
        this(Pos.UNKNOWN, Collections.emptyList(), qnames, null, Collections.emptyList(), block);
    }

    public AlloySigPara(AlloyQnameExpr qname, AlloyBlock block) {
        this(
                Pos.UNKNOWN,
                Collections.emptyList(),
                Collections.singletonList(qname),
                null,
                Collections.emptyList(),
                block);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        // cannot use ASTNode.join here b/c Qual is not ASTNode; will fail
        // dynamic cast consider changing these Enum to an object and extend
        // ASTNode
        for (Qual qual : this.quals) {
            sb.append(qual.toString() + AlloyStrings.SPACE);
        }
        sb.append(AlloyStrings.SIG);
        sb.append(AlloyStrings.SPACE);
        ASTNode.join(sb, indent, this.qnames, AlloyStrings.COMMA + AlloyStrings.SPACE);
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

    @Override
    public Optional<String> getName() {
        if (this.qnames.size() > 1) {
            throw ImplementationError.methodShouldNotBeCalled(this.pos, "AlloySigPara.getName");
        }
        return Optional.of(this.qnames.get(0).toString());
    }

    public List<AlloySigPara> expand() {
        if (1 == this.qnames.size()) {
            return Collections.singletonList(this);
        }
        List<AlloySigPara> expandedLi = new ArrayList<>();
        for (AlloyQnameExpr qname : this.qnames) {
            expandedLi.add(
                    new AlloySigPara(
                            this.pos,
                            this.quals,
                            Collections.singletonList(qname),
                            this.rel.orElse(null),
                            this.decls,
                            this.block.orElse(null)));
        }
        return expandedLi;
    }
}
