package ca.uwaterloo.watform.dashmodel;

// NADTODO: what if var and buffer have the same name!!!

import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashFile;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.DashStrings.IntEnvKind;
import ca.uwaterloo.watform.utils.Pos;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class VarsDM extends StatesDM {

    // stores Var, Buffer Decls in a HashMap based on the FQN

    // LinkedHashMap so order of keySet is consistent
    // Alloy requires declaration before use for variables
    private LinkedHashMap<String, VarEntry> vt = new LinkedHashMap<String, VarEntry>();

    // private String tableName = "Var"; // TODO this is never used and is not visible anywhere else

    public VarsDM() {
        super();
    }

    public VarsDM(DashFile d) {
        super(d);
    }

    // individual var non-complex getters/testers

    public boolean isIntVar(String vfqn) {
        return (this.vt.get(vfqn).kind == IntEnvKind.INT);
    }

    public boolean isEnvVar(String vfqn) {
        return (this.vt.get(vfqn).kind == IntEnvKind.ENV);
    }

    public List<DashParam> varParams(String vfqn) {
        return this.vt.get(vfqn).params;
    }

    public AlloyExpr varTyp(String vfqn) {
        return this.vt.get(vfqn).typ;
    }

    public void setVarTyp(String vfqn, AlloyExpr t) {
        this.vt.get(vfqn).typ = t;
    }

    // group getters/testers

    public List<String> allVarNames() {
        return new ArrayList<String>(this.vt.keySet());
    }

    public List<String> allIntVarNames() {
        List<String> ret = new ArrayList<String>();
        ret = filterBy(setToList(this.vt.keySet()), x -> this.isIntVar(x));
        return ret;
    }

    public boolean hasVars() {
        return this.vt.isEmpty();
    }

    public boolean containsVar(String vfqn) {
        return (this.vt.containsKey(vfqn));
    }

    public List<String> varsOfState(String sfqn) {
        // return all events declared in this state
        // will have the sfqn as a prefix
        return this.vt.keySet().stream()
                // prefix of vfqn are state names
                .filter(i -> DashFQN.chopPrefixFromFQN(i).equals(sfqn))
                .collect(Collectors.toList());
    }

    public void addVar(Pos pos, String vfqn, IntEnvKind k, List<DashParam> prms, AlloyExpr t) {
        assert (prms != null);
        if (this.vt.containsKey(vfqn)) {
            DashModelErrors.duplicateName(pos, "var", vfqn);
        } else if (hasPrime(vfqn)) {
            DashModelErrors.nameShouldNotBePrimed(pos, vfqn);
        } else {
            this.vt.put(vfqn, new VarEntry(pos, k, prms, t));
        }
    }

    public void addVar(String vfqn, IntEnvKind k, List<DashParam> prms, AlloyExpr t) {
        addVar(Pos.UNKNOWN, vfqn, k, prms, t);
    }

    public String vtToString() {
        String s = new String("VAR TABLE\n");
        for (String k : vt.keySet()) {
            s += " ----- \n";
            s += k + "\n";
            s += this.vt.get(k).toString();
        }
        return s;
    }

    private class VarEntry {

        public final Pos pos;
        public final IntEnvKind kind;
        public final List<DashParam> params;
        // can't be final because it has to be resolved
        // after all the vars are in the var table
        public AlloyExpr typ;

        public VarEntry(Pos p, IntEnvKind k, List<DashParam> prms, AlloyExpr t) {
            assert (prms != null);
            this.pos = p;
            this.kind = k;
            this.params = prms;
            this.typ = t;
        }

        public String toString() {
            String s = new String();
            s += "kind: " + kind + "\n";
            s += "params: " + NoneStringIfNeeded(params) + "\n";
            s += "typ: " + typ.toString() + "\n";
            return s;
        }
    }
}
