package ca.uwaterloo.watform.alloytotla;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;

import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaVar;

public class SigVarsA2T extends SigConstsA2T {

	public SigVarsA2T(AlloyModel alloyModel, String moduleName, boolean verbose, boolean debug) {
		super(alloyModel, moduleName, verbose, debug);
		translate();
	}
	public SigVarsA2T(AlloyModel alloyModel, TlaModel tlaModel, boolean verbose, boolean debug) {
		super(alloyModel, tlaModel, verbose, debug);
		translate();
	}


	public void translate()
	{
		for (String sigName : alloyModel.allSigs()) tlaModel.addVar(TlaVar(sigName));
	}
	
}
