package ca.uwaterloo.watform.tlamodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.filterBy;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaexpvisitor.TlaApplCollector;

public class TreeShaker {
	public static TlaModel removeUnusedDefns(TlaModel tlaModel)
	{
		TlaModel answer = new TlaModel(tlaModel.name, tlaModel.getInit(), tlaModel.getNext());

		return answer;
	}
	private static List<TlaDefn> actuallyUsed(TlaModel tlaModel)
	{
		
		List<TlaDefn> answer = new ArrayList<>();
		List<TlaAppl> usedAppls = new ArrayList<>();
		List<TlaDefn> allDefns = new ArrayList<>();
		for(var b : tlaModel.module.body)
			if(b instanceof TlaDefn defn)
				allDefns.add(defn);
		
		// add Init and Next and constants and invariants and properties
		var collector = new TlaApplCollector();
		usedAppls.addAll(collector.visit(tlaModel.getInit()));
		usedAppls.addAll(collector.visit(tlaModel.getNext()));
		for(var prop : tlaModel.cfg.properties)
			usedAppls.addAll(collector.visit(prop));
		for(var inv : tlaModel.cfg.invariants)
			usedAppls.addAll(collector.visit(inv));
		for(var c : tlaModel.cfg.properties)
			usedAppls.addAll(collector.visit(c));

		boolean changes;
		do
		{
			changes = false;
		}
		while(changes);

		return answer;
		
	}
	
}
