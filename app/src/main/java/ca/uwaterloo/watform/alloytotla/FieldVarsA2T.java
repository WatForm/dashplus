package ca.uwaterloo.watform.alloytotla;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.fieldVar;

public class FieldVarsA2T extends FactsA2T {
	
	
	public FieldVarsA2T(AlloyModel alloyModel, TlaModel tlaModel, boolean verbose, boolean debug) {
		super(alloyModel, tlaModel, verbose, debug);
	}
	
	protected void addFieldVars()
	{
		alloyModel
                .allSigs()
                .forEach(
                        sn ->
                                alloyModel
                                        .fieldNames(sn)
                                        .forEach(fn -> tlaModel.addVar(fieldVar(fn, sn))));
	}

}
