package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.utils.ParserUtil.*;

import ca.uwaterloo.watform.dashast.DashFile;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlamodel.*;
import java.io.IOException;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Hello from the dashtotlaplus class");

        // TODO implement pico-cli

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

            DashFile df = (DashFile) parse(inPath);
            System.out.println("Dash File generated");
            DashModel dm = new DashModel(df);

            TlaModel model = DashToTla.translate(dm, moduleName, true);
            Files.writeString(outPath.resolve(moduleName + ".tla"), model.moduleCode());
            Files.writeString(outPath.resolve(moduleName + ".cfg"), model.configCode());

            System.out.println("translation complete");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
