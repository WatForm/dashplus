package ca.uwaterloo.watform.alloymodel.alloytype;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import java.util.*;

public class AlloyUnionType {
    // certain strings are fixed and represent known types
    public static final String VARIABLE_TYPE_STR = "[Variable]";
    public static final String FORMULA_TYPE_STR = "[Formula]";
    public static final String NONE_TYPE_STR = "[None]";
    public static final String INTEGER_TYPE_STR = "[String]";
    public static final String UNIV_TYPE_STR = "[Univ]";
    public static final String IDEN_TYPE_STR = "[Iden]";
    public static final String INVALID_TYPE_STR = "[Invalid]";

    // each member of the list is a possible overload
    private List<AlloyRelationType> type;

    // constructors
    AlloyUnionType() {
        this.type = emptyList();
    }

    public AlloyUnionType(String s) {
        this.type = new ArrayList<>(List.of(new AlloyRelationType(s)));
    }

    AlloyUnionType(List<AlloyRelationType> typeList) {
        this.type = typeList;
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
        return this.equals(new AlloyUnionType(FORMULA_TYPE_STR));
    }

    public boolean isInt() {
        return this.equals(new AlloyUnionType(INTEGER_TYPE_STR));
    }

    public boolean isNone() {
        return this.equals(new AlloyUnionType(NONE_TYPE_STR));
    }

    public boolean isUniverse() {
        return this.equals(new AlloyUnionType(UNIV_TYPE_STR));
    }

    // type computations
    public AlloyUnionType satisfyParam(AlloyBasicType b) {
        AlloyUnionType res = new AlloyUnionType();
        for (AlloyRelationType meType : this.type) {
            Optional<AlloyRelationType> newType = meType.satisfyParam(b);
            if (newType.isPresent()) res.addOverload(newType.get());
        }
        return res;
    }

    public void applyArity(int arity) {
        AlloyUnionType res = new AlloyUnionType();
        for (AlloyRelationType t : this.type) {
            if (t.getArity() == arity) res.addOverload(t);
        }
    }

    public void applyEqualArity(AlloyUnionType o) {
        AlloyUnionType meNew = new AlloyUnionType();
        AlloyUnionType oNew = new AlloyUnionType();
        for (AlloyRelationType meType : this.type) {
            for (AlloyRelationType oType : o.type) {
                if (meType.getArity() == oType.getArity()) {
                    meNew.addOverload(meType);
                    break;
                }
            }
        }
        for (AlloyRelationType oType : o.type) {
            for (AlloyRelationType meType : this.type) {
                if (meType.getArity() == oType.getArity()) {
                    oNew.addOverload(meType);
                    break;
                }
            }
        }
        this.type = meNew.type;
        o.type = oNew.type;
    }

    // ^this
    public AlloyUnionType closure() {
        AlloyUnionType res = new AlloyUnionType();
        for (AlloyRelationType t : this.type) {
            Optional<AlloyRelationType> closureT = t.closure();
            if (closureT.isPresent()) res.addOverload(closureT.get());
        }
        return res;
    }

    // *this
    public AlloyUnionType closureRefl() {
        AlloyUnionType res = new AlloyUnionType();
        for (AlloyRelationType t : this.type) {
            Optional<AlloyRelationType> closureT = t.closureRefl();
            if (closureT.isPresent()) res.addOverload(closureT.get());
        }
        return res;
    }

    // this.o
    public AlloyUnionType compose(AlloyUnionType o) {
        AlloyUnionType res = new AlloyUnionType();
        for (AlloyRelationType meType : this.type) {
            for (AlloyRelationType oType : o.type) {
                Optional<AlloyRelationType> newType = meType.compose(oType);
                if (newType.isPresent()) res.addOverload(newType.get());
            }
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
        if (this.type.size() == 0) {
            res = o; // KSE: is this actually supposed to be valid?
        } else if (o.type.size() == 0) {
            res.type = this.type;
        } else {
            for (AlloyRelationType meType : this.type) {
                for (AlloyRelationType oType : o.type) {
                    res.addOverload(meType.extend(oType));
                }
            }
        }
        return res;
    }

    // iden this
    public AlloyUnionType iden() {
        AlloyUnionType res = new AlloyUnionType();
        for (AlloyRelationType t : this.type) {
            res.addOverload(t.duplicate());
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

    public AlloyUnionType omitFirst() {
        AlloyUnionType res = new AlloyUnionType();
        for (AlloyRelationType t : this.type) {
            Optional<AlloyRelationType> newType = t.omitFirst();
            if (newType.isPresent()) res.addOverload(newType.get());
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

    public AlloyUnionType remove(AlloyUnionType o) {
        AlloyUnionType res = new AlloyUnionType();
        for (AlloyRelationType meType : this.type) {
            for (AlloyRelationType oType : o.type) {
                Optional<AlloyRelationType> newType = meType.remove(oType);
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
                Optional<AlloyRelationType> unionT = meType.union(oType);
                if (unionT.isPresent()) res.addOverload(unionT.get());
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
