package ca.uwaterloo.watform;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.dashast.DashState;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;

public final class TestUtil {
    private TestUtil() {}

    public static AlloyFactPara createNamelessFact() {
        return new AlloyFactPara(new AlloyBlock(new AlloyQnameExpr("a")));
    }

    public static AlloySigPara createSig(String name) {
        return new AlloySigPara(new AlloyQnameExpr(name), TestUtil.createBlock());
    }

    public static AlloyBlock createBlock() {
        return new AlloyBlock(Collections.emptyList());
    }

    public static DashState createDashState() {
        return new DashState(
                Pos.UNKNOWN,
                "s",
                DashState.noParam(),
                DashStrings.StateKind.OR,
                DashStrings.DefKind.NOTDEFAULT,
                Collections.emptyList());
    }
}
