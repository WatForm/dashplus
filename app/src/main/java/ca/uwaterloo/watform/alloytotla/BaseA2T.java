package ca.uwaterloo.watform.alloytotla;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.utils.CustomLoggerFactory;
import java.util.logging.Logger;

public class BaseA2T {
    public final AlloyModel alloyModel;
    public final boolean verbose;
    public final boolean debug;
    public final Logger l;
    public final AlloyToTlaExprVis translator;

    public TlaExp translateSnippet(AlloyExpr e) {

        l.info("translating core:" + e.toString());

        return translator.visit(e).core;
    }

    public BaseA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
        this.alloyModel = alloyModel;
        this.verbose = verbose;
        this.debug = debug;
        this.l = CustomLoggerFactory.make("AlloyToTla", debug);
        this.translator = new AlloyToTlaExprVis(alloyModel, l);
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
    public (AlloyModel alloyModel, boolean verbose, boolean debug) {
    	super(alloyModel,verbose, debug);
    	translate();
    }

    public void translate()
    {

    }
    */

}
