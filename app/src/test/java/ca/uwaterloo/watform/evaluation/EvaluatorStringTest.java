package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.evaluation.ThreeVal.FALSE;
import static ca.uwaterloo.watform.evaluation.ThreeVal.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import ca.uwaterloo.watform.alloyast.expr.binary.AlloyCmpExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyEqualsExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyStrLiteralExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyStringExpr;
import ca.uwaterloo.watform.alloyinterface.Instance;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.alloymodel.Qname;
import org.junit.jupiter.api.Test;

public class EvaluatorStringTest {
  private static final String LITERAL = "\"Node$1\"";

  private static EvaluationTable table() {
    String xml =
        "<alloy><instance bitwidth=\"4\" maxseq=\"4\">"
            + "<sig label=\"String\" ID=\"3\" parentID=\"2\" builtin=\"yes\">"
            + "<atom label=\"&quot;Node$1&quot;\"/></sig>"
            + "<sig label=\"this/Node\" ID=\"4\" parentID=\"2\">"
            + "<atom label=\"Node$1\"/></sig>"
            + "<sig label=\"univ\" ID=\"2\" builtin=\"yes\"/>"
            + "</instance></alloy>";
    var model = new AlloyModel("string-test.als");
    model.resolve();
    return new EvaluationTable(new Instance(xml), model);
  }

  @Test
  public void labelCategoriesAreNeverEqual() {
    Atom string = new StringAtom("Node$1");
    Atom generic = new GenericLabelAtom("Node$1");

    assertEquals(FALSE, Atom.threeEqual(string, generic));
    assertFalse(Atom.structurallyIdentical(string, generic));
    assertEquals(TRUE, Atom.threeEqual(string, new StringAtom("Node$1")));
  }

  @Test
  public void atomFactoryUsesTheExplicitStringFlag() {
    AtomFactory factory = new AtomFactory(-8, 7);

    assertInstanceOf(StringAtom.class, factory.createAtom("same label", true));
    assertInstanceOf(GenericLabelAtom.class, factory.createAtom("same label", false));
    assertInstanceOf(StringAtom.class, factory.createAtom("1", true));
    assertInstanceOf(IntegerAtom.class, factory.createAtom("1", false));
  }

  @Test
  public void instanceAtomsAreClassifiedUsingTheStringSignature() {
    EvaluationTable table = table();

    assertInstanceOf(StringAtom.class, table.getStringSet().getScalar());
    assertInstanceOf(
        GenericLabelAtom.class,
        table.get(Qname.nameSpaceQname("this", "Node")).orElseThrow().getScalar());
  }

  @Test
  public void literalsCompareAndBelongToTheStringSet() {
    FormulaEvaluator evaluator = new FormulaEvaluator(table(), false);
    AlloyStrLiteralExpr literal = new AlloyStrLiteralExpr(LITERAL);

    assertEquals(
        TRUE,
        new AlloyEqualsExpr(literal, new AlloyStrLiteralExpr(LITERAL)).accept(evaluator));
    assertEquals(
        TRUE,
        new AlloyCmpExpr(literal, false, AlloyCmpExpr.Comp.IN, new AlloyStringExpr())
            .accept(evaluator));
  }

  @Test
  public void dollarSignsInsideStringsAreNotRewrittenAsAtomSeparators() {
    EvaluationTable table = table();
    Atom string = table.getStringSet().getScalar();
    Atom generic = table.get(Qname.nameSpaceQname("this", "Node")).orElseThrow().getScalar();

    assertEquals(LITERAL, string.toString());
    assertEquals("Nodeʃ1", generic.toString());
    assertFalse(Atom.structurallyIdentical(string, generic));
  }
}
