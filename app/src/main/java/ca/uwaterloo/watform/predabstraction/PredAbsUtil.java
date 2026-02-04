package ca.uwaterloo.watform.predabstraction;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashtoalloy.DSL;
import ca.uwaterloo.watform.utils.GeneralUtil;
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
        AlloyExpr e = loe.get(idx);
        if (loe.size() == 1) {
            combos.add(List.of(e));
            combos.add(List.of(AlloyExprFactory.AlloyNot(e)));
            return;
        }
        if (idx == loe.size()) {
            combos.add(new ArrayList<>(path));
            return;
        }

        path.add(e);
        backtrack(loe, idx + 1, path, combos);
        path.remove(path.size() - 1);

        path.add(AlloyExprFactory.AlloyNot(e));
        backtrack(loe, idx + 1, path, combos);
        path.remove(path.size() - 1);
    }

    public static boolean checkSAT(
            Set<AlloyExpr> exprs, AlloyModel am, DashModel dm, boolean snReqd) {
        return INSTANCE.checkSATInternal(exprs, am, dm, snReqd);
    }

    private boolean checkSATInternal(
            Set<AlloyExpr> exprs, AlloyModel am, DashModel dm, boolean snReqd) {
        Object key = canonicalKey(exprs);
        Boolean cached = cache.get(key);
        if (cached != null) {
            return cached;
        } else {
            String pname = "query_" + Integer.toString(cache.size());
            DSL dsl = new DSL(false);
            if (snReqd) {
                am.addPred(pname, dsl.curNextDecls(), GeneralUtil.setToList(exprs));
            } else {
                am.addPred(pname, dsl.curDecls(), GeneralUtil.setToList(exprs));
            }
        }
        return true;
    }

    private static Object canonicalKey(Set<AlloyExpr> exprs) {
        List<String> ids = exprs.stream().map(e -> e.toString()).sorted().toList();
        return ids;
    }
}
