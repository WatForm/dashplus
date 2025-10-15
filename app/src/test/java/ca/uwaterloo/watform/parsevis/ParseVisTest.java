package ca.uwaterloo.watform.parsevis;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyNumCardinalityExpr;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyFactPara;
import ca.uwaterloo.watform.parser.*;
import ca.uwaterloo.watform.utils.*;
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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParseVisTest {

	@Test
	@Order(1)
	public void parseToStr() throws Exception {
		Path dir = Paths.get("src/test/resources/parsevis/tostr");
		List<Path> paths = ParserUtil.recurGetFiles(dir, ".als");
		for (Path filePath : paths) {
			try {

				System.out.println(filePath);
				System.out.println("--- File Content ---");

				String originalStr = Files.readString(filePath);

				System.out.println(originalStr);

				AlloyFile af = assertDoesNotThrow(() -> (ParserUtil.parse(filePath)));
				String parsedStr = assertDoesNotThrow(() -> af.toString());

				System.out.println("--- Parse & toString ---");
				System.out.println(parsedStr);
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

	@Test
	@Order(2)
	public void parseMatchStr() throws Exception {
		Path dir = Paths.get("src/test/resources/parsevis/tostr/matchstr");
		List<Path> paths = ParserUtil.recurGetFiles(dir, ".als");
		for (Path filePath : paths) {
			try {

				System.out.println(filePath);
				String originalStr = Files.readString(filePath);
				AlloyFile af = assertDoesNotThrow(() -> (ParserUtil.parse(filePath)));
				String parsedStr = assertDoesNotThrow(() -> af.toString());

				assertEquals(originalStr.replaceAll("\\s", ""), parsedStr.replaceAll("\\s", ""));

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

	@Test
	@Order(3)
	public void parsePos() throws Exception {
		Path dir = Paths.get("src/test/resources/parsevis/pos");
		List<Path> paths = ParserUtil.recurGetFiles(dir, ".als");
		for (Path filePath : paths) {
			try {

				System.out.println(filePath);
				System.out.println("--- File Content ---");

				String originalStr = Files.readString(filePath);

				System.out.println(originalStr);

				AlloyFile af = assertDoesNotThrow(() -> (ParserUtil.parse(filePath)));

				assertEquals(af.getPos(), new Pos(1, 0, 26, 1));

				AlloyFactPara fact = (AlloyFactPara) af.paragraphs.get(0);
				assertEquals(fact.getPos(), new Pos(1, 0, 26, 1));

				AlloyBlock block = fact.block;
				assertEquals(block.getPos(), new Pos(1, 5, 26, 1));

				AlloyAndExpr and = (AlloyAndExpr) block.exprs.get(0);
				assertEquals(and.getPos(), new Pos(2, 1, 2, 8));
				assertEquals(and.left.getPos(), new Pos(2, 1, 2, 2));
				assertEquals(and.right.getPos(), new Pos(2, 7, 2, 8));

				AlloyNumCardinalityExpr card = (AlloyNumCardinalityExpr) block.exprs.get(1);
				assertEquals(card.getPos(), new Pos(3, 1, 3, 3));
				assertEquals(card.sub.getPos(), new Pos(3, 2, 3, 3));

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
