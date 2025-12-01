package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModel;

public class DashToTLAPlus {
    public static TLAPlusModel translate(DashModel dashModel, String moduleName) {

        TLAPlusModel model = new TLAPlusModel(moduleName, Common.getInit(), Common.getNext());

        StandardLibraries.addStandardLibraries(dashModel, model);
        StandardVariables.standardVariables(dashModel, model);

        model.addComment(
                "State literals, represented as sets of strings. Leaf-states become strings and non-leaf states are composed of their descendants");
        StateFormulae.stateFormulae(dashModel, model);

        model.addBlankLine();
        model.addComment(
                "string literal representations of transitions taken, which are the values taken by the "
                        + Common.getTransTaken()
                        + " variable");
        Transitions.transitionFormulae(dashModel, model);

        model.addBlankLine();
        model.addComment("type restrictions on variables");
        TypeOKFormula.AddTypeOKFormula(dashModel, model);

        model.addBlankLine();
        model.addComment("initial values for variables");
        InitFormula.addInitFormula(dashModel, model);

        return model;
    }
}
