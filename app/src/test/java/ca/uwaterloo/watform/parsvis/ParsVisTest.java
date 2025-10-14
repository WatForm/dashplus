package ca.uwaterloo.watform.parsvis;

import ca.uwaterloo.watform.parser.*;
import ca.uwaterloo.watform.utils.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import ca.uwaterloo.watform.alloyast.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParsVisTest {

	@Test
	@Order(1)
	public void parseSimple() throws Exception {
		Path dir = Paths.get("src/test/resources/parsvis/simple");
		List<Path> paths = ParserUtil.recurGetFiles(dir, ".als");
		for (Path filePath : paths) {
			try {

				System.out.println(filePath);
				System.out.println("--- File Content ---");

				String originalStr = Files.readString(filePath);

				System.out.println(originalStr);

				AlloyFile af = ParserUtil.parse(filePath);
				String parsedStr = af.toString();

				System.out.println("--- Parse & toString ---");
				System.out.println(af.toString());
				System.out.println("--------------------");

			} catch (IOException e) {
				System.err.println("Error reading file: " + e.getMessage());
			} catch (RecognitionException | ParseCancellationException e) {
				System.err.println("Error occurred during parsing: " + e.getMessage());
			} catch (Exception e) {
				System.err.println(
						"Error occurred during parsing or in parser visitors: " + e.getMessage());
			}
		}
	}
}
