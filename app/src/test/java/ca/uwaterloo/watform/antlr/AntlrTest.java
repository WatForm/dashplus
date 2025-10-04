package ca.uwaterloo.watform.antlr;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import ca.uwaterloo.watform.alloyinterface.AlloyUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UnexpectedParsePassException extends RuntimeException {
	public UnexpectedParsePassException() {
		super("Jar didn't parse, but Antlr parsed. ");
	}
}

public class AntlrTest {
	private static boolean stopOnFirstFail;

	private List<Path> jarPassedAntlrPassed = new ArrayList<>();
	private List<Path> jarPassedAntlrFailed = new ArrayList<>();
	private List<Path> jarFailedAntlrPassed = new ArrayList<>();
	private List<Path> jarFailedAntlrFailed = new ArrayList<>();

	@BeforeAll
	public static void setup() {
		String prop = System.getProperty("stopOnFirstFail", "True");
		stopOnFirstFail = Boolean.parseBoolean(prop);
		if (stopOnFirstFail) {
			System.out.println("stopOnFirstFail: true");
		} else {
			System.out.println("stopOnFirstFail: false");
		}
	}

	private void printList(String title, List<Path> list) {
		int max = 10;
		System.out.println(title + " (" + list.size() + "):");
		if (list.isEmpty()) {
			System.out.println("  [none]");
		} else {
			int count = 0;
			for (Path p : list) {
				System.out.println("  " + p.toString());
				count++;
				if (count >= max) {
					System.out.println("  ...and " + (list.size()-max) +" more" + "...");
					break;
				}
			}
		}
		System.out.println();
	}

	private void printResults() {
		System.out.println("=== Parsing Results ===");
		printList("Jar Passed, ANTLR Passed", jarPassedAntlrPassed);
		printList("Jar Passed, ANTLR Failed", jarPassedAntlrFailed);
		printList("Jar Failed, ANTLR Passed (ignored for now)", jarFailedAntlrPassed);
		printList("Jar Failed, ANTLR Failed (can be removed)", jarFailedAntlrFailed);
		System.out.println("=======================");
	}

	private void tryParse(CharStream input, Path filePath) throws ParseCancellationException {
		boolean jarPassed = AlloyUtils.canParse(input.toString());

		BailLexer bailLexer = new BailLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(bailLexer);
		BailParser bailParser = new BailParser(tokens);

		try {
			bailParser.alloyFile();
			if (jarPassed) {
				jarPassedAntlrPassed.add(filePath);
			} else {
				jarFailedAntlrPassed.add(filePath);
				// if (stopOnFirstFail) {
				// throw new UnexpectedParsePassException();
				// }
			}
		} catch (ParseCancellationException e) {
			if (jarPassed) {
				jarPassedAntlrFailed.add(filePath);
				if (stopOnFirstFail) {
					throw e;
				}
			} else {
				jarFailedAntlrFailed.add(filePath);
			}
		}
	}

	// // @Test
	// public void parseCatalyst2021_05_06_10_28_11_watform() throws Exception {
	// Path p = Paths.get("src/test/resources/antlr/catalyst/model-sets/" +
	// "2021-05-06-10-28-11-watform"); try (DirectoryStream<Path> dirStream =
	// Files.newDirectoryStream(p, "*.als")) { for (Path filePath : dirStream) {
	// CharStream input = CharStreams.fromPath(filePath);
	// assertDoesNotThrow(() -> this.tryParse(input, filePath), "Parse
	// failed");
	// }
	// }
	//
	// printResults();
	// }
	//
	// // @Test
	// public void parseCatalyst2021_05_25_13_24_28_jackson() throws Exception {
	// Path p = Paths.get("src/test/resources/antlr/catalyst/model-sets/" +
	// "2021-05-25-13-24-28-jackson"); try (DirectoryStream<Path> dirStream =
	// Files.newDirectoryStream(p, "*.als")) { for (Path filePath : dirStream) {
	// CharStream input = CharStreams.fromPath(filePath);
	// assertDoesNotThrow(() -> this.tryParse(input, filePath), "Parse
	// failed");
	// }
	// }
	// }
	//
	// // @Test
	// public void parseUtil() throws Exception {
	// Path p = Paths.get("src/test/resources/antlr/util");
	// try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(p,
	// "*.als")) { for (Path filePath : dirStream) { CharStream input =
	// CharStreams.fromPath(filePath); assertDoesNotThrow(() ->
	// this.tryParse(input, filePath), "Parse failed");
	// }
	// }
	// }

	@Test
	public void parseCatalystNewest() throws Exception {
		Path p = Paths.get("src/test/resources/antlr/catalyst/");
		try (Stream<Path> stream = Files.walk(p)) {
			stream
					.filter(Files::isRegularFile)
					.filter(path -> path.toString().endsWith(".als")) // filter .als files
					.forEach(
							filePath -> {
								try {
									CharStream input = CharStreams.fromPath(filePath);
									assertDoesNotThrow(() -> this.tryParse(input, filePath), "Parse failed");
								} catch (IOException e) {
									throw new RuntimeException("Failed to read file: " + filePath, e);
								}
							});
		} finally {
			printResults();
		}
	}
}
