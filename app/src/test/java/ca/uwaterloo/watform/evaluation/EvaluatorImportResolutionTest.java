package ca.uwaterloo.watform.evaluation;

import static ca.uwaterloo.watform.parser.AlloyParser.alloyParse;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.alloymodel.Qname;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class EvaluatorImportResolutionTest {
  @Test
  public void repeatedTopLevelImportsUseAlloysGeneratedAliases() throws Exception {
    AlloyModel model = model("repeated-ordering.als");

    assertTrue(model.allSigQnames().contains(Qname.nameSpaceQname("ordering", "Ord")));
    assertTrue(model.allSigQnames().contains(Qname.nameSpaceQname("open$3", "Ord")));
    assertTrue(model.allSigQnames().contains(Qname.nameSpaceQname("open$4", "Ord")));
    assertFalse(model.allSigQnames().contains(Qname.nameSpaceQname("open$3/ordering", "Ord")));

    assertTrue(
        model.allFieldQnames().contains(Qname.fieldQname("open$3", "Ord", "First")));
    assertTrue(model.allFieldQnames().contains(Qname.fieldQname("open$4", "Ord", "Next")));
  }

  @Test
  public void nestedImportsQualifyAliasesWithTheirParentNamespace() throws Exception {
    AlloyModel model = model("nested-main.als");

    assertTrue(model.allSigQnames().contains(Qname.nameSpaceQname("nested", "A1")));
    assertTrue(model.allSigQnames().contains(Qname.nameSpaceQname("nested/ordering", "Ord")));
    assertTrue(model.allSigQnames().contains(Qname.nameSpaceQname("nested/open$3", "Ord")));
    assertFalse(model.allSigQnames().contains(Qname.nameSpaceQname("open$3/ordering", "Ord")));
  }

  private static AlloyModel model(String filename) throws Exception {
    Path path = Path.of("src/test/resources/evaluation/imports", filename);
    AlloyModel model = new AlloyModel(alloyParse(path.toString()));
    model.resolve();
    return model;
  }
}
