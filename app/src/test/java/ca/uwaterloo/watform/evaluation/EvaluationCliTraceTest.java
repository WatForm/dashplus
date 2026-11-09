package ca.uwaterloo.watform.evaluation;

import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.utils.Reporter;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

class EvaluationCliTraceTest {
  @TempDir Path temp;

  @AfterEach
  void resetReporter() {
    Reporter.INSTANCE.reset();
  }

  @Test
  void rejectsMultipleInstancesBeforeEvaluatingConstraints() throws Exception {
    Path model = temp.resolve("model.als");
    Files.writeString(model, "sig A {}\nfact { no A }");
    String emptyState = state("");
    String populatedState = state("<atom label=\"A$0\"/>");

    Path single = temp.resolve("single.xml");
    Files.writeString(single, "<alloy>" + emptyState + "</alloy>");
    assertEquals(0, evaluate(model, single, temp.resolve("single.jsonl")));
    assertTrue(Files.readString(temp.resolve("single.jsonl")).contains("\"status\":\"true\""));

    // The last state violates the fact. A temporal trace must be rejected instead of
    // silently evaluating that state and reporting FALSE.
    Path trace = temp.resolve("trace.xml");
    Files.writeString(trace, "<alloy>" + emptyState + populatedState + "</alloy>");
    Path telemetry = temp.resolve("trace.jsonl");
    assertEquals(1, evaluate(model, trace, telemetry));
    String records = Files.readString(telemetry);
    assertTrue(records.contains("\"status\":\"unsupported\""));
    assertTrue(records.contains("XML contains 2 instances"));
    assertFalse(records.contains("\"status\":\"false\""));
  }

  private int evaluate(Path model, Path xml, Path telemetry) {
    return new CommandLine(new EvaluationCli())
        .execute(model.toString(), "-evalConstraints", "-xml=" + xml, "--telemetry=" + telemetry);
  }

  private String state(String atoms) {
    return "<instance bitwidth=\"4\" maxseq=\"4\">"
        + "<sig label=\"univ\" ID=\"0\"/>"
        + "<sig label=\"this/A\" ID=\"1\" parentID=\"0\">"
        + atoms
        + "</sig></instance>";
  }
}
