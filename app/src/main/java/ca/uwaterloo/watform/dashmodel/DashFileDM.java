/*
	The purpose of this function is to turn a DashModel back to a
	DashState for printing of a DashModel.

    The DashModel tables contain resolved "names" for everything, meaning
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
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.dashNamedExpr.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.Arrays;
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

    private DashState stateRecurse(String sfqn) {

        List<Object> itemList = new ArrayList<Object>();
        if (!this.isLeaf(sfqn)) {
            for (String childFQN : this.immChildren(sfqn)) {
                itemList.add(stateRecurse(childFQN));
            }
        } else {
            itemList = DashState.noSubstates();
        }

        // add var decls
        // just one at a time (in original might have been multiple vars
        // declared with same type)
        // parameters in declarations
        // parameters are determined by position in state hierarchy
        for (String vfqn : this.varsOfState(sfqn)) {
            itemList.add(
                    new DashVarDecls(
                            Pos.UNKNOWN,
                            // list of size 1
                            new ArrayList<String>(Arrays.asList(DashFQN.chopNameFromFQN(vfqn))),
                            this.varTyp(vfqn),
                            this.varKind(vfqn)));
        }

        // add buffer decls
        // just one at a time (in original might have been multiple vars
        // declared with same type)
        // no parameters in declarations
        // parameters are determined by position in state hierarchy
        for (String bfqn : this.buffersOfState(sfqn)) {
            itemList.add(
                    new DashBufferDecls(
                            Pos.UNKNOWN,
                            // list of size 1
                            new ArrayList<String>(Arrays.asList(DashFQN.chopNameFromFQN(bfqn))),
                            this.bufferElement(bfqn),
                            this.bufferKind(bfqn)));
        }

        // add event decls
        // just one at a time
        // no parameters in declarations
        // parameters are determined by position in state hierarchy
        for (String efqn : this.eventsOfState(sfqn)) {
            itemList.add(
                    new DashEventDecls(
                            Pos.UNKNOWN,
                            // list of size 1
                            new ArrayList<String>(Arrays.asList(DashFQN.chopNameFromFQN(efqn))),
                            this.eventKind(efqn)));
        }

        // collect trans
        // fqn name of trans tells us where it was declared
        // no parameters in declarations
        // parameters are determined by position in state hierarchy
        for (String tfqn : this.transOfState(sfqn)) {

            // these will be the resolved ones
            // getSrc returns a DashRef
            // DashFrom needs an expression,
            // but a DashRef is a form of expression

            // src -- cannot be null when in TransTable
            DashFrom fromR = new DashFrom(Pos.UNKNOWN, this.fromR(tfqn));

            DashWhen whenR =
                    (this.whenR(tfqn) != null) ? new DashWhen(Pos.UNKNOWN, this.whenR(tfqn)) : null;

            DashOn onR = (this.onR(tfqn) != null) ? new DashOn(Pos.UNKNOWN, this.onR(tfqn)) : null;

            DashDo doR = (this.doR(tfqn) != null) ? new DashDo(Pos.UNKNOWN, this.doR(tfqn)) : null;
            DashSend sendR =
                    (this.sendR(tfqn) != null) ? new DashSend(Pos.UNKNOWN, this.sendR(tfqn)) : null;

            // dest -- cannot be null when in TransTable
            DashGoto gotoR = new DashGoto(Pos.UNKNOWN, this.gotoR(tfqn));

            itemList.add(
                    new DashTrans(
                            Pos.UNKNOWN,
                            DashFQN.chopNameFromFQN(tfqn),
                            fromR,
                            gotoR,
                            onR,
                            sendR,
                            whenR,
                            doR));
        }
        /*
        // get invariants -- note that these are the original versions
        // so they aren't resolved, which makes them easy to print
        for (DashInv inv: d.st.getOrigInvariants(stateName)) itemList.add(inv);
        // get inits -- note that these are the original versions
        // so they aren't resolved, which makes them easy to print
        for (DashInit init: d.st.getOrigInits(stateName)) itemList.add(init);
        */
        // statenames are all FQNs
        return new DashState(
                Pos.UNKNOWN,
                DashFQN.chopNameFromFQN(sfqn),
                this.stateParam(sfqn).paramSig,
                this.stateKind(sfqn),
                this.def(sfqn),
                itemList);
    }
}
