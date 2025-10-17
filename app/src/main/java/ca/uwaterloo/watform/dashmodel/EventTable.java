package ca.uwaterloo.watform.dashmodel;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.stream.Collectors;

import ca.uwaterloo.watform.dashast.DashStrings;
import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;


public class EventTable {

	// stores Event Decls in a HashMap based on the event FQN

	private LinkedHashMap<String,EventElement> table;

	public class EventElement {
		private IntEnvKind kind = IntEnvKind.INT;
		private List<DashParam> params = null;
		//private List<Integer> paramsIdx;

		public EventElement(
			IntEnvKind k,
			List<DashParam> prms
			) {
			assert(prms != null);
			this.kind = k;
			this.params = prms;
			//this.paramsIdx = prmsIdx;
		}
		public String toString() {
			String s = new String();
			s += "kind: "+kind+"\n";
			s += "params: "+ NoneStringIfNeeded(params) +"\n";
			//s += "paramsIdx: "+ NoneStringIfNeeded(paramsIdx) +"\n";
			return s;
		}
	}

	public EventTable() {
		this.table = new LinkedHashMap<String,EventElement>();

	}
	public String toString() {
		String s = new String("EVENT TABLE\n");
		for (String k:table.keySet()) {
			s += " ----- \n";
			s += k + "\n";
			s += table.get(k).toString();
		}
		return s;
	}	
	public Boolean add(String efqn, IntEnvKind k, List<DashParam> prms) {
		assert(prms!=null);
		if (table.containsKey(efqn)) return false;
		else if (hasPrime(efqn)) { DashModelErrors.nameShouldNotBePrimed(efqn); return false; }
		else { table.put(efqn, new EventElement(k,prms)); return true; }
	}
	
	public void resolveAllEventTable() {}

	// Accessors of whole table --------------------------

	public boolean hasEvents() {
		return (!table.keySet().isEmpty() );
	}
	public boolean hasEvent(String e) {
		return table.containsKey(e);
	}
	public boolean hasEventsAti(int i) {
		for (String e: table.keySet()) {
			if (table.get(e).params.size() == i) return true;
		}
		return false;	
	}
	public boolean hasInternalEventsAti(int i) {
		for (String e: table.keySet()) {
			if (table.get(e).params.size() == i && table.get(e).kind == IntEnvKind.INT) return true;
		}
		return false;	
	}
	public boolean hasEnvironmentalEventsAti(int i) {
		for (String e: table.keySet()) {
			if (table.get(e).params.size() == i && table.get(e).kind == IntEnvKind.ENV) return true;
		}
		return false;	
	}
	public boolean hasInternalEvents() {
		for (String e: table.keySet()) {
			if (table.get(e).kind == IntEnvKind.INT) return true;
		}
		return false;
	}
	public boolean hasEnvironmentalEvents() {
		for (String e: table.keySet()) {
			if (table.get(e).kind == IntEnvKind.ENV) return true;
		}
		return false;
	}
	public List<String> getAllInternalEvents() {
		return table.keySet().stream()
			.filter(i -> table.get(i).kind == IntEnvKind.INT)
			.collect(Collectors.toList());	
	}
	public List<String> getAllEnvironmentalEvents() {
		return table.keySet().stream()
			.filter(i -> table.get(i).kind == IntEnvKind.ENV)
			.collect(Collectors.toList());	
	}
	public List<String> getAllNames() {
		return new ArrayList<String>(table.keySet());
	}
	public List<String> getEventsOfState(String sfqn) {
		// return all events _declared_ at the level of this state
		// will have the sfqn as a prefix
		// purely based on names
		return table.keySet().stream()
			.filter(i -> DashFQN.chopPrefixFromFQN(i).equals(sfqn))
			.collect(Collectors.toList());	
	}
	public List<String> getEventsWithinState(String sfqn) {
		// return all events _declared_ somewhere within this state
		// will have the sfqn as a prefix
		// purely based on names
		return table.keySet().stream()
			.filter(i -> DashFQN.prefix(sfqn,i))
			.collect(Collectors.toList());	
	}

	// Accessors of individual event -----------------------

	private boolean eventPresent(String efqn, String fcnCall) {
		if (table.containsKey(efqn)) 
			return true;
		else { 
			DashModelErrors.eventTableEventNotFound(fcnCall, efqn); 
			return false; 
		}
	}
	public List<DashParam> getParams(String efqn) {
		if (eventPresent(efqn, "getParams"))
			return table.get(efqn).params;
		else 
			return null;
	}
	/*
	public List<Integer> getParamsIdx(String efqn) {
		if (table.containsKey(efqn)) return table.get(efqn).paramsIdx;
		else { DashModelErrors.eventTableEventNotFound("getParamsIdx", efqn); return null; }
	}
	*/
	public boolean isEnvironmentalEvent(String efqn) {
		if (eventPresent(efqn, "isEnvironmentalEvent"))
			return (table.get(efqn).kind == IntEnvKind.ENV);
		else 
			return false;
	}
	public boolean isInternalEvent(String efqn) {
		if (eventPresent(efqn, "isInternalEvent"))
			return (table.get(efqn).kind == IntEnvKind.INT);
		else 
			return false;
	}
	public IntEnvKind getIntEnvKind(String efqn) {
		if (eventPresent(efqn, "isInternalEvent"))
			return (table.get(efqn).kind);
		else 
			return null;
	}


}