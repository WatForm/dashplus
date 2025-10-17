package ca.uwaterloo.watform.utils;

import antlr.generated.AlloyBaseVisitor;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.AlloyFileParseVis;
import ca.uwaterloo.watform.antlr.*;
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
			stream
					.filter(Files::isRegularFile)
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

	public static AlloyFile parse(Path filePath) throws Exception {
		CharStream input = CharStreams.fromPath(filePath);
		BailLexer lexer = new BailLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		BailParser parser = new BailParser(tokens);
		ParseTree antlrAST = parser.alloyFile();

		AlloyFileParseVis afpv = new AlloyFileParseVis();
		AlloyFile af = afpv.visit(antlrAST);
		af.filename = filePath.toString();
		return af;
	}

	public static <C extends ParseTree, T> List<T> visitAll(
			List<C> contexts, AlloyBaseVisitor<?> visitor, Class<T> targetType) {
		if (contexts == null || contexts.isEmpty()) {
			return Collections.emptyList();
		}
		try {
			return contexts.stream()
					.map(visitor::visit)
					.map(targetType::cast)
					.collect(Collectors.toList());
		} catch (ClassCastException e) {
			throw CodingError.failedCast(
					"Failed to cast an item to " + targetType.getSimpleName() + " in visitAll.");
		}
	}
}
