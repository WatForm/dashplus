package ca.uwaterloo.watform.dashast;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashast.dashNamedExpr.*;
import ca.uwaterloo.watform.utils.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class DashState extends DashParagraph implements DashStateItem {

    // stuff from parsing
    public final String name;
    // private String sfqn; // set during resolveAllState
    public final String param;
    public final DashStrings.StateKind kind; // basic state = OR with no subStates
    public final DashStrings.DefKind def;
    private final List<Object> items;

    public DashState(
            Pos pos,
            String n,
            String prm,
            DashStrings.StateKind k,
            DashStrings.DefKind d,
            List<Object> i) {
        super(pos);
        assert (n != null);
        assert (i != null);
        this.name = n;
        this.param = prm;
        this.kind = k;
        this.def = d;
        this.items = i;
    }

    // to sort the items in a state for display
    // this order is very arbitrary

    public Integer itemToInt(Object i) {

        if (DashVarDecls.class.isInstance(i)) return 1;
        else if (DashBufferDecls.class.isInstance(i)) return 2;
        else if (DashEventDecls.class.isInstance(i)) return 3;
        else if (DashInit.class.isInstance(i)) return 4;
        else if (DashInv.class.isInstance(i)) return 5;
        else if (DashTrans.class.isInstance(i)) return 6;
        else if (DashState.class.isInstance(i)) return 7;
        else if (DashPred.class.isInstance(i)) return 8;
        else if (DashEntered.class.isInstance(i)) return 9;
        else if (DashExited.class.isInstance(i)) return 10;
        else {
            System.out.println(i.getClass());
            throw ImplementationError.missingCase("itemToInt");
        }
    }

    @Override
    public void toString(StringBuilder sb, int indent) {
        String ind = DashStrings.indent(indent);
        String s = new String(ind);
        if (def == DashStrings.DefKind.DEFAULT) {
            s += DashStrings.defaultName + " ";
        }
        if (kind == DashStrings.StateKind.AND) {
            s += DashStrings.concName + " ";
        }
        if (items.isEmpty()) {
            s += DashStrings.stateName + " " + name + " {}\n";
        } else {
            s += DashStrings.stateName + " " + name;
            if (param == null) s += " {\n";
            else s += " [" + param + "] {\n";
            StringJoiner j = new StringJoiner("");
            // sorting items for display order
            // map type of item to an integer (in function above)
            Collections.sort(items, (i1, i2) -> Integer.compare(itemToInt(i1), itemToInt(i2)));
            items.forEach(k -> j.add(((ASTNode) k).toString(indent + 1)));
            s += j.toString() + ind + "}\n";
        }
        sb.append(s);
    }

    public static String noParam() {
        return null;
    }

    public static List<Object> noSubstates() {
        return new ArrayList<Object>();
    }

    // getters from items
    public List<DashState> substates() {
        return extractItemsOfClass(items, DashState.class);
    }

    public List<DashEventDecls> eventDecls() {
        return extractItemsOfClass(items, DashEventDecls.class);
    }

    public List<DashVarDecls> varDecls() {
        return extractItemsOfClass(items, DashVarDecls.class);
    }

    public List<DashPred> preds() {
        return extractItemsOfClass(items, DashPred.class);
    }

    public List<DashBufferDecls> bufferDecls() {
        return extractItemsOfClass(items, DashBufferDecls.class);
    }

    public List<DashTrans> trans() {
        return extractItemsOfClass(items, DashTrans.class);
    }

    public List<DashInv> invs() {
        return extractItemsOfClass(items, DashInv.class);
    }

    public List<DashInit> inits() {
        return extractItemsOfClass(items, DashInit.class);
    }
}
