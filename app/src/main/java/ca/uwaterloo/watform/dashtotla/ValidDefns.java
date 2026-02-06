package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.*;
import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidDefns {

    public static void translate(DashModel dashModel, TlaModel tlaModel) {

        // these are separate functions since the presence of the variables themselves are subject
        // to optimization

        List<String> vars = new ArrayList<>();

        if (!dashModel.hasOnlyOneState()) {
            vars.add(CONF);
            List<String> leafStateFQNs = dashModel.leafStateNames();
            tlaModel.addDefn(
                    generateValid(
                            CONF,
                            TlaSubsetUnary(
                                    repeatedUnion(mapBy(leafStateFQNs, x -> TlaAppl(tlaFQN(x)))))));
        }

        if (dashModel.hasConcurrency()) {

            vars.add(SCOPES_USED);
            vars.add(STABLE);

            // scopes used
            List<String> leafStateFQNs = dashModel.leafStateNames();
            tlaModel.addDefn(
                    generateValid(
                            SCOPES_USED,
                            TlaSubsetUnary(
                                    repeatedUnion(mapBy(leafStateFQNs, x -> TlaAppl(tlaFQN(x)))))));

            // stable
            tlaModel.addDefn(generateValid(STABLE, TlaBoolean()));
        }

        vars.add(TRANS_TAKEN);
        List<String> transTakenNames = mapBy(dashModel.allTransNames(), x -> takenTransTlaFQN(x));
        transTakenNames.add((NONE_TRANSITION));
        tlaModel.addDefn(
                generateValid(TRANS_TAKEN, TlaSet(mapBy(transTakenNames, t -> TlaAppl(t)))));

        if (dashModel.hasEvents()) {
            vars.add(EVENTS);
            tlaModel.addDefn(
                    generateValid(
                            EVENTS,
                            TlaSubsetUnary(INTERNAL_EVENTS().UNION(ENVIRONMENTAL_EVENTS()))));
        }

        // valid_unprimed
        tlaModel.addDefn(
                TlaDefn(
                        VALID_UNPRIMED,
                        repeatedAnd(
                                mapBy(
                                        vars,
                                        varName ->
                                                TlaAppl(
                                                        validDefn(varName),
                                                        Arrays.asList(TlaVar(varName)))))));

        // valid_primed
        tlaModel.addDefn(
                TlaDefn(
                        VALID_PRIMED,
                        repeatedAnd(
                                mapBy(
                                        vars,
                                        varName ->
                                                TlaAppl(
                                                        validDefn(varName),
                                                        Arrays.asList(TlaVar(varName).PRIME()))))));
    }

    private static TlaDefn generateValid(String varName, TlaExp exp) {
        /*
        returns  valid_v(_arg) == _arg \in exp
        */
        return TlaDefn(TlaDecl(validDefn(varName), Arrays.asList(ARGUMENT())), ARGUMENT().IN(exp));
    }
}
