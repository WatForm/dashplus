package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.*;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class CommandA2T extends BoilerplateA2T {

    public CommandA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
        super(alloyModel, verbose, debug);
    }

    protected TlaExp cmdBody(AlloyCmdPara.CommandDecl cmdDecl) {
        TlaExp core = cmdDecl.constrBlock.map(b -> translateSnippet(b)).orElse(TlaTrue());
        if (cmdDecl.cmdType == AlloyCmdPara.CommandDecl.CmdType.CHECK) core = TlaNot(core);
        core = core.AND(augmentedTrue());
        return core;
    }

    private TlaExp augmentedTrue() {

        /*
        TRUE cannot be used in an invariant
        thus V=V is used as a stand-in for True
        */

        String dummy = alloyModel.allSigs().get(0);
        return TlaVar(dummy).EQUALS(TlaVar(dummy));
    }

    public void addCommand(TlaModel tlaModel, AlloyCmdPara.CommandDecl cmdDecl) {

        tlaModel.addComment("command: " + cmdDecl.toString(), verbose);
        tlaModel.addDefn(cmdConstraints(tlaModel, cmdDecl));
        tlaModel.addDefn(scopeConstraints(tlaModel, cmdDecl));
        tlaModel.addInvariant(TlaAppl(COMMAND));
    }

    public TlaDefn scopeConstraints(TlaModel tlaModel, AlloyCmdPara.CommandDecl cmdDecl) {
        List<TlaExp> clauses = new ArrayList<>();
        var scopeLimits = alloyModel.getScopeLimits(cmdDecl);

        l.info("computed scopes:" + alloyModel.getScopeLimits(cmdDecl).toString());

        for (var s : alloyModel.topLevelSigs()) {

            var scope = scopeLimits.getTopLevelScope(s);
            int n = scope.map(sc -> sc.max()).orElse(DEFAULT_SCOPE);
            boolean exact = scope.map(sc -> sc.isExact()).orElse(false);

            if (exact) clauses.add(TlaVar(s).EQUALS(sigAtoms(s, 0, n - 1)));
            else {
                List<TlaExp> subClauses = new ArrayList<>();
                for (int i = 0; i < n; i++) subClauses.add(TlaVar(s).EQUALS(sigAtoms(s, 0, i)));
                clauses.add(repeatedOr(subClauses));
            }
        }
        return TlaDefn(SCOPE, repeatedAnd(clauses));
    }

    public TlaDefn cmdConstraints(TlaModel tlaModel, AlloyCmdPara.CommandDecl cmdDecl) {

        /*
        [run/check] {block} for [num but] ([exactly] num sig)* [expect 1/0]

        parity is defined as: is_run XOR is_1

        if there is no block, it defaults to {true}


        commandname == (~)(block) /\
        commandname_scope == [scope_constraints]

        exact -> A = {"a0"..."an"}
        not exact -> A = {"a0"..."an"} \/ ...
        */

        boolean isRun = cmdDecl.cmdType == AlloyCmdPara.CommandDecl.CmdType.RUN;

        // var expect = cmdDecl.expect.map(e -> e.value).orElse(0);
        // boolean is1 = expect == 1;

        // if (!is1 && isRun || is1 && !isRun) block = TlaNot(block);

        // TODO invokeQname, which is an alternative for the block

        TlaExp block = cmdDecl.constrBlock.map(b -> (translateSnippet(b))).orElse(TlaTrue());

        block = block.AND(augmentedTrue());
        if (isRun) block = TlaNot(block);

        return TlaDefn(COMMAND, block);
    }
}
