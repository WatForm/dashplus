/*  The TransTable maps:
	FQN trans name -> info about trans

	It is populated during the resolve phase.

    The "R" is kept as a suffix here
    because "goto" is a keyword
*/

package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.dashast.DashFQN.*;
import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.DashFile;
import ca.uwaterloo.watform.dashast.dashNamedExpr.*;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.utils.Pos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TransDM extends InitsInvsDM {

    private HashMap<String, TransEntry> tt = new HashMap<String, TransEntry>();

    protected TransDM() {
        super();
    }

    protected TransDM(DashFile d) {
        super(d);
    }

    // individual trans element getters

    public List<DashParam> transParams(String tfqn) {
        return this.tt.get(tfqn).params;
    }

    public DashRef fromR(String tfqn) {
        return this.tt.get(tfqn).fromR;
    }

    public DashRef gotoR(String tfqn) {
        return this.tt.get(tfqn).gotoR;
    }

    public DashRef onR(String tfqn) {
        return this.tt.get(tfqn).onR;
    }

    public DashRef sendR(String tfqn) {
        return this.tt.get(tfqn).sendR;
    }

    public AlloyExpr whenR(String tfqn) {
        return this.tt.get(tfqn).whenR;
    }

    public AlloyExpr doR(String tfqn) {
        return this.tt.get(tfqn).doR;
    }

    public List<String> allTransNames() {
        return setToList(this.tt.keySet());
    }

    public boolean hasTrans() {
        return !this.tt.isEmpty();
    }

    // complex getters

    private List<String> transWithTheseSrcs(List<String> slist) {
        // might be better to make this transWithThisSrc
        // but this is more efficient if it is only used for higherPriTrans
        List<String> tlist = new ArrayList<String>();
        for (String k : allTransNames()) {
            if (slist.contains(tt.get(k).fromR.name)) tlist.add(k);
        }
        return tlist;
    }

    public List<String> transOfState(String sfqn) {
        // return all events declared in this state
        // will have the sfqn as a prefix
        return allTransNames().stream()
                // prefix of vfqn are state names
                .filter(i -> DashFQN.chopPrefixFromFQN(i).equals(sfqn))
                .collect(Collectors.toList());
    }

    public List<String> higherPriTrans(String tfqn) {
        // list returned could be empty
        String src = tt.get(tfqn).fromR.name;
        List<String> tlist = new ArrayList<String>();
        // have to look for transitions from sources earlier on the path of this transitions src
        // allPrefixes includes t so it contains at least one item
        List<String> allPrefixes = DashFQN.allPrefixes(src);
        // remove the src state itself
        allPrefixes.remove(src);
        tlist.addAll(transWithTheseSrcs(allPrefixes));
        return tlist;
    }

    public boolean[] transAtThisParamDepth(int max) {
        boolean[] depthsInUse = new boolean[max + 1]; // 0..max
        for (int i = 0; i <= max; i++) depthsInUse[i] = false;
        for (String k : tt.keySet())
            if (this.tt.get(k).params.isEmpty()) depthsInUse[0] = true;
            else depthsInUse[this.tt.get(k).params.size()] = true;
        return depthsInUse;
    }

    public List<String> intEventsGenerated() {
        return mapBy(
                filterBy(allTransNames(), i -> this.tt.get(i).sendR != null),
                i -> this.tt.get(i).sendR.name);
    }

    public List<String> eventsThatTriggerTrans() {
        return mapBy(
                filterBy(allTransNames(), i -> this.tt.get(i).onR != null),
                i -> this.tt.get(i).onR.name);
    }

    public List<String> transDestNames() {
        return mapBy(
                filterBy(allTransNames(), i -> this.tt.get(i).gotoR != null),
                i -> this.tt.get(i).gotoR.name);
    }

    public void addTrans(
            Pos pos,
            String tfqn,
            List<DashParam> params,
            DashRef fromR,
            DashRef onR,
            AlloyExpr whenR,
            DashRef gotoR,
            DashRef sendR,
            AlloyExpr doR) {
        // this is a resolved transition
        assert (!tfqn.isEmpty());
        assert (params != null);
        if (tt.containsKey(tfqn)) {
            DashModelErrors.duplicateName(pos, "trans", tfqn);
        } else if (hasPrime(tfqn)) {
            DashModelErrors.nameShouldNotBePrimed(pos, tfqn);
        } else {
            this.tt.put(tfqn, new TransEntry(params, fromR, onR, whenR, gotoR, sendR, doR));
        }
    }

    public String ttToString() {
        String s = new String();
        s += "TRANS TABLE\n";
        for (String k : tt.keySet()) {
            s += " ----- \n";
            s += k + "\n";
            s += this.tt.get(k).toString();
        }
        return s;
    }

    private class TransEntry {

        // empty if no params
        public final List<DashParam> params;

        public final DashRef fromR;
        public final DashRef gotoR;
        public final DashRef onR; // event
        public final DashRef sendR; // event
        public final AlloyExpr whenR; // expr
        public final AlloyExpr doR;

        protected TransEntry(
                List<DashParam> prms,
                // List<Integer> prmsIdx,
                DashRef f,
                DashRef o,
                AlloyExpr w,
                DashRef g,
                DashRef s,
                AlloyExpr d) {
            this.params = prms;
            // this.paramsIdx = prmsIdx;
            this.fromR = f;
            this.onR = o;
            this.whenR = w;
            this.gotoR = g;
            this.sendR = s;
            this.doR = d;
        }

        public String toString() {
            String s = new String();
            s += "params: " + NoneStringIfNeeded(params) + "\n";
            // s += "paramsIdx: " + NoneStringIfNeeded(paramsIdx) +"\n";
            s += "from: " + NoneStringIfNeeded(fromR) + "\n";
            s += "goto: " + NoneStringIfNeeded(gotoR) + "\n";
            s += "on: " + NoneStringIfNeeded(onR) + "\n";
            s += "send: " + NoneStringIfNeeded(sendR) + "\n";
            s += "when: " + NoneStringIfNeeded(whenR) + "\n";
            s += "do: " + NoneStringIfNeeded(doR) + "\n";
            // add more
            return s;
        }
    }
}
