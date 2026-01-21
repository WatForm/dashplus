package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.dashtotla.DashToTlaHelpers.*;
import static ca.uwaterloo.watform.dashtotla.DashToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.*;

public class TransDefns {
    public static void translate(List<String> vars, DashModel dashModel, TlaModel tlaModel) {

        List<String> transFQNs = AuxDashAccessors.getTransitionNames(dashModel);

        if (vars.contains(TRANS_TAKEN)) {

            transFQNs.forEach(
                    transFQN ->
                            tlaModel.addDefn(
                                    // taken_<trans-name> == "<transFQN>"
                                    TlaDefn(
                                            takenTransTlaFQN(transFQN),
                                            TlaStringLiteral(transFQN))));

            tlaModel.addDefn(
                    // _none_transition == "[none]"
                    TlaDefn(NONE_TRANSITION, NONE_TRANSITION_LITERAL()));
        }

        if (vars.contains(STABLE)) {

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
                                    scope.INTERSECTION(SCOPES_USED()).EQUALS(NULL_SET())));
        }

        if (vars.contains(EVENTS)) {
            if (dashModel.onR(transFQN) != null) {
                TlaAppl onEvent = TlaAppl(tlaFQN(dashModel.onR(transFQN).name));

                TlaExp stableCase = onEvent.IN(EVENTS().INTERSECTION(ENVIRONMENTAL_EVENTS()));
                TlaExp unstableCase = onEvent.IN(EVENTS());

                // IF stable
                // THEN <on-event> \in  _events \intersect _all_env_events
                // ELSE <on-event> \in _events

                if (vars.contains(STABLE))
                    exps.add(new TlaIfThenElse(STABLE(), stableCase, unstableCase));
                else exps.add(stableCase);
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

        TlaExp sentEvents = NULL_SET();
        if (dashModel.onR(transFQN) != null)
            sentEvents = TlaSet(TlaAppl((tlaFQN(dashModel.onR(transFQN).name))));

        if (vars.contains(STABLE)) {

            List<TlaExp> nextStableExps = new ArrayList<>();
            nextStableExps.add(STABLE().PRIME().EQUALS(TlaTrue()));
            if (vars.contains(SCOPES_USED))
                nextStableExps.add(SCOPES_USED().PRIME().EQUALS(NULL_SET()));

            if (vars.contains(EVENTS)) {
                TlaExp stableCase =
                        EVENTS().PRIME().INTERSECTION(INTERNAL_EVENTS()).EQUALS(sentEvents);
                TlaExp unstableCase =
                        EVENTS().PRIME()
                                .INTERSECTION(INTERNAL_EVENTS())
                                .EQUALS(sentEvents.UNION(EVENTS().INTERSECTION(INTERNAL_EVENTS())));

                nextStableExps.add(new TlaIfThenElse(STABLE(), stableCase, unstableCase));
            }

            List<TlaExp> nextUnstableExps = new ArrayList<>();
            nextUnstableExps.add(STABLE().PRIME().EQUALS(TlaFalse()));

            List<TlaExp> stableExps = new ArrayList<>();
            List<TlaExp> unstableExps = new ArrayList<>();
            if (vars.contains(SCOPES_USED)) {
                TlaExp scopesUsed =
                        repeatedUnion(
                                mapBy(
                                        dashModel.scopesUsed(transFQN),
                                        dr -> TlaAppl(tlaFQN(dr.name))));

                stableExps.add(TlaUnchanged(SCOPES_USED()));

                unstableExps.add(SCOPES_USED().PRIME().EQUALS(SCOPES_USED().UNION(scopesUsed)));
            }
            if (vars.contains(EVENTS)) {
                stableExps.add(EVENTS().PRIME().INTERSECTION(INTERNAL_EVENTS()).EQUALS(sentEvents));

                stableExps.add(
                        EVENTS().PRIME()
                                .INTERSECTION(ENVIRONMENTAL_EVENTS())
                                .EQUALS(EVENTS().INTERSECTION(ENVIRONMENTAL_EVENTS())));

                unstableExps.add(EVENTS().PRIME().EQUALS(EVENTS().UNION(sentEvents)));
            }

            nextUnstableExps.add(
                    new TlaIfThenElse(
                            STABLE(), repeatedAnd(stableExps), repeatedAnd(unstableExps)));

            exps.add(
                    new TlaIfThenElse(
                            TlaDecl(NEXT_IS_STABLE, enabledArgs(vars)),
                            repeatedAnd(nextStableExps),
                            repeatedAnd(nextUnstableExps)));

        } else {

            // every snapshot is stable
            if (vars.contains(SCOPES_USED)) exps.add(TlaUnchanged(SCOPES_USED()));

            if (vars.contains(EVENTS))
                exps.add(EVENTS().PRIME().INTERSECTION(INTERNAL_EVENTS()).EQUALS(sentEvents));
        }

        tlaModel.addDefn(TlaDefn(postTransTlaFQN(transFQN), repeatedAnd(exps)));

        // TODO add stuff
    }

    public static void TransEnabledDefn(
            String transFQN, List<String> vars, DashModel dashModel, TlaModel tlaModel) {

        List<TlaExp> exps = new ArrayList<>();

        if (vars.contains(CONF)) {
            TlaAppl sourceState = TlaAppl(tlaFQN(dashModel.fromR(transFQN).name));
            exps.add(sourceState.INTERSECTION(CONF().PRIME()).NOT_EQUALS(NULL_SET()));
        }

        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl(enabledTransTlaFQN(transFQN), enabledParams(vars)),
                        repeatedAnd(exps)));
    }

    public static void TransDefn(String transFQN, TlaModel tlaModel) {

        TlaAppl preTrans = TlaAppl(preTransTlaFQN(transFQN));
        TlaAppl postTrans = TlaAppl(postTransTlaFQN(transFQN));
        tlaModel.addDefn(
                // body == pre /\ post
                TlaDefn(tlaFQN(transFQN), preTrans.AND(postTrans)));
    }
}
