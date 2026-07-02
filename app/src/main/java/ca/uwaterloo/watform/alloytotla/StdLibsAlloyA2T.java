package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.filterBy;

import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StdLibsAlloyA2T extends SigVarsA2T {

    public StdLibsAlloyA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
        super(alloyModel, verbose, debug);
    }

    public void addStdLibsAlloy(TlaModel tlaModel, AlloyCmdPara.CommandDecl cmdDecl) {

        tlaModel.addComment("macros for special alloy libraries", verbose);

        orderingModule(tlaModel, cmdDecl);

        log("completed all special alloy library translations");

        l.info(dump());
    }

    protected void orderingModule(TlaModel tlaModel, AlloyCmdPara.CommandDecl cmdDecl) {

        var scopeLimits = alloyModel.getScopeLimits(cmdDecl);
        var orderingParas =
                filterBy(
                        alloyModel.allImportParas(),
                        p -> p.qname.getName().equals(ORDERING_MODULE_ALLOY));

        log("ordering module used " + orderingParas.size() + " times");

        for (var p : orderingParas) {
            log(
                    "ordering module used on: "
                            + p.sigRefs
                            + p.asQname.map(qn -> " with alias " + qn.getName()).orElse(""));

            String prefix = p.asQname.map(qn -> qn.getName()).orElse("") + SPECIAL;
            String sig = p.sigRefs.get(0).getName();
            log("implmenting ordering module for sig " + sig);
            scopeLimits
                    .getTopLevelScope(sig)
                    .ifPresent(
                            sc -> {
                                int n = sc.max();
                                // exactness is assumed here
                                orderingMacros(tlaModel, prefix, sig, n);
                            });
        }
    }

    private void orderingMacros(TlaModel tlaModel, String prefix, String sig, int n) {
        tlaModel.addDefn(TlaDefn(prefix + "first", sigAtoms(sig, 0, 0)));
        tlaModel.addDefn(TlaDefn(prefix + "last", sigAtoms(sig, n - 1, n - 1)));
        tlaModel.addDefn(TlaDefn(prefix + "prev", _prev(sig, n)));
        tlaModel.addDefn(TlaDefn(prefix + "next", _next(sig, n)));
        tlaModel.addDefn(TlaDefn(prefix + "nexts", _nexts(sig, n)));
        tlaModel.addDefn(TlaDefn(prefix + "prevs", _prevs(sig, n)));
        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl(prefix + "lt", Arrays.asList(E1(), E2())),
                        E1().IN(_DOT(E2(), TlaAppl(prefix + "prevs")))));

        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl(prefix + "gt", Arrays.asList(E1(), E2())),
                        E1().IN(_DOT(E2(), TlaAppl(prefix + "nexts")))));

        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl(prefix + "lte", Arrays.asList(E1(), E2())),
                        (E1().EQUALS(E2())).OR(E1().IN(_DOT(E2(), TlaAppl(prefix + "prevs"))))));

        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl(prefix + "gte", Arrays.asList(E1(), E2())),
                        (E1().EQUALS(E2())).OR(E1().IN(_DOT(E2(), TlaAppl(prefix + "nexts"))))));
        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl(prefix + "smaller", Arrays.asList(E1(), E2())),
                        TlaIfThenElse(
                                E1().EQUALS(E2()),
                                TlaNullSet(),
                                TlaIfThenElse(
                                        TlaAppl(prefix + "lt", Arrays.asList(E1(), E2())),
                                        E1(),
                                        E2()))));

        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl(prefix + "larger", Arrays.asList(E1(), E2())),
                        TlaIfThenElse(
                                E1().EQUALS(E2()),
                                TlaNullSet(),
                                TlaIfThenElse(
                                        TlaAppl(prefix + "gt", Arrays.asList(E1(), E2())),
                                        E1(),
                                        E2()))));

        // min(S) = {x : \A y \in S: lte(x,y)}
        var min =
                TlaSetFilter(
                        TlaQuantOpHead(X(), S()),
                        TlaForAll(
                                TlaQuantOpHead(Y(), S()),
                                TlaAppl(prefix + "lte", Arrays.asList(X(), Y()))));
        // max(S) = {x : \A y \in S: gte(x,y)}
        var max =
                TlaSetFilter(
                        TlaQuantOpHead(X(), S()),
                        TlaForAll(
                                TlaQuantOpHead(Y(), S()),
                                TlaAppl(prefix + "gte", Arrays.asList(X(), Y()))));
        tlaModel.addDefn(TlaDefn(TlaDecl(prefix + "min", Arrays.asList(S())), min));
        tlaModel.addDefn(TlaDefn(TlaDecl(prefix + "max", Arrays.asList(S())), max));
    }

    private TlaExp _next(String sig, int n) {
        List<TlaExp> elements = new ArrayList<>();
        for (int i = 0; i < n - 1; i++)
            elements.add(TlaTuple(Arrays.asList(sigAtomString(sig, i), sigAtomString(sig, i + 1))));
        return TlaSet(elements);
    }

    private TlaExp _prev(String sig, int n) {
        List<TlaExp> elements = new ArrayList<>();
        for (int i = 0; i < n - 1; i++)
            elements.add(TlaTuple(Arrays.asList(sigAtomString(sig, i + 1), sigAtomString(sig, i))));
        return TlaSet(elements);
    }

    private TlaExp _nexts(String sig, int n) {
        List<TlaExp> elements = new ArrayList<>();
        for (int i = 0; i < n - 1; i++)
            for (int j = i + 1; j < n; j++)
                elements.add(TlaTuple(Arrays.asList(sigAtomString(sig, i), sigAtomString(sig, j))));
        return TlaSet(elements);
    }

    private TlaExp _prevs(String sig, int n) {
        List<TlaExp> elements = new ArrayList<>();
        for (int i = 1; i < n; i++)
            for (int j = 0; j < i; j++)
                elements.add(TlaTuple(Arrays.asList(sigAtomString(sig, i), sigAtomString(sig, j))));
        return TlaSet(elements);
    }
}
