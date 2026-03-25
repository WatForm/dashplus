package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.DashFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InitsInvsDM extends BaseDM {

    protected LinkedHashMap<String, List<AlloyExpr>> initsR = new LinkedHashMap<>();
    protected LinkedHashMap<String, List<AlloyExpr>> invsR = new LinkedHashMap<>();

    protected InitsInvsDM() {
        super();
    }

    protected InitsInvsDM(DashFile d) {
        super(d);
    }

    public List<AlloyExpr> initsR() {
        // returns all of them from all states
        return initsR.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public List<AlloyExpr> invsR() {
        // returns all of them from all states
        return invsR.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public void addInv(String sfqn, List<AlloyExpr> invs) {
        if (this.invsR.containsKey(sfqn)) {
            this.invsR.get(sfqn).addAll(invs);
        } else {
            this.invsR.put(sfqn, invs);
        }
    }

    public void addInit(String sfqn, List<AlloyExpr> inits) {
        if (this.initsR.containsKey(sfqn)) {
            this.initsR.get(sfqn).addAll(inits);
        } else {
            this.initsR.put(sfqn, inits);
        }
    }

    public void addInv(String sfqn, AlloyExpr inv) {
        this.addInv(sfqn, Arrays.asList(inv));
    }

    public void addInit(String sfqn, AlloyExpr init) {
        this.addInit(sfqn, Arrays.asList(init));
    }

    public List<AlloyExpr> initsOfState(String sfqn) {
        // return all inits declared in this state
        if (this.initsR.containsKey(sfqn)) {
            return new ArrayList<AlloyExpr>(this.initsR.get(sfqn));
        } else {
            return emptyList();
        }
    }

    public List<AlloyExpr> invsOfState(String sfqn) {
        // return all inits declared in this state
        if (this.invsR.containsKey(sfqn)) {
            return new ArrayList<AlloyExpr>(this.invsR.get(sfqn));
        } else {
            return emptyList();
        }
    }
}
