package ca.uwaterloo.watform.alloymodel.alloytype;

import static ca.uwaterloo.watform.alloymodel.Builtins.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import java.util.*;

public class AlloyBasicType {
    private List<String> type;

    // constructors
    public AlloyBasicType() {
        this.type = emptyList();
    }

    public AlloyBasicType(String s) {
        this.type = new ArrayList<>(List.of(s));
    }

    public AlloyBasicType(List<String> type) {
        this.type = type;
    }

    // used for building the type
    private void addType(String newType) {
        this.type.add(newType);
    }

    public AlloyBasicType copy() {
        return new AlloyBasicType(new ArrayList<>(this.type));
    }

    // accessors
    public int getArity() {
        // arity is the number of elements in type
        return this.type.size();
    }

    public String first() {
        return this.type.get(0);
    }

    public String last() {
        return this.type.get(this.type.size() - 1);
    }

    // utilities
    private static boolean matches(String t1, String t2) {
        if (t1.equals(UNIVERSE_TYPE_STR) || t2.equals(UNIVERSE_TYPE_STR)) return true;
        return t1.equals(t2);
    }

    private static String restrict(String t1, String t2) {
        if (t1.equals(UNIVERSE_TYPE_STR)) return t2;
        return t1;
    }

    // type computations
    public Optional<AlloyBasicType> omitFirst() {
        if (this.getArity() < 2) return Optional.empty();
        return Optional.of(new AlloyBasicType(this.type.subList(1, this.type.size())));
    }

    public Optional<AlloyBasicType> omitLast() {
        if (this.getArity() < 2) return Optional.empty();
        return Optional.of(new AlloyBasicType(allButLast(this.type)));
    }

    public Optional<AlloyBasicType> compose(AlloyBasicType o) {
        // return nothing if first and last atoms do not match
        if (!matches(this.last(), o.first())) return Optional.empty();
        Optional<AlloyBasicType> dom = this.omitLast();
        Optional<AlloyBasicType> rng = o.omitFirst();
        // return nothing if either AlloyBasicType had arity < 2
        if (!dom.isPresent() || !rng.isPresent()) return Optional.empty();
        return Optional.of(dom.get().extend(rng.get()));
    }

    public Optional<AlloyBasicType> domainRestrict(AlloyBasicType o) {
        // return nothing if no match
        if (!matches(this.first(), o.first())) return Optional.empty();

        AlloyBasicType res = this.copy();
        // replace last element with the more restrictive of the 2 possible bounds
        // (if one is univ, we cannot necessarily use that if the other is stricter)
        res.type.set(0, restrict(this.first(), o.first()));
        return Optional.of(res);
    }

    public AlloyBasicType extend(AlloyBasicType o) {
        // build a copy
        AlloyBasicType res = new AlloyBasicType();
        for (String s : this.type) {
            res.addType(s);
        }
        // add elem(s)
        for (String s : o.type) {
            res.addType(s);
        }
        return res;
    }

    public Optional<AlloyBasicType> iden() {
        // return nothing if this is not unary
        if (this.type.size() != 1) return Optional.empty();
        return Optional.of(new AlloyBasicType(List.of(this.first(), this.first())));
    }

    public boolean matches(AlloyBasicType o) {
        if (this.getArity() != o.getArity()) return false;
        for (int i = 0; i < this.type.size(); ++i) {
            if (!matches(this.type.get(i), o.type.get(i))) return false;
        }
        return true;
    }

    public Optional<AlloyBasicType> rangeRestrict(AlloyBasicType o) {
        // return nothing if no match
        if (!matches(this.last(), o.first())) return Optional.empty();

        AlloyBasicType res = this.copy();
        // replace last element with the more restrictive of the 2 possible bounds
        // (if one is univ, we cannot necessarily use that if the other is stricter)
        res.type.set(res.type.size() - 1, restrict(this.last(), o.first()));
        return Optional.of(res);
    }

    public Optional<AlloyBasicType> restrict(AlloyBasicType o) {
        // must have same arity
        if (this.getArity() != o.getArity()) return Optional.empty();

        AlloyBasicType res = new AlloyBasicType();
        for (int i = 0; i < this.type.size(); ++i) {
            res.addType(restrict(this.type.get(i), o.type.get(i)));
        }
        return Optional.of(res);
    }

    public Optional<AlloyBasicType> reverse() {
        // return nothing if this is not binary
        if (this.type.size() != 2) return Optional.empty();
        return Optional.of(new AlloyBasicType(List.of(this.last(), this.first())));
    }

    // overriding default methods
    @Override
    public String toString() {
        // String types separated by commas, with enclosing round braces
        return "(" + String.join(", ", this.type) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AlloyBasicType)) return false;
        AlloyBasicType other = (AlloyBasicType) o;

        return this.type.equals(other.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
