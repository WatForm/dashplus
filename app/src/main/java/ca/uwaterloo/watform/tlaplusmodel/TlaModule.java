package ca.uwaterloo.watform.tlaplusmodel;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

public class TlaModule {
    public final List<TlaConst> constants;
    public final List<TlaVar> variables;
    public final List<TlaStdLibs> extended_libraries;
    public final List<ASTNode> body;

    public TlaModule() {
        this.constants = new ArrayList<>();
        this.variables = new ArrayList<>();
        this.extended_libraries = new ArrayList<>();
        this.body = new ArrayList<>();
    }

    public List<TlaFormulaDefn> getFormulaDefinitions() {
        return GeneralUtil.extractItemsOfClass(this.body, TlaFormulaDefn.class);
    }

    public List<TlaComment> getComments() {
        return GeneralUtil.extractItemsOfClass(this.body, TlaComment.class);
    }

    private String codeHead(String name) {
        return TlaStrings.HEAD_DELIMITER
                + TlaStrings.SPACE
                + TlaStrings.MODULE
                + TlaStrings.SPACE
                + name
                + TlaStrings.SPACE
                + TlaStrings.HEAD_DELIMITER;
    }

    private static String simpleBuilder(String initial, List<? extends ASTNode> l) {
        StringBuilder sb = new StringBuilder(initial);
        int n = l.size();
        if (n == 0) return ""; // no need for the line if nothing exists
        for (int i = 0; i < n; i++) {
            sb.append(TlaStrings.SPACE);
            l.get(i).toString(sb, 0);
            if (i < n - 1) sb.append(TlaStrings.COMMA);
        }
        return sb.toString();
    }

    public String bodyString() {
        StringBuilder sb = new StringBuilder();
        for (ASTNode f : this.body) {
            sb.append("\n");
            f.toString(sb, 0);
        }
        return sb.toString();
    }

    private String codeBody() {
        String doubleSpace = "\n\n";
        return TlaModule.simpleBuilder(TlaStrings.EXTENDS, this.extended_libraries)
                + doubleSpace
                + TlaModule.simpleBuilder(TlaStrings.CONSTANTS, this.constants)
                + doubleSpace
                + TlaModule.simpleBuilder(TlaStrings.VARIABLES, this.variables)
                + doubleSpace
                + this.bodyString()
                + doubleSpace
                + TlaStrings.BODY_DELIMITER;
    }

    public String code(String name) {
        return this.codeHead(name) + "\n" + this.codeBody();
    }
}
