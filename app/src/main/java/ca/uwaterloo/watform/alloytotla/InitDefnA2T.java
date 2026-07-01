package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.tlanaryops.TlaSet;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InitDefnA2T extends FieldsA2T {

    public InitDefnA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
        super(alloyModel, verbose, debug);
    }

    protected List<TlaExp> membershipConstraintsTopLevelSigs(AlloyCmdPara.CommandDecl cmdDecl) {
        int DEFAULT_SCOPE = 3;
        int defaultScope =
                cmdDecl.scope
                        .map(s -> s.num.map(n -> n.value).orElse(DEFAULT_SCOPE))
                        .orElse(DEFAULT_SCOPE);

        var scopes = new HashMap<String, Integer>();
        var exact = new HashMap<String, Boolean>();

        for (var signame : alloyModel.topLevelSigs()) {
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
            if (e) scopeConstraints.add(TlaVar(s).EQUALS(sigAtoms(s, 0, n - 1)));
            else {
                List<TlaSet> possibleSets = new ArrayList<>();
                for (int i = 0; i <= n; i++) possibleSets.add(sigAtoms(s, 0, i - 1));
                scopeConstraints.add(repeatedOr(mapBy(possibleSets, set -> TlaVar(s).EQUALS(set))));
            }
        }

        return scopeConstraints;
    }

    // protected TlaExp cmdBody(AlloyCmdPara.CommandDecl cmdDecl) {
    //     TlaExp core = cmdDecl.constrBlock.map(b -> translateSnippet(b)).orElse(TlaTrue());
    //     if (cmdDecl.cmdType == AlloyCmdPara.CommandDecl.CmdType.CHECK) core = TlaNot(core);
    //     return core;
    // }

    protected void addInitDefn(TlaModel tlaModel, AlloyCmdPara.CommandDecl cmdDecl) {
        List<TlaExp> exps = membershipConstraintsTopLevelSigs(cmdDecl);

        exps.add(TlaAppl(SIG_SETS_UNPRIMED));
        exps.add(TlaAppl(FIELD_TYPES));
        exps.add(TlaAppl(ALL_SIG_CONSTRAINTS));
        exps.add(TlaAppl(ALL_FACTS));
        // exps.add(cmdBody(cmdDecl));

        tlaModel.addDefn(TlaDefn(INIT, repeatedAnd(exps)));
    }
}
