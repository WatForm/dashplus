package ca.uwaterloo.watform.tlaplusmodel;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.utils.*;
import java.util.ArrayList;
import java.util.List;

public class TLAPlusModule {
    private List<TLAPlusConstant> constants;
    private List<TLAPlusVariable> variables;
    private List<TLAPlusSTL> extended_libraries;
    private List<TLAPlusFormulaDefinition> formulae;

    public TLAPlusModule() {
        this.constants = new ArrayList<>();
        this.variables = new ArrayList<>();
        this.extended_libraries = new ArrayList<>();
        this.formulae = new ArrayList<>();
    }

    public void addSTL(TLAPlusSTL stl) {
        this.extended_libraries.add(stl);
    }

    public void addVariable(TLAPlusVariable v) {
        this.variables.add(v);
    }

    public void addConstant(TLAPlusConstant c) {
        this.constants.add(c);
    }

    public void addFormulaDefinition(TLAPlusFormulaDefinition d) {
        this.formulae.add(d);
    }

    private String stringHead(String name) {
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

    public String formulaeString() {
        StringBuilder sb = new StringBuilder();
        for (TLAPlusFormulaDefinition f : this.formulae) {
            sb.append("\n");
            f.toString(sb, 0);
        }
        return sb.toString();
    }

    private String stringBody() {
        String doubleSpace = "\n\n";
        return TLAPlusModule.simpleBuilder(TLAPlusStrings.EXTENDS, this.extended_libraries)
                + doubleSpace
                + TLAPlusModule.simpleBuilder(TLAPlusStrings.CONSTANTS, this.constants)
                + doubleSpace
                + TLAPlusModule.simpleBuilder(TLAPlusStrings.VARIABLES, this.variables)
                + doubleSpace
                + this.formulaeString()
                + doubleSpace
                + TLAPlusStrings.BODY_DELIMITER;
    }

    public String code(String name) {
        return this.stringHead(name) + "\n" + this.stringBody();
    }
}
