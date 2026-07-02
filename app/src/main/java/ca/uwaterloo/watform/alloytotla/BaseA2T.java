package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaSet;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaStringLiteral;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaTuple;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaStringLiteral;
import ca.uwaterloo.watform.tlaast.tlanaryops.TlaSet;
import ca.uwaterloo.watform.tlaast.tlanaryops.TlaTuple;
import ca.uwaterloo.watform.utils.CustomLoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BaseA2T {
    public final AlloyModel alloyModel;
    public final boolean verbose;
    public final boolean debug;
    public final Logger l;
    public final AlloyToTlaExprVis translator;

    public final StringBuilder transcript;

    public TlaExp translateSnippet(AlloyExpr e) {
        return translator.extract(translator.visit(e));
    }

    public String dump() {
        String answer = transcript.toString();
        transcript.setLength(0);
        return answer;
    }

    public void log(String s) {
        transcript.append("\n" + s);
    }

    public BaseA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
        this.alloyModel = alloyModel;
        this.verbose = verbose;
        this.debug = debug;
        this.l = CustomLoggerFactory.make("AlloyToTla", debug);
        this.translator = new AlloyToTlaExprVis(alloyModel, l);
        this.transcript = new StringBuilder("");
    }

    /*
    commonly used functions:
    */

    protected TlaStringLiteral sigAtomString(String signame, int n) {
        return TlaStringLiteral(signame + AlloyToTlaStrings.DOLLAR + n);
    }

    protected TlaTuple sigAtom(String signame, int n) {
        return TlaTuple(sigAtomString(signame, n));
    }

    protected TlaSet sigAtoms(String signame, int start, int end) {
        List<TlaTuple> atoms = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            atoms.add(sigAtom(signame, i));
        }
        return TlaSet(atoms);
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
