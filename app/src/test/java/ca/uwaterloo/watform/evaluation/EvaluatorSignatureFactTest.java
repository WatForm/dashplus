package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.parser.AlloyParser.alloyParse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.expr.binary.AlloyDotExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyQuantificationExpr;
import ca.uwaterloo.watform.alloyast.expr.unary.AlloyQtExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.alloymodel.Qname;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class EvaluatorSignatureFactTest {
  @Test
  public void inheritedFieldsInSignatureFactsAreJoinedWithTheImplicitReceiver() {
    Path path = Path.of("src/test/resources/evaluation/inherited-field-fact.als");
    AlloyModel model = new AlloyModel(alloyParse(path.toString()));
    model.resolve();

    AlloyQuantificationExpr rootFact =
        model.allConstraints().stream()
            .filter(AlloyQuantificationExpr.class::isInstance)
            .map(AlloyQuantificationExpr.class::cast)
            .filter(
                quantification ->
                    quantification.decls.stream()
                        .anyMatch(decl -> decl.expr.toString().equals("this/Root")))
            .findFirst()
            .orElseThrow();

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
}
