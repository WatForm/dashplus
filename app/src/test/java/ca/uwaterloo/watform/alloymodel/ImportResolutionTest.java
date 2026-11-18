package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.parser.AlloyParser.alloyParse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.parser.CompUtil;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

public class ImportResolutionTest {
  @Test
  public void repeatedTopLevelImportsUseAlloysGeneratedAliases() throws Exception {
    AlloyModel model = model("repeated-ordering.als");

    assertTrue(model.allSigQnames().contains(Qname.nameSpaceQname("ordering", "Ord")));
    assertTrue(model.allSigQnames().contains(Qname.nameSpaceQname("open$3", "Ord")));
    assertTrue(model.allSigQnames().contains(Qname.nameSpaceQname("open$4", "Ord")));
    assertFalse(model.allSigQnames().contains(Qname.nameSpaceQname("open$3/ordering", "Ord")));

    assertTrue(model.allFieldQnames().contains(Qname.fieldQname("open$3", "Ord", "First")));
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

  @Test
  public void unrelatedImportsAdvanceGeneratedAliases() throws Exception {
    assertOrderingNamesMatchAnalyzer(
        Path.of("src/test/resources/alloymodel/imports/ImportsMixedOrdering.als"),
        Set.of("ordering", "open$4", "open$5"));
  }

  @Test
  public void explicitAliasesAlsoAdvanceGeneratedAliases() throws Exception {
    assertOrderingNamesMatchAnalyzer(
        Path.of("src/test/resources/alloymodel/imports/ImportsInterleavedAliases.als"),
        Set.of("ordering", "named", "open$5", "open$6"));
  }

  @Test
  public void repeatedInstantiationsShareNamesButStillOccupyImportPositions() throws Exception {
    assertOrderingNamesMatchAnalyzer(
        Path.of("src/test/resources/alloymodel/imports/ImportsRepeatedInstantiation.als"),
        Set.of("ordering", "open$6"));
  }

  @Test
  public void transitiveImportsKeepIndependentCountersAndLocalAliases() throws Exception {
    assertOrderingNamesMatchAnalyzer(
        Path.of("src/test/resources/alloymodel/imports/ImportsTransitive.als"),
        Set.of(
            "left/ord",
            "left/ordering",
            "left/open$5",
            "right/ord",
            "right/ordering",
            "right/open$4",
            "ordering",
            "open$5"));
  }

  private static void assertOrderingNamesMatchAnalyzer(Path path, Set<String> namespaces)
      throws Exception {
    AlloyModel model = new AlloyModel(alloyParse(path.toString()));
    model.resolve();
    var analyzer = CompUtil.parseEverything_fromFile(A4Reporter.NOP, null, path.toString());
    Set<String> expectedSigs = new TreeSet<>();
    Set<String> expectedFields = new TreeSet<>();
    for (var sig : analyzer.getAllReachableSigs()) {
      if (!sig.label.endsWith("/Ord")) continue;
      expectedSigs.add(sig.label);
      for (var field : sig.getFields()) {
        expectedFields.add(sig.label + "/" + field.label);
      }
    }
    Set<String> actualSigs = new TreeSet<>();
    Set<String> actualFields = new TreeSet<>();
    Set<String> actualNamespaces = new TreeSet<>();
    for (Qname sig : model.allSigQnames()) {
      if (!sig.name.equals("Ord")) continue;
      actualNamespaces.add(sig.nameSpace);
      actualSigs.add(sig.fullName());
    }
    for (Qname field : model.allFieldQnames()) {
      if (field.sigParent.equals("Ord")) {
        actualFields.add(field.nameSpace + "/Ord/" + field.name);
      }
    }
    assertEquals(namespaces, actualNamespaces);
    assertEquals(expectedSigs, actualSigs, "Signature names must match the bundled Analyzer");
    assertEquals(expectedFields, actualFields, "Field names must match the bundled Analyzer");
  }

  private static AlloyModel model(String filename) throws Exception {
    Path path = Path.of("src/test/resources/alloymodel/imports", filename);
    AlloyModel model = new AlloyModel(alloyParse(path.toString()));
    model.resolve();
    return model;
  }
}
