package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.parser.AlloyParser.parseExpr;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyexprvisitor.ReplaceExprVis;
import org.junit.jupiter.api.Test;

public class ReplaceExprVisTest {
  private static boolean isFieldName(AlloyExpr expr) {
    return expr instanceof AlloyQnameExpr name && name.getName().equals("f");
  }

  @Test
  public void existingConstructorStillReplacesReferencesUnderLocalBindings() {
    ReplaceExprVis visitor =
        new ReplaceExprVis(ReplaceExprVisTest::isFieldName, e -> parseExpr("g"));
    assertEquals(parseExpr("all f: g | some g"), visitor.visit(parseExpr("all f: f | some f")));
    assertEquals(parseExpr("let f = g | some g"), visitor.visit(parseExpr("let f = f | some f")));
    assertEquals(parseExpr("{f: g | some g}"), visitor.visit(parseExpr("{f: f | some f}")));
  }

  @Test
  public void respectingBindingsPreservesLocalsAndRestoresTheOuterScope() {
    ReplaceExprVis visitor =
        new ReplaceExprVis(ReplaceExprVisTest::isFieldName, e -> parseExpr("g"), true);
    assertEquals(
        parseExpr("(all f: g | some f) and some g"),
        visitor.visit(parseExpr("(all f: f | some f) and some f")));
    assertEquals(
        parseExpr("(let f = g | some f) and some g"),
        visitor.visit(parseExpr("(let f = f | some f) and some f")));
    assertEquals(
        parseExpr("some {f: g | some f} and some g"),
        visitor.visit(parseExpr("some {f: f | some f} and some f")));
  }

  @Test
  public void respectingBindingsStillAllowsReplacingWholeExpressions() {
    ReplaceExprVis visitor = new ReplaceExprVis(e -> true, e -> parseExpr("none"), true);
    assertEquals(parseExpr("none"), visitor.visit(parseExpr("all f: f | some f")));
    assertEquals(parseExpr("none"), visitor.visit(parseExpr("let f = f | some f")));
    assertEquals(parseExpr("none"), visitor.visit(parseExpr("{f: f | some f}")));
  }
}
