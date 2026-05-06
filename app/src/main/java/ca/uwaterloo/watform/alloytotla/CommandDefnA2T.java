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

public class CommandDefnA2T extends NextDefnA2T {

    public CommandDefnA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
        super(alloyModel, verbose, debug);
    }

    public TlaDefn cmdConstraints(TlaModel tlaModel, AlloyCmdPara.CommandDecl cmdDecl) {

        tlaModel.addComment(cmdDecl.toString(), verbose);

        List<TlaExp> conditions = new ArrayList<>();

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

        // TODO invokeQname, which is an alternative for the block

        TlaExp block =
                cmdDecl.constrBlock.map(b -> (new AlloyToTlaExprVis().visit(b))).orElse(TlaTrue());

        // if (!is1 && isRun || is1 && !isRun) block = TlaNot(block);

        if (isRun) block = TlaNot(block);
        conditions.add(block);

        return TlaDefn(COMMAND, TlaAndList(conditions));
    }

    public void addScopeConstraints(TlaModel tlaModel, AlloyCmdPara.CommandDecl cmdDecl) {

        tlaModel.addDefn(cmdConstraints(tlaModel, cmdDecl));
        tlaModel.addInvariant(TlaAppl(COMMAND));
    }
}
