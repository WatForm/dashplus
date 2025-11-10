package ca.uwaterloo.watform.dashmodel;

// NADTODO: what if var and buffer have the same name!!!

import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.utils.Pos;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class VarTable {

    // stores Var, Buffer Decls in a HashMap based on the FQN

    // LinkedHashMap so order of keySet is consistent
    // Alloy requires declaration before use for variables
    private LinkedHashMap<String, VarElement> vt;
    private String tableName = "Var";

    public VarTable() {
        this.vt = new LinkedHashMap<String, VarElement>();
    }

    public class VarElement {
        public IntEnvKind kind;
        public List<DashParam> params;
        // private List<Integer> paramsIdx;
        public AlloyExpr typ;

        public VarElement(
                IntEnvKind k,
                List<DashParam> prms,
                // List<Integer> prmsIdx,
                AlloyExpr t) {
            assert (prms != null);
            this.kind = k;
            this.params = prms;
            // this.paramsIdx = prmsIdx;
            this.typ = t;
        }

        public String toString() {
            String s = new String();
            s += "kind: " + kind + "\n";
            s += "params: " + NoneStringIfNeeded(params) + "\n";
            // s += "paramsIdx: "+ NoneStringIfNeeded(paramsIdx) +"\n";
            s += "typ: " + typ.toString() + "\n";
            return s;
        }

        public void setType(AlloyExpr typ) {
            this.typ = typ;
        }
    }

    public void add(Pos pos, String vfqn, IntEnvKind k, List<DashParam> prms, AlloyExpr t) {
        assert (prms != null);
        if (vt.containsKey(vfqn)) {
            DashModelErrors.duplicateName(pos, "var", vfqn);
        } else if (hasPrime(vfqn)) {
            DashModelErrors.nameShouldNotBePrimed(pos, vfqn);
        } else {
            vt.put(vfqn, new VarElement(k, prms, t));
        }
    }

    public void add(String vfqn, IntEnvKind k, List<DashParam> prms, AlloyExpr t) {
        add(Pos.UNKNOWN, vfqn, k, prms, t);
    }

    public String toString() {
        String s = new String("VAR TABLE\n");
        for (String k : vt.keySet()) {
            s += " ----- \n";
            s += k + "\n";
            s += vt.get(k).toString();
        }
        return s;
    }

    // so we can treat this as a table
    // to the outside world
    public VarElement get(String vfqn) {
        return vt.get(vfqn);
    }

    public List<String> keySet() {
        return setToList(vt.keySet());
    }

    public boolean isEmpty() {
        return vt.isEmpty();
    }

    // getters
    public boolean contains(String vfqn) {
        return (vt.containsKey(vfqn));
    }

    public boolean isInternal(String vfqn) {
        return (vt.get(vfqn).kind == IntEnvKind.INT);
    }

    // group getters
    public List<String> getAllVarNames() {
        return new ArrayList<String>(vt.keySet());
    }

    public List<String> getVarsOfState(String sfqn) {
        // return all events declared in this state
        // will have the sfqn as a prefix
        return vt.keySet().stream()
                // prefix of vfqn are state names
                .filter(i -> DashFQN.chopPrefixFromFQN(i).equals(sfqn))
                .collect(Collectors.toList());
    }

    public List<String> getAllInternalBufferNames() {
        return getAllVarNames().stream().filter(i -> isInternal(i)).collect(Collectors.toList());
    }
}
