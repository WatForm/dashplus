package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashast.DashFile;
import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.tlaplusmodel.*;
import ca.uwaterloo.watform.utils.ParserUtil;
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

            // System.out.println(input);

            DashFile df = ParserUtil.parseDash(inPath);
            // we don't know what this is or why it fails
            // DashModelInitialize dm = new DashModelInitialize(df);

        } catch (IOException e) {
            e.printStackTrace();
        }

        TLAPlusModule module = new TLAPlusModule("test");

        Temp test1 = Temp.testOne();

        States.translateStates(test1, module);

        System.out.println(module.code());
    }
}
