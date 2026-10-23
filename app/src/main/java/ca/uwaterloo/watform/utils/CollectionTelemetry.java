package ca.uwaterloo.watform.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/** Optional bounded telemetry for archive collection; ordinary CLI behavior is unchanged. */
public final class CollectionTelemetry {
  private static final Gson JSON = new GsonBuilder().serializeNulls().create();
  private static BufferedWriter writer;
  private static String phase = "initialization";
  private static final Map<String, Long> integers = new LinkedHashMap<>();
  private static final Map<String, Map<String, Long>> overflows = new LinkedHashMap<>();

  private CollectionTelemetry() {}

  public static boolean enabled() {
    return writer != null;
  }

  public static void open(Path path) throws IOException {
    phase = "initialization";
    integers.clear();
    overflows.clear();
    writer = Files.newBufferedWriter(path);
    emit("started", Map.of());
  }

  public static void emit(String event, Map<String, ?> fields) {
    if (!enabled()) return;
    Map<String, Object> record = new LinkedHashMap<>(fields);
    record.put("event", event);
    try {
      writer.write(JSON.toJson(record));
      writer.newLine();
      writer.flush();
    } catch (IOException error) {
      throw new UncheckedIOException(error);
    }
  }

  public static void phase(String value) {
    phase = value;
    emit("phase", Map.of("phase", phase));
  }

  public static void integer() {
    if (!enabled()) return;
    long count = integers.merge(phase, 1L, Long::sum);
    if (count == 1) emit("integer", Map.of("phase", phase));
  }

  public static void overflow(String direction, Pos pos) {
    if (!enabled()) return;
    long count =
        overflows
            .computeIfAbsent(phase, unused -> new LinkedHashMap<>())
            .merge(direction, 1L, Long::sum);
    // Only the first occurrence per phase/direction is flushed; totals follow at completion.
    if (count == 1) {
      Map<String, Object> fields = new LinkedHashMap<>();
      fields.put("phase", phase);
      fields.put("direction", direction);
      if (pos != null && pos != Pos.UNKNOWN) {
        fields.put("file", pos.fullFileName);
        fields.put("line", pos.rowStart);
        fields.put("column", pos.colStart);
      }
      emit("overflow", fields);
    }
  }

  public static void failure(String status, String reason) {
    emit("failure", Map.of("status", status, "reason", reason == null ? "" : reason));
  }

  public static String classify(Throwable error) {
    String message = String.valueOf(error.getMessage()).toLowerCase(java.util.Locale.ROOT);
    return error.getClass().getSimpleName().equals("AlloyEvaluatorError")
            || message.contains("not supported")
            || message.contains("does not support")
            || message.contains("not yet support")
            || message.contains("only supports")
        ? "unsupported"
        : "error";
  }

  public static void close() throws IOException {
    if (!enabled()) return;
    try {
      emit("finished", Map.of("integer_counts", integers, "overflow_counts", overflows));
    } finally {
      BufferedWriter closing = writer;
      writer = null;
      closing.close();
    }
  }
}
