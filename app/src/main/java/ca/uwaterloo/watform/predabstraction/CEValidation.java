package ca.uwaterloo.watform.predabstraction;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPredPara;
// import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloyinterface.AlloyInterface;
import ca.uwaterloo.watform.alloyinterface.Solution;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashtoalloy.D2AStrings;
import ca.uwaterloo.watform.dashtoalloy.DashToAlloy;
import com.google.common.collect.Iterables;
import java.io.*;
import java.util.*;

public class CEValidation extends AbstractMC {
    public String snShot = "snShot";
    public String ceValPrefix = "CEVal_";
    public String bPredPrefix = "CAF_";

    protected AlloyModel concreteAlloy;
    protected List<String> renamedSnaps;
    protected Map<String, String> renamedSnapToOrig;

    public boolean isCEValid = false;

    // if isCEValid == false:
    public String spuriousTFQN = null;
    protected String spuriousSnapName = null;

    // if isCEValid == true:
    public Solution realConcreteCE = null;

    // the following map stores {SnapshotName: {varName: value}} eg. __Snapshot$0 : {B0:
    // boolean/True$0}
    protected HashMap<String, HashMap<String, String>> snapshotVarVals;

    public CEValidation(DashModel input) {
        super(input);
        this.snapshotVarVals = new HashMap<String, HashMap<String, String>>();
    }

    public CEValidation(DashModel input, int n) {
        super(input, n);
        this.snapshotVarVals = new HashMap<String, HashMap<String, String>>();
    }

    private AlloyExpr generateCAFConj(String snapName) {
        HashMap<String, String> varValMap = snapshotVarVals.get(renamedSnapToOrig.get(snapName));
        List<AlloyExpr> cafList = new ArrayList<>();
        for (int i = 0; i < absModel.allVarNames().size(); i++) {
            String vname = "B" + Integer.toString(i);
            AlloyExpr caf = AlloyPredCall(bPredPrefix + vname, List.of(AlloyVar(snapName)));
            String val = varValMap.get(vname);
            if (val.contains("True")) {
                cafList.add(caf);
            } else {
                cafList.add(AlloyNot(caf));
            }
        }
        return new AlloyBlock(AlloyAndList(cafList));
    }

    private void addABVtoCAFPreds() {
        for (String v : ABVNameCAFTransMap.keySet()) {
            AlloyExpr caf = ABVNameCAFTransMap.get(v);
            concreteAlloy.addPredPara(
                    new AlloyPredPara(
                            new AlloyQnameExpr(bPredPrefix + v),
                            dsl.curDecls(),
                            new AlloyBlock(List.of(caf))));
        }
    }

    private String removeDollarSuffix(String s) {
        return s.substring(0, s.length() - 2);
    }

    public String getABVfqn(String vname) {
        return DashFQN.translateFQN(DashFQN.fqn(absModel.rootName(), vname));
    }

    public void validateCE() {
        // System.out.println("Abstract counterexample:\n" + solution.toString());

        // translate concrete Dash model to Alloy
        DashToAlloy d2a = new DashToAlloy(concreteModel);
        this.concreteAlloy = d2a.translate();

        addABVtoCAFPreds();

        // Get the next relation on the Snapshots
        Set<List<String>> nextRelSnapSet =
                solution.get(D2AStrings.snapshotName + DashStrings.SLASH + "Ord.Next");
        Map<String, String> nextRelSnapMap = new HashMap<>();
        if (nextRelSnapSet == null) {
            System.out.println(
                    D2AStrings.snapshotName
                            + DashStrings.SLASH
                            + "Ord.Next is not a valid key in the solution map");
        } else {
            for (List<String> pair : nextRelSnapSet) {
                nextRelSnapMap.put(pair.getFirst(), pair.getLast());
            }
        }

        // Get the first Snapshot
        Set<List<String>> firstSnapSet =
                solution.get(D2AStrings.snapshotName + DashStrings.SLASH + "Ord.First");
        String firstSnap = "";
        if (firstSnapSet == null) {
            System.out.println(
                    D2AStrings.snapshotName
                            + DashStrings.SLASH
                            + "Ord.First is not a valid key in the solution map");
        } else {
            firstSnap = Iterables.getOnlyElement(firstSnapSet).getFirst();
        }

        // Get the boolean variable values
        // [{"__Snapshot$0" : "boolean/True$0", ...},
        //  {"__Snapshot$0" : "boolean/False$0", ...}, ...]
        // where the map stored at the ith index belongs to the ABV "Bi"

        for (int i = 0; i < ABVNameCAFTransMap.size(); i++) {
            String vname = abvNamePre + Integer.toString(i);
            String vfqn = getABVfqn(vname);
            Set<List<String>> val =
                    solution.get(
                            AlloyStrings.THIS
                                    + DashStrings.SLASH
                                    + D2AStrings.snapshotName
                                    + AlloyStrings.DOT
                                    + vfqn);
            if (val != null) {
                for (List<String> pair : val) {
                    String snapName = pair.getFirst();
                    String varVal = pair.getLast();
                    if (snapshotVarVals.containsKey(snapName)) {
                        HashMap<String, String> varValMap = snapshotVarVals.get(snapName);
                        varValMap.put(vname, varVal);
                        snapshotVarVals.put(snapName, varValMap);
                    } else {
                        HashMap<String, String> varValMap = new HashMap<>();
                        varValMap.put(vname, varVal);
                        snapshotVarVals.put(snapName, varValMap);
                    }
                }
            }
        }

        // Rename snapshots; DshSnapshot$0 may not be the first snapshot and
        // it's confusing
        renamedSnapToOrig = new HashMap<>();
        renamedSnapToOrig.put(this.snShot + String.valueOf(0), firstSnap);
        renamedSnaps = new ArrayList<>(); // ordered by the next relation
        renamedSnaps.add(this.snShot + String.valueOf(0));

        int count = 1;
        String currSnap = firstSnap;

        while (nextRelSnapMap.containsKey(currSnap)) {
            String nextSnap = nextRelSnapMap.get(currSnap);
            String renamedNextSnap = this.snShot + String.valueOf(count);
            renamedSnapToOrig.put(renamedNextSnap, nextSnap);
            renamedSnaps.add(renamedNextSnap);
            count++;
            currSnap = nextSnap;
        }

        // ---------------------------------------
        // add one sigs for each renamed snapshot

        for (String renamedSnap : renamedSnaps) {
            concreteAlloy.addOneExtendsSig(renamedSnap, D2AStrings.snapshotName);
        }

        // add an alloy pred called "CEVal_ctr" where
        // pred CEVal_0 {
        //  __initial[S0]
        //  S0.caf0 && !S0.caf1 && ... // based on S0.B0, S0.B1 ...
        // }

        List<AlloyExpr> body = new ArrayList<>();

        // __initial[S0]
        body.add(
                AlloyPredCall(D2AStrings.initPredName, List.of(AlloyVar(renamedSnaps.getFirst()))));

        // __small_step[S0, S1]
        // body.add(
        //         AlloyPredCall(
        //                 D2AStrings.smallStepName,
        //                 List.of(AlloyVar(renamedSnaps.get(0)), AlloyVar(renamedSnaps.get(1)))));

        // B's of S0
        body.add(generateCAFConj(renamedSnaps.getFirst()));

        // B's of S1
        // body.add(generateCAFConj(renamedSnaps.get(1)));
        concreteAlloy.addPredPara(
                new AlloyPredPara(ceValPrefix + String.valueOf(0), new AlloyBlock(body)));

        // pred CEVal_i {
        //  CEVal_{i-1}
        //  __small_step[S_{i-1}, S_{i}]
        //  S_i.caf0 && !S_i.caf1 && ... // based on S_i.B0, S_i.B1 ...

        for (int i = 1; i < renamedSnaps.size(); i++) {
            body = new ArrayList<>();
            body.add(AlloyPredCall(ceValPrefix + Integer.toString(i - 1), emptyList()));
            body.add(
                    AlloyPredCall(
                            D2AStrings.smallStepName,
                            List.of(
                                    AlloyVar(renamedSnaps.get(i - 1)),
                                    AlloyVar(renamedSnaps.get(i)))));
            body.add(generateCAFConj(renamedSnaps.get(i)));
            // body.add(generateCAFConj(renamedSnaps.get(i + 1)));
            concreteAlloy.addPredPara(
                    new AlloyPredPara(ceValPrefix + Integer.toString(i), new AlloyBlock(body)));
        }

        int cmdIdx = PredAbsUtil.addRunCmd(ceValPrefix + String.valueOf(0), concreteAlloy, scope);
        for (int i = 1; i < renamedSnaps.size(); i++) {
            PredAbsUtil.addRunCmd(ceValPrefix + Integer.toString(i), concreteAlloy, scope);
        }

        System.out.println("\n\nConcrete Alloy with CE Validation queries:\n");
        System.out.println(concreteAlloy.toString());

        boolean flag = true;
        Solution sol = null;
        int idx = cmdIdx;
        while (idx < concreteAlloy.getNumCmds()) {
            sol = AlloyInterface.executeCommand(concreteAlloy, idx);
            if (sol.isSat()) {
                idx++;
            } else {
                flag = false;
                break;
            }
        }

        if (!flag) {
            this.isCEValid = false;
            int failCmdIdx = idx - cmdIdx;
            String failTransSrcSnap = renamedSnapToOrig.get(renamedSnaps.get(failCmdIdx));
            if (failTransSrcSnap != null) {
                Set<List<String>> transTaken =
                        solution.get(
                                AlloyStrings.THIS
                                        + DashStrings.SLASH
                                        + D2AStrings.snapshotName
                                        + AlloyStrings.DOT
                                        + D2AStrings.transTakenName
                                        + String.valueOf(0));
                HashMap<String, String> takenMap = new HashMap<>();
                for (List<String> pair : transTaken) {
                    takenMap.put(pair.getFirst(), removeDollarSuffix(pair.getLast()));
                }

                boolean f = false;

                if (failCmdIdx == 0) {
                    String snap = failTransSrcSnap;
                    int ctr = renamedSnaps.size();
                    while (ctr > 0) {
                        if (!takenMap.containsKey(snap)) {
                            failTransSrcSnap = snap;
                            snap = nextRelSnapMap.get(snap);
                            ctr--;
                        } else {
                            f = true;
                            break;
                        }
                    }
                    if (f) {
                        this.spuriousSnapName = snap;
                        this.spuriousTFQN = DashFQN.translateToDashFQN(takenMap.get(snap));
                    } else {
                        System.out.println("In validateCE(): CE does not have any __taken0.");
                    }
                } else {
                    if (takenMap.containsKey(failTransSrcSnap)) {
                        this.spuriousTFQN =
                                DashFQN.translateToDashFQN(takenMap.get(failTransSrcSnap));
                        this.spuriousSnapName = failTransSrcSnap;
                        System.out.println(
                                "The abstract Dash transition that causes the spurious behaviour is: "
                                        + spuriousTFQN);
                    } else {
                        System.out.println("In validateCE(): Could not find spurious transition.");
                    }
                }
            } else {
                System.out.println(
                        "renamedSnapToOrig.get(renamedSnaps.get(idx - xmdIdx)) failed in validateCE().");
            }

        } else {
            this.isCEValid = true;
            this.realConcreteCE = sol;
        }
    }
}
