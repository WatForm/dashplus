package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.*;
import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaIfThenElse;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransDefns {
    public static void translate(List<String> vars, DashModel dashModel, TlaModel tlaModel) {

        List<String> transFQNs = AuxDashAccessors.getTransitionNames(dashModel);

        if (vars.contains(TRANS_TAKEN)) {

            transFQNs.forEach(
                    transFQN ->
                            tlaModel.addDefn(
                                    // taken_<trans-name> == "taken_<transFQN>"
                                    TlaDefn(
                                            takenTransTlaFQN(transFQN),
                                            TlaStringLiteral(transFQN))));

            tlaModel.addDefn(
                    // _none_transition == "[none]"
                    TlaDefn(NONE_TRANSITION, NONE_TRANSITION_LITERAL()));
        }

        if (vars.contains(STABLE) && vars.contains(SCOPES_USED)) {

            tlaModel.addComment("parameterized formulae to check if transitions are enabled");
            // _enabled_<transFQN> == <body>
            transFQNs.forEach(x -> TransEnabledDefn(x, vars, dashModel, tlaModel));

            tlaModel.addComment("negation of disjunction of enabled-formulae");
            NextIsStableDefn(vars, dashModel, tlaModel);
        }

        System.out.println("translated enabled definitions");

        transFQNs.forEach(
                transFQN -> {
                    // pre, post, and body

                    tlaModel.addComment("Translation of transition " + transFQN);
                    PreTransDefn(transFQN, vars, dashModel, tlaModel);
                    PostTransDefn(transFQN, vars, dashModel, tlaModel);
                    TransDefn(transFQN, tlaModel);
                    System.out.println("translated transition " + transFQN);
                });
    }

    public static List<TlaVar> enabledParams(List<String> vars) {

        // list of parameters for enabledTransition formulae
        List<String> parameters = filterBy(Arrays.asList(SCOPES_USED, EVENTS), vars::contains);
        return mapBy(parameters, v -> TlaVar(paramVar(v)));
    }

    public static List<TlaVar> enabledArgs(List<String> vars) {

        // list of arguments for enabledTransition formulae, using the current variables
        List<String> parameters = filterBy(Arrays.asList(SCOPES_USED, EVENTS), vars::contains);
        return mapBy(parameters, v -> TlaVar(v));
    }

    public static void NextIsStableDefn(List<String> vars, DashModel dashModel, TlaModel tlaModel) {

        List<TlaExp> notEnabledTrans =
                mapBy(
                        AuxDashAccessors.getTransitionNames(dashModel),
                        tFQN -> TlaNot(TlaAppl(enabledTransTlaFQN(tFQN), enabledParams(vars))));

        tlaModel.addDefn(
                // _next_is_stable(args) = /\ (~ enabled_after_step_ti(args) ...)
                TlaDefn(
                        TlaDecl(NEXT_IS_STABLE, enabledParams(vars)),
                        repeatedAnd(notEnabledTrans)));
    }

    public static void PreTransDefn(
            String transFQN, List<String> vars, DashModel dashModel, TlaModel tlaModel) {

        TlaAppl fromState = TlaAppl(tlaFQN(dashModel.fromR(transFQN).name));

        List<TlaExp> exps = new ArrayList<>();

        if (vars.contains(CONF))
            exps.add(
                    // _conf \intersection <fromState> \= {}
                    CONF().INTERSECTION(fromState).NOT_EQUALS(NULL_SET()));

        if (vars.contains(SCOPES_USED)) {
            // has a scope orthogonal to the scopes used
            List<TlaAppl> nonOrthogonalScopes =
                    mapBy(
                            dashModel.nonOrthogonalScopesOf(transFQN),
                            dashRef -> TlaAppl(tlaFQN(dashRef.name)));

            nonOrthogonalScopes.forEach(
                    scope ->
                            exps.add(
                                    // (<non-orthogonal-scope-i> \notin _scopes_used)
                                    scope.NOTIN(SCOPES_USED())));
        }

        if (vars.contains(EVENTS)) {
            if (dashModel.onR(transFQN) != null) {
                TlaAppl onEvent = TlaAppl(tlaFQN(dashModel.onR(transFQN).name));
                exps.add(
                        // <on-event> \in _events
                        onEvent.IN(EVENTS()));
            }
        }

        tlaModel.addDefn(TlaDefn(preTransTlaFQN(transFQN), repeatedAnd(exps)));
    }

    public static void PostTransDefn(
            String transFQN, List<String> vars, DashModel dashModel, TlaModel tlaModel) {

        List<TlaExp> exps = new ArrayList<>();

        if (vars.contains(TRANS_TAKEN))
            exps.add(
                    // _trans_taken' = <taken-trans-formula>
                    TRANS_TAKEN().PRIME().EQUALS(TlaAppl(takenTransTlaFQN(transFQN))));

        if (vars.contains(CONF)) {
            List<TlaAppl> entered =
                    mapBy(dashModel.entered(transFQN), s -> TlaAppl(tlaFQN(s.name)));
            List<TlaAppl> exited = mapBy(dashModel.exited(transFQN), s -> TlaAppl(tlaFQN(s.name)));
            exps.add(
                    // _conf' = conf \ (union <exited>...) union (union <entered>...)
                    CONF().PRIME()
                            .EQUALS(
                                    CONF().DIFF(repeatedUnion(exited))
                                            .UNION(repeatedUnion(entered))));
        }

        if (vars.contains(SCOPES_USED)) {
            TlaExp nextStable = NULL_SET();
            TlaExp scopesUsed =
                    repeatedUnion(
                            mapBy(dashModel.scopesUsed(transFQN), d -> TlaAppl(tlaFQN(d.name))));
            TlaExp notNextStableAndStable = scopesUsed;
            TlaExp notNextStableAndNotStable = SCOPES_USED().UNION(scopesUsed);
            TlaExp body =
                    new TlaIfThenElse(
                            TlaDecl(NEXT_IS_STABLE, enabledArgs(vars)),
                            nextStable,
                            new TlaIfThenElse(
                                    STABLE(), notNextStableAndStable, notNextStableAndNotStable));
            exps.add(
                    // if next_is_stable,
                    // then _scopes_used'  = none
                    // else
                    //          if stable
                    //          then _scopes_used' = <scopes-used>
                    //          else scopes_used' = scopes_used \\union <scopes-used>
                    SCOPES_USED().PRIME().EQUALS(body));
        }

        if (vars.contains(STABLE))
            exps.add(
                    // _stable' = next_is_stable(args)
                    STABLE().PRIME().EQUALS(TlaDecl(NEXT_IS_STABLE, enabledArgs(vars))));

        if (vars.contains(EVENTS))
            exps.add(
                    // todo fix this
                    TlaUnchanged(EVENTS()));

        tlaModel.addDefn(TlaDefn(postTransTlaFQN(transFQN), repeatedAnd(exps)));

        // TODO add stuff
    }

    public static void TransEnabledDefn(
            String transFQN, List<String> vars, DashModel dashModel, TlaModel tlaModel) {
        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl(enabledTransTlaFQN(transFQN), enabledParams(vars)),
                        repeatedAnd(Arrays.asList())));
    }

    public static void TransDefn(String transFQN, TlaModel tlaModel) {

        TlaAppl preTrans = TlaAppl(preTransTlaFQN(transFQN));
        TlaAppl postTrans = TlaAppl(postTransTlaFQN(transFQN));
        tlaModel.addDefn(
                // body == pre /\ post
                TlaDefn(tlaFQN(transFQN), preTrans.AND(postTrans)));
    }
}
