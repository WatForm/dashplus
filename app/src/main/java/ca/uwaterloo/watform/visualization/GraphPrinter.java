package ca.uwaterloo.watform.visualization;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GraphPrinter {

    private final StringBuilder graphBuilder = new StringBuilder();
    private Path outputDir;

    public GraphPrinter() {
        this(null);
    }

    public GraphPrinter(Path outputDir) {
        this.outputDir = outputDir;
    }

    public void setOutputDir(Path outputDir) {
        this.outputDir = outputDir;
    }

    public void addln(String line) {
        graphBuilder.append(line).append("\n");
    }

    public void add(String text) {
        graphBuilder.append(text);
    }

    public void addnewln() {
        graphBuilder.append("\n");
    }

    public void print(String prefix) {
        try {
            if ("control_states".equals(prefix)) {
                graphBuilder.insert(0, "compound=true \n");
            }
            graphBuilder.insert(0, "digraph G {").append("\n");
            graphBuilder.append("}").append("\n");

            Path dotPath = resolvePath(prefix + ".dot");
            writeTextToFile(dotPath, graphBuilder.toString());

            Path pngPath = resolvePath(prefix + ".png");
            executeCommand(List.of("dot", "-Tpng", dotPath.toString(), "-o", pngPath.toString()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void generateJson(String prefix) {
        try {
            Path dotPath = resolvePath(prefix + ".dot");
            Path jsonPath = resolvePath(prefix + ".json");
            executeCommand(List.of("dot", "-Tjson", dotPath.toString(), "-o", jsonPath.toString()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void executeCommand(List<String> command) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Command execution failed with exit code: " + exitCode);
        }
    }

    private void writeTextToFile(Path filePath, String text) throws IOException {
        Files.writeString(filePath, text, StandardCharsets.UTF_8);
    }

    private Path resolvePath(String fileName) {
        if (outputDir == null) {
            return Path.of(fileName);
        }
        return outputDir.resolve(fileName);
    }
}
