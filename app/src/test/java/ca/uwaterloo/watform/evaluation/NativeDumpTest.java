package ca.uwaterloo.watform.evaluation;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.utils.XmlDumper;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

class NativeDumpTest {
  @TempDir Path temp;

  private Path model(String source) throws Exception {
    Path file = temp.resolve("model.als");
    Files.writeString(file, source);
    return file;
  }

  private int dump(Path model, String... selection) {
    var args = new java.util.ArrayList<String>();
    args.add(model.toString());
    args.add("-dumpInstance");
    args.add("-dumpDir=" + temp.resolve("out"));
    args.addAll(java.util.List.of(selection));
    return new CommandLine(new EvaluationCli()).execute(args.toArray(String[]::new));
  }

  @Test void noCommandsProducesOnlyDefaultEvenWithTrailingComment() throws Exception {
    Path file = model("one sig A {} // trailing comment");
    assertEquals(0, XmlDumper.commandCount(XmlDumper.parseForDump(file)));
    assertEquals(0, dump(file));
    assertTrue(Files.exists(temp.resolve("out/model-instance.xml")));
    assertFalse(Files.exists(temp.resolve("out/model-instance-cmd0.xml")));
    assertNotEquals(0, dump(file, "-cmd=0"));
  }

  @Test void defaultDoesNotInheritUnsatCommandOrItsScope() throws Exception {
    Path file = model("sig A {}\nrun { some A and no A } for 7 but 6 Int");
    assertEquals(0, dump(file, "--default-only"));
    String xml = Files.readString(temp.resolve("out/model-instance.xml"));
    assertTrue(xml.contains("bitwidth=\"4\""));
    assertFalse(Files.exists(temp.resolve("out/model-instance-cmd0.xml")));
    assertEquals(0, dump(file, "-cmd=0"));
    assertFalse(Files.exists(temp.resolve("out/model-instance-cmd0.xml")));
  }

  @Test void defaultRetainsFacts() throws Exception {
    Path file = model("sig A {}\nfact { some A and no A }\nrun {} for 5");
    assertEquals(0, dump(file, "--default-only"));
    assertFalse(Files.exists(temp.resolve("out/model-instance.xml")));
  }

  @Test void indexedCommandsKeepOrderAndCheckSemantics() throws Exception {
    Path file = model("sig A {}\nrun { some A and no A }\nassert absent { no A }\ncheck absent for 2 but 5 Int");
    assertEquals(2, XmlDumper.commandCount(XmlDumper.parseForDump(file)));
    assertEquals(0, dump(file));
    assertTrue(Files.exists(temp.resolve("out/model-instance.xml")));
    assertFalse(Files.exists(temp.resolve("out/model-instance-cmd0.xml")));
    String xml = Files.readString(temp.resolve("out/model-instance-cmd1.xml"));
    assertTrue(xml.contains("bitwidth=\"5\""));
    assertTrue(xml.contains("Check absent"));
    assertNotEquals(0, dump(file, "-cmd=2"));
  }

  @Test void relativeImportsWorkForDefaultAndExplicitCommand() throws Exception {
    Files.writeString(temp.resolve("helper.als"), "module helper\none sig A {}");
    Path file = model("open helper\nrun { some A } for 2");
    assertEquals(0, dump(file));
    assertTrue(Files.exists(temp.resolve("out/model-instance.xml")));
    assertTrue(Files.exists(temp.resolve("out/model-instance-cmd0.xml")));
  }

  @Test void overloadedFieldsDoNotRequireDashplusResolution() throws Exception {
    Path file = model("sig A { keys: set A }\nsig B { keys: set B }\nfact { all a: A | no a.keys }\nrun { some A and some B }");
    assertEquals(0, dump(file));
    assertTrue(Files.exists(temp.resolve("out/model-instance.xml")));
    assertTrue(Files.exists(temp.resolve("out/model-instance-cmd0.xml")));
  }
}
