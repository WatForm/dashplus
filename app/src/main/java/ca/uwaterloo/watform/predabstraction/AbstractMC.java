package ca.uwaterloo.watform.predabstraction;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyinterface.AlloyInterface;
import ca.uwaterloo.watform.alloyinterface.Solution;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashtoalloy.DashToAlloy;
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
            DashToAlloy d2a = new DashToAlloy(absModel);
            absAlloy = d2a.translate();
            Solution modSat = AlloyInterface.checkModelSatisfiability(absAlloy);
            if (modSat.isSat()) {
                System.out.println("Running abstract command.");
                this.solution = AlloyInterface.executeCommand(absAlloy, absCmdIdx);
            } else {
                System.out.println("The abstract model is not satisfiable.");
                AlloyInterface.executeCommand(absAlloy, absCmdIdx);
                this.solution = null;
            }
        }
    }

    public void executeAbsCmdIn(AlloyModel am) {
        if (absCmdIdx >= 0) {
            am.resolve();
            this.solution = AlloyInterface.executeCommand(am, absCmdIdx);
        }
    }

    public boolean hasInstance() {
        return !((!this.solution.isSat() && this.isAbsCmdCheck)
                || (this.solution.isSat() && !this.isAbsCmdCheck));
    }
}
