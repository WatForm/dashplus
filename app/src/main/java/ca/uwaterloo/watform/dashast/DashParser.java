/*
	dashParseToModel calls dashParser, which calls dashParseFromCharStream
*/
package ca.uwaterloo.watform.dashast;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.parser.*;
import ca.uwaterloo.watform.utils.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class DashParser extends AlloyParser {

  // Use this function for parsing both dash and alloy files
  public static DashModel dashParseToModel(String fullFileName) {
    // Path filePath = Paths.get(fullFileName);
    DashModel model = null;
    try {
      // this could be errors in the Reporter
      // but continues on after these errors
      DashFile file = dashParse(fullFileName);
      if (null == file) {
        // This happens when
        // 1) Parser.parse found UserError
        // 2) Reporter.INSTANCE.exitFunction has been swapped for testing
        // We don't want to continue
        return null;
      }
      model = new DashModel((DashFile) file);

    } catch (UserOrImplError error) {
      // this is an error from the phase of putting
      // the AST into the AlloyModel or DashModel
      Reporter.INSTANCE.addError(error);
    }
    // could have errors that are caught at parser
    // so can continue parsing
    Reporter.INSTANCE.exitIfHasErrors();
    return model;
  }

  public static DashFile dashParse(String fullFileName) {
    Path filePath = Paths.get(fullFileName);
    if (!fullFileName.endsWith(".dsh")) {
      throw new Reporter.ErrorUser("File extension must be .dsh");
    }
    CharStream input = null;
    try {
      input = CharStreams.fromPath(filePath);
    } catch (IOException ioException) {
      throw new Reporter.ErrorUser("Input file cannot be found: " + fullFileName);
    }
    return dashParseFromCharStream(input, fullFileName);
  }

  public static DashFile dashParseFromCharStream(CharStream input, String fullFileName) {
    BailLexer lexer = new BailLexer(input);
    assert (fullFileName.endsWith(".dsh"));
    lexer.dashMode = true;
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    BailParser parser = new BailParser(tokens);
    // Remove default console error listener
    parser.removeErrorListeners();

    // Add custom one
    parser.addErrorListener(
        new BaseErrorListener() {
          @Override
          public void syntaxError(
              Recognizer<?, ?> recognizer,
              Object offendingSymbol,
              int line,
              int charPositionInLine,
              String msg,
              RecognitionException e) {

            Token t = (Token) offendingSymbol;

            System.err.println("Error at line " + line + ":" + charPositionInLine);
            System.err.println("Found token: " + t.getText());
            System.err.println("Expected: " + msg);
            Reporter.INSTANCE.addError(new UserError("Parsing stopped."));
          }
        });

    ParseTree antlrAST = parser.dashFile();
    DashFileParseVis dfpv = new DashFileParseVis(fullFileName);
    DashFile dashFile = dfpv.visit(antlrAST);
    return dashFile;
  }
}
