package ca.uwaterloo.watform.tlaplusmodel;

import ca.uwaterloo.watform.tlaplusast.*;
import java.util.ArrayList;
import java.util.List;

public class TLAPlusConfiguration {
    public List<TLAPlusExp> invariants;
    public List<TLAPlusExp> properties;
    public List<TLAPlusExp> constants;
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
        return TLAPlusStrings.CONSTANTS + TLAPlusStrings.SPACE + "";
    }

    private String invariantsString() {
        // TODO do this
        return TLAPlusStrings.INVARIANTS + TLAPlusStrings.SPACE + "";
    }

    private String propertiesString() {
        // TODO do this
        return TLAPlusStrings.PROPERTIES + TLAPlusStrings.SPACE + "";
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
