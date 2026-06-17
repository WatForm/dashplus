package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaAppl;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaDecl;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaDefn;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaIntLiteral;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaSet;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaStringLiteral;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaTuple;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaVar;
import static ca.uwaterloo.watform.utils.GeneralUtil.filterBy;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlaast.TlaRecord;
import ca.uwaterloo.watform.tlaast.TlaStdLibs;
import ca.uwaterloo.watform.tlaast.tlabinops.TlaDot;
import ca.uwaterloo.watform.tlaast.tlaliterals.TlaStringLiteral;
import ca.uwaterloo.watform.tlaast.tlanaryops.TlaSet;
import ca.uwaterloo.watform.tlaast.tlanaryops.TlaTuple;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StdLibsA2T extends SigVarsA2T {

    public StdLibsA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
        super(alloyModel, verbose, debug);
    }

    protected void addStdLibs(TlaModel tlaModel) {
        tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_FiniteSets));
        tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_Integers));
        tlaModel.addSTL(new TlaStdLibs(TlaStdLibs.LIBRARIES.STL_Sequences));

        testOrdering(tlaModel);
    }

    protected TlaStringLiteral sigAtomString(String signame, int n) {
        return TlaStringLiteral(signame + n);
    }

    protected TlaTuple sigAtom(String signame, int n) {
        return TlaTuple(sigAtomString(signame, n));
    }

    protected TlaSet sigAtoms(String signame, int start, int end) {
        List<TlaTuple> atoms = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            atoms.add(sigAtom(signame, i));
        }
        return TlaSet(atoms);
    }

    protected int maxOrdering(String signame) {
        // TODO this needs to change
        return 4;
    }

    protected void makeNextPrev(List<String> sigrefs, TlaModel tlaModel) {
        List<TlaRecord.KeyValue> nextPairs = new ArrayList<>();
        List<TlaRecord.KeyValue> prevPairs = new ArrayList<>();

        for (var sigref : sigrefs) {
            for (int i = 0; i < maxOrdering(sigref) - 1; i++) {
                nextPairs.add(
                        new TlaRecord.KeyValue(sigAtomString(sigref, i), sigAtom(sigref, i + 1)));
                prevPairs.add(
                        new TlaRecord.KeyValue(sigAtomString(sigref, i + 1), sigAtom(sigref, i)));
            }
        }

        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl("next", Arrays.asList(TlaVar("_S"))),
                        new TlaDot(
                                new TlaRecord(nextPairs), TlaVar("_S").INDEX(TlaIntLiteral(1)))));
        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl("prev", Arrays.asList(TlaVar("_S"))),
                        new TlaDot(
                                new TlaRecord(prevPairs), TlaVar("_S").INDEX(TlaIntLiteral(1)))));
    }

    protected void makeNextsPrevs(List<String> sigrefs, TlaModel tlaModel) {
        List<TlaRecord.KeyValue> nextsPairs = new ArrayList<>();
        List<TlaRecord.KeyValue> prevsPairs = new ArrayList<>();

        for (var sigref : sigrefs) {
            for (int i = 0; i < maxOrdering(sigref); i++) {

                nextsPairs.add(
                        new TlaRecord.KeyValue(
                                sigAtomString(sigref, i),
                                sigAtoms(sigref, i + 1, maxOrdering(sigref))));
                prevsPairs.add(
                        new TlaRecord.KeyValue(
                                sigAtomString(sigref, i), sigAtoms(sigref, 0, i - 1)));
            }
        }

        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl("nexts", Arrays.asList(TlaVar("_S"))),
                        new TlaDot(
                                new TlaRecord(nextsPairs), TlaVar("_S").INDEX(TlaIntLiteral(1)))));
        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl("prevs", Arrays.asList(TlaVar("_S"))),
                        new TlaDot(
                                new TlaRecord(prevsPairs), TlaVar("_S").INDEX(TlaIntLiteral(1)))));
    }

    protected void orderingPredicates(TlaModel tlaModel) {

        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl("lt", Arrays.asList(E1(), E2())),
                        E1().IN(TlaAppl("prevs", Arrays.asList(E2())))));

        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl("gt", Arrays.asList(E1(), E2())),
                        E1().IN(TlaAppl("nexts", Arrays.asList(E2())))));

        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl("lte", Arrays.asList(E1(), E2())),
                        (E1().EQUALS(E2())).OR(E1().IN(TlaAppl("prevs", Arrays.asList(E2()))))));

        tlaModel.addDefn(
                TlaDefn(
                        TlaDecl("gte", Arrays.asList(E1(), E2())),
                        (E1().EQUALS(E2())).OR(E1().IN(TlaAppl("nexts", Arrays.asList(E2()))))));
    }

    protected void orderingModule(TlaModel tlaModel) {
        var orderingParas =
                filterBy(
                        alloyModel.allImportParas(),
                        p -> p.qname.getName().equals("util/ordering"));

        List<String> sigrefs = mapBy(orderingParas, p -> p.sigRefs.getFirst().getName());

        if (orderingParas.size() == 1) {
            var para = orderingParas.get(0);
            String sigref = para.sigRefs.get(0).getName();
            tlaModel.addDefn(TlaDefn("first", sigAtom(sigref, 0)));
            tlaModel.addDefn(TlaDefn("last", sigAtom(sigref, maxOrdering(sigref))));
        }

        makeNextPrev(sigrefs, tlaModel);
        makeNextsPrevs(sigrefs, tlaModel);
        orderingPredicates(tlaModel);
    }

    protected void testOrdering(TlaModel tlaModel) {
        l.info("imports: " + alloyModel.allImportParas());

        for (var importPara : alloyModel.allImportParas()) {
            l.info("qname: " + importPara.qname);
            l.info("sigrefs: " + importPara.sigRefs);
            l.info("as qname: " + importPara.asQname);
        }
    }
}
