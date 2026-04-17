package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaStringLiteral;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import ca.uwaterloo.watform.utils.CustomLoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class ScopeConstraintsA2T extends NextDefnA2T {

    private int runCmdNum = 0;
    private int checkCmdNum = 0;

    public String generateRunCmdName() {
        runCmdNum += 1;
        return runCmd(runCmdNum);
    }

    public String generateCheckCmdName() {
        checkCmdNum += 1;
        return checkCmd(checkCmdNum);
    }

    public TlaExp generateSet(String predicate, int n) {
        List<TlaStringLiteral> elements = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            elements.add(TlaStringLiteral(predicate + n));
        }
        return TlaSet(mapBy(elements, e -> TlaTuple(Arrays.asList(e))));
    }

    public ScopeConstraintsA2T(
            AlloyModel alloyModel, TlaModel tlaModel, boolean verbose, boolean debug) {
        super(alloyModel, tlaModel, verbose, debug);
    }

    public TlaDefn translateRunCmd(AlloyCmdPara.CommandDecl cmdDecl) {

        tlaModel.addComment(cmdDecl.toString(), verbose);

        List<TlaExp> conditions = new ArrayList<>();

        // if there is contents in the block, add it as a clause
        cmdDecl.constrBlock.ifPresent(b -> conditions.add(new AlloyToTlaExprVis().visit(b)));

        List<TlaExp> scopeSets = new ArrayList<>();
        if (1 + 1 != 2) // TODO fix this
        {
            scopeSets.add(generateSet("placeholder", 4));
        } else {
            for (int i = 0; i < 4; i++) scopeSets.add(generateSet("placeholder", i));
        }
        conditions.add(TlaOrList(mapBy(scopeSets, s -> TlaVar("placeholder").EQUALS(s))));

        return TlaDefn(generateRunCmdName(), TlaAndList(conditions));
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
        paras.forEach(
                p -> {
                    checkCmdDecls.addAll(
                            filterBy(
                                    p.cmdDecls,
                                    cd -> cd.cmdType == AlloyCmdPara.CommandDecl.CmdType.CHECK));
                });

        runCmdDecls.forEach(cd -> tlaModel.addDefn(translateRunCmd(cd)));
    }
}
