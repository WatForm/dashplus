package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaplusmodel.TlaModel;

public class DashToTLAPlus {
    public static TlaModel translate(DashModel dashModel, String moduleName) {

        TlaModel tlaModel =
                new TlaModel(
                        moduleName, TranslationStrings.INIT.appl(), TranslationStrings.NEXT.appl());

        StandardLibraries.addStandardLibraries(dashModel, tlaModel);
        StandardVariables.standardVariables(dashModel, tlaModel);

        tlaModel.addComment(
                "State literals, represented as sets of strings. Leaf-states become strings and non-leaf states are composed of their descendants");
        StateDefinitions.stateFormulae(dashModel, tlaModel);

        tlaModel.addBlankLine();
        tlaModel.addComment(
                "string literal representations of transitions taken, which are the values taken by the "
                        + TranslationStrings.TRANS_TAKEN.name
                        + " variable");
        TransitionDefinitions.transitionFormulae(dashModel, tlaModel);

        tlaModel.addBlankLine();
        tlaModel.addComment("type restrictions on variables");
        TypeOKDefinitions.AddTypeOKFormula(dashModel, tlaModel);

        tlaModel.addBlankLine();
        tlaModel.addComment("initial values for variables");
        InitDefinition.addInitFormula(dashModel, tlaModel);

        return tlaModel;
    }
}
