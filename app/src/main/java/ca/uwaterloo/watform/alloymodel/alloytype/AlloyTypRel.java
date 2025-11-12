package ca.uwaterloo.watform.alloymodel.alloytype;

import ca.uwaterloo.watform.alloymodel.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public sealed class AlloyTypRel implements AlloyTyp permits AlloyTypInt {
    // This field represents a UNION of types of relations. For example:
    // sig A {}
    // sig B {}
    // sig C in A + B {}
    // AlloyTypRel.unionRel of AlloySigPara(C) is [["A"], ["B"]]
    // This means C could contain elements from both A and B
    // So it could be used as an A or a B
    // sig D {}
    // (C -> D).unionRel is [["A", "D"], ["B", "D"]]
    // It is assumed that the arity of the elements in unionRel
    // are the same. Otherwise, it doesn't really make sense.
    // I don't think it's even possible to create different
    // arities from the grammar.
    public final Set<List<String>> unionRel;
    public final int arity;

    public AlloyTypRel(Set<List<String>> unionRel) {
        if (unionRel == null) {
            throw AlloyModelImplError.invalidTypRelArg();
        }

        if (unionRel.isEmpty()) {
            throw AlloyModelImplError.invalidTypRelArg();
        }

        List<String> anyRel = unionRel.iterator().next();
        if (anyRel.size() == 0) {
            throw AlloyModelImplError.invalidTypRelArg();
        }
        this.arity = anyRel.size();

        for (List<String> innerList : unionRel) {
            if (innerList == null) {
                throw AlloyModelImplError.invalidTypRelArg();
            }
            if (this.arity != innerList.size()) {
                throw AlloyModelImplError.diffArity();
            }
            for (String s : innerList) {
                if (s == null || s.isBlank()) {
                    throw AlloyModelImplError.invalidTypRelArg();
                }
            }
        }
        this.unionRel =
                unionRel.stream()
                        .map(Collections::unmodifiableList)
                        .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(unionRel);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AlloyTypRel other = (AlloyTypRel) obj;
        if (unionRel == null) {
            if (other.unionRel != null) return false;
        } else if (!unionRel.equals(other.unionRel)) return false;
        return true;
    }
}
