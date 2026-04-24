package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaStringLiteral;
import ca.uwaterloo.watform.tlaast.tlanaryops.TlaSet;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import ca.uwaterloo.watform.utils.CustomLoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class ScopeConstraintsA2T extends NextDefnA2T {

    private int cmdNum = 0;

    public String generateCmdName() {
        cmdNum += 1;
        return cmd(cmdNum);
    }

    public TlaSet generateSet(String predicate, int n) {
        List<TlaStringLiteral> elements = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            elements.add(TlaStringLiteral(predicate + i));
        }
        return TlaSet(mapBy(elements, e -> TlaTuple(Arrays.asList(e))));
    }

    public ScopeConstraintsA2T(
            AlloyModel alloyModel, TlaModel tlaModel, boolean verbose, boolean debug) {
        super(alloyModel, tlaModel, verbose, debug);
    }

    public TlaDefn translateCmd(AlloyCmdPara.CommandDecl cmdDecl) {

        tlaModel.addComment(cmdDecl.toString(), verbose);

        List<TlaExp> conditions = new ArrayList<>();

        /*
        [run/check] {block} for [num but] ([exactly] num sig)* [expect 1/0]

        parity is defined as: is_run XOR is_1

        if there is no block, it defaults to {true}


        commandname == (~)(block) /\ [scope_constraints]

        exact -> A = {"a0"..."an"}
        not exact -> A = {"a0"..."an"} \/ ...
        */

        boolean isRun = cmdDecl.cmdType == AlloyCmdPara.CommandDecl.CmdType.RUN;

        var expect = cmdDecl.expect.map(e -> e.value).orElse(0);
        boolean is1 = expect == 1;

        // TODO invokeQname, which is an alternative for the block

        TlaExp block =
                cmdDecl.constrBlock.map(b -> (new AlloyToTlaExprVis().visit(b))).orElse(TlaTrue());

        if (!is1 && isRun || is1 && !isRun) block = TlaNot(block);

        conditions.add(block);

        int defaultScope = cmdDecl.scope.map(s -> s.num.map(n -> n.value).orElse(4)).orElse(4);

        var scopes = new HashMap<String, Integer>();
        var exact = new HashMap<String, Boolean>();

        for (var signame : alloyModel.allSigs()) {
            scopes.put(signame, defaultScope);
            exact.put(signame, false);
        }

        var typeScopes = cmdDecl.scope.map(s -> s.typescopes).orElse(new ArrayList<>());
        for (var typeScope : typeScopes) {

            String key = typeScope.scopableExpr.toString();
            scopes.put(key, typeScope.start.value);
            exact.put(key, typeScope.isExactly);
        }

        List<TlaExp> scopeConstraints = new ArrayList<>();

        for (var s : scopes.keySet()) {
            int n = scopes.get(s);
            boolean e = exact.get(s);
            if (e) scopeConstraints.add(TlaVar(s).EQUALS(generateSet(s, n)));
            else {
                List<TlaSet> possibleSets = new ArrayList<>();
                for (int i = 1; i <= n; i++) possibleSets.add(generateSet(s, i));
                scopeConstraints.add(repeatedOr(mapBy(possibleSets, set -> TlaVar(s).EQUALS(set))));
            }
        }

        conditions.add(repeatedAnd(scopeConstraints));

        // if there is contents in the block, add it as a clause

        // List<TlaExp> scopeSets = new ArrayList<>();
        // if (1 + 1 != 2) // TODO fix this
        // {
        //     scopeSets.add(generateSet("placeholder", 4));
        // } else {
        //     for (int i = 0; i < 4; i++) scopeSets.add(generateSet("placeholder", i));
        // }
        // conditions.add(TlaOrList(mapBy(scopeSets, s -> TlaVar("placeholder").EQUALS(s))));

        return TlaDefn(generateCmdName(), TlaAndList(conditions));
    }

    public void addScopeConstraints() {
        Logger l = CustomLoggerFactory.make("AlloyToTla", true);

        List<AlloyCmdPara> paras = alloyModel.getParas(AlloyCmdPara.class);

        List<AlloyCmdPara.CommandDecl> runCmdDecls = new ArrayList<>();
        paras.forEach(
                p -> {
                    runCmdDecls.addAll(
                            filterBy(
                                    p.cmdDecls,
                                    cd -> cd.cmdType == AlloyCmdPara.CommandDecl.CmdType.RUN));
                });

        List<AlloyCmdPara.CommandDecl> checkCmdDecls = new ArrayList<>();

        for (var p : paras) {
            var checkCmds =
                    filterBy(
                            p.cmdDecls, cd -> cd.cmdType == AlloyCmdPara.CommandDecl.CmdType.CHECK);
            checkCmdDecls.addAll(checkCmds);
        }

        for (var p : paras) {
            for (var cd : p.cmdDecls) {
                tlaModel.addDefn(translateCmd(cd));
            }
        }
    }
}
