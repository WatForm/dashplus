package ca.uwaterloo.watform.dashmodel;

//import java.util.Set;
import java.util.List;
import java.util.ArrayList;
//import java.util.HashMap;
import java.util.LinkedHashMap;
//import java.util.Collections;
import java.util.stream.Collectors;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.dashast.DashStrings.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;

public class VarTable {

	// stores Var, Buffer Decls in a HashMap based on the FQN

	// LinkedHashMap so order of keySet is consistent
	// Alloy requires declaration before use for variables
	private LinkedHashMap<String,VarElement> vt;

	public VarTable() {
		this.vt = new LinkedHashMap<String,VarElement>();
	}

	public class VarElement  {
		private IntEnvKind kind;
		private List<DashParam> params;
		//private List<Integer> paramsIdx;
		private AlloyExpr typ;

		public VarElement(
			IntEnvKind k,
			List<DashParam> prms,
			//List<Integer> prmsIdx,
			AlloyExpr t) {
			assert(prms != null);
			this.kind = k;
			this.params = prms;
			//this.paramsIdx = prmsIdx;
			this.typ = t;
		}
		public String toString() {
			String s = new String();
			s += "kind: "+kind+"\n";
			s += "params: "+ NoneStringIfNeeded(params) +"\n";
			//s += "paramsIdx: "+ NoneStringIfNeeded(paramsIdx) +"\n";
			s += "typ: "+typ.toString() + "\n";
			return s;
		}
		public void setType(AlloyExpr typ) {
			this.typ = typ;
		}
	}

	public Boolean addVar(
			String vfqn, 
			IntEnvKind k, 
			List<DashParam> prms, 
			AlloyExpr t) {
		assert(prms!=null);
		if (vt.containsKey(vfqn)) 
			return false;
		else if (hasPrime(vfqn)) { 
			DashModelErrors.nameShouldNotBePrimed(vfqn); 
			return false; 
		} else { 
			vt.put(vfqn, new VarElement(k,prms, t)); 
			return true; 
		}
	}
	public String toString() {
		String s = new String("VAR TABLE\n");
		for (String k:vt.keySet()) {
			s += " ----- \n";
			s += k + "\n";
			s += vt.get(k).toString();
		}
		return s;
	}	
	private boolean inTable(String vfqn) {
		if (vt.containsKey(vfqn)) return true;
		else { 
			DashModelErrors.notInTable("var","getIndex", vfqn); 
			return false;
		}		
	}

	// setters
	public void setVarType(String vfqn, AlloyExpr typ) {
		if (inTable(vfqn)) 
			vt.get(vfqn).setType(typ);
	}

	// getters
	public boolean hasVar(String name) {
		return (vt.containsKey(name));
	}
	public AlloyExpr getVarType(String vfqn) {
		if (inTable(vfqn)) 
			return vt.get(vfqn).typ;
		else 
			return null; 
	}
	// same function for buffers and variables
	//TODO: what if var and buffer have the same name!!!
	public List<DashParam> getParams(String vfqn) {
		if (inTable(vfqn)) 
			return vt.get(vfqn).params;
		else 
			return null; 
	}
	// same function for buffers and variables
	public boolean isInternal(String vfqn) {
		if (inTable(vfqn)) 
			return (vt.get(vfqn).kind == IntEnvKind.INT);
		else 
			return false; 
	}
	public IntEnvKind getIntEnvKind(String vfqn) {
		if (inTable(vfqn)) 
			return (vt.get(vfqn).kind);
		else 
			return IntEnvKind.INT; 	
	}
	// group getters
	public List<String> getAllVarNames() {
		return new ArrayList<String>(vt.keySet());
	}

	private List<String> getVarsOfState(String sfqn) {
		// return all events declared in this state
		// will have the sfqn as a prefix
		return vt.keySet().stream()
			// prefix of vfqn are state names
			.filter(i -> DashFQN.chopPrefixFromFQN(i).equals(sfqn))
			.collect(Collectors.toList());	
	}
	/*
	public List<Integer> getParamsIdx(String fqn) {
		if (bufferTable.containsKey(fqn)) return bufferTable.get(fqn).paramsIdx;
		if (vt.containsKey(fqn)) return vt.get(fqn).paramsIdx;
		else { DashErrors.varBufferDoesNotExist("getParams", fqn); return null; }
	}
	*/

	public List<String> getAllInternalVarNames() {
    	return getAllVarNames().stream()
    			.filter(i -> isInternal(i))
    			.collect(Collectors.toList());
    }
}