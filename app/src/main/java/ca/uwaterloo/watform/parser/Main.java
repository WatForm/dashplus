package ca.uwaterloo.watform.parser;

import antlr.generated.AlloyLexer;
import antlr.generated.AlloyParser;
import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.AlloyFileParsVis;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.*;

public class Main {
	public static void main(String[] args) throws Exception {
        // if (args.length != 1) {
        // 	System.err.println("Usage: ./gradle run --args \"filename\"");
        // 	System.exit(1);
        // }
        //
        // String filePath = args[0];

        Path filePath;

        if(args.length == 1) {
            filePath = Paths.get(args[0]);
        } else {
            filePath = Paths.get("src/test/resources/simpleFact.als");
        }

        try {
            String inputContent =
                new String(Files.readAllBytes(filePath));
            CharStream input = CharStreams.fromString(inputContent);
            AlloyLexer lexer = new AlloyLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            AlloyParser parser = new AlloyParser(tokens);
            ParseTree antlrAST = parser.alloyFile();

            AlloyFileParsVis afpv = new AlloyFileParsVis();
            AlloyFile af = afpv.visit(antlrAST);
			af.filename = filePath.toString();
        } catch(IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch(RecognitionException e) {
            System.err.println("Parsing error: " + e.getMessage());
        }
    }
}
