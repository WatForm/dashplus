package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.parser.AlloyParser.alloyParse;
import static ca.uwaterloo.watform.parser.AlloyParser.alloyParseFromCharStream;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyDotExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyQuantificationExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyQtExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.parser.CompUtil;
import edu.mit.csail.sdg.translator.A4Options;
import edu.mit.csail.sdg.translator.TranslateAlloyToKodkod;
import java.nio.file.Path;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.Test;

public class SignatureFactTest {
  @Test
  public void inheritedFieldsInSignatureFactsAreJoinedWithTheImplicitReceiver() {
    Path path = Path.of("src/test/resources/alloymodel/inherited-field-fact.als");
    AlloyModel model = new AlloyModel(alloyParse(path.toString()));
    model.resolve();

    AlloyQuantificationExpr rootFact =
        filterBy(
                extractItemsOfClass(model.allConstraints(), AlloyQuantificationExpr.class),
                quantification ->
                    containsMatch(
                        quantification.decls, decl -> decl.expr.toString().equals("this/Root")))
            .getFirst();

    AlloyQtExpr noParent = assertInstanceOf(AlloyQtExpr.class, rootFact.body);
    assertEquals(AlloyQtEnum.NO, noParent.qt);
    AlloyDotExpr access = assertInstanceOf(AlloyDotExpr.class, noParent.sub);
    AlloyQnameExpr receiver = assertInstanceOf(AlloyQnameExpr.class, access.left);
    AlloyQnameExpr field = assertInstanceOf(AlloyQnameExpr.class, access.right);

    assertEquals("this_var", receiver.getName());
    Qname fieldQname = Qname.fieldExprQname(field);
    assertEquals("this", fieldQname.nameSpace);
    assertEquals("Dir", fieldQname.sigParent);
    assertEquals("parent", fieldQname.name);
    assertTrue(field.kind == ca.uwaterloo.watform.alloyast.AlloyStrings.Kind.FIELD);
  }

  @Test
  public void inheritedFieldsDoNotDependOnDeclarationOrder() throws Exception {
    assertAnalyzerInstancesSatisfyConstraints(
        """
        one sig Root extends Dir {} { no parent }
        sig Dir { parent: lone Dir }
        one sig Other extends Dir {}
        fact { some Other.parent }
        """,
        false);
  }

  @Test
  public void laterGrandparentsAndSubsetParentsAreVisible() throws Exception {
    for (String relation : new String[] {"extends", "in"}) {
      assertAnalyzerInstancesSatisfyConstraints(
          "one sig Root "
              + relation
              + " Mid {} { no parent }\n"
              + "sig Mid extends Dir {}\n"
              + "sig Dir { parent: lone Dir }\n"
              + "fact { some (Dir - Root).parent }\n",
          false);
    }
  }

  @Test
  public void quantifiedVariablesShadowInheritedFields() throws Exception {
    assertScopedFact("all parent: Dir | some parent");
  }

  @Test
  public void letVariablesShadowInheritedFields() throws Exception {
    assertScopedFact("let parent = Dir | some parent");
  }

  @Test
  public void comprehensionsRespectShadowingAndRestoreTheOuterScope() throws Exception {
    assertScopedFact("some { parent: Dir | some parent } and no parent");
  }

  @Test
  public void declarationDomainsAndLetValuesUseTheEnclosingScope() throws Exception {
    assertScopedFact("(no parent: parent | some parent) and no parent");
    assertScopedFact("(let parent = parent | no parent) and no parent");
  }

  @Test
  public void laterBindingsSeeEarlierBindingsAndNestedScopesAreRestored() throws Exception {
    assertScopedFact("all parent: Dir, child: parent | some child");
    assertScopedFact("let parent = Dir, child = parent | some child");
    assertScopedFact("all parent: Dir | (let parent = none | no parent) and some parent");
    assertScopedFact("(all parent: Dir | some parent) and no parent");
    assertScopedFact("(let parent = Dir | some parent) and no parent");
  }

  @Test
  public void atFieldStillRefersToTheWholeRelationWhileANameIsShadowed() throws Exception {
    assertAnalyzerInstancesSatisfyConstraints(
        """
        one sig Root extends Dir {} {
          all parent: Dir | some parent and some @parent
          no parent
        }
        sig Dir { parent: lone Dir }
        fact { some (Dir - Root).parent }
        """,
        false);
  }

  @Test
  public void copiesKeepPendingSignatureConstraints() throws Exception {
    assertAnalyzerInstancesSatisfyConstraints(
        """
        one sig Root extends Dir {} { no parent }
        sig Dir { parent: lone Dir }
        fact { some (Dir - Root).parent }
        """,
        true);
  }

  private static void assertScopedFact(String fact) throws Exception {
    assertAnalyzerInstancesSatisfyConstraints(
        "sig Dir { parent: lone Dir }\n"
            + "one sig Root extends Dir {} { "
            + fact
            + " }\n"
            + "fact { no Root.parent }\n",
        false);
  }

  private static void assertAnalyzerInstancesSatisfyConstraints(String source, boolean copy)
      throws Exception {
    source += "run {} for 3\n";
    var analyzer = CompUtil.parseEverything_fromString(A4Reporter.NOP, source);
    var solution =
        TranslateAlloyToKodkod.execute_command(
            A4Reporter.NOP,
            analyzer.getAllReachableSigs(),
            analyzer.getAllCommands().get(0),
            new A4Options());
    assertTrue(solution.satisfiable(), "Regression example must have an Alloy instance");
    AlloyModel model =
        new AlloyModel(
            alloyParseFromCharStream(
                CharStreams.fromString(source), "signature-fact-regression.als"));
    if (copy) model = model.copy();
    model.resolve();
    assertTrue(
        containsMatch(
            extractItemsOfClass(model.allConstraints(), AlloyQuantificationExpr.class),
            q -> containsMatch(q.decls, d -> d.expr.toString().equals("this/Root"))),
        "Root signature constraints must survive deferred desugaring and copying");
    // Valid Analyzer instances must remain valid after translating the signature facts.
    for (int i = 0; i < 5 && solution.satisfiable(); i++, solution = solution.next()) {
      for (var constraint : model.allConstraints()) {
        var translated = CompUtil.parseOneExpression_fromString(analyzer, constraint.toString());
        assertEquals(Boolean.TRUE, solution.eval(translated), constraint.toString());
      }
    }
  }
}
