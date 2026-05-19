package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.FIELD_TYPES;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.List;

public class FieldsA2T extends FactsA2T {

    /*
    each field f gets a VARIABLE added
    VARIABLES f1, f2...

    */

    public FieldsA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
        super(alloyModel, verbose, debug);
    }

    protected void addFieldTypes(TlaModel tlaModel) {
        List<TlaExp> fieldTypes = mapBy(alloyModel.allFields(), f -> fieldType(f));
        var defn = TlaDefn(FIELD_TYPES, repeatedAnd(fieldTypes));
        tlaModel.addDefn(defn);
    }

    protected void addFieldVars(TlaModel tlaModel) {

        for (String field : alloyModel.allFields()) {
            tlaModel.addVar(TlaVar(field));
        }
    }

    protected TlaExp fieldType(String field) {

        return TlaNullSet();
        // TlaVar parentSig = TlaVar(alloyModel.sigOfField(field));
        // TlaExp expr = new AlloyToTlaExprVis().visit(alloyModel.declOfField(field).expr);
        // return TlaVar(field).IN(TlaSubsetUnary(_CROSS(parentSig, expr)));
    }

    protected void fieldFacts(String field) {

        return;
        // var expr = alloyModel.declOfField(field).expr;
        // var mul = alloyModel.declOfField(field).mul;
    }
}
