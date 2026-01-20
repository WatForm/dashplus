package ca.uwaterloo.watform.parsevis;

import static ca.uwaterloo.watform.parser.Parser.*;
import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.test.*;
import ca.uwaterloo.watform.utils.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParseVisTest {
    @Test
    @Order(1)
    @DisplayName("parse Jackson's examples from Software Abstraction book")
    public void parseJackson() throws Exception {
        Path p = Paths.get("src/test/resources/parsevis/jackson");
        new AntlrTestUtil().recurParseDir(p, 5 * 1000, ".als");
    }

    @Test
    @Order(2)
    @DisplayName("parse WatForm alloy files")
    public void parseWatformAls() throws Exception {
        Path p = Paths.get("src/test/resources/parsevis/watformals");
        new AntlrTestUtil().recurParseDir(p, 5 * 1000, ".als");
    }

    @Test
    @Order(3)
    @DisplayName("Alloy builtin util files")
    public void parseUtil() throws Exception {
        Path p = Paths.get("src/test/resources/parsevis/util");
        new AntlrTestUtil().recurParseDir(p, 5 * 1000, ".als");
    }

    // test catalyst corpus with
    // app/src/main/java/ca/uwaterloo/watform/test/Main.java

    @Test
    @Order(4)
    @DisplayName("Parse, create our Alloy AST with parser visitors, and call toString")
    public void parseToStr() throws Exception {
        Path dir = Paths.get("src/test/resources/parsevis/tostr");
        List<Path> paths = recurGetFiles(dir, ".als");
        for (Path filePath : paths) {
            try {

                System.out.println(filePath);
                System.out.println("--- File Content ---");

                String originalStr = Files.readString(filePath);

                System.out.println(originalStr);

                AlloyFile af = assertDoesNotThrow(() -> (parse(filePath)));
                String parsedStr = assertDoesNotThrow(() -> af.toString());

                System.out.println("--- Parse & toString ---");
                System.out.println(parsedStr);
                System.out.println("--------------------");

            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
                System.exit(1);
            } catch (RecognitionException | ParseCancellationException e) {
                System.err.println("Error occurred during parsing: " + e.getMessage());
                System.exit(1);
            } catch (Exception e) {
                System.err.println(
                        "Error occurred during parsing or in parser visitors: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    @Test
    @Order(5)
    @DisplayName(
            "Parse, create our Alloy AST with parser visitors, and call toString "
                    + "to match exactly the input string (cannot include comments)")
    public void parseMatchStr() throws Exception {
        Path dir = Paths.get("src/test/resources/parsevis/tostr/matchstr");
        List<Path> paths = recurGetFiles(dir, ".als");
        for (Path filePath : paths) {
            try {

                System.out.println(filePath);
                String originalStr = Files.readString(filePath);
                AlloyFile af = assertDoesNotThrow(() -> (parse(filePath)));
                String parsedStr = assertDoesNotThrow(() -> af.toString());

                assertEquals(originalStr.replaceAll("\\s", ""), parsedStr.replaceAll("\\s", ""));

            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
                System.exit(1);
            } catch (RecognitionException | ParseCancellationException e) {
                System.err.println("Error occurred during parsing: " + e.getMessage());
                System.exit(1);
            } catch (Exception e) {
                System.err.println(
                        "Error occurred during parsing or in parser visitors: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    @Test
    @Order(6)
    @DisplayName("Antlr grammar parses dash-testing")
    public void parseDashTesting() throws Exception {
        Path p = Paths.get("src/test/resources/dashmodel/");
        new AntlrTestUtil().recurParseDir(p, 5 * 1000, ".dsh");
    }

    @Test
    @Order(7)
    @DisplayName("Parsing bad files should fail")
    public void parseFail() throws Exception {
        List<Path> paths =
                recurGetFiles(Paths.get("src/test/resources/parsevis/parsefail"), ".dsh");
        for (Path path : paths) {
            assertThrows(RuntimeException.class, () -> parse(path));
        }
    }
}
