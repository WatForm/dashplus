package ca.uwaterloo.watform.parser;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.utils.*;
import java.io.IOException;
import java.nio.file.Paths;
import org.antlr.v4.runtime.*;
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
			AlloyFile af = ParserUtil.parse(Paths.get(filePath));
			System.out.println(af.getPos());

		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
		} catch (RecognitionException | ParseCancellationException e) {
			System.err.println("Error occurred during parsing: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error occurred during parsing or in parser visitors: " + e.getMessage());
		}
	}
}
