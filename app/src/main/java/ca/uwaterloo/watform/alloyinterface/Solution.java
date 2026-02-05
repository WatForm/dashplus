package ca.uwaterloo.watform.alloyinterface;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.utils.CommonStrings;
import ca.uwaterloo.watform.utils.ImplementationError;
import ca.uwaterloo.watform.utils.Pos;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorFatal;
import edu.mit.csail.sdg.ast.Sig;
import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.parser.CompUtil;
import edu.mit.csail.sdg.translator.A4Solution;
import edu.mit.csail.sdg.translator.A4TupleSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import kodkod.ast.Relation;
import kodkod.instance.Instance;
import kodkod.instance.Tuple;
import kodkod.instance.TupleSet;

public final class Solution {
    private A4Solution a4Solution;
    private final Map<String, Set<List<String>>> map;
    private final CompModule alloyModule;

    public Solution(A4Solution a4Solution, CompModule alloyModule) {
        this.a4Solution = a4Solution;
        this.map = new HashMap<>();
        this.alloyModule = alloyModule;
        if (this.a4Solution.satisfiable()) populateMap();
    }

    public boolean isSat() {
        return this.a4Solution.satisfiable();
    }

    private void throwErrorIfUnsat() {
        // used in the rest of the methods b/c
        // they should not be called if Solution is unsat
        if (!this.isSat()) {
            printStackTrace();
            throw ImplementationError.shouldNotReach();
        }
    }

    // if neither of the above are true then it is unknown
    public boolean contains(String name) {
        this.throwErrorIfUnsat();
        return this.map.containsKey(name);
    }

    public Set<List<String>> get(String name) {
        this.throwErrorIfUnsat();
        return this.map.getOrDefault(name, Collections.emptySet());
    }

    public void next() {
        this.throwErrorIfUnsat();
        this.a4Solution = this.a4Solution.next();
        if (this.isSat()) this.populateMap();
    }

    public EvalRes eval(AlloyExpr alloyExpr) {
        this.throwErrorIfUnsat();
        Object evalResult = null;
        try {
            evalResult =
                    this.a4Solution.eval(
                            CompUtil.parseOneExpression_fromString(
                                    this.alloyModule, alloyExpr.toString()));
        } catch (ErrorFatal alloyJarErrFatal) {
            throw new ImplementationError(alloyJarErrFatal.toString());
        } catch (Err alloyJarErr) {
            throw AlloyInterfaceError.solutionEvalErr(
                    new Pos(alloyJarErr.pos), alloyJarErr.toString());
        }

        if (evalResult instanceof String) {
            // Alloy Analyzer gives back string representation of integers
            return EvalRes.of(Integer.parseInt((String) evalResult));
        } else if (evalResult instanceof Boolean) {
            return EvalRes.of((boolean) evalResult);
        } else if (evalResult instanceof A4TupleSet) {
            return EvalRes.of(
                    (Set<List<String>>)
                            this.convertKKTupleSet(
                                    ((A4TupleSet) evalResult).debugGetKodkodTupleset()));
        } else {
            throw ImplementationError.missingCase("Solution.eval(AlloyExpr alloyExpr)");
        }
    }

    public Set<List<String>> eval(AlloySigPara sigPara) {
        this.throwErrorIfUnsat();
        String sigName = AlloyStrings.THIS + AlloyStrings.SLASH + sigPara.getId().name;
        if (!this.contains(sigName)) {
            return Collections.emptySet();
        }

        Optional<Sig> optionalSig = this.findSigInSolution(sigName);
        if (optionalSig.isEmpty()) return Collections.emptySet();
        Sig sig = optionalSig.get();

        return this.convertKKTupleSet(this.a4Solution.eval(sig).debugGetKodkodTupleset());
    }

    public Set<List<String>> eval(AlloySigPara sigPara, AlloyDecl fieldDecl) {
        this.throwErrorIfUnsat();
        String sigName = AlloyStrings.THIS + AlloyStrings.SLASH + sigPara.getId().name;
        String fieldName = fieldDecl.getName().get();
        if (!this.contains(sigName)) {
            return Collections.emptySet();
        }

        Optional<Sig> optionalSig = this.findSigInSolution(sigName);
        if (optionalSig.isEmpty()) return Collections.emptySet();
        Sig sig = optionalSig.get();

        for (Sig.Field field : sig.getFields()) {
            if (!field.label.equals(fieldName)) continue;
            return this.convertKKTupleSet(this.a4Solution.eval(field).debugGetKodkodTupleset());
        }
        return Collections.emptySet();
    }

    public void writeXML(String filename) {
        this.throwErrorIfUnsat();
        this.a4Solution.writeXML(filename);
    }

    @Override
    public String toString() {
        this.throwErrorIfUnsat();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CommonStrings.DIVIDER + CommonStrings.NEWLINE);
        for (Map.Entry<String, Set<List<String>>> entry : this.map.entrySet()) {
            stringBuilder.append(entry.getKey() + CommonStrings.NEWLINE);
            stringBuilder.append(entry.getValue() + CommonStrings.NEWLINE);
            stringBuilder.append(CommonStrings.DIVIDER + CommonStrings.NEWLINE);
        }
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        this.throwErrorIfUnsat();
        return Objects.hash(this.a4Solution, this.alloyModule, this.map);
    }

    @Override
    public boolean equals(Object obj) {
        this.throwErrorIfUnsat();
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Solution other = (Solution) obj;
        if (a4Solution == null) {
            if (other.a4Solution != null) return false;
        } else if (!a4Solution.equals(other.a4Solution)) return false;
        if (map == null) {
            if (other.map != null) return false;
        } else if (!map.equals(other.map)) return false;
        return true;
    }

    private void populateMap() {
        this.throwErrorIfUnsat();
        this.map.clear();
        Instance instance = this.a4Solution.debugExtractKInstance();
        for (Relation r : instance.relations()) {
            this.map.put(r.name(), this.convertKKTupleSet(instance.tuples(r)));
        }
    }

    private Set<List<String>> convertKKTupleSet(TupleSet tupleSet) {
        this.throwErrorIfUnsat();
        Set<List<String>> set = new HashSet<>();
        for (Tuple tuple : tupleSet) {
            List<String> li = new ArrayList<>();
            for (int i = 0; i < tuple.arity(); i++) {
                li.add((String) tuple.atom(i));
            }
            set.add(Collections.unmodifiableList(li));
        }
        return Collections.unmodifiableSet(set);
    }

    private Optional<Sig> findSigInSolution(String sigName) {
        this.throwErrorIfUnsat();
        for (Sig sig : this.a4Solution.getAllReachableSigs()) {
            if (sig.label.equals(sigName)) {
                return Optional.of(sig);
            }
        }
        return Optional.empty();
    }

    public record EvalRes(Integer intVal, Boolean boolVal, Set<List<String>> setVal) {
        public static EvalRes of(int integer) {
            return new EvalRes(integer, null, null);
        }

        public static EvalRes of(boolean bool) {
            return new EvalRes(null, bool, null);
        }

        public static EvalRes of(Set<List<String>> set) {
            return new EvalRes(null, null, set);
        }

        public boolean isInt() {
            return intVal != null;
        }

        public boolean isBool() {
            return boolVal != null;
        }

        public boolean isSet() {
            return setVal != null;
        }
    }
}
