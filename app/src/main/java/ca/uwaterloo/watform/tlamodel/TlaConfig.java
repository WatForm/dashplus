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
        return TlaStrings.INIT + TlaStrings.SPACE + init.toTLAPlusSnippetCore();
    }

    private String nextString() {
        return TlaStrings.NEXT + TlaStrings.SPACE + next.toTLAPlusSnippetCore();
    }

    private String constantsString() {
        StringBuilder answer = new StringBuilder(TlaStrings.CONSTANTS);
        if (this.constants.isEmpty()) return answer.toString();
        answer.append(TlaStrings.NEWLINE);
        for (TlaExp e : this.constants)
            answer.append(e.toTLAPlusSnippetCore() + TlaStrings.NEWLINE);
        return answer.toString();
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
