package ca.uwaterloo.watform.alloytotla;

import java.util.HashMap;

import ca.uwaterloo.watform.alloymodel.AlloyModel;

public class ScopeComputer
{

	/*
	
	General constraints:

	0) For every sig s, there is a max(s) and a min(s)
	1) if one s, then max(s) = min(s) = 1
	2) if t1, t2... tn in s, then:
		scope(ti) <= scope(s)
		min(s) >= min(ti) for i in 1..n
		max(ti) <= max(s) for i in 1..n
	3) if t1, t2.. tn extends s, then:
		sum of scope(ti) = scope(s)
		sum of scope(ti) >= scope(s) and
		sum of scope(ti) <= scope(s)

	two core facts from structure:

	if t in s, then scope(t) <= scope(s)
	if t1..tn in s, then sum of scope(ti) = scope(s)

	solution: mapping from set of s to N
	scope_set(s) = {n : there is a solution for the whole thing made up from scope-sets, where scope(s) = n}
	min(s) = min(scope_set(s))
	max(s) = max(scope_set(s))

	if one s, then min(s) = max(s) = 1

	1) if t in s:
		min(s) >= min(t)
		max(t) <= max(s)

	2) if t1..tn extends s:
		min(s) >= min(ti) for all i
		min(s) >= sum of min(ti) for all i
		min(s) <= sum of max(ti) for all i

		max(ti) <= max(s) for all i
		
		
	
	



	
	*/

	private HashMap<String, Integer> maxima;
	private HashMap<String, Integer> minima;

	private AlloyModel alloyModel;

	public ScopeComputer(AlloyModel alloyModel, int max)
	{
		this.maxima = new HashMap<>();
		this.minima = new HashMap<>();

		for(String s : alloyModel.allSigs())
		{
			maxima.put(s,max);
			minima.put(s,0);
		}

		for(String s : alloyModel.allSigs())
		{
			if(alloyModel.isOneSig(s))
				equalConstraint(s, 1);
		}
	}

	public int maximumScope(String s)
	{
		return this.maxima.get(s);
	}
	public int minimumScope(String s)
	{
		return this.minima.get(s);
	}

	public void maxConstraint(String s, int n)
	{
		// make it such that max(s) = n

		if(maxima.get(s) < n)
			maxima.put(s, n);

		// if sig t in sig s, then max(t) <= max(s)
		for(String t : alloyModel.inChildrenOfSig(s))
		{
			maxConstraint(t, n);
		}

		// if sig t1, t2... extends sig s, max(ti) <= max(s) - sum of min(tj) where j!=i
		for(String t : alloyModel.extendsChildrenOfSig(s))
		{
			// todo how is this done?
		}
	}

	public void minConstraint(String s, int n)
	{
		// make it such that min(s) = n

		if(minima.get(s) > n)
			minima.put(s, n);

		// if sig s in sig t, then min(t) >= min(s)
		for(String t : alloyModel.inParentsOfSig(s))
		{
			minConstraint(t, n);
		}

		alloyModel.extendsParentOfSig(s).ifPresent(t -> {
			int sum = 0;
			for(String u : alloyModel.extendsChildrenOfSig(t))
				sum+=minima.get(u);
			minConstraint(t, sum);
		});
		
	}

	public void equalConstraint(String s, int n)
	{
		minConstraint(s, n);
		maxConstraint(s, n);
	}

}