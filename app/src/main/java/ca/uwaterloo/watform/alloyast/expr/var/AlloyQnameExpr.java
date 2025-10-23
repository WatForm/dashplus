package ca.uwaterloo.watform.alloyast.expr.var;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class AlloyQnameExpr extends AlloyVarExpr
        implements AlloySigRefExpr, AlloyScopableExpr {
    public final List<AlloyNameExpr> nameExprList;

    public AlloyQnameExpr(Pos pos, List<AlloyNameExpr> nameExprList) {
        super(
                pos,
                nameExprList.stream()
                        .map(AlloyNameExpr::getLabel)
                        .collect(Collectors.joining(AlloyStrings.SLASH)));
        this.nameExprList = Collections.unmodifiableList(nameExprList);
    }

    public AlloyQnameExpr(List<AlloyNameExpr> nameExprList) {
        super(
                nameExprList.stream()
                        .map(AlloyNameExpr::getLabel)
                        .collect(Collectors.joining(AlloyStrings.SLASH)));
        this.nameExprList = Collections.unmodifiableList(nameExprList);
    }

    public AlloyQnameExpr(Pos pos, AlloyNameExpr nameExpr) {
        super(pos, nameExpr.getLabel());
        this.nameExprList = Collections.unmodifiableList(Collections.singletonList(nameExpr));
    }

    public AlloyQnameExpr(AlloyNameExpr nameExpr) {
        super(nameExpr.getLabel());
        this.nameExprList = Collections.unmodifiableList(Collections.singletonList(nameExpr));
    }
}
