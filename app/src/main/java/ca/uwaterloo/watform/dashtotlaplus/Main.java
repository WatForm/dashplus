package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.tlaplusmodel.*;
import java.io.IOException;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Hello from the dashtotlaplus class");

        if (args.length != 2) {
            System.out.println("Path to input file.dsh and output file.tla are needed");
            return;
        }

        try {
            Path inPath = Paths.get(args[0]);
            Path outPath = Paths.get(args[1]);
            String input = Files.readString(inPath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        TLAPlusModule module = new TLAPlusModule("test");

        TLAPlusSTL fs = new TLAPlusSTL(TLAPlusSTL.LIBRARIES.STL_FiniteSets);
        TLAPlusConstant c1 = new TLAPlusConstant("c1");
        TLAPlusConstant c2 = new TLAPlusConstant("c2");
        TLAPlusVariable v1 = new TLAPlusVariable("v1");
        TLAPlusVariable v2 = new TLAPlusVariable("v2");
        module.addConstant(c1);
        module.addConstant(c2);
        module.addVariable(v1);
        module.addVariable(v2);
        module.addSTL(fs);

        System.out.println(module.code());
    }
}
