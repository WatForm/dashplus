/*  The TransTable maps:
	FQN trans name -> info about trans

	It is created during the resolveAll phase.
*/

package ca.uwaterloo.watform.dashmodel;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashmodel.dashref.DashRef;

public class TransTable  {

	private HashMap<String,TransElement> tt;
	private boolean isResolved;
	public String name = "Trans";

	public TransTable() {
		tt = new HashMap<String, TransElement>();
		isResolved = false;
	}
	public boolean add(
			String tfqn,
			List<DashParam> params,
			//List<Integer> paramsIdx,
			List<DashFrom> fromList,
			List<DashOn> onList,
			List<DashWhen> whenList,
			List<DashGoto> gotoList,
			List<DashSend> sendList,
			List<DashDo> doList
		)
	{
		assert(!tfqn.isEmpty());
		assert(params != null );
		//assert(paramsIdx != null);
		assert(fromList != null);
		assert(onList != null);
		assert(whenList != null);
		assert(gotoList != null);
		assert(sendList != null);
		assert(doList != null);
		if (tt.containsKey(tfqn)) 
			return false;
		else if (hasPrime(tfqn)) { DashModelErrors.nameShouldNotBePrimed(tfqn); return false; }
		else { 
			tt.put(tfqn, new TransElement(params,fromList,onList,whenList,gotoList,sendList,doList)); return true; }
	}
	public String toString() {
		String s = new String();
		s += "TRANS TABLE\n";
		for (String k:tt.keySet()) {
			s += " ----- \n";
			s += k + "\n";
			s += tt.get(k).toString();
		}
		return s;
	}

	// individual getters
	// should return null if nothing 
	public DashRef getSrc(String tfqn) {
		return tt.get(tfqn).src;
	}
	public DashRef getDest(String tfqn) {
		return tt.get(tfqn).dest;
	}
	public DashRef getOn(String tfqn) {
		return tt.get(tfqn).on;
	}
	public DashRef getSend(String tfqn) {
		return tt.get(tfqn).send;
	}
	public DashExpr getDo(String tfqn) {
		return tt.get(tfqn).act;
	}
	public DashExpr getWhen(String tfqn) {
		return tt.get(tfqn).when;
	}

	// group getters
	public List<String> getAllNames() {
		return new ArrayList<String>(tt.keySet());
	}

	// might be better to make this getTransWithThisSrc
	// but this is more efficient if it is only used for higherPriTrans
	public List<String> getTransWithTheseSrcs(List<String> slist) {
		List<String> tlist = new ArrayList<String>();
		for (String k:tt.keySet()) {
			if (slist.contains(tt.get(k).src.getName())) tlist.add(k);
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
		String src = tt.get(tfqn).src.getName();
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
		boolean[] depthsInUse = new boolean[max+1]; // 0..max
		for (int i=0; i <= max; i++) depthsInUse[i] = false;
		for (String k:tt.keySet()) 
			if (tt.get(k).params.isEmpty()) depthsInUse[0] = true;
			else depthsInUse[tt.get(k).params.size()] = true;
		return depthsInUse;
	}
	

}
