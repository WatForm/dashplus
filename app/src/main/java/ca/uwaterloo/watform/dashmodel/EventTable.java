/*
	Stores Event Decls in a HashMap based on the event FQN
*/

package ca.uwaterloo.watform.dashmodel;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import ca.uwaterloo.watform.dashast.DashStrings;
import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import ca.uwaterloo.watform.dashast.DashParam;
public class EventTable {

	private HashMap<String,EventElement> et;
	public String name = "Event";
	
	public class EventElement {
		private IntEnvKind kind = IntEnvKind.INT;
		private List<DashParam> params = null;

		public EventElement(
			IntEnvKind k,
			List<DashParam> prms
			) {
			assert(prms != null);
			this.kind = k;
			this.params = prms;
		}
		public String toString() {
			String s = new String();
			s += "kind: "+kind+"\n";
			s += "params: "+ NoneStringIfNeeded(params) +"\n";
			return s;
		}
	}

	public EventTable() {
		this.et = new HashMap<String,EventElement>();
	}
	public String toString() {
		String s = new String("EVENT TABLE\n");
		for (String k:et.keySet()) {
			s += " ----- \n";
			s += k + "\n";
			s += et.get(k).toString();
		}
		return s;
	}	
	public Boolean add(String efqn, IntEnvKind k, List<DashParam> prms) {
		assert(prms!=null);
		if (et.containsKey(efqn)) return false;
		else if (hasPrime(efqn)) { DashModelErrors.nameShouldNotBePrimed(efqn); return false; }
		else { et.put(efqn, new EventElement(k,prms)); return true; }
	}
	
	// individual getters
	public List<DashParam> getParams(String efqn) {
		return et.get(efqn).params;
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
		return (!et.keySet().isEmpty() );
	}
	public boolean contains(String efqn) {
		return et.containsKey(efqn);
	}
	public boolean hasEventsAti(int i) {
		for (String e: et.keySet()) {
			if (et.get(e).params.size() == i) return true;
		}
		return false;	
	}
	public boolean hasInternalEventsAti(int i) {
		for (String e: et.keySet()) {
			if (et.get(e).params.size() == i && et.get(e).kind == IntEnvKind.INT) return true;
		}
		return false;	
	}
	public boolean hasEnvironmentalEventsAti(int i) {
		for (String e: et.keySet()) {
			if (et.get(e).params.size() == i && et.get(e).kind == IntEnvKind.ENV) return true;
		}
		return false;	
	}
	public boolean hasInternalEvents() {
		for (String e: et.keySet()) {
			if (et.get(e).kind == IntEnvKind.INT) return true;
		}
		return false;
	}
	public boolean hasEnvironmentalEvents() {
		for (String e: et.keySet()) {
			if (et.get(e).kind == IntEnvKind.ENV) return true;
		}
		return false;
	}
	public List<String> getAllInternalEvents() {
		return et.keySet().stream()
			.filter(i -> et.get(i).kind == IntEnvKind.INT)
			.collect(Collectors.toList());	
	}
	public List<String> getAllEnvironmentalEvents() {
		return et.keySet().stream()
			.filter(i -> et.get(i).kind == IntEnvKind.ENV)
			.collect(Collectors.toList());	
	}
	public List<String> getAllNames() {
		return new ArrayList<String>(et.keySet());
	}
	public List<String> getEventsOfState(String sfqn) {
		// return all events _declared_ at the level of this state
		// will have the sfqn as a prefix
		// purely based on names
		return et.keySet().stream()
			.filter(i -> DashFQN.chopPrefixFromFQN(i).equals(sfqn))
			.collect(Collectors.toList());	
	}
	public List<String> getEventsWithinState(String sfqn) {
		// return all events _declared_ somewhere within this state
		// will have the sfqn as a prefix
		// purely based on names
		return et.keySet().stream()
			.filter(i -> DashFQN.prefix(sfqn,i))
			.collect(Collectors.toList());	
	}
}