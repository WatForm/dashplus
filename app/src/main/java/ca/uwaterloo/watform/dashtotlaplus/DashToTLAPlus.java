package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;

public class DashToTLAPlus {
    public static TLAPlusModel translate(DashModel dashModel, String moduleName) {
        TLAPlusModel model = new TLAPlusModel(moduleName, Common.getInit(), Common.getNext());

        StandardVariables.standardVariables(dashModel, model);
        StateFormulae.stateFormulae(dashModel, model);

        return model;
    }
}
