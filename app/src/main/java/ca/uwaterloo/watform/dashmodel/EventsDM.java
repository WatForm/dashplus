/*
	Stores Event Decls in a HashMap based on the event FQN
*/

package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashast.DashFile;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.DashStrings.IntEnvKind;
import ca.uwaterloo.watform.utils.Pos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventsDM extends BuffersDM {

    protected HashMap<String, EventEntry> et = new HashMap<String, EventEntry>();

    protected EventsDM() {
        super();
    }

    protected EventsDM(DashFile d) {
        super(d);
    }

    // individual event non-complex getters

    public boolean isEnvEvent(String efqn) {
        return (this.et.get(efqn).kind == IntEnvKind.ENV);
    }

    public boolean isIntEvent(String efqn) {
        return (this.et.get(efqn).kind == IntEnvKind.INT);
    }

    public List<DashParam> eventParams(String efqn) {
        return this.et.get(efqn).params;
    }

    // overall getters

    public List<String> allEventNames() {
        return new ArrayList<String>(this.et.keySet());
    }

    public boolean hasEvents() {
        return (!allEventNames().isEmpty());
    }

    public List<String> allIntEvents() {
        return filterBy(allEventNames(), i -> this.et.get(i).kind == IntEnvKind.INT);
    }

    public boolean hasIntEvents() {
        return !allIntEvents().isEmpty();
    }

    public List<String> allEnvEvents() {
        return filterBy(allEventNames(), i -> this.et.get(i).kind == IntEnvKind.ENV);
    }

    public boolean hasEnvEvents() {
        return !allEnvEvents().isEmpty();
    }

    public boolean containsEvent(String efqn) {
        return this.et.containsKey(efqn);
    }

    public boolean hasEventsAti(int i) {
        // at level i
        for (String e : allEventNames()) {
            if (eventParams(e).size() == i) return true;
        }
        return false;
    }

    public boolean hasIntEventsAti(int i) {
        for (String e : allEventNames()) {
            if (et.get(e).params.size() == i && isIntEvent(e)) return true;
        }
        return false;
    }

    public boolean hasEnvEventsAti(int i) {
        for (String e : allEventNames()) {
            if (eventParams(e).size() == i && isEnvEvent(e)) return true;
        }
        return false;
    }

    // complex getters

    public List<String> eventsOfState(String sfqn) {
        // return all events _declared_ at the level of this state
        // will have the sfqn as a prefix
        // purely based on names
        return filterBy(allEventNames(), i -> DashFQN.chopPrefixFromFQN(i).equals(sfqn));
    }

    public List<String> eventsWithinState(String sfqn) {
        // return all events _declared_ somewhere within this state
        // will have the sfqn as a prefix
        // purely based on names
        return filterBy(allEventNames(), i -> DashFQN.prefix(sfqn, i));
    }

    public String etToString() {
        String s = new String("EVENT TABLE\n");
        for (String k : et.keySet()) {
            s += " ----- \n";
            s += k + "\n";
            s += et.get(k).toString();
        }
        return s;
    }

    public void addEvent(Pos pos, String efqn, IntEnvKind k, List<DashParam> prms) {
        assert (prms != null);
        if (et.containsKey(efqn)) {
            DashModelErrors.duplicateName(pos, "event", efqn);
        } else if (hasPrime(efqn)) {
            DashModelErrors.nameShouldNotBePrimed(pos, efqn);
        } else {
            et.put(efqn, new EventEntry(pos, k, prms));
        }
    }

    public void addEvent(String efqn, IntEnvKind k, List<DashParam> prms) {
        addEvent(Pos.UNKNOWN, efqn, k, prms);
    }

    private class EventEntry {
        public final Pos pos;
        public final IntEnvKind kind;
        public final List<DashParam> params;

        public EventEntry(Pos p, IntEnvKind k, List<DashParam> prms) {
            assert (p != null);
            assert (prms != null);
            this.pos = p;
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
}
