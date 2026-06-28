package ca.uwaterloo.watform.parser;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import antlr.generated.DashBaseVisitor;
import antlr.generated.DashParser;
import ca.uwaterloo.watform.alloyast.AlloyCtorError;
import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.AlloyFileParseVis;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParseVis;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdDeclParseVis;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.dashast.DashFile;
import ca.uwaterloo.watform.dashast.DashFileParseVis;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.utils.*;
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

public class Parser {
    public static List<Path> recurGetFiles(Path dir, String filter) {
        List<Path> filePaths = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(dir)) {
            stream.filter(Files::isRegularFile)
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

    /**
     * This method will catch AlloyCtorError and store them in Reporter
     *
     * @param filePath
     * @return
     */
    public static AlloyFile parse(Path filePath) {
        if (!filePath.getFileName().toString().endsWith(".als")
                && !filePath.getFileName().toString().endsWith(".dsh")) {
            throw new Reporter.ErrorUser("File extension must be .dsh or .als");
        }
        CharStream input = null;
        try {
            input = CharStreams.fromPath(filePath);
        } catch (IOException ioException) {
            throw new Reporter.ErrorUser("Input file cannot be found. ");
        }
        BailLexer lexer = new BailLexer(input);
        if (filePath.getFileName().toString().endsWith(".dsh")) {
            lexer.dashMode = true;
        }
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

        if (filePath.getFileName().toString().endsWith(".als")) {
            ParseTree antlrAST = parser.alloyFile();
            Reporter.INSTANCE.exitIfHasErrors();
            AlloyFileParseVis afpv = new AlloyFileParseVis(filePath);
            AlloyFile alloyFile = null;
            alloyFile = afpv.visit(antlrAST);
            alloyFile.filename = filePath.toString();
            return alloyFile;
        } else {
            ParseTree antlrAST = parser.dashFile();
            DashFileParseVis dfpv = new DashFileParseVis(filePath);
            DashFile dashFile = null;
            dashFile = dfpv.visit(antlrAST);
            dashFile.filename = filePath.toString();
            return dashFile;
        }
    }

    // Use this function for parsing both dash and alloy files
    public static AlloyModel parseToModel(Path filePath) {
        AlloyModel model = null;
        try {
            // this could be errors in the Reporter
            // but continues on after these errors
            AlloyFile file = Parser.parse(filePath);
            if (null == file) {
                // This happens when
                // 1) Parser.parse found UserError
                // 2) Reporter.INSTANCE.exitFunction has been swapped for testing
                // We don't want to continue
                return null;
            }
            if (file instanceof DashFile) {
                // System.out.println(file);
                model = new DashModel((DashFile) file);
            } else {
                model = new AlloyModel(file);
            }
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

    public static <C extends ParseTree, T> List<T> visitAll(
            List<C> contexts, DashBaseVisitor<?> visitor, Class<T> targetType) {
        if (contexts == null || contexts.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return contexts.stream()
                    .map(visitor::visit)
                    .map(targetType::cast)
                    .collect(Collectors.toList());
        } catch (ClassCastException e) {
            throw ImplementationError.failedCast(
                    "Failed to cast an item to " + targetType.getSimpleName() + " in visitAll.");
        }
    }

    public static AlloyCmdPara.CommandDecl parseCmdDecl(String s) {
        CharStream input = CharStreams.fromString(s);
        BailLexer lexer = new BailLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BailParser parser = new BailParser(tokens);
        ParseTree antlrAST = parser.commandDecl();
        AlloyCmdDeclParseVis afpv = new AlloyCmdDeclParseVis();
        AlloyCmdPara.CommandDecl cmd = null;
        try {
            cmd = afpv.visitCommandDecl((DashParser.CommandDeclContext) antlrAST);
        } catch (AlloyCtorError alloyCtorError) {
            Reporter.INSTANCE.addError(alloyCtorError);
        }
        Reporter.INSTANCE.exitIfHasErrors();
        return cmd;
    }

    public static AlloyExpr parseExpr(String s) {
        CharStream input = CharStreams.fromString(s);
        BailLexer lexer = new BailLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BailParser parser = new BailParser(tokens);
        ParseTree antlrAST = parser.expr1();
        AlloyExprParseVis afpv = new AlloyExprParseVis();
        AlloyExpr expr = null;
        try {
            expr = afpv.visit((DashParser.Expr1Context) antlrAST);
        } catch (AlloyCtorError alloyCtorError) {
            Reporter.INSTANCE.addError(alloyCtorError);
        }
        Reporter.INSTANCE.exitIfHasErrors();
        return expr;
    }

    public static AlloyDecl parseDecl(String s) {
        CharStream input = CharStreams.fromString(s);
        BailLexer lexer = new BailLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BailParser parser = new BailParser(tokens);
        ParseTree antlrAST = parser.decl();
        AlloyExprParseVis afpv = new AlloyExprParseVis();
        AlloyDecl decl = null;
        try {
            decl = afpv.visitDecl((DashParser.DeclContext) antlrAST);
        } catch (AlloyCtorError alloyCtorError) {
            Reporter.INSTANCE.addError(alloyCtorError);
        }
        Reporter.INSTANCE.exitIfHasErrors();
        return decl;
    }
}
