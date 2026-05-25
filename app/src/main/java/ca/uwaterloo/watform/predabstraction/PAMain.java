package ca.uwaterloo.watform.predabstraction;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.dashtoalloy.DashToAlloy;
import java.io.*;
import java.util.*;

public class PAMain extends CERefinement {

    public PAMain(DashModel input) {
        super(input);
    }

    public PAMain(DashModel input, int n) {
        super(input, n);
    }

    public void runCEGARLoop() {
        this.createABVMap();

        System.out.println("\nABV map created:");
        for (String k : this.ABVNameCAFTransMap.keySet()) {
            AlloyExpr v = this.ABVNameCAFTransMap.get(k);
            System.out.println(k + " : " + v.toString());
        }
        System.out.println("*********");

        this.createAbstractModel();

        System.out.println("\nIn createAbstractModel(), Abstract model:\n");
        System.out.println(this.absModel.toString());
        DashToAlloy absd2a = new DashToAlloy(this.absModel);
        this.absAlloy = absd2a.translate();
        System.out.println("\nAbstract Alloy:\n" + this.absAlloy.toString());

        this.executeAbsCmd();

        if (this.solution == null) {
            System.out.println("Abstract model checking failed. No solution generated.");
        } else {
            if ((!this.solution.isSat() && this.isAbsCmdCheck)
                    || (this.solution.isSat() && !this.isAbsCmdCheck)) {
                System.out.println("Abstract model verified the abstract property.");
            } else {
                this.validateCE();
                if (this.isCEValid) {
                    System.out.println(
                            "The abstract counterexample is valid. The real counterexample is:\n");
                    System.out.println(this.realConcreteCE.orElse(null).toString());
                } else {
                    this.refineAbsModel();
                    System.out.println("Refined abstract model:\n" + this.absModel.toString());

                    int count = 1;
                    while (count < 5) {
                        this.executeAbsCmd();
                        if (this.solution != null) {
                            if (this.solution.isSat()) {
                                System.out.println(
                                        "Abstract model verified the abstract property.");
                                break;
                            } else {
                                this.validateCE();
                                if (this.isCEValid) {
                                    System.out.println(
                                            "The abstract counterexample is valid. The real counterexample is:\n");
                                    System.out.println(this.realConcreteCE.orElse(null).toString());
                                    break;
                                } else {
                                    this.refineAbsModel();
                                    System.out.println(
                                            "Refined abstract model:\n" + this.absModel.toString());
                                    count++;
                                }
                            }
                        }
                    }
                    System.out.println(String.valueOf(count) + " loops of CEGAR done!");
                }
            }
        }
    }
}
