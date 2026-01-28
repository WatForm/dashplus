package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.List;

public class DashToTla {
    public static TlaModel translate(
            DashModel dashModel,
            String moduleName,
            boolean singleEnvInput,
            boolean verbose,
            boolean debug) {

        TlaModel tlaModel = new TlaModel(moduleName, new TlaAppl(INIT), new TlaAppl(NEXT));

        StdLibDefns.translate(dashModel, tlaModel);
        System.out.println("translated libraries");

        List<String> vars = StandardVars.translate(dashModel, tlaModel);
        System.out.println("translated variables");

        tlaModel.addComment(
                "State literals, represented as sets of strings. Leaf-states become strings and non-leaf states are composed of their descendants");
        StateDefns.translate(dashModel, tlaModel);
        System.out.println("translated states");

        tlaModel.addComment(
                "string literal representations of transitions taken, which are the values taken by the "
                        + EVENTS
                        + " variable");
        EventDefns.translate(dashModel, tlaModel);
        System.out.println("translated events");

        tlaModel.addComment(
                "string literal representations of transitions taken, which are the values taken by the "
                        + TRANS_TAKEN
                        + " variable");
        TransDefns.translate(vars, dashModel, tlaModel);
        System.out.println("translated transitions");

        tlaModel.addComment("Small step definition");
        SmallStepDefn.translate(dashModel, tlaModel);

        tlaModel.addComment("type restrictions on variables");
        ValidDefns.translate(dashModel, tlaModel);

        tlaModel.addComment("initial values for variables");
        InitDefn.translate(dashModel, tlaModel);

        tlaModel.addComment("Next relation");
        NextDefn.translate(dashModel, tlaModel);

        tlaModel.addComment("single environmental event assumption");
        SingleEnvEvent.translate(dashModel, tlaModel);

        return tlaModel;
    }
}
