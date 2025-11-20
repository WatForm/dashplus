package ca.uwaterloo.watform.tlaplusmodel;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.ArrayList;
import java.util.List;

public class TLAPlusConfiguration {
    private List<TLAPlusExpression> invariants;
    private List<TLAPlusExpression> properties;
    private List<TLAPlusExpression> constants;
    private TLAPlusFormulaApplication init;
    private TLAPlusFormulaApplication next;

    public TLAPlusConfiguration(TLAPlusFormulaApplication init, TLAPlusFormulaApplication next) {
        this.invariants = new ArrayList<>();
        this.properties = new ArrayList<>();
        this.constants = new ArrayList<>();
        this.init = init;
        this.next = next;
    }

    private String init() {
        return TLAPlusStrings.INIT + TLAPlusStrings.SPACE + init.toString();
    }

    private String next() {
        return TLAPlusStrings.NEXT + TLAPlusStrings.SPACE + next.toString();
    }

    private String constants() {
        // TODO do this
        return "";
    }

    private String invariants() {
        // TODO do this
        return "";
    }

    private String properties() {
        // TODO do this
        return "";
    }

    public String code() {
        return "\n"
                + constants()
                + "\n"
                + invariants()
                + "\n"
                + properties()
                + "\n"
                + init()
                + "\n"
                + next();
    }
}
