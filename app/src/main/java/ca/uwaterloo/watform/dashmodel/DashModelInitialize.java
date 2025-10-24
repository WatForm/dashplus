/*
 * Initialize
 *
 * The purpose of the dash model initialization phase
 * is to load the tables with information from the AST.
 *
 * The error checking here is largely syntactic:
 * - duplicate names (which is discovered when adding to a table)
 * - names are fqns when they should not be
 * - too many/no defaults
 * - too many froms in a transition
 * etc.
 *
 */

package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashmodel.DashFQN.*;
import java.util.ArrayList;
import java.util.List;

public class DashModelInitialize { // extends AlloyModel {

    // we don't store the DashFile here
    // because these tables might change
    // and might get out of sync with the DashFile

    public StateTable st = new StateTable();
    public TransTable tt = new TransTable();
    public EventTable et = new EventTable();
    public VarTable vt = new VarTable();
    public BufferTable bt = new BufferTable();
    public DashPredTable pt = new DashPredTable();

    public DashModelInitialize(DashFile d) {

        // super((AlloyFile) d);

        // we have to go through the paragraphs in
        // the entire model and do this for the one
        // root state

        List<DashState> dashStates = extractItemsOfClass(d.paragraphs, DashState.class);
        if (dashStates.isEmpty()) {
            DashModelErrors.notDashModel();
        } else if (dashStates.size() > 1) {
            DashModelErrors.onlyOneState(dashStates.get(1).pos);
        } else {
            DashState root = dashStates.get(0);
            st.root = (root.name);
            stateRecurse(root, emptyList());
        }
    }

    private void stateRecurse(DashState s, List<String> ances) {
        // this state is not yet in the st
        // but its parent is in the st

        // figure out its sfqn and its parent's fqn
        if (DashFQN.isFQN(s.name)) DashModelErrors.nameCantBeFQN(s.pos, s.name);
        String sfqn = DashFQN.fqn(ances, s.name);
        String parentfqn = DashFQN.fqn(ances);

        // ---------------------
        // process the children
        // have to make a copy so that recursion does not just
        // continue to add to list everywhere
        List<String> newAnces = new ArrayList<String>(ances);
        newAnces.add(s.name);

        List<DashParam> newParams = new ArrayList<DashParam>();
        if (parentfqn != null) {
            newParams.addAll(st.getParams(parentfqn));
        }
        if (s.param != null) {
            DashParam p = new DashParam(sfqn, s.param);
            newParams.add(p);
            st.addToParamsList(p);
        }

        List<DashInv> invList = s.invs();
        List<DashInit> initList = s.inits();

        List<DashState> substatesList = s.substates();
        if (substatesList.isEmpty()) {
            st.add(
                    s.pos,
                    sfqn,
                    s.kind,
                    newParams,
                    s.def,
                    parentfqn,
                    // no children
                    new ArrayList<String>(),
                    invList,
                    initList);
        } else {

            ArrayList<String> childFQNs = new ArrayList();
            s.substates().forEach(i -> childFQNs.add(DashFQN.fqn(ances, s.name, i.name)));

            // add this state to the table
            st.add(s.pos, sfqn, s.kind, newParams, s.def, parentfqn, childFQNs, invList, initList);
            // NADTODO we might want to return here if that st is a duplicate
            // if this does not already happen in the stateTable

            // add all substates to the table
            for (DashState sub : substatesList)
                // all sibling states must have different names
                // will be caught when children are added to state table
                stateRecurse(sub, newAnces);

            // make sure defaults are correct
            // if there's only one child it is automatically the default
            if (substatesList.size() == 1) {
                // make sure it is set as default
                // this child should already be in the state table
                // might already be set as default but that's okay
                // have to use the substate's FQN here
                st.setDefault(childFQNs.get(0));
            } else {

                List<DashState> defaultsList =
                        filterBy(substatesList, i -> (i.def == DefKind.DEFAULT));
                List<DashState> andList = filterBy(substatesList, i -> (i.kind == StateKind.AND));

                if (andList.equals(substatesList) && defaultsList.size() == 0) {
                    // all AND-states are not designated as defaults
                    // therefore all are defaults
                    for (String ch : childFQNs) st.setDefault(ch);

                } else if (defaultsList.size() == 0) {
                    DashModelErrors.noDefaultState(s.pos, sfqn);

                } else if (containsMatch(substatesList, o -> o.kind == StateKind.OR)) {
                    // if defaults list contains an OR state, it should be size 1
                    if (defaultsList.size() != 1) {
                        DashModelErrors.tooManyDefaults(defaultsList.get(1).pos, sfqn);
                    }
                    // o/w one OR state is default

                } else {
                    // if defaults list is all AND, then all children should be included
                    if (!(defaultsList.equals(andList))) {
                        DashModelErrors.allAndDefaults(andList.get(0).pos, sfqn);
                    }
                }
            }
        }

        // add declared events ---------------------
        // this can be a list of events
        List<DashEventDecls> eventDeclsList = s.eventDecls();

        // put in event table with FQN
        for (DashEventDecls e : eventDeclsList) {
            IntEnvKind k = e.getKind();
            for (String x : e.getNames()) {
                if (DashFQN.isFQN(x)) {
                    DashModelErrors.nameCantBeFQN(e.pos, x);
                } else {
                    String xfqn = DashFQN.fqn(sfqn, x);
                    et.add(e.pos, xfqn, k, newParams);
                }
            }
        }

        // add declared variables ------------------------
        // this can be a list of vars
        List<DashVarDecls> varDeclsList = s.varDecls();

        // put in var table with FQN
        for (DashVarDecls v : varDeclsList) {
            IntEnvKind k = v.getKind();
            AlloyExpr t = v.getTyp();
            for (String x : v.getNames()) {
                if (DashFQN.isFQN(x)) {
                    DashModelErrors.nameCantBeFQN(v.pos, x);
                } else {
                    String xfqn = DashFQN.fqn(sfqn, x);
                    vt.addVar(v.pos, xfqn, k, newParams, t);
                }
            }
        }

        // add preds ------------------------
        List<DashPred> predsList = s.preds();

        // put in var table with FQN
        for (DashPred p : predsList) {
            String name = p.getName();
            AlloyExpr e = p.getExp();
            if (DashFQN.isFQN(name)) {
                DashModelErrors.nameCantBeFQN(p.pos, name);
            } else {
                String nfqn = DashFQN.fqn(sfqn, name);
                pt.add(p.pos, nfqn, e);
            }
        }

        // add declared buffers ---------------------------
        List<DashBufferDecls> bufferDeclsList = s.bufferDecls();

        // put in buffer table with FQN
        for (DashBufferDecls b : bufferDeclsList) {
            IntEnvKind k = b.getKind();
            String el = b.getElement();
            for (String x : b.getNames()) {
                if (DashFQN.isFQN(x)) {
                    DashModelErrors.nameCantBeFQN(b.pos, x);
                } else {
                    String bfqn = DashFQN.fqn(sfqn, x);
                    bt.add(b.pos, bfqn, k, newParams, el);
                }
            }
        }

        // add transitions ----------------------
        List<DashTrans> transList = s.trans();

        for (DashTrans t : transList) {
            // System.out.println("newAnces: " +newAnces);
            addTrans(t, newParams, newAnces);
        }
    }

    public void addTrans(DashTrans t, List<DashParam> params, List<String> ances) {

        if (DashFQN.isFQN(t.name)) DashModelErrors.nameCantBeFQN(t.pos, t.name);
        String tfqn = DashFQN.fqn(ances, t.name);

        List<DashFrom> fromList = t.froms();
        if (fromList.size() > 1) DashModelErrors.tooMany(fromList.get(1).pos, "from", tfqn);

        List<DashGoto> gotoList = t.gotos();
        if (gotoList.size() > 1) DashModelErrors.tooMany(gotoList.get(1).pos, "goto", tfqn);

        List<DashOn> onList = t.ons();
        if (onList.size() > 1) DashModelErrors.tooMany(onList.get(1).pos, "on", tfqn);

        List<DashWhen> whenList = t.whens();
        if (whenList.size() > 1) DashModelErrors.tooMany(whenList.get(1).pos, "when", tfqn);

        List<DashSend> sendList = t.sends();
        if (sendList.size() > 1) DashModelErrors.tooMany(sendList.get(1).pos, "send", tfqn);

        List<DashDo> doList = t.dos();
        if (doList.size() > 1) DashModelErrors.tooMany(doList.get(1).pos, "do", tfqn);

        tt.add(t.pos, tfqn, params, fromList, onList, whenList, gotoList, sendList, doList);
    }
}
