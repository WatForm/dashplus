package ca.uwaterloo.watform.alloytotla;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaStdLibs;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class StdLibsA2T extends BaseA2T {

	public StdLibsA2T(AlloyModel alloyModel, String moduleName, boolean verbose, boolean debug) {
		super(alloyModel, moduleName, verbose, debug);
		translate();
	}
	public StdLibsA2T(AlloyModel alloyModel, TlaModel tlaModel, boolean verbose, boolean debug) {
		super(alloyModel, tlaModel, verbose, debug);
		translate();
	}

	private void translate()
	{
		tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_FiniteSets));
        tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_Integers));
        tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_Sequences));
	}
	
}
