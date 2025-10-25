/*
	Stores Event Decls in a HashMap based on the event FQN
*/

package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.utils.Pos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventTable {

    private HashMap<String, EventElement> et;
    private String tableName = "Event";

    public class EventElement {
        public IntEnvKind kind = IntEnvKind.INT;
        public List<DashParam> params = null;

        public EventElement(IntEnvKind k, List<DashParam> prms) {
            assert (prms != null);
            this.kind = k;
            this.params = prms;
        }

        public String toString() {
            String s = new String();
            s += "kind: " + kind + "\n";
            s += "params: " + NoneStringIfNeeded(params) + "\n";
            return s;
        }
    }

    public EventTable() {
        this.et = new HashMap<String, EventElement>();
    }

    public String toString() {
        String s = new String("EVENT TABLE\n");
        for (String k : et.keySet()) {
            s += " ----- \n";
            s += k + "\n";
            s += et.get(k).toString();
        }
        return s;
    }

    public void add(Pos pos, String efqn, IntEnvKind k, List<DashParam> prms) {
        assert (prms != null);
        if (et.containsKey(efqn)) {
            DashModelErrors.duplicateName(pos, "event", efqn);
        } else if (hasPrime(efqn)) {
            DashModelErrors.nameShouldNotBePrimed(pos, efqn);
        } else {
            et.put(efqn, new EventElement(k, prms));
        }
    }

    // so we can treat this as a table
    // to the outside world
    public EventElement get(String efqn) {
        return et.get(efqn);
    }

    public List<String> keySet() {
        return setToList(et.keySet());
    }

    public boolean isEmpty() {
        return et.isEmpty();
    }

    public boolean isEnvironmentalEvent(String efqn) {
        return (et.get(efqn).kind == IntEnvKind.ENV);
    }

    public boolean isInternalEvent(String efqn) {
        return (et.get(efqn).kind == IntEnvKind.INT);
    }

    public IntEnvKind getIntEnvKind(String efqn) {
        return (et.get(efqn).kind);
    }

    // group getters
    public boolean hasEvents() {
        return (!et.keySet().isEmpty());
    }

    public boolean contains(String efqn) {
        return et.containsKey(efqn);
    }

    public boolean hasEventsAti(int i) {
        for (String e : et.keySet()) {
            if (et.get(e).params.size() == i) return true;
        }
        return false;
    }

    public boolean hasInternalEventsAti(int i) {
        for (String e : et.keySet()) {
            if (et.get(e).params.size() == i && et.get(e).kind == IntEnvKind.INT) return true;
        }
        return false;
    }

    public boolean hasEnvironmentalEventsAti(int i) {
        for (String e : et.keySet()) {
            if (et.get(e).params.size() == i && et.get(e).kind == IntEnvKind.ENV) return true;
        }
        return false;
    }

    public boolean hasInternalEvents() {
        for (String e : et.keySet()) {
            if (et.get(e).kind == IntEnvKind.INT) return true;
        }
        return false;
    }

    public boolean hasEnvironmentalEvents() {
        for (String e : et.keySet()) {
            if (et.get(e).kind == IntEnvKind.ENV) return true;
        }
        return false;
    }

    public List<String> getAllInternalEvents() {
        return filterBy(keySet(), i -> et.get(i).kind == IntEnvKind.INT);
    }

    public List<String> getAllEnvironmentalEvents() {
        return filterBy(keySet(), i -> et.get(i).kind == IntEnvKind.ENV);
    }

    public List<String> getAllNames() {
        return new ArrayList<String>(et.keySet());
    }

    public List<String> getEventsOfState(String sfqn) {
        // return all events _declared_ at the level of this state
        // will have the sfqn as a prefix
        // purely based on names
        return filterBy(keySet(), i -> DashFQN.chopPrefixFromFQN(i).equals(sfqn));
    }

    public List<String> getEventsWithinState(String sfqn) {
        // return all events _declared_ somewhere within this state
        // will have the sfqn as a prefix
        // purely based on names
        return filterBy(keySet(), i -> DashFQN.prefix(sfqn, i));
    }
}
