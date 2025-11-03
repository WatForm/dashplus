package ca.uwaterloo.watform.utils;

import antlr.generated.DashBaseVisitor;
import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.AlloyFileParseVis;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.antlr.*;
import ca.uwaterloo.watform.dashast.DashFile;
import ca.uwaterloo.watform.dashast.DashFileParseVis;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public final class ParserUtil {
    public static List<Path> recurGetFiles(Path dir, String filter) {
        List<Path> filePaths = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(dir)) {
            stream.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(filter))
                    .forEach(
                            filePath -> {
                                filePaths.add(filePath);
                            });
        } catch (IOException e) {
            System.err.println("Dir not found");
        }
        return filePaths;
    }

    public static AlloyFile parse(Path filePath) throws IOException {
        if (!filePath.getFileName().toString().endsWith(".als")
                && !filePath.getFileName().toString().endsWith(".dsh")) {
            throw new Reporter.ErrorUser("File extension must be .dsh or .als");
        }
        CharStream input = CharStreams.fromPath(filePath);
        BailLexer lexer = new BailLexer(input);
        if (filePath.getFileName().toString().endsWith(".dsh")) {
            lexer.dashMode = true;
        }
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BailParser parser = new BailParser(tokens);
        if (filePath.getFileName().toString().endsWith(".als")) {
            ParseTree antlrAST = parser.alloyFile();
            AlloyFileParseVis afpv = new AlloyFileParseVis();
            AlloyFile alloyFile = afpv.visit(antlrAST);
            Reporter.INSTANCE.exitIfHasErrors();
            alloyFile.filename = filePath.toString();
            return alloyFile;
        } else {
            ParseTree antlrAST = parser.dashFile();
            DashFileParseVis dfpv = new DashFileParseVis();
            DashFile dashFile = dfpv.visit(antlrAST);
            Reporter.INSTANCE.exitIfHasErrors();
            dashFile.filename = filePath.toString();
            return dashFile;
        }
    }

    public static <C extends ParseTree, T> List<T> visitAll(
            List<C> contexts, DashBaseVisitor<?> visitor, Class<T> targetType) {
        if (contexts == null || contexts.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return contexts.stream()
                    .map(visitor::visit)
                    .map(targetType::cast)
                    .collect(Collectors.toList());
        } catch (ClassCastException e) {
            throw ImplementationError.failedCast(
                    "Failed to cast an item to " + targetType.getSimpleName() + " in visitAll.");
        }
    }
}
