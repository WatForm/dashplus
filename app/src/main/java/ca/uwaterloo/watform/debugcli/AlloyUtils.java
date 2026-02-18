package ca.uwaterloo.watform.debugcli;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class AlloyUtils {
    public static void writeToFile(String contents, File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(contents);
        writer.close();
    }

    public static String readFromFile(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }
}
