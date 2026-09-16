package ca.uwaterloo.watform.alloytotla;

import ca.uwaterloo.watform.alloytotla.BaseA2T.Optimization;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class PostProcessing 
{
	public static void main(TlaModel tlaModel, Optimization optimization)
	{
		if(optimization.unusedMacros())
		{
			removeUnusedMacros(tlaModel);
		}
		if(optimization.unusedSigs())
		{
			removeUnusedVars(tlaModel);
		}
	}

	private static void removeUnusedMacros(TlaModel tlaModel)
	{
		// TODO: recursively search and remove unused macros
		// The root is Init and Next
		// from the root, BFS to list every used Defn
		// then iterate through existing defns and remove all unused ones
	}
	private static void removeUnusedVars(TlaModel tlaModel)
	{
		/*
		TODO: if any variable is not used in any definition other than Init, Next and Scope, it can be purged entirely
		purge algorithm:
		for a given variable V:
		find all nodes of AST where it appears
		let N be one such node
		for all N:
		go up until encountering a AND or OR
		remove that child along the path towards N
		
		then, remove V from the list of variables
		
		*/
	}
}
