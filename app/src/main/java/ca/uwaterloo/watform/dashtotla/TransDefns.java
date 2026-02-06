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
    public static void translate(
            DashModel dashModel, TlaModel tlaModel, boolean verbose, boolean debug) {

        List<String> transFQNs = dashModel.allTransNames();

        transFQNs.forEach(
                transFQN ->
                        tlaModel.addDefn(
                                // taken_<trans-name> == "<transFQN>"
                                TlaDefn(takenTransTlaFQN(transFQN), TlaStringLiteral(transFQN))));

        tlaModel.addDefn(
                // _none_transition == "[none]"
                TlaDefn(NONE_TRANSITION, NONE_TRANSITION_LITERAL()));

        if (!dashModel.hasOnlyOneState()) {

            tlaModel.addComment(
                    "parameterized formulae to check if transitions are enabled", verbose);
            // _enabled_<transFQN> == <body>
            transFQNs.forEach(x -> TransEnabledDefn(x, dashModel, tlaModel));

            tlaModel.addComment("negation of disjunction of enabled-formulae", verbose);
            NextIsStableDefn(dashModel, tlaModel);
        }

        if (debug) System.out.println("translated enabled definitions");

        transFQNs.forEach(
                transFQN -> {
                    // pre, post, and body

                    tlaModel.addComment("Translation of transition " + transFQN, verbose);
                    PreTransDefn(transFQN, dashModel, tlaModel);
                    PostTransDefn(transFQN, dashModel, tlaModel);
                    TransDefn(transFQN, tlaModel);
                    if (debug) System.out.println("translated transition " + transFQN);
                });
    }

    private static List<String> varNames(DashModel dashModel) {
        // names of variables that are parameterized
        List<String> names = new ArrayList<>();
        if (dashModel.hasConcurrency()) names.add(SCOPES_USED);
        if (dashModel.hasEvents()) names.add(EVENTS);
        return names;
    }

    public static List<TlaVar> enabledParams(DashModel dashModel) {

        // list of parameters for enabledTransition formulae

        return mapBy(varNames(dashModel), v -> TlaVar(paramVar(v)));
    }

    public static List<TlaVar> enabledArgs(DashModel dashModel) {

        // list of arguments for enabledTransition formulae, using the current variables

        return mapBy(varNames(dashModel), v -> TlaVar(v));
    }

    public static void NextIsStableDefn(DashModel dashModel, TlaModel tlaModel) {

        List<TlaExp> notEnabledTrans =
                mapBy(
                        dashModel.allTransNames(),
                        tFQN ->
                                TlaNot(
                                        TlaAppl(
                                                enabledTransTlaFQN(tFQN),
                                                enabledParams(dashModel))));

        tlaModel.addDefn(
                // _next_is_stable(args) = /\ (~ enabled_after_step_ti(args) ...)
                TlaDefn(
                        TlaDecl(NEXT_IS_STABLE, enabledParams(dashModel)),
                        repeatedAnd(notEnabledTrans)));
    }

    public static void PreTransDefn(String transFQN, DashModel dashModel, TlaModel tlaModel) {

        TlaAppl fromState = TlaAppl(tlaFQN(dashModel.fromR(transFQN).name));

        List<TlaExp> exps = new ArrayList<>();

        if (!dashModel.hasOnlyOneState())
            exps.add(
                    // _conf \intersection <fromState> \= {}
                    CONF().INTERSECTION(fromState).NOT_EQUALS(NULL_SET()));

        if (dashModel.hasConcurrency()) {
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

        if (dashModel.hasEvents()) {
            if (dashModel.onR(transFQN) != null) {
                TlaAppl onEvent = TlaAppl(tlaFQN(dashModel.onR(transFQN).name));

                TlaExp stableCase = onEvent.IN(EVENTS().INTERSECTION(ENVIRONMENTAL_EVENTS()));
                TlaExp unstableCase = onEvent.IN(EVENTS());

                // IF stable
                // THEN <on-event> \in  _events \intersect _all_env_events
                // ELSE <on-event> \in _events

                if (!dashModel.hasOnlyOneState())
                    exps.add(new TlaIfThenElse(STABLE(), stableCase, unstableCase));
                else exps.add(stableCase);
            }
        }

        tlaModel.addDefn(TlaDefn(preTransTlaFQN(transFQN), repeatedAnd(exps)));
    }

    public static void PostTransDefn(String transFQN, DashModel dashModel, TlaModel tlaModel) {

        List<TlaExp> exps = new ArrayList<>();

        exps.add(
                // _trans_taken' = <taken-trans-formula>
                TRANS_TAKEN().PRIME().EQUALS(TlaAppl(takenTransTlaFQN(transFQN))));

        if (!dashModel.hasOnlyOneState()) {
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

        if (!dashModel.hasOnlyOneState()) {

            List<TlaExp> nextStableExps = new ArrayList<>();
            nextStableExps.add(STABLE().PRIME().EQUALS(TlaTrue()));
            if (dashModel.hasConcurrency())
                nextStableExps.add(SCOPES_USED().PRIME().EQUALS(NULL_SET()));

            if (dashModel.hasEvents()) {
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
            if (dashModel.hasConcurrency()) {
                TlaExp scopesUsed =
                        repeatedUnion(
                                mapBy(
                                        dashModel.scopesUsed(transFQN),
                                        dr -> TlaAppl(tlaFQN(dr.name))));

                stableExps.add(SCOPES_USED().PRIME().EQUALS(scopesUsed));

                unstableExps.add(SCOPES_USED().PRIME().EQUALS(SCOPES_USED().UNION(scopesUsed)));
            }
            if (dashModel.hasEvents()) {
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
                            TlaDecl(NEXT_IS_STABLE, enabledArgs(dashModel)),
                            repeatedAnd(nextStableExps),
                            repeatedAnd(nextUnstableExps)));

        } else {

            // every snapshot is stable
            if (dashModel.hasConcurrency()) exps.add(TlaUnchanged(SCOPES_USED()));

            if (dashModel.hasEvents())
                exps.add(EVENTS().PRIME().INTERSECTION(INTERNAL_EVENTS()).EQUALS(sentEvents));
        }

        tlaModel.addDefn(TlaDefn(postTransTlaFQN(transFQN), repeatedAnd(exps)));

        // TODO add stuff
    }

    public static void TransEnabledDefn(String transFQN, DashModel dashModel, TlaModel tlaModel) {

        List<TlaExp> exps = new ArrayList<>();

        if (!dashModel.hasOnlyOneState()) {
            TlaAppl sourceState = TlaAppl(tlaFQN(dashModel.fromR(transFQN).name));
            exps.add(sourceState.INTERSECTION(CONF().PRIME()).NOT_EQUALS(NULL_SET()));
        }

        List<TlaExp> stableExps = new ArrayList<>();
        List<TlaExp> unstableExps = new ArrayList<>();

        if (dashModel.hasConcurrency()) {
            List<TlaAppl> nonOrthogonalScopes =
                    mapBy(
                            dashModel.nonOrthogonalScopesOf(transFQN),
                            dashRef -> TlaAppl(tlaFQN(dashRef.name)));

            nonOrthogonalScopes.forEach(
                    s ->
                            stableExps.add(
                                    s.INTERSECTION(TlaVar(paramVar(SCOPES_USED)))
                                            .EQUALS(NULL_SET())));

            nonOrthogonalScopes.forEach(
                    s ->
                            unstableExps.add(
                                    s.INTERSECTION(
                                                    SCOPES_USED()
                                                            .UNION(TlaVar(paramVar(SCOPES_USED))))
                                            .EQUALS(NULL_SET())));
        }

        if (dashModel.hasEvents() && dashModel.onR(transFQN) != null) {

            TlaExp sentEvents = TlaSet(TlaAppl((tlaFQN(dashModel.onR(transFQN).name))));

            stableExps.add(
                    sentEvents
                            .INTERSECTION(
                                    EVENTS().INTERSECTION(ENVIRONMENTAL_EVENTS())
                                            .UNION(TlaVar(paramVar(EVENTS))))
                            .NOT_EQUALS(NULL_SET()));

            unstableExps.add(
                    sentEvents
                            .INTERSECTION(EVENTS().UNION(TlaVar(paramVar(EVENTS))))
                            .NOT_EQUALS(NULL_SET()));
        }

        if (!dashModel.hasOnlyOneState())
            exps.add(
                    new TlaIfThenElse(
                            STABLE(), repeatedAnd(stableExps), repeatedAnd(unstableExps)));
        else exps.addAll(stableExps);

        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl(enabledTransTlaFQN(transFQN), enabledParams(dashModel)),
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
