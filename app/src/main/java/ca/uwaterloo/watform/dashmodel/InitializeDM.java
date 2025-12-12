/*
 * Initialize
 *
 * The purpose of the dash model initialization phase
 * is to load the tables with information from the AST.
 *
 * A DashModel can have everything an AlloyModel can have
 * plus more.
 *
 * The error checking here is largely syntactic:
 * - duplicate names (which is discovered when adding to a table)
 * - names are fqns when they should not be
 * - too many/no defaults
 * - too many froms in a transition
 * etc.
 *
 * Initialization is straightforward:
 * - build up the list fqn name and list of parameters
 * as we descend through the DashState hierarchy
 * - the one tricky part is setting the default of a DashState
 * prior to entering it in the table
 */

package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloymodel.*;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.dashNamedExpr.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

public class InitializeDM extends PredsDM {

    // we don't store the DashFile here
    // because these tables might change
    // and might get out of sync with the DashFile

    public InitializeDM() {
        super();
    }

    public InitializeDM(DashFile d) {
        super(d);

        // we have to go through the paragraphs in
        // the entire model and do this for the one
        // root state

        DashState root = d.stateRoot;
        this.rootName = root.name;
        stateRecurseToInitilizeStatesVarsEventsBuffers(root, null, emptyList(), DefKind.DEFAULT, 0);
    }

    private void addToParamsList(DashParam p) {
        allParamsInOrder.add(p);
    }

    private void stateRecurseToInitilizeStatesVarsEventsBuffers(
            DashState s, String parentFQN, List<DashParam> parentParams, DefKind def, int depth) {

        // this state is not yet in the st
        // but its parent is in the st

        // figure out its sfqn and its parent's fqn
        if (DashFQN.isFQN(s.name)) Error.nameCantBeFQN(s.pos, s.name);
        String sfqn = DashFQN.fqn(parentFQN, s.name);

        List<DashParam> newParams = new ArrayList<DashParam>(parentParams);
        if (s.param != null) {
            DashParam p = new DashParam(sfqn, s.param);
            newParams.add(p);
            // add to the overall param list for the DashModel
            this.allParamsInOrder.add(p);
            if (this.maxDepthParams < depth) this.maxDepthParams = depth + 1;
        }

        // stuff that is put in the table with this state
        // and dealt with at the resolve phase
        List<DashInv> invList = s.invs();
        List<DashInit> initList = s.inits();

        List<DashTrans> transList = s.trans();
        for (DashTrans t : transList) {
            if (DashFQN.isFQN(t.name)) Error.nameCantBeFQN(t.pos, t.name);
        }

        // have to make a copy so that recursion does not just
        // continue to add to list everywhere
        List<DashState> substatesList = s.substates();

        List<String> childFQNs = new ArrayList<String>();
        for (DashState c : substatesList) childFQNs.add(DashFQN.fqn(sfqn, c.name));

        // add this state to the table
        // childFQNs can be empty
        addState(
                s.pos,
                sfqn,
                s.kind,
                new DashParam(sfqn, s.param),
                newParams,
                def,
                parentFQN,
                childFQNs);

        // process the children
        if (!substatesList.isEmpty()) {
            // check and set the default state(s)
            List<String> defList = new ArrayList<String>();

            if (substatesList.size() == 1) {
                // if there's only one child it is
                // automatically the default
                // whether the user set it is a default
                // or not
                defList.add(childFQNs.get(0));

            } else {

                List<DashState> givenDefaultsList =
                        filterBy(substatesList, i -> (i.def == DefKind.DEFAULT));
                List<DashState> andList = filterBy(substatesList, i -> (i.kind == StateKind.AND));
                List<DashState> orList = filterBy(substatesList, i -> (i.kind == StateKind.OR));

                if (andList.equals(substatesList)) {
                    // all substates are AND
                    if (!(givenDefaultsList.equals(andList))) {
                        // only some were called default
                        Error.allAndDefaults(andList.get(0).pos, sfqn);
                    } else {
                        // none or all AND states were called
                        // defaults
                        defList.addAll(childFQNs);
                    }
                } else if (givenDefaultsList.size() == 0) {
                    // we know there is more than one child
                    // and none were labelled defaults
                    Error.noDefaultState(s.pos, sfqn);

                } else {
                    // is a child OR state
                    if (givenDefaultsList.size() != 1) {
                        Error.tooManyDefaults(givenDefaultsList.get(1).pos, sfqn);
                    } else {
                        // one given def state
                        defList.add(givenDefaultsList.get(0).name);
                    }
                }
            }

            // add all substates to the table
            DefKind defk;
            for (DashState sub : substatesList) {
                // making sure all sibling states must
                // have different names
                // will be caught when children are
                // added to the state table
                defk = null;
                if (defList.contains(sub)) defk = DefKind.DEFAULT;
                else defk = DefKind.NOTDEFAULT;
                // want to keep only one place
                // where we call stateRecurse
                // to make sure all args are correct
                stateRecurseToInitilizeStatesVarsEventsBuffers(
                        sub, parentFQN, newParams, defk, depth + 1);
            }
        }

        // add declared events ---------------------
        List<DashEventDecls> eventDeclsList = s.eventDecls();

        // put in event table with FQN
        for (DashEventDecls e : eventDeclsList) {
            for (String x : e.getNames()) {
                if (DashFQN.isFQN(x)) {
                    Error.nameCantBeFQN(e.pos, x);
                } else {
                    String efqn = DashFQN.fqn(sfqn, x);
                    this.addEvent(e.pos, efqn, e.kind, newParams);
                }
            }
        }

        // add declared variables ------------------------
        List<DashVarDecls> varDeclsList = s.varDecls();

        // put in var table with FQN
        for (DashVarDecls v : varDeclsList) {
            for (String x : v.getNames()) {
                if (DashFQN.isFQN(x)) {
                    Error.nameCantBeFQN(v.pos, x);
                } else {
                    String vfqn = DashFQN.fqn(sfqn, x);
                    // v.typ will have to be resolved later
                    this.addVar(v.pos, vfqn, v.kind, newParams, v.typ);
                }
            }
        }

        // add declared buffers ---------------------------
        List<DashBufferDecls> bufferDeclsList = s.bufferDecls();

        // put in buffer table with FQN
        for (DashBufferDecls b : bufferDeclsList) {
            for (String x : b.getNames()) {
                if (DashFQN.isFQN(x)) {
                    Error.nameCantBeFQN(b.pos, x);
                } else {
                    String bfqn = DashFQN.fqn(sfqn, x);
                    this.addBuffer(b.pos, bfqn, b.kind, newParams, b.element);
                }
            }
        }

        // add preds ------------------------
        List<DashPred> predsList = s.preds();

        // put in var table with FQN
        for (DashPred p : predsList) {
            if (DashFQN.isFQN(p.name)) {
                Error.nameCantBeFQN(p.pos, p.name);
            } else {
                String nfqn = DashFQN.fqn(sfqn, p.name);
                this.addPred(p.pos, nfqn, p.exp);
            }
        }
    }

    private class Error {

        private static void notDashModel() throws Reporter.ErrorUser {
            throw new Reporter.ErrorUser("No Dash state in this model.");
        }

        public static void allAndDefaults(Pos pos, String sfqn) throws Reporter.ErrorUser {
            throw new Reporter.ErrorUser(
                    pos,
                    "All conc children of state must be defaults if one is a default: " + sfqn);
        }

        public static void noDefaultState(Pos pos, String fqn) throws Reporter.ErrorUser {
            throw new Reporter.ErrorUser(pos, "State does not have default state: " + fqn);
        }

        public static void tooManyDefaults(Pos pos, String fqn) throws Reporter.ErrorUser {
            throw new Reporter.ErrorUser(pos, "Too many default states in state: " + fqn);
        }

        public static void duplicateStateName(Pos pos, String fqn) throws Reporter.ErrorUser {
            throw new Reporter.ErrorUser(pos, fqn + "is a duplicate state name");
        }

        public static void onlyOneState(Pos pos) throws Reporter.ErrorUser {
            throw new Reporter.ErrorUser(pos, "Dash model can only have one 'state' section");
        }

        public static void nameCantBeFQN(Pos pos, String name) throws Reporter.ErrorUser {
            throw new Reporter.ErrorUser(pos, "When declared, name cannot have slash: " + name);
        }

        public static void dupNames(Pos pos, String dups) throws Reporter.ErrorUser {
            throw new Reporter.ErrorUser(pos, "Duplicate names: " + dups);
        }
    }
}
