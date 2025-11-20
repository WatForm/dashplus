package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.dashast.DashFile;
import ca.uwaterloo.watform.dashmodel.DashModelInitialize;
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
            // String input = Files.readString(inPath);

            String fileName = inPath.getFileName().toString();
            if (!fileName.endsWith(".dsh")) {
                System.out.println("First path must be a .dsh file");
                throw new IOException();
            }
            if (!Files.isDirectory(outPath)) {
                System.out.println("Second path must be a directory/folder");
                throw new IOException();
            }
            String moduleName = fileName.substring(0, fileName.lastIndexOf("."));

            // System.out.println(input);

            DashFile df = ParserUtil.parseDash(inPath);
            // we don't know what this is or why it fails
            DashModelInitialize dm = new DashModelInitialize(df);

            TLAPlusModel model = new TLAPlusModel(moduleName, Util.getInit(), Util.getNext());

            Temp test1 = Temp.testOne();

            States.translateStates(test1, model.module);
            Events.translateEvents(test1, model.module);
            Transitions.translateTransitions(test1, model.module);
            Util.makeInit(model);
            Util.makeNext(model);

            Files.writeString(outPath.resolve(moduleName + ".tla"), model.moduleCode());
            Files.writeString(outPath.resolve(moduleName + ".cfg"), model.configCode());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
