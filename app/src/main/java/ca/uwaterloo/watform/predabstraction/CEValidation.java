package ca.uwaterloo.watform.predabstraction;

import ca.uwaterloo.watform.alloyast.expr.binary.AlloyEqualsExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBracketExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPredPara;
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
    public static final String snShot = "snShot";

    public static void initAndNext(AlloyModel alloyModel, Solution solution) {
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
        origToRenamedSnap.put(firstSnap, CEValidation.snShot + String.valueOf(0));
        List<String> renamedSnaps = new ArrayList<>(); // ordered by the next relation
        renamedSnaps.add(CEValidation.snShot + String.valueOf(0));

        int count = 1;
        String currSnap = firstSnap;

        while (nextRelSnapMap.containsKey(currSnap)) {
            String nextSnap = nextRelSnapMap.get(currSnap);
            String renamedNextSnap = CEValidation.snShot + String.valueOf(count);
            origToRenamedSnap.put(nextSnap, renamedNextSnap);
            renamedSnaps.add(renamedNextSnap);
            count++;
            currSnap = nextSnap;
        }

        // ---------------------------------------
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

        alloyModel.addPara(
                new AlloyPredPara(
                        renamedSnaps.getFirst() + "Pred",
                        new AlloyBlock(
                                List.of(
                                        new AlloyBracketExpr(
                                                new AlloyQnameExpr(DashStrings.initFactName),
                                                List.of(
                                                        new AlloyQnameExpr(
                                                                renamedSnaps.getFirst())))))));

        for (int i = 1; i < renamedSnaps.size(); i++) {
            alloyModel.addPara(
                    new AlloyPredPara(
                            renamedSnaps.get(i) + "Pred",
                            new AlloyBlock(
                                    new AlloyEqualsExpr(
                                            new AlloyBracketExpr(
                                                    new AlloyQnameExpr(
                                                            DashStrings.snapshotName
                                                                    + DashStrings.SLASH
                                                                    + DashStrings.tracesNextName),
                                                    List.of(
                                                            new AlloyQnameExpr(
                                                                    renamedSnaps.get(i - 1)))),
                                            new AlloyQnameExpr(renamedSnaps.get(i))))));
        }
    }
}
