package org.dashToAlloy;

import antlr.generated.AlloyLexer; // ignore the lsp error
import antlr.generated.AlloyParser; // ignore the lsp error
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.*;

public class Main {
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage: ./gradle run --args \"filename\"");
			System.exit(1);
		}

		String filePath = args[0];

		try {
			String inputContent = new String(Files.readAllBytes(Paths.get(filePath)));
			CharStream input = CharStreams.fromString(inputContent);
			AlloyLexer lexer = new AlloyLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			AlloyParser parser = new AlloyParser(tokens);
			ParseTree tree = parser.alloyFile();

			System.out.println(tree.toStringTree(parser));
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
		} catch (RecognitionException e) {
			System.err.println("Parsing error: " + e.getMessage());
		}
	}
}

