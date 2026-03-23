/*
	The purpose of this function is to translate from DashModule tables back to a
	syntactic DashState for printing of a DashModel.

	This is tricky because the DashTables contain resolved "names" for everything, meaning
	the names are fully qualified names (FQNs) and have all parameters attached.

	We can find the states within a state.  We can't print these FQNs

	We can find the transition FQNS with the same prefix as a state, meaning they were declared in
	that state.  We can't print the transition names as FQNs, but we can print the transition
	parts with full FQNs.

	Inits and invs will be written globally.

	Transitions will all be written with from/goto even if they did not originally have these listed.

	For variables within expressions (actions, guards, on, send, src, dest):
		- we'll print the full FQN
		- for parameters, we will print all the parameters, but we will replace "p_Statename" with "thisStatename" to be equivalent to if they were written with no parameters.

*/

package ca.uwaterloo.watform.dashmodel;

import ca.uwaterloo.watform.alloyast.paragraph.AlloyPara;
import ca.uwaterloo.watform.dashast.DashFile;
import ca.uwaterloo.watform.dashast.DashState;
import java.util.List;

public class DashFileDM extends ResolveDM {

    protected DashFileDM() {
        super();
    }

    protected DashFileDM(DashFile d) {
        super(d);
    }

    public DashFile toDashFile() {
        List<AlloyPara> ap = this.getAllParas(true);
        DashState ds = stateRecurse(this.rootName);
        ap.add(ds);
        return new DashFile(ap);
    }

    private DashState stateRecurse(String stateFQN) {
        return null;
        /*
        List<Object> itemList = new ArrayList<Object>();
        if (!this.isLeaf(stateFQN)) {
        	for (String childFQN: this.immChildren(stateFQN)) {
        		itemList.add(stateRecurse(childFQN));
        	}
        } else {
        	itemList = DashState.noSubstates();
        }

        // add var decls
        // just one at a time (in original might have been multiple vars
        // declared with same type)
        // use FQN, but no parameters in declarations
        // parameters are determined by position in state hierarchy
        for (String varName: d.vt.getVarsOfState(stateName)) {
        	itemList.add(new DashVarDecls(
        		Pos.UNKNOWN,
        		// list of size 1
        		new ArrayList<String>(Arrays.asList(DashFQN.translateFQN(varName))),
        		d.vt.getVarType(varName),
        		d.vt.getIntEnvKind(varName)
        	));
        }
        // add buffer decls
        // just one at a time (in original might have been multiple vars
        // declared with same type)
        // use FQN, but no parameters in declarations
        // parameters are determined by position in state hierarchy
        for (String bufName: d.vt.getBuffersOfState(stateName)) {
        	itemList.add(new DashBufferDecls(
        		Pos.UNKNOWN,
        		// list of size 1
        		new ArrayList<String>(Arrays.asList(DashFQN.translateFQN(bufName))),
        		d.vt.getBufferElement(bufName),
        		d.vt.getIntEnvKind(bufName),
        		//since we are doing one buffer at a time
        		// start and end index will be the same
        		// TODO: not certain about this
        		d.vt.getBufferIndex(bufName), // startIndex
        		d.vt.getBufferIndex(bufName) // endIndex
        	));
        }
        // add event decls
        // just one at a time
        // use FQN, but no parameters in declarations
        // parameters are determined by position in state hierarchy
        for (String evName: d.et.getEventsOfState(stateName)) {
        	itemList.add(new DashEventDecls(
        		Pos.UNKNOWN,
        		// list of size 1
        		new ArrayList<String>(Arrays.asList(DashFQN.translateFQN(evName))),
        		d.et.getIntEnvKind(evName)
        	));
        }


        // collect trans
        // fqn name of trans will tell us where it was declared
        // use FQN, but no parameters in declarations
        // parameters are determined by position in state hierarchy
        for (String transName: d.tt.getTransOfState(stateName)) {
        	List<Object> transItemList = new ArrayList<Object>();
        	// these will be the resolved ones
        	// TODO: will parameters be printed in expressions??
        	// getSrc returns a DashRef
        	// DashFrom needs an expression

        	// src -- cannot be null when in TransTable
        	transItemList.add(
        		new DashFrom(Pos.UNKNOWN, d.tt.getSrc(transName)));

        	// when
        	if (d.tt.getWhen(transName) != null) {
        		transItemList.add(
        			new DashWhen(Pos.UNKNOWN, ((Expr) d.tt.getWhen(transName))));
        	}

        	// on
        	if (d.tt.getOn(transName) != null) {
        		transItemList.add(
        			new DashOn(Pos.UNKNOWN, ((Expr) d.tt.getOn(transName))));
        	}

        	// do
        	if (d.tt.getDo(transName) != null) {
        		transItemList.add(
        			new DashDo(Pos.UNKNOWN, ((Expr) d.tt.getDo(transName))));
        	}
        	// send
        	if (d.tt.getSend(transName) != null) {
        		transItemList.add(
        			new DashSend(Pos.UNKNOWN, ((Expr) d.tt.getSend(transName))));
        	}

        	// dest -- cannot be null when in TransTable
        	transItemList.add(
        		new DashGoto(Pos.UNKNOWN, ((Expr) d.tt.getDest(transName))));

        	itemList.add(new DashTrans(
        		Pos.UNKNOWN,
        		// list of size 1
        		DashFQN.translateFQN(transName),
        		transItemList
        	));
        }

        // get invariants -- note that these are the original versions
        // so they aren't resolved, which makes them easy to print
        for (DashInv inv: d.st.getOrigInvariants(stateName)) itemList.add(inv);
        // get inits -- note that these are the original versions
        // so they aren't resolved, which makes them easy to print
        for (DashInit init: d.st.getOrigInits(stateName)) itemList.add(init);

        // statenames are all FQNs
        return new DashState(
        		Pos.UNKNOWN,
        		DashFQN.translateFQN(stateName),
        		d.st.getParam(stateName),
        		d.st.getKind(stateName),
        		d.st.getDef(stateName),
        		itemList) ;
        	*/
    }
}
