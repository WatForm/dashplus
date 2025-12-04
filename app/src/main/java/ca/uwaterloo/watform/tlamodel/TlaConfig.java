package ca.uwaterloo.watform.tlamodel;

import ca.uwaterloo.watform.tlaast.*;
import java.util.ArrayList;
import java.util.List;

public class TlaConfig {
    public List<TlaExp> invariants;
    public List<TlaExp> properties;
    public List<TlaExp> constants;
    public TlaAppl init;
    public TlaAppl next;

    public TlaConfig(TlaAppl init, TlaAppl next) {
        this.invariants = new ArrayList<>();
        this.properties = new ArrayList<>();
        this.constants = new ArrayList<>();
        this.init = init;
        this.next = next;
    }

    private String initString() {
        return TlaStrings.INIT + TlaStrings.SPACE + init.toString();
    }

    private String nextString() {
        return TlaStrings.NEXT + TlaStrings.SPACE + next.toString();
    }

    private String constantsString() {
        // TODO do this
        return TlaStrings.CONSTANTS + TlaStrings.SPACE + "";
    }

    private String invariantsString() {
        // TODO do this
        return TlaStrings.INVARIANTS + TlaStrings.SPACE + "";
    }

    private String propertiesString() {
        // TODO do this
        return TlaStrings.PROPERTIES + TlaStrings.SPACE + "";
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
