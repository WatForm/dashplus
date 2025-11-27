package ca.uwaterloo.watform.predabstraction;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyEqualsExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBracketExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyFactPara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloyinterface.Solution;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashast.DashStrings;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CEValidation {

    public static void addSigs(AlloyModel alloyModel, Solution solution) {
        // Get the next relation on the Snapshots
        Set<List<String>> nextRelSnapSet =
                solution.get(DashStrings.snapshotName + DashStrings.SLASH + "Ord.Next");
        Map<String, String> nextRelSnapMap = new HashMap<>();
        for (List<String> pair : nextRelSnapSet) {
            nextRelSnapMap.put(pair.getFirst(), pair.getLast());
        }

        // Get the first Snapshot
        Set<List<String>> firstSnapSet =
                solution.get(DashStrings.snapshotName + DashStrings.SLASH + "Ord.First");
        String firstSnap = Iterables.getOnlyElement(firstSnapSet).getFirst();

        // Rename snapshots; DshSnapshot$0 may not be the first snapshot and
        // it's confusing
        Map<String, String> origToRenamedSnap = new HashMap<>();
        origToRenamedSnap.put(firstSnap, DashStrings.snapshotName + String.valueOf(0));
        List<String> renamedSnaps = new ArrayList<>(); // ordered by the next relation
        renamedSnaps.add(DashStrings.snapshotName + String.valueOf(0));

        int count = 1;
        String currSnap = firstSnap;

        while (nextRelSnapMap.containsKey(currSnap)) {
            String nextSnap = nextRelSnapMap.get(currSnap);
            String renamedNextSnap = DashStrings.snapshotName + String.valueOf(count);
            origToRenamedSnap.put(nextSnap, renamedNextSnap);
            renamedSnaps.add(renamedNextSnap);
            count++;
            currSnap = nextSnap;
        }

        // add sigs
        for (String renamedSnap : renamedSnaps) {
            alloyModel.addPara(
                    new AlloySigPara(
                            List.of(AlloySigPara.Qual.ONE),
                            List.of(new AlloyQnameExpr(renamedSnap)),
                            new AlloySigPara.Extends(new AlloyQnameExpr(DashStrings.snapshotName)),
                            Collections.emptyList(),
                            new AlloyBlock()));
        }

        // add Fact
        List<AlloyExpr> alloyExprs = new ArrayList<>();
        // add init
        alloyExprs.add(
                new AlloyBracketExpr(
                        new AlloyQnameExpr(DashStrings.initFactName),
                        List.of(new AlloyQnameExpr(renamedSnaps.getFirst()))));

        // add next
        for (int i = 0; i < renamedSnaps.size() - 1; i++) {
            alloyExprs.add(
                    new AlloyEqualsExpr(
                            new AlloyBracketExpr(
                                    new AlloyQnameExpr(
                                            DashStrings.snapshotName
                                                    + DashStrings.SLASH
                                                    + DashStrings.tracesNextName),
                                    List.of(new AlloyQnameExpr(renamedSnaps.get(i)))),
                            new AlloyQnameExpr(renamedSnaps.get(i + 1))));
        }

        alloyModel.addPara(new AlloyFactPara(new AlloyBlock(alloyExprs)));
    }
}
