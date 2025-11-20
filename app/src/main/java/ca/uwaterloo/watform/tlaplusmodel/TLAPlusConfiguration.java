package ca.uwaterloo.watform.tlaplusmodel;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.ArrayList;
import java.util.List;

public class TLAPlusConfiguration {
    public List<TLAPlusExpression> invariants;
    public List<TLAPlusExpression> properties;
    public List<TLAPlusExpression> constants;
    public TLAPlusFormulaApplication init;
    public TLAPlusFormulaApplication next;

    public TLAPlusConfiguration(TLAPlusFormulaApplication init, TLAPlusFormulaApplication next) {
        this.invariants = new ArrayList<>();
        this.properties = new ArrayList<>();
        this.constants = new ArrayList<>();
        this.init = init;
        this.next = next;
    }


    private String initString() {
        return TLAPlusStrings.INIT + TLAPlusStrings.SPACE + init.toString();
    }

    private String nextString() {
        return TLAPlusStrings.NEXT + TLAPlusStrings.SPACE + next.toString();
    }

    private String constantsString() {
        // TODO do this
        return "";
    }

    private String invariantsString() {
        // TODO do this
        return "";
    }

    private String propertiesString() {
        // TODO do this
        return "";
    }

    public String code() {
        return "\n"
                + constantsString()
                + "\n"
                + invariantsString()
                + "\n"
                + propertiesString()
                + "\n"
                + initString()
                + "\n"
                + nextString();
    }
}
