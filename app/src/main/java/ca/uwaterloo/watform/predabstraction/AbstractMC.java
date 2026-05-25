package ca.uwaterloo.watform.predabstraction;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyinterface.AlloyInterface;
import ca.uwaterloo.watform.alloyinterface.Solution;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.io.*;
import java.util.*;

public class AbstractMC extends AbstractBuildPA {

    public Solution solution;

    public AbstractMC(DashModel input) {
        super(input);
        solution = null;
    }

    public AbstractMC(DashModel input, int n) {
        super(input, n);
        solution = null;
    }

    public void executeAbsCmd() {
        if (absCmdIdx >= 0) {
            System.out.println("Running abstract command.");
            this.solution = AlloyInterface.executeCommand(absAlloy, absCmdIdx);
        }
    }
}
