package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.AlloyStrings.Kind;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class Qname {

    public final String nameSpace;
    public final String sigParent;
    public final String name;

    private Qname(String nameSpace, String sigParent, String name) {

        // nameSpace can be empty -> that means UNKNOWN
        if (name.isBlank()) {
            throw AlloyModelImplError.blankStringNotAllowed();
        }
        this.nameSpace = nameSpace;
        // sigParent is only used for fields
        this.sigParent = sigParent;
        this.name = name;
    }

    // if name has a nameSpace use that nameSpace
    // o/w use THIS_NAMESPACE
    public static Qname thisQname(String name) {
        String newName;
        String nameSpace;
        if (name.contains(AlloyStrings.SLASH)) {
            newName = name(name);
            nameSpace = nameSpace(name);
        } else {
            newName = name;
            nameSpace = THIS_NAMESPACE;
        }
        return new Qname(nameSpace, null, newName);
    }

    // if name has a nameSpace use that nameSpace
    // o/w use UNKNOWN_NAMESPACE
    public static Qname unknownQname(String name) {
        String newName;
        String nameSpace;
        if (name.contains(AlloyStrings.SLASH)) {
            newName = name(name);
            nameSpace = nameSpace(name);
        } else {
            newName = name;
            nameSpace = UNKNOWN_NAMESPACE;
        }

        return new Qname(nameSpace, null, newName);
    }

    public static Qname nameSpaceQname(String nameSpace, String name) {
        if (name.contains(AlloyStrings.SLASH)) {
            throw AlloyModelImplError.qnameNameCannotHaveSlash(name);
        }
        return new Qname(nameSpace, null, name);
    }

    public static Qname fieldQname(String nameSpace, String sigParent, String name) {
        if (name.contains(AlloyStrings.SLASH)) {
            throw AlloyModelImplError.qnameNameCannotHaveSlash(name);
        }
        return new Qname(nameSpace, sigParent, name);
    }

    // only works for sigs/pred/funs -- not fields
    String fullName() {
        assert (this.sigParent.isEmpty());
        return this.nameSpace + "/" + this.name;
    }

    /*
    @Override
    String toString() {
        return this.fullName();
    }
    */

    protected static String UNKNOWN_NAMESPACE = "?";
    public static String THIS_NAMESPACE = AlloyStrings.THIS;

    protected static String[] splitAtLastSlash(String s) {
        int i = s.lastIndexOf('/');
        if (i == -1) {
            return new String[] {UNKNOWN_NAMESPACE, s};
        }
        return new String[] {s.substring(0, i), s.substring(i + 1)};
    }

    private static String nameSpace(String s) {
        String[] parts = splitAtLastSlash(s);
        return parts[0];
    }

    private static String name(String s) {
        String[] parts = splitAtLastSlash(s);
        return parts[1];
    }

    /*
    public static Qname thisQname(String s) {
        return new Qname(THIS_NAMESPACE, s);
    }


    public static Qname Qname(String name) {
        return new Qname(name);
    }

    public static Qname Qname(String nameSpace, String name) {
        return new Qname(nameSpace, name);
    }

    public static Qname Qname(String nameSpace, String sigParent, String name) {
        return new Qname(nameSpace, sigParent, name);
    }
    */
    public boolean isUnknownNameSpace() {
        return this.nameSpace.equals(UNKNOWN_NAMESPACE);
    }

    public AlloyExpr toAlloyExpr(Pos p, Kind k) {
        assert (this.nameSpace != UNKNOWN_NAMESPACE);
        if (k == Kind.SIG) {
            assert (this.sigParent == null);
            return SigVar(p, List.of(this.nameSpace, this.name));
        } else if (k == Kind.FIELD) {
            return AlloyDomainRes(
                    SigVar(p, List.of(this.nameSpace, this.sigParent)),
                    FieldVar(p, List.of(this.name)));
        } else if (k == Kind.PREDFUN) {
            return PredFunVar(p, List.of(this.nameSpace, this.name));
        } else {
            // can't handle Kind.UNKNOWN
            throw ImplementationError.shouldNotReach();
        }
    }

    public AlloyExpr toAlloyExpr(Kind k) {
        return this.toAlloyExpr(Pos.UNKNOWN, k);
    }

    public static AlloyQnameExpr SigVar(Pos p, List<String> names) {
        return new AlloyQnameExpr(p, mapBy(names, v -> new AlloyNameExpr(v)), Kind.SIG);
    }

    public static AlloyQnameExpr FieldVar(Pos p, List<String> names) {
        return new AlloyQnameExpr(p, mapBy(names, v -> new AlloyNameExpr(v)), Kind.FIELD);
    }

    public static AlloyQnameExpr PredFunVar(Pos p, List<String> names) {
        return new AlloyQnameExpr(p, mapBy(names, v -> new AlloyNameExpr(v)), Kind.PREDFUN);
    }

    @Override
    public String toString() {
        return "["
                + ((sigParent == null)
                        ? nameSpace + ", " + name
                        : nameSpace + ", " + sigParent + ", " + name)
                + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Qname other)) {
            return false;
        }
        return Objects.equals(nameSpace, other.nameSpace)
                && Objects.equals(sigParent, other.sigParent)
                && Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameSpace, sigParent, name);
    }

    public static List<Qname> possibleMatches(List<Qname> keys, Qname qname) {
        return keys.stream()
                .filter(
                        q ->
                                q.name.equals(qname.name)
                                        & (q.sigParent.equals(qname.sigParent)
                                                || qname.sigParent == null)
                                        & (q.nameSpace.equals(qname.nameSpace)
                                                || qname.nameSpace.equals(UNKNOWN_NAMESPACE)))
                .toList();
    }
    /*
    public static AlloyQnameExpr SigVar(Pos p, List<String> names) {
        return new AlloyQnameExpr(p, mapBy(names, v -> new AlloyQnameExpr(v)), Kind.SIG);
    }

    public static AlloyQnameExpr FieldVar(Pos p, List<String> names) {
        return new AlloyQnameExpr(p, mapBy(names, v -> new AlloyQnameExpr(v)), Kind.FIELD);
    }

    public static AlloyQnameExpr PredFunVar(Pos p, List<String> names) {
        return new AlloyQnameExpr(p, mapBy(names, v -> new AlloyQnameExpr(v)), Kind.PREDFUN);
    }
    */

}
