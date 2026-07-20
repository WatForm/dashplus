package ca.uwaterloo.watform.alloymodel.alloytype;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloymodel.AlloyModelImplError;
import java.util.*;

public class AlloyRelationType {
    private Set<AlloyBasicType> type;

    // constructors
    public AlloyRelationType() {
        this.type = emptySet();
    }

    public AlloyRelationType(String s) {
        this.type = new HashSet<>(Set.of(new AlloyBasicType(s)));
    }

    public AlloyRelationType(Set<AlloyBasicType> type) {
        this.type = type;
    }

    // used for building the type
    private void addType(AlloyBasicType newType) {
        this.type.add(newType);
    }

    // accessors
    public int getArity() {
        // there should always be at least one entry
        if (this.type.size() == 0) throw AlloyModelImplError.shouldNotReach();

        // arity is the number of elements in any List
        // (they should all be the same, theoretically)
        return this.type.iterator().next().getArity();
    }

    // type computations
    public Optional<AlloyRelationType> closure() {
        // return nothing if not binary relation
        if (this.getArity() != 2) return Optional.empty();

        AlloyRelationType res = new AlloyRelationType(new HashSet<>(this.type));
        while (true) {
            Set<AlloyBasicType> newClosures = new HashSet<>();
            for (AlloyBasicType type1 : res.type) {
                for (AlloyBasicType type2 : res.type) {
                    Optional<AlloyBasicType> newType = type1.compose(type2);
                    if (newType.isPresent() && !newClosures.contains(newType.get())) {
                        newClosures.add(newType.get());
                    }
                }
                for (AlloyBasicType type2 : this.type) {
                    Optional<AlloyBasicType> newType = type1.compose(type2);
                    if (newType.isPresent() && !newClosures.contains(newType.get())) {
                        newClosures.add(newType.get());
                    }
                }
            }
            if (newClosures.size() == 0) {
                break;
            }
            for (AlloyBasicType newType : newClosures) {
                res.addType(newType);
            }
        }
        if (res.type.size() == 0) return Optional.empty();
        return Optional.of(res);
    }

    public AlloyRelationType extend(AlloyRelationType o) {
        AlloyRelationType res = new AlloyRelationType();
        for (AlloyBasicType meType : this.type) {
            for (AlloyBasicType oType : o.type) {
                res.addType(meType.extend(oType));
            }
        }
        return res;
    }

    public Optional<AlloyRelationType> iden() {
        if (this.getArity() != 1) return Optional.empty();
        Set<AlloyBasicType> types = mapBy(this.type, n -> n.iden().get());
        return Optional.of(new AlloyRelationType(types));
    }

    public Optional<AlloyRelationType> reverse() {
        if (this.getArity() != 2) return Optional.empty();
        Set<AlloyBasicType> types = mapBy(this.type, n -> n.reverse().get());
        return Optional.of(new AlloyRelationType(types));
    }

    public AlloyRelationType union(AlloyRelationType o) {
        AlloyRelationType res = new AlloyRelationType();
        for (AlloyBasicType oType : o.type) {
            res.addType(oType);
        }
        for (AlloyBasicType meType : this.type) {
            res.addType(meType);
        }
        return res;
    }

    public AlloyRelationType compose(AlloyRelationType o) {
        AlloyRelationType res = new AlloyRelationType();
        for (AlloyBasicType meType : this.type) {
            for (AlloyBasicType oType : o.type) {
                Optional<AlloyBasicType> newType = meType.compose(oType);
                if (newType.isPresent()) {
                    res.addType(newType.get());
                }
            }
        }
        return res;
    }

    public Optional<AlloyRelationType> domainRestrict(AlloyRelationType o) {
        AlloyRelationType res = new AlloyRelationType();
        for (AlloyBasicType meType : this.type) {
            for (AlloyBasicType oType : o.type) {
                Optional<AlloyBasicType> newType = meType.domainRestrict(oType);
                if (newType.isPresent()) {
                    res.addType(newType.get());
                    // can't break, meType might be univ
                }
            }
        }
        if (res.type.size() == 0) return Optional.empty();
        return Optional.of(res);
    }

    public Optional<AlloyRelationType> intersect(AlloyRelationType o) {
        AlloyRelationType res = new AlloyRelationType();
        for (AlloyBasicType oType : o.type) {
            for (AlloyBasicType meType : this.type) {
                if (oType.matches(meType)) {
                    res.addType(meType.restrict(oType).get());
                }
            }
        }
        if (res.type.size() == 0) return Optional.empty();
        return Optional.of(res);
    }

    public Optional<AlloyRelationType> rangeRestrict(AlloyRelationType o) {
        AlloyRelationType res = new AlloyRelationType();
        for (AlloyBasicType meType : this.type) {
            for (AlloyBasicType oType : o.type) {
                Optional<AlloyBasicType> newType = meType.rangeRestrict(oType);
                if (newType.isPresent()) {
                    res.addType(newType.get());
                    // can't break, meType might be univ
                }
            }
        }
        if (res.type.size() == 0) return Optional.empty();
        return Optional.of(res);
    }

    // overriding default methods
    @Override
    public String toString() {
        // AlloyBasicTypes separated by commas, with enclosing curly braces
        return "{" + String.join(", ", mapBy(this.type, n -> n.toString())) + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AlloyRelationType)) return false;
        AlloyRelationType other = (AlloyRelationType) o;

        // must have same number of possibilities
        if (this.type.size() != other.type.size()) return false;

        // each type in this must have a match in other
        for (AlloyBasicType meType : this.type) {
            boolean match = false;
            for (AlloyBasicType oType : other.type) {
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
