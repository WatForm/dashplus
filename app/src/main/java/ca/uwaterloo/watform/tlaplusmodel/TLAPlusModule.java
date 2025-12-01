package ca.uwaterloo.watform.tlaplusmodel;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

public class TLAPlusModule {
    public final List<TLAPlusConst> constants;
    public final List<TLAPlusVar> variables;
    public final List<TLAPlusStandardLibraries> extended_libraries;
    public final List<ASTNode> body;

    public TLAPlusModule() {
        this.constants = new ArrayList<>();
        this.variables = new ArrayList<>();
        this.extended_libraries = new ArrayList<>();
        this.body = new ArrayList<>();
    }

    public List<TLAPlusFormulaDefn> getFormulaDefinitions() {
        return GeneralUtil.extractItemsOfClass(this.body, TLAPlusFormulaDefn.class);
    }

    public List<TLAPlusComment> getComments() {
        return GeneralUtil.extractItemsOfClass(this.body, TLAPlusComment.class);
    }

    private String codeHead(String name) {
        return TLAPlusStrings.HEAD_DELIMITER
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.MODULE
                + TLAPlusStrings.SPACE
                + name
                + TLAPlusStrings.SPACE
                + TLAPlusStrings.HEAD_DELIMITER;
    }

    private static String simpleBuilder(String initial, List<? extends ASTNode> l) {
        StringBuilder sb = new StringBuilder(initial);
        int n = l.size();
        if (n == 0) return ""; // no need for the line if nothing exists
        for (int i = 0; i < n; i++) {
            sb.append(TLAPlusStrings.SPACE);
            l.get(i).toString(sb, 0);
            if (i < n - 1) sb.append(TLAPlusStrings.COMMA);
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
        return TLAPlusModule.simpleBuilder(TLAPlusStrings.EXTENDS, this.extended_libraries)
                + doubleSpace
                + TLAPlusModule.simpleBuilder(TLAPlusStrings.CONSTANTS, this.constants)
                + doubleSpace
                + TLAPlusModule.simpleBuilder(TLAPlusStrings.VARIABLES, this.variables)
                + doubleSpace
                + this.bodyString()
                + doubleSpace
                + TLAPlusStrings.BODY_DELIMITER;
    }

    public String code(String name) {
        return this.codeHead(name) + "\n" + this.codeBody();
    }
}
