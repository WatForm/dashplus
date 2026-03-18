package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import ca.uwaterloo.watform.utils.CustomLoggerFactory;
import java.util.logging.Logger;

public class BaseA2T {
    public final AlloyModel alloyModel;
    public final TlaModel tlaModel;
    public final boolean verbose;
    public final boolean debug;
    public final Logger l;

    public BaseA2T(AlloyModel alloyModel, TlaModel tlaModel, boolean verbose, boolean debug) {
        this.alloyModel = alloyModel;
        this.tlaModel = tlaModel;
        this.verbose = verbose;
        this.debug = debug;
        this.l = CustomLoggerFactory.make("AlloyToTla", debug);
    }

    /*
    order:
    StdLibs
    Boilerplate
    SigConsts
    SigVars
    FieldVars
    SigHierarchy
    SigConstraints
    Facts
    InitDefn
    NextDefn

    */

    /*
    public (AlloyModel alloyModel, String moduleName, boolean verbose, boolean debug) {
    	super(alloyModel, moduleName, verbose, debug);
    	translate();
    }
    public (AlloyModel alloyModel, TlaModel tlaModel, boolean verbose, boolean debug) {
    	super(alloyModel, tlaModel, verbose, debug);
    	translate();
    }

    public void translate()
    {

    }
    */

}
