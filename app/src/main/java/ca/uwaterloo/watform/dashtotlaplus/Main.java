package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.tlaplusmodel.*;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Hello from the dashtotlaplus class");

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
