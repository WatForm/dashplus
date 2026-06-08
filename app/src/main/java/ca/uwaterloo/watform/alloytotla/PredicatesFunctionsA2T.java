package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.*;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.AlloyPredPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.List;

class PredicatesFunctionsA2T extends NextDefnA2T {

    public PredicatesFunctionsA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
        super(alloyModel, verbose, debug);
    }

    protected void addPredicatesFunctions(TlaModel tlaModel) {
        testing(tlaModel);

        for (var p : alloyModel.allPredParas()) {
            TlaExp body = new AlloyToTlaExprVis().visit(p.block).extract();
            List<TlaVar> args = mapBy(p.arguments, decl -> TlaVar(decl.getName()));
            tlaModel.addDefn(new TlaDefn(new TlaDecl(p.getName(), args), body));
        }

        for (var p : alloyModel.allFunParas()) {
            TlaExp body = new AlloyToTlaExprVis().visit(p.block).extract();
            List<TlaVar> args = mapBy(p.arguments, decl -> TlaVar(decl.getName()));
            tlaModel.addDefn(new TlaDefn(new TlaDecl(p.qname.getName(), args), body));
        }
    }

    private void testing(TlaModel tlaModel) {
        List<AlloyPredPara> predParas = alloyModel.allPredParas();
        for (var p : predParas) {
            l.info(p.toString());
            l.info("name: " + p.getName());
            l.info("arguments:" + p.arguments.toString());
            l.info("block: " + p.block.toString());
        }
    }
}
