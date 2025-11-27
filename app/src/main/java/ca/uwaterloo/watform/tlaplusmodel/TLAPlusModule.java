package ca.uwaterloo.watform.tlaplusmodel;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

public class TLAPlusModule {
    private List<TLAPlusConstant> constants;
    private List<TLAPlusVariable> variables;
    private List<TLAPlusStandardLibraries> extended_libraries;
    private List<ASTNode> body;

    public TLAPlusModule() {
        this.constants = new ArrayList<>();
        this.variables = new ArrayList<>();
        this.extended_libraries = new ArrayList<>();
        this.body = new ArrayList<>();
    }

    public void addSTL(TLAPlusStandardLibraries stl) {
        this.extended_libraries.add(stl);
    }

    public void addVariable(TLAPlusVariable v) {
        this.variables.add(v);
    }

    public List<TLAPlusVariable> getVariables() {
        return GeneralUtil.mapBy(this.variables, c -> c);
    }

    public List<TLAPlusConstant> getConstants() {
        return GeneralUtil.mapBy(this.constants, c -> c);
    }

    public List<TLAPlusFormulaDefinition> getFormulaDefinitions() {
        return GeneralUtil.extractItemsOfClass(this.body, TLAPlusFormulaDefinition.class);
    }

    public List<TLAPlusComment> getComments() {
        return GeneralUtil.extractItemsOfClass(this.body, TLAPlusComment.class);
    }

    public void addConstant(TLAPlusConstant c) {
        this.constants.add(c);
    }

    public void addFormulaDefinition(TLAPlusFormulaDefinition d) {
        this.body.add(d);
    }

    public void addComment(TLAPlusComment c) {
        this.body.add(c);
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
