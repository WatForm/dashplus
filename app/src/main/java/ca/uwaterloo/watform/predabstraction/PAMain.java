package ca.uwaterloo.watform.predabstraction;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class PAMain extends CERefinement {

    private int cmdIdx = -1;

    public PAMain(DashModel input) {
        super(input);
    }

    public PAMain(DashModel input, int n) {
        super(input, n);
        this.cmdIdx = n;
    }

    public void runCEGARLoop() {
        this.createABVMap();

        System.out.println("\nABV map created:");
        for (String k : this.ABVNameCAFTransMap.keySet()) {
            AlloyExpr v = this.ABVNameCAFTransMap.get(k);
            VarNameCollector vc = new VarNameCollector();
            System.out.println(k + " : " + v.toString() + "\t" + vc.getVarNames(v).toString());
        }
        System.out.println("*********");

        this.createAbstractModel();

        System.out.println("\nIn createAbstractModel(), Abstract model created.");
        // System.out.println(this.absModel.toString());
        // DashToAlloy absd2a = new DashToAlloy(this.absModel);
        // this.absAlloy = absd2a.translate();
        // System.out.println("\nAbstract Alloy:\n" + this.absAlloy.toString());

        this.executeAbsCmd();

        if (this.solution == null) {
            System.out.println("Abstract model checking failed. No solution generated.");
            System.out.println("Abstract model:\n\n" + absModel.toString());
        } else {
            if (!this.hasInstance()) {
                System.out.println("Abstract model verified the abstract property.");
            } else {
                this.validateCE();
                if (this.isCEValid == true) {
                    System.out.println(
                            "The abstract counterexample is valid. The real counterexample is:\n");
                    System.out.println(this.realConcreteCE.toString());
                } else {
                    // try {
                    //     for (int i = 1; i <= 5; i++) {
                    //         this.solution.next();
                    //         this.validateCE();
                    //         this.refineAbsModel();
                    //     }
                    // } catch (Exception e) {
                    //     System.out.println(
                    //             "In runCEGARLoop: solution.next and ce refinement did not
                    // work.");
                    // }
                    this.refineAbsModel();
                    System.out.println("In runCEGARLoop, refined the abstract model");
                    System.out.println("Refined abstract model:\n" + this.absModel.toString());

                    int count = 1;
                    while (count < 5) {
                        this.executeAbsCmd();
                        if (this.solution == null) {
                            break;
                        }
                        if (!this.hasInstance()) {
                            System.out.println("Abstract model verified the abstract property.");
                            break;
                        } else {
                            this.validateCE();
                            if (this.isCEValid) {
                                System.out.println(
                                        "The abstract counterexample is valid. The real counterexample is:\n");
                                System.out.println(this.realConcreteCE.toString());
                                break;
                            } else {
                                // try {
                                //     for (int i = 1; i <= 5; i++) {
                                //         this.solution.next();
                                //         this.validateCE();
                                //         this.refineAbsModel();
                                //     }
                                // } catch (Exception e) {
                                //     System.out.println(
                                //             "In runCEGARLoop: solution.next and ce refinement did
                                // not work.");
                                // }
                                this.refineAbsModel();
                                if (this.spuriousSnapName != null) {
                                    System.out.println(
                                            "In runCEGARLoop, refined the abstract model for the "
                                                    + String.valueOf(count + 1)
                                                    + "th time.");
                                } else {
                                    System.out.println(
                                            "Could not refine the abstract model with the counterexample.");
                                    break;
                                }
                            }
                        }

                        count++;
                    }
                    System.out.println(String.valueOf(count) + " loops of CEGAR done!");
                }
            }
        }
    }

    public void writeAllModels(String fullFileName) throws IOException {
        String fileNamePrefix = fullFileName.substring(0, fullFileName.length() - 4);
        String absDashModelFN;
        String absAlloyFN;
        if (this.cmdIdx >= 0) {
            absDashModelFN = fileNamePrefix + "-abs-" + String.valueOf(this.cmdIdx) + ".dsh";
            absAlloyFN = fileNamePrefix + "-abs-" + String.valueOf(this.cmdIdx) + ".als";
        } else {
            absDashModelFN = fileNamePrefix + "-abs-noCmd.dsh";
            absAlloyFN = fileNamePrefix + "abs-noCmd.als";
        }
        String queryModelFN = fileNamePrefix + "-query.als";
        String concAlloyFN = fileNamePrefix + "-conc-alloy.als";

        File absDMFile = new File(absDashModelFN);
        File absAMFile = new File(absAlloyFN);
        File concAlloyFile = new File(concAlloyFN);
        File queryFile = new File(queryModelFN);

        System.out.println("Writing Abstract Dash Model to " + absDashModelFN);
        Files.writeString(absDMFile.toPath(), this.absModel.toString());

        System.out.println("Writing Abstract Translated Alloy Model to " + absAlloyFN);
        Files.writeString(absAMFile.toPath(), this.absAlloy.toString());

        System.out.println("Writing Query Model to " + queryModelFN);
        Files.writeString(queryFile.toPath(), this.queryModel.toString());

        System.out.println(
                "Writing Concrete Translated Alloy Model (with CE Validation) to " + concAlloyFN);
        Files.writeString(concAlloyFile.toPath(), this.concreteAlloy.toString());
    }

    public String getQueryModelString() {
        return this.queryModel.toString();
    }
}
