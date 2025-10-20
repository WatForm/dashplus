package ca.uwaterloo.watform.dashmodel;

import java.util.List;
import java.util.ArrayList;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.AlloyParagraph;
import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloymodel.AlloyModel;

import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.dashast.DashParagraph;
import ca.uwaterloo.watform.dashast.DashState;
import ca.uwaterloo.watform.dashast.DashDo;
import ca.uwaterloo.watform.dashast.DashFrom;
import ca.uwaterloo.watform.dashast.DashGoto;
import ca.uwaterloo.watform.dashast.DashSend;
import ca.uwaterloo.watform.dashast.DashWhen;
import ca.uwaterloo.watform.dashast.DashOn;
import ca.uwaterloo.watform.dashast.DashTrans;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.DashFile;

import ca.uwaterloo.watform.dashmodel.DashFQN.*;
import ca.uwaterloo.watform.dashmodel.DashPredTable;
import ca.uwaterloo.watform.dashmodel.VarTable;
import ca.uwaterloo.watform.dashmodel.BufferTable;
import ca.uwaterloo.watform.dashmodel.EventTable;
import ca.uwaterloo.watform.dashmodel.StateTable;
import ca.uwaterloo.watform.dashmodel.TransTable;

public class DashModelInitialize { // extends AlloyModel {

	// we don't store the DashFile here 
	// because these tables are mutable
	// and might get out of sync with the DashFile

	public StateTable st = new StateTable();
	public TransTable tt = new TransTable();
	public EventTable et = new EventTable();
	public VarTable vt = new VarTable();
	public DashPredTable pt = new DashPredTable();

	public DashModelInitialize(DashFile d)  {

		/*
	 	 * check for errors in the state hierarchy
		 * and put all states in the state table
	 	*/
	 	// we have to go through the paragraphs in
	 	// the entire model and do this for the one
	 	// root state
	 	//super((AlloyFile) d);

	 	Boolean foundOneState = false;
	 	for (DashParagraph p:d.paragraphs) {
	 		if (p instanceof DashState) {
	 			if (!foundOneState) {
					st.root = (((DashState)p).name);
					//stateRecurse(p,emptyList());
					foundOneState = true;
				} else {
					DashModelErrors.onlyOneState(p.pos);
				}
			}
		}
	}
	/*
	private void stateRecurse(
		DashState s,
	 	List<String> ances)  {
		// this state is not yet in the st
		// but its parent is in the st

		String name = s.name;
		if (DashFQN.isFQN(name)) DashModelErrors.stateNameCantBeFQN(s.pos, name);
		String sfqn = DashFQN.fqn(ances,name);
		String parentfqn = DashFQN.fqn(ances);

		// make a copy of the items list so we can
		// subtract from it for error checking
		// but keep the original items for printing, etc.
		List<Object> xItems = new ArrayList<Object>(items);

		// invariants ---------------------
		List <DashInv> invList = new ArrayList<DashInv>();
		if (items != null)
			invList = mapBy(filterBy(items, i -> i instanceof DashInv),p -> (DashInv) p);
		xItems.removeAll(invList);

		// inits ---------------------
		List <DashInit> initList = new ArrayList<DashInit>();
		if (items != null)
			initList = mapBy(filterBy(items, i -> i instanceof DashInit),p -> (DashInit) p);
		xItems.removeAll(initList);

		// entered ---------------------
		List <Expr> enteredList = new ArrayList<Expr>();
		if (items != null)
			enteredList = 
				mapBy(filterBy(items, i -> i instanceof DashEntered),p -> ((DashEntered) p).getExp());
		// enteredList is a list of Exp
		// to remove, we need a list of items
		xItems.removeAll(
				items.stream()
				.filter(i -> i instanceof DashEntered)
				.collect(Collectors.toList()));

		// exited ---------------------
		List <Expr> exitedList = new ArrayList<Expr>();
		if (items != null)
			exitedList = 
				items.stream()
				.filter(i -> i instanceof DashExited)
				.map(p -> ((DashExited) p).getExp())
				.collect(Collectors.toList());
		// exitedList is a list of Exp
		// to remove, we need a list of items
		xItems.removeAll(
				items.stream()
				.filter(i -> i instanceof DashExited)
				.collect(Collectors.toList()));

		// ---------------------
		// process the children
		// have to make a copy so that recursion does not just
		// continue to add to list everywhere				
		List<String> newAnces = new ArrayList<String>(ances);
		newAnces.add(name);		
		List<DashParam> newParams = new ArrayList<DashParam>();
		if (parentfqn != null) {
			newParams.addAll(st.getParams(parentfqn));
		}
		if (param != null) {
			DashParam p = new DashParam(sfqn,param);
			newParams.add(p);
			st.addToParamsList(p);
		}

		List<DashState> substatesList = new ArrayList<DashState>();
		if (items != null)
			substatesList = 
				xItems.stream()
				.filter(i -> i instanceof DashState)
				.map(p -> (DashState) p)
				.collect(Collectors.toList());

		if (substatesList.isEmpty() ) {
			if (!st.add(sfqn, kind, newParams, def,parentfqn, new ArrayList<String>(),
				invList, initList, enteredList, exitedList)) DashErrors.addStateToStateTableDup(sfqn);;
			
		} else {
			
			// all sibling states must have different names
			ArrayList<String> childFQNs = new ArrayList();
			substatesList.forEach(i -> childFQNs.add(DashFQN.fqn(ances,name, i.name)));
			Set<String> dups = DashUtilFcns.findDuplicates(childFQNs);
			if (!dups.isEmpty()) 
				DashErrors.dupSiblingNames(DashUtilFcns.strCommaList(dups.stream().collect(Collectors.toList())));

			// add this state to the table
			if (!st.add(sfqn,kind, newParams,def, parentfqn, childFQNs,
				invList, initList, enteredList, exitedList)) DashErrors.addStateToStateTableDup(sfqn);;

			// add all substates to the table
			for (DashState sub: substatesList) 
				stateRecurse(sub, newAnces);

			// make sure defaults are correct
			// if there's only one child it is automatically the default
			if (substatesList.size() == 1) {
				// make sure it is set as default
				// this child should already be in the state table
				// might already be set as duplicate but that's okay
				// have to use the substate's FQN here
				st.setAsDefault(childFQNs.get(0));
			} else {
				// default states
				List<DashState> defaultsList = 
					substatesList.stream()
					.filter(i -> (i.def == DashStrings.DefKind.DEFAULT))
					.collect(Collectors.toList());
				List<DashState> andList = 
					substatesList.stream()
					.filter(i -> (i.kind == DashStrings.StateKind.AND))
					.collect(Collectors.toList());
				if (andList.equals(substatesList) && defaultsList.size() == 0) {
					// all AND-states are not designated as defaults so all are defaults
					for (String ch: childFQNs) st.setAsDefault(ch);
				} else if (defaultsList.size() == 0) 
					DashErrors.noDefaultState(sfqn);
				else {
					// if defaults list contains an OR states, it should be size 1
					boolean flag = defaultsList.stream().anyMatch( (s) -> s.kind == DashStrings.StateKind.OR);
					if (flag) {
						if (defaultsList.size() != 1) DashErrors.tooManyDefaults(sfqn);	
						// o/w one OR state is default
					} else {					
						// if defaults list is all c's, all c children should be included
						//System.out.println("defaultsList: "+defaultsList);
						//System.out.println("andList: "+andList);
						if (!(defaultsList.equals(andList))) DashErrors.allAndDefaults(sfqn);
					}
				}
			}
		}
		xItems.removeAll(substatesList);
		
		// add declared events ---------------------
		List <DashEventDecls> eventDeclsList = new ArrayList<DashEventDecls>();
		if (items != null)
			eventDeclsList = 
				xItems.stream()
				.filter(i -> i instanceof DashEventDecls)
				.map(p -> (DashEventDecls) p)
				.collect(Collectors.toList());
		// put in event table with FQN 
		for (DashEventDecls e:eventDeclsList) {
			DashStrings.IntEnvKind k = e.getKind();
			for (String x: e.getNames()) {
				if (DashFQN.isFQN(x)) DashErrors.eventNameCantBeFQN(e.getPos(), x);
				String xfqn = DashFQN.fqn(sfqn,x);
				if (!et.add(xfqn,k, newParams)) DashErrors.duplicateEventName(e.getPos(),x);
			}
		}
		xItems.removeAll(eventDeclsList);

		// add declared variables ------------------------
		List <DashVarDecls> varDeclsList = new ArrayList<DashVarDecls>();
		if (items != null)
			varDeclsList = 
				items.stream()
				.filter(i -> i instanceof DashVarDecls)
				.map(p -> (DashVarDecls) p)
				.collect(Collectors.toList());
		// put in var table with FQN 
		for (DashVarDecls v:varDeclsList) {
			DashStrings.IntEnvKind k = v.getKind();
			Expr t = v.getTyp();
			for (String x: v.getNames()) {
				if (DashFQN.isFQN(x)) DashErrors.varNameCantBeFQN(v.getPos(), x);
				String xfqn = DashFQN.fqn(sfqn,x);
				if (!vt.addVar(xfqn,k, newParams,t)) DashErrors.duplicateVarName(v.getPos(),x);
			}
		}
		xItems.removeAll(varDeclsList);

		// add preds ------------------------
		List <DashPred> predsList = new ArrayList<DashPred>();
		if (items != null)
			predsList = 
				items.stream()
				.filter(i -> i instanceof DashPred)
				.map(p -> (DashPred) p)
				.collect(Collectors.toList());
		// put in var table with FQN 
		for (DashPred p:predsList) {
			String name = p.getName();
			Expr e = p.getExp();
			if (DashFQN.isFQN(name)) DashErrors.nameCantBeFQN(p.getPos(), name);
			String nfqn = DashFQN.fqn(sfqn,name);
			if (!pt.addPred(nfqn,e)) DashErrors.duplicateName(p.getPos(),name);
		}
		xItems.removeAll(predsList);

		// add declared buffers ---------------------------
		List <DashBufferDecls> bufferDeclsList = new ArrayList<DashBufferDecls>();
		if (items != null)
			bufferDeclsList = 
				items.stream()
				.filter(i -> i instanceof DashBufferDecls)
				.map(p -> (DashBufferDecls) p)
				.collect(Collectors.toList());
		// put in var table with FQN 
		for (DashBufferDecls b:bufferDeclsList) {
			DashStrings.IntEnvKind k = b.getKind();
			String el = b.getElement();
			Integer idx = b.getStartIndex();
			for (String x: b.getNames()) {
				if (DashFQN.isFQN(x)) DashErrors.bufferNameCantBeFQN(b.getPos(), x);
				String xfqn = DashFQN.fqn(sfqn,x);
				if (!vt.addBuffer(xfqn,k, newParams, el, idx)) DashErrors.duplicateBufferName(b.getPos(),x);
				idx++;
			}
			if (idx != b.getEndIndex()+1) DashErrors.bufferIndexDoesNotMatchBufferNumber();
		}
		xItems.removeAll(bufferDeclsList);

		// add transitions ----------------------
		List <DashTrans> transList = new ArrayList<DashTrans>();
		if (items != null)
			transList = 
				items.stream()
				.filter(i -> i instanceof DashTrans)
				.map(p -> (DashTrans) p)
				.collect(Collectors.toList());
		
		for (DashTrans t:transList) {
			//System.out.println("newAnces: " +newAnces);
			addTrans(t, newParams, newAnces);
		}
		
		xItems.removeAll(transList);

		if (!xItems.isEmpty()) DashErrors.nonEmptyStateItems(xItems);
	}
	*/
	public void addTrans(DashTrans t, List<DashParam> params, List<String> ances) {

		if (DashFQN.isFQN(t.name)) 
			DashModelErrors.nameCantBeFQN(t.pos, t.name);
		String tfqn = DashFQN.fqn(ances,t.name);
        List<DashFrom> fromList = 
        	extractItemsOfClass(t.items, DashFrom.class);
		List<DashOn> onList =
			extractItemsOfClass(t.items, DashOn.class);
		List<DashWhen> whenList =
			extractItemsOfClass(t.items, DashWhen.class);
		List<DashGoto> gotoList = 
			extractItemsOfClass(t.items, DashGoto.class);
		List<DashSend> sendList =
			extractItemsOfClass(t.items, DashSend.class);
		List<DashDo> doList =
			extractItemsOfClass(t.items, DashDo.class);
		if (!tt.add(tfqn,params, fromList, onList, whenList, gotoList, sendList, doList)) 
			DashModelErrors.dupNames(t.pos,t.name);
	}	
}
