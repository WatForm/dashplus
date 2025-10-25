/*  The TransTable maps:
	FQN trans name -> info about trans

	It is created during the resolveAll phase.
*/

package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.utils.Pos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TransTable {

    private HashMap<String, TransElement> tt;
    private String tableName = "Trans";

    public TransTable() {
        tt = new HashMap<String, TransElement>();
    }

    public void add(
            Pos pos,
            String tfqn,
            List<DashParam> params,
            // List<Integer> paramsIdx,
            DashFrom fromP,
            DashOn onP,
            DashWhen whenP,
            DashGoto gotoP,
            DashSend sendP,
            DashDo doP) {
        assert (!tfqn.isEmpty());
        assert (params != null);
        if (tt.containsKey(tfqn)) {
            DashModelErrors.duplicateName(pos, "trans", tfqn);
        } else if (hasPrime(tfqn)) {
            DashModelErrors.nameShouldNotBePrimed(pos, tfqn);
        } else {
            tt.put(tfqn, new TransElement(params, fromP, onP, whenP, gotoP, sendP, doP));
        }
    }

    public String toString() {
        String s = new String();
        s += "TRANS TABLE\n";
        for (String k : tt.keySet()) {
            s += " ----- \n";
            s += k + "\n";
            s += tt.get(k).toString();
        }
        return s;
    }

    // so we can treat this as a table
    // to the outside world
    public TransElement get(String tfqn) {
        return tt.get(tfqn);
    }

    public List<String> keySet() {
        return setToList(tt.keySet());
    }

    public boolean isEmpty() {
        return tt.isEmpty();
    }

    // might be better to make this getTransWithThisSrc
    // but this is more efficient if it is only used for higherPriTrans
    public List<String> getTransWithTheseSrcs(List<String> slist) {
        List<String> tlist = new ArrayList<String>();
        for (String k : tt.keySet()) {
            if (slist.contains(tt.get(k).fromR.name)) tlist.add(k);
        }
        return tlist;
    }

    public List<String> getTransOfState(String sfqn) {
        // return all events declared in this state
        // will have the sfqn as a prefix
        return tt.keySet().stream()
                // prefix of vfqn are state names
                .filter(i -> DashFQN.chopPrefixFromFQN(i).equals(sfqn))
                .collect(Collectors.toList());
    }

    public List<String> getHigherPriTrans(String tfqn) {
        // list returned could be empty
        String src = tt.get(tfqn).fromR.name;
        List<String> tlist = new ArrayList<String>();
        // have to look for transitions from sources earlier on the path of this transitions src
        // allPrefixes includes t so it contains at least one item
        List<String> allPrefixes = DashFQN.allPrefixes(src);
        // remove the src state itself
        allPrefixes.remove(src);
        tlist.addAll(getTransWithTheseSrcs(allPrefixes));
        return tlist;
    }

    public boolean[] transAtThisParamDepth(int max) {
        boolean[] depthsInUse = new boolean[max + 1]; // 0..max
        for (int i = 0; i <= max; i++) depthsInUse[i] = false;
        for (String k : tt.keySet())
            if (tt.get(k).params.isEmpty()) depthsInUse[0] = true;
            else depthsInUse[tt.get(k).params.size()] = true;
        return depthsInUse;
    }

    public List<String> intEventsGenerated() {
        return mapBy(filterBy(keySet(), i -> tt.get(i).sendR != null), i -> tt.get(i).sendR.name);
    }

    public List<String> eventsThatTriggerTrans() {
        return mapBy(filterBy(keySet(), i -> tt.get(i).onR != null), i -> tt.get(i).onR.name);
    }

    public List<String> allTransDestNames() {
        return mapBy(filterBy(keySet(), i -> tt.get(i).gotoR != null), i -> tt.get(i).gotoR.name);
    }
}
