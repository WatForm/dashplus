package ca.uwaterloo.watform.alloymodel.alloytype;

import static ca.uwaterloo.watform.alloymodel.Builtins.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import java.util.*;

public class AlloyUnionType {
    // each member of the set is a possible overload
    private Set<AlloyRelationType> type;

    // constructors
    AlloyUnionType() {
        this.type = emptySet();
    }

    public AlloyUnionType(String s) {
        this.type = new HashSet<>(Set.of(new AlloyRelationType(s)));
    }

    AlloyUnionType(Set<AlloyRelationType> typeSet) {
        this.type = typeSet;
    }

    // used for building the type
    private void addOverload(AlloyRelationType newType) {
        this.type.add(newType);
    }

    // accessors
    public int getArity() {
        // arity is 0 if this is empty
        if (this.type.size() == 0) return 0;

        // arity is arity of all AlloyRelationTypes, if they are the same
        // if they are not all the same, arity is -1
        int arity = -1;
        for (AlloyRelationType t : this.type) {
            int temp = t.getArity();
            if (arity != -1 && temp != arity) {
                return -1;
            } else if (arity == -1) {
                arity = temp;
            }
        }
        return arity;
    }

    public boolean isEmpty() {
        return this.type.size() == 0;
    }

    public boolean isAmbiguous() {
        // TODO: should 0 be excluded?
        return this.type.size() != 1;
    }

    public boolean isBoolean() {
        return this.type.equals(BOOLEAN_TYPE);
    }

    public boolean isInt() {
        return this.type.equals(INTEGER_TYPE);
    }

    public boolean isNone() {
        return this.type.equals(NONE_TYPE);
    }

    public boolean isUniverse() {
        return this.type.equals(UNIVERSE_TYPE);
    }

    // type computations
    // ^this
    public AlloyUnionType closure() {
        AlloyUnionType res = new AlloyUnionType();
        for (AlloyRelationType t : this.type) {
            Optional<AlloyRelationType> closureT = t.closure();
            if (closureT.isPresent()) res.addOverload(closureT.get());
        }
        return res;
    }

    // o <: this
    public AlloyUnionType domainRestrict(AlloyUnionType o) {
        AlloyUnionType res = new AlloyUnionType();
        for (AlloyRelationType meType : this.type) {
            for (AlloyRelationType oType : o.type) {
                Optional<AlloyRelationType> newType = meType.domainRestrict(oType);
                if (newType.isPresent()) res.addOverload(newType.get());
            }
        }
        return res;
    }

    // this -> o
    public AlloyUnionType extend(AlloyUnionType o) {
        AlloyUnionType res = new AlloyUnionType();
        for (AlloyRelationType meType : this.type) {
            for (AlloyRelationType oType : o.type) {
                res.addOverload(meType.extend(oType));
            }
        }
        return res;
    }

    // iden this
    public AlloyUnionType iden() {
        AlloyUnionType res = new AlloyUnionType();
        for (AlloyRelationType t : this.type) {
            Optional<AlloyRelationType> idenT = t.iden();
            if (idenT.isPresent()) res.addOverload(idenT.get());
        }
        return res;
    }

    // this & o
    public AlloyUnionType intersect(AlloyUnionType o) {
        AlloyUnionType res = new AlloyUnionType();
        for (AlloyRelationType oType : o.type) {
            for (AlloyRelationType meType : this.type) {
                Optional<AlloyRelationType> newType = meType.intersect(oType);
                if (newType.isPresent()) res.addOverload(newType.get());
            }
        }
        return res;
    }

    // this :> o
    public AlloyUnionType rangeRestrict(AlloyUnionType o) {
        AlloyUnionType res = new AlloyUnionType();
        for (AlloyRelationType meType : this.type) {
            for (AlloyRelationType oType : o.type) {
                Optional<AlloyRelationType> newType = meType.rangeRestrict(oType);
                if (newType.isPresent()) res.addOverload(newType.get());
            }
        }
        return res;
    }

    // ~this
    public AlloyUnionType reverse() {
        AlloyUnionType res = new AlloyUnionType();
        for (AlloyRelationType t : this.type) {
            Optional<AlloyRelationType> revT = t.reverse();
            if (revT.isPresent()) res.addOverload(revT.get());
        }
        return res;
    }

    // this + o
    public AlloyUnionType union(AlloyUnionType o) {
        AlloyUnionType res = new AlloyUnionType();
        for (AlloyRelationType oType : o.type) {
            for (AlloyRelationType meType : this.type) {
                res.addOverload(meType.union(oType));
            }
        }
        return res;
    }

    // overriding default methods
    @Override
    public String toString() {
        // AlloyRelationTypes separated by " U ", with enclosing square
        // braces if there are more than one AlloyRelationTypes
        String s = String.join(" U ", mapBy(this.type, n -> n.toString()));
        if (this.type.size() > 1) {
            s = "[" + s + "]";
        }
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AlloyUnionType)) return false;
        AlloyUnionType other = (AlloyUnionType) o;

        // must have same number of possibilities
        if (this.type.size() != other.type.size()) return false;

        // each type in this must have a match in other
        for (AlloyRelationType meType : this.type) {
            boolean match = false;
            for (AlloyRelationType oType : other.type) {
                if (meType.equals(oType)) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
