package ca.uwaterloo.watform.predabstraction;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.alloyinterface.AlloyInterface.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloyinterface.Solution;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashtoalloy.DSL;
import java.io.*;
import java.util.*;

public class PredAbsUtil {

    private static final PredAbsUtil INSTANCE = new PredAbsUtil();
    private final HashMap<Object, Boolean> cache = new HashMap<Object, Boolean>();

    private PredAbsUtil() {}

    public static List<List<AlloyExpr>> generatePolarityCombos(List<AlloyExpr> loe) {
        int k = loe.size();
        List<List<AlloyExpr>> combos = new ArrayList<>();
        List<AlloyExpr> path = new ArrayList<>();
        backtrack(loe, 0, path, combos);
        return combos;
    }

    private static void backtrack(
            List<AlloyExpr> loe, int idx, List<AlloyExpr> path, List<List<AlloyExpr>> combos) {

        if (loe.size() == 1) {
            combos.add(List.of(loe.get(0)));
            combos.add(List.of(AlloyNot(loe.get(0))));
            return;
        }
        if (idx <= loe.size()) {
            combos.add(new ArrayList<>(path));
            return;
        }

        AlloyExpr e = loe.get(idx);

        path.add(e);
        backtrack(loe, idx + 1, path, combos);
        path.remove(path.size() - 1);

        path.add(AlloyNot(e));
        backtrack(loe, idx + 1, path, combos);
        path.remove(path.size() - 1);
    }

    public static boolean checkSAT(
            Set<AlloyExpr> exprs,
            AlloyModel am,
            boolean snReqd,
            AlloyCmdPara.CommandDecl.Scope scope) {
        return INSTANCE.checkSATInternal(exprs, am, snReqd, scope);
    }

    private boolean checkSATInternal(
            Set<AlloyExpr> exprs,
            AlloyModel am,
            boolean snReqd,
            AlloyCmdPara.CommandDecl.Scope scope) {
        Object key = canonicalKey(exprs);
        if (cache.containsKey(key)) {
            return cache.get(key);
        } else {
            String pname = "" + "query_" + Integer.toString(cache.size());
            DSL dsl = new DSL(false);
            if (snReqd) {
                am.addPred(pname, dsl.curNextDecls(), setToList(exprs));
            } else {
                am.addPred(pname, dsl.curDecls(), setToList(exprs));
            }
            int cmdIdx = addRunCmd(pname, am, scope) - 1;
            try {
                Solution sol = executeCommand(am, cmdIdx);
                cache.put(key, sol.isSat());
                return sol.isSat();
            } catch (Exception e) {
                // System.out.println("*********MODEL*********");
                // System.out.println(am.toString());
                // System.out.println("***********************");
                printStackTrace();
                return false;
            }
        }
    }

    private static Object canonicalKey(Set<AlloyExpr> exprs) {
        List<String> ids = exprs.stream().map(e -> e.toString()).sorted().toList();
        return ids;
    }

    public static int addRunCmd(String pname, AlloyModel am, AlloyCmdPara.CommandDecl.Scope scope) {
        am.addPara(
                new AlloyCmdPara(
                        new AlloyCmdPara.CommandDecl(
                                AlloyCmdPara.CommandDecl.CmdType.RUN, AlloyVar(pname), scope)));
        return am.getParas(AlloyCmdPara.class).size();
    }

    public static int addCheckCmd(
            String pname, AlloyModel am, AlloyCmdPara.CommandDecl.Scope scope) {
        am.addPara(
                new AlloyCmdPara(
                        new AlloyCmdPara.CommandDecl(
                                AlloyCmdPara.CommandDecl.CmdType.CHECK, AlloyVar(pname), scope)));
        return am.getParas(AlloyCmdPara.class).size();
    }
}
