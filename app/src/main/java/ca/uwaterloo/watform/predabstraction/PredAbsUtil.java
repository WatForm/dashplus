package ca.uwaterloo.watform.predabstraction;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashtoalloy.DSL;
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
            combos.add(List.of(AlloyNot(e)));
            return;
        }
        if (idx == loe.size()) {
            combos.add(new ArrayList<>(path));
            return;
        }

        path.add(e);
        backtrack(loe, idx + 1, path, combos);
        path.remove(path.size() - 1);

        path.add(AlloyNot(e));
        backtrack(loe, idx + 1, path, combos);
        path.remove(path.size() - 1);
    }

    public static boolean checkSAT(Set<AlloyExpr> exprs, AlloyModel am, boolean snReqd) {
        return INSTANCE.checkSATInternal(exprs, am, snReqd);
    }

    private boolean checkSATInternal(Set<AlloyExpr> exprs, AlloyModel am, boolean snReqd) {
        Object key = canonicalKey(exprs);
        Boolean cached = cache.get(key);
        if (cached != null) {
            return cached;
        } else {
            String pname = "query_" + Integer.toString(cache.size());
            DSL dsl = new DSL(false);
            if (snReqd) {
                am.addPred(pname, dsl.curNextDecls(), setToList(exprs));
            } else {
                am.addPred(pname, dsl.curDecls(), setToList(exprs));
            }
        }
        return true;
    }

    private static Object canonicalKey(Set<AlloyExpr> exprs) {
        List<String> ids = exprs.stream().map(e -> e.toString()).sorted().toList();
        return ids;
    }
}
