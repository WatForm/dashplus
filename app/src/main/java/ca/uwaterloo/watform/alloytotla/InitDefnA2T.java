package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.SIG_SETS_UNPRIMED;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import java.util.Arrays;
import java.util.List;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;


public class InitDefnA2T extends FieldVarsA2T {
	
	
	public InitDefnA2T(AlloyModel alloyModel, TlaModel tlaModel, boolean verbose, boolean debug) {
		super(alloyModel, tlaModel, verbose, debug);
	}
	
	protected void addInitDefn()
	{
		List<TlaAppl> exps =
                Arrays.asList(
                        TlaAppl(SIG_SETS_UNPRIMED),
                        TlaAppl(ALL_SIG_CONSTRAINTS),
                        TlaAppl(ALL_FACTS));
        tlaModel.addDefn(TlaDefn(INIT, repeatedAnd(exps)));
	}
}
