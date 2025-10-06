package ca.uwaterloo.watform.antlr;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import ca.uwaterloo.watform.alloyinterface.AlloyUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UnexpectedParsePassException extends RuntimeException {
	public UnexpectedParsePassException() {
		super("Jar didn't parse, but Antlr parsed. ");
	}
}

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AntlrTest {
	private static boolean stopOnFirstFail;
	private static long timeoutMs = 10 * 1000;
	private static int filenamesToPrint = 20;

	private List<Path> jarPassedAntlrPassed = new ArrayList<>();
	private List<Path> jarPassedAntlrFailed = new ArrayList<>();
	private List<Path> jarFailedAntlrPassed = new ArrayList<>();
	private List<Path> jarFailedAntlrFailed = new ArrayList<>();
	private List<Path> timeout = new ArrayList<>();

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
		System.out.println(title + " (" + list.size() + "):");
		if (list.isEmpty()) {
			System.out.println("  [none]");
		} else {
			int count = 0;
			for (Path p : list) {
				System.out.println("  " + p.toString());
				count++;
				if (count >= filenamesToPrint) {
					System.out.println("  ...and " + (list.size() - filenamesToPrint) + " more" + "...");
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
		printList("Timeout Files", timeout);
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
				System.out.println("Successfully parsed " + jarPassedAntlrPassed.size() + " files. ");
			} else {
				jarFailedAntlrPassed.add(filePath);
				// if (stopOnFirstFail) {
				// throw new UnexpectedParsePassException();
				// }
			}
		} catch (ParseCancellationException pe) {
			if (jarPassed) {
				jarPassedAntlrFailed.add(filePath);
				if (stopOnFirstFail) {
					throw pe;
				}
			} else {
				try {
					System.out.println("Both Jar and Antlr failed. ");
					boolean deleted = Files.deleteIfExists(filePath);
					if (deleted) {
						System.out.println("File deleted successfully.");
					} else {
						System.out.println("File did not exist.");
					}
				} catch (IOException e) {
					System.err.println("Failed to delete the file: " + e.getMessage());
				}
				jarFailedAntlrFailed.add(filePath);
			}
		}
	}

	private void tryParseWithTimeout(CharStream input, Path filePath)
			throws ParseCancellationException {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<?> future = executor.submit(
				() -> {
					tryParse(input, filePath);
				});

		try {
			future.get(timeoutMs, TimeUnit.MILLISECONDS);
		} catch (TimeoutException te) {
			System.out.println(
					"Parsing took longer than " + timeoutMs / 1000 + " seconds, file: " + filePath);
			timeout.add(filePath);
			future.cancel(true); // interrupt the parsing thread
		} catch (ExecutionException ee) {
			Throwable cause = ee.getCause();
			if (cause instanceof ParseCancellationException) {
				throw (ParseCancellationException) cause;
			} else {
				throw new RuntimeException(cause);
			}
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		} finally {
			executor.shutdownNow();
		}
	}

	@Test
	@Order(1)
	public void parseCatalystQuickTests() throws Exception {
		Path p = Paths.get("src/test/resources/antlr/catalyst/quick-tests");
		try (Stream<Path> stream = Files.walk(p)) {
			stream
					.filter(Files::isRegularFile)
					.filter(path -> path.toString().endsWith(".als"))
					.forEach(
							filePath -> {
								try {
									CharStream input = CharStreams.fromPath(filePath);
									assertDoesNotThrow(
											() -> this.tryParseWithTimeout(input, filePath), "Parse failed");
								} catch (IOException e) {
									throw new RuntimeException("Failed to read file: " + filePath, e);
								}
							});
		} finally {
			printResults();
		}
	}

	@Test
	@Order(2)
	public void parseCatalystCorpus() throws Exception {
		Path p = Paths.get("src/test/resources/antlr/catalyst/catalyst-corpus");
		try (Stream<Path> stream = Files.walk(p)) {
			stream
					.filter(Files::isRegularFile)
					.filter(path -> path.toString().endsWith(".als"))
					.forEach(
							filePath -> {
								try {
									CharStream input = CharStreams.fromPath(filePath);
									assertDoesNotThrow(
											() -> this.tryParseWithTimeout(input, filePath), "Parse failed");
								} catch (IOException e) {
									throw new RuntimeException("Failed to read file: " + filePath, e);
								}
							});
		} finally {
			printResults();
		}
	}
}
