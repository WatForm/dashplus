package ca.uwaterloo.watform.alloyast.paragraph.sig;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.reqNonNull;
import static ca.uwaterloo.watform.utils.ImplementationError.nullField;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/*
 * Use the following to import to avoid long names
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara.*;

 */
public final class AlloySigPara extends AlloyPara {
    public final List<Qual> quals;
    public final List<AlloyQnameExpr> qnames;
    public final Optional<Rel> rel;
    public final List<AlloyDecl> fields; // sig's fields
    public final Optional<AlloyBlock> block;

    public AlloySigPara(
            Pos pos,
            List<Qual> quals,
            List<AlloyQnameExpr> qnames,
            Rel rel,
            List<AlloyDecl> fields,
            AlloyBlock block) {
        super(pos);
        this.quals = Collections.unmodifiableList(quals);
        this.qnames = Collections.unmodifiableList(qnames);
        this.rel = Optional.ofNullable(rel);
        this.fields = fields;
        this.block = Optional.ofNullable(block);
        if (this.quals.contains(Qual.LONE) && this.quals.contains(Qual.ONE)) {
            throw AlloyCtorError.sigContradictQuals(pos, "lone", "one");
        }
        if (this.quals.contains(Qual.LONE) && this.quals.contains(Qual.SOME)) {
            throw AlloyCtorError.sigContradictQuals(pos, "lone", "some");
        }
        if (this.quals.contains(Qual.ONE) && this.quals.contains(Qual.SOME)) {
            throw AlloyCtorError.sigContradictQuals(pos, "one", "some");
        }
        if (this.isSubset() && this.quals.contains(Qual.ABSTRACT)) {
            throw AlloyCtorError.sigAbsSubset(pos);
        }
        reqNonNull(
                nullField(pos, this), this.quals, this.qnames, this.rel, this.fields, this.block);
    }

    public AlloySigPara(
            List<Qual> quals,
            List<AlloyQnameExpr> qnames,
            Rel rel,
            List<AlloyDecl> fields,
            AlloyBlock block) {
        this(Pos.UNKNOWN, quals, qnames, rel, fields, block);
    }

    public AlloySigPara(
            List<AlloyQnameExpr> qnames, Rel rel, List<AlloyDecl> fields, AlloyBlock block) {
        this(Pos.UNKNOWN, Collections.emptyList(), qnames, rel, fields, block);
    }

    public AlloySigPara(List<AlloyQnameExpr> qnames, List<AlloyDecl> fields, AlloyBlock block) {
        this(Pos.UNKNOWN, Collections.emptyList(), qnames, null, fields, block);
    }

    public AlloySigPara(String s, List<AlloyDecl> fields, AlloyBlock block) {
        this(
                Pos.UNKNOWN,
                Collections.emptyList(),
                Collections.singletonList(new AlloyQnameExpr(s)),
                null,
                fields,
                block);
    }

    public AlloySigPara(AlloyQnameExpr qname, List<AlloyDecl> fields, AlloyBlock block) {
        this(
                Pos.UNKNOWN,
                Collections.emptyList(),
                Collections.singletonList(qname),
                null,
                fields,
                block);
    }

    public AlloySigPara(AlloyQnameExpr qname, List<AlloyDecl> fields) {
        this(
                Pos.UNKNOWN,
                Collections.emptyList(),
                Collections.singletonList(qname),
                null,
                fields,
                null);
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

    public AlloySigPara(AlloyQnameExpr qname) {
        this(
                Pos.UNKNOWN,
                Collections.emptyList(),
                Collections.singletonList(qname),
                null,
                Collections.emptyList(),
                null);
    }

    public AlloySigPara(String label) {
        this(
                Pos.UNKNOWN,
                Collections.emptyList(),
                Collections.singletonList(new AlloyQnameExpr(label)),
                null,
                Collections.emptyList(),
                null);
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        // cannot use ASTNode.join here b/c Qual is not ASTNode; will fail
        // dynamic cast. consider changing these Enum to an object and extend
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
                this.fields,
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

    @Override
    public void pp(PrintContext pCtx) {
        for (Qual qual : this.quals) {
            pCtx.append(qual.toString() + AlloyStrings.SPACE);
        }
        pCtx.append(SIG + SPACE);
        pCtx.appendList(this.qnames, COMMA);
        pCtx.append(SPACE);
        if (this.rel.isPresent()) {
            ((ASTNode) this.rel.get()).pp(pCtx);
            pCtx.append(SPACE);
        }
        pCtx.append(LBRACE);
        if (!this.fields.isEmpty()) {
            pCtx.brkNoSpace();
            pCtx.appendList(this.fields, COMMA);
            pCtx.brkNoSpaceNoIndent();
        }
        pCtx.append(RBRACE + SPACE);
        if (this.block.isPresent()) {
            this.block.get().pp(pCtx);
        }
    }

    @Override
    public Optional<String> getName() {
        if (this.qnames.size() > 1) {
            throw ImplementationError.methodShouldNotBeCalled(
                    this.pos,
                    "AlloySigPara.getName. This should not be called because the "
                            + "signature doesn't have a single name, but multiple. "
                            + "See AlloySigPara.expand(). ");
        }
        return Optional.of(this.qnames.get(0).toString());
    }

    /**
     * Expand AlloySigPara.qnames and AlloyDecl in AlloySigPara.fields
     *
     * @return List<AlloySigPara>
     */
    public List<AlloySigPara> expand() {
        // expand this.fields
        List<AlloyDecl> expandedDecls = new ArrayList<>();
        for (AlloyDecl field : this.fields) {
            expandedDecls.addAll(field.expand());
        }
        // expand this.qnames
        List<AlloySigPara> expandedSigs = new ArrayList<>();
        for (AlloyQnameExpr qname : this.qnames) {
            expandedSigs.add(
                    new AlloySigPara(
                            this.pos,
                            this.quals,
                            Collections.singletonList(qname),
                            this.rel.orElse(null),
                            expandedDecls,
                            this.block.orElse(null)));
        }
        return expandedSigs;
    }

    public boolean isTopLevel() {
        return this.rel.isEmpty();
    }

    public boolean isSubsig() {
        return this.rel.isPresent() && this.rel.get().getClass() == Extends.class;
    }

    public boolean isSubset() {
        return this.rel.isPresent()
                && (this.rel.get().getClass() == In.class
                        || this.rel.get().getClass() == Equal.class);
    }

    public boolean isVar() {
        return this.quals.contains(Qual.VAR);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.quals, this.qnames, this.rel, this.fields, this.block);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloySigPara other = (AlloySigPara) obj;
        if (quals == null) {
            if (other.quals != null) return false;
        } else if (!quals.equals(other.quals)) return false;
        if (qnames == null) {
            if (other.qnames != null) return false;
        } else if (!qnames.equals(other.qnames)) return false;
        if (rel == null) {
            if (other.rel != null) return false;
        } else if (!rel.equals(other.rel)) return false;
        if (fields == null) {
            if (other.fields != null) return false;
        } else if (!fields.equals(other.fields)) return false;
        if (block == null) {
            if (other.block != null) return false;
        } else if (!block.equals(other.block)) return false;
        return true;
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
            if (this.sigRef instanceof AlloySigIntExpr
                    || this.sigRef instanceof AlloySeqIntExpr
                    || this.sigRef instanceof AlloyStringExpr
                    || this.sigRef instanceof AlloyNoneExpr) {
                throw AlloyCtorError.sigCannotExtend(pos, sigRef.toString());
            }
            reqNonNull(nullField(pos, this), this.sigRef);
        }

        public Extends(AlloySigRefExpr sigRef) {
            this(Pos.UNKNOWN, sigRef);
        }

        @Override
        public void toString(StringBuilder sb, int indent) {
            sb.append(AlloyStrings.EXTENDS + AlloyStrings.SPACE);
            ((AlloyVarExpr) this.sigRef).toString(sb, indent);
        }

        @Override
        public void pp(PrintContext pCtx) {
            pCtx.append(EXTENDS);
            pCtx.brk();
            ((AlloyVarExpr) this.sigRef).pp(pCtx);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.sigRef);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Extends other = (Extends) obj;
            if (sigRef == null) {
                if (other.sigRef != null) return false;
            } else if (!sigRef.equals(other.sigRef)) return false;
            return true;
        }
    }

    public static final class In extends ASTNode implements Rel {
        public final List<AlloySigRefExpr> sigRefs;

        public In(Pos pos, List<AlloySigRefExpr> sigRefs) {
            super(pos);
            this.sigRefs = Collections.unmodifiableList(sigRefs);
            reqNonNull(nullField(pos, this), this.sigRefs);
        }

        public In(List<AlloySigRefExpr> sigRefs) {
            this(Pos.UNKNOWN, sigRefs);
        }

        public In(AlloySigRefExpr sigRef) {
            this(Pos.UNKNOWN, Collections.singletonList(sigRef));
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

        @Override
        public void pp(PrintContext pCtx) {
            pCtx.append(IN);
            pCtx.brk();
            pCtx.appendList(this.sigRefs, SPACE + PLUS);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.sigRefs);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            In other = (In) obj;
            if (sigRefs == null) {
                if (other.sigRefs != null) return false;
            } else if (!sigRefs.equals(other.sigRefs)) return false;
            return true;
        }
    }

    public static final class Equal extends ASTNode implements Rel {
        public final List<AlloySigRefExpr> sigRefs;

        public Equal(Pos pos, List<AlloySigRefExpr> sigRefs) {
            super(pos);
            this.sigRefs = Collections.unmodifiableList(sigRefs);
            reqNonNull(nullField(pos, this), this.sigRefs);
        }

        public Equal(List<AlloySigRefExpr> sigRefs) {
            this(Pos.UNKNOWN, sigRefs);
        }

        public Equal(AlloySigRefExpr sigRef) {
            this(Pos.UNKNOWN, Collections.singletonList(sigRef));
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

        @Override
        public void pp(PrintContext pCtx) {
            pCtx.append(EQUAL);
            pCtx.brk();
            pCtx.appendList(this.sigRefs, SPACE + PLUS);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.sigRefs);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Equal other = (Equal) obj;
            if (sigRefs == null) {
                if (other.sigRefs != null) return false;
            } else if (!sigRefs.equals(other.sigRefs)) return false;
            return true;
        }
    }
}
