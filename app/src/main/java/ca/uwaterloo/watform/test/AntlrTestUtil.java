package ca.uwaterloo.watform.test;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyinterface.AlloyInterface;
import ca.uwaterloo.watform.antlr.*;
import ca.uwaterloo.watform.utils.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class AntlrTestUtil {
    private static boolean stopOnFirstFail = true;
    private long timeoutMs = 20 * 1000;
    private static int filenamesToPrint = 20;

    private void printList(String title, List<Path> list) {
        System.out.println(title + " (" + list.size() + "):");
        if (list.isEmpty()) {
            System.out.println("  [none]");
        } else {
            int count = 0;
            for (Path p : list) {
                System.out.println("  " + p.toString());
                count++;
                if (count >= filenamesToPrint) {
                    System.out.println(
                            "  ...and " + (list.size() - filenamesToPrint) + " more" + "...");
                    break;
                }
            }
        }
        System.out.println();
    }

    public void printResults(Map<String, List<Path>> resultsMap) {
        System.out.println("=== Parsing Results ===");
        for (Map.Entry<String, List<Path>> entry : resultsMap.entrySet()) {
            printList(entry.getKey(), entry.getValue());
        }
        System.out.println("=======================");
    }

    private void clearResults() {
        this.alloyResults.get("jarPassedAntlrPassed").clear();
        this.alloyResults.get("jarPassedAntlrFailed").clear();
        this.alloyResults.get("jarFailedAntlrPassed").clear();
        this.alloyResults.get("jarFailedAntlrFailed").clear();
        this.alloyResults.get("timeout").clear();
        this.dashResults.get("dashPassed").clear();
        this.dashResults.get("dashFailed").clear();
        this.dashResults.get("timeout").clear();
    }

    // ====================================================================================
    // For Alloy Tests
    // ====================================================================================
    Map<String, List<Path>> alloyResults =
            new LinkedHashMap<>(
                    Map.of(
                            "jarPassedAntlrPassed", new ArrayList<>(),
                            "jarPassedAntlrFailed", new ArrayList<>(),
                            "jarFailedAntlrPassed", new ArrayList<>(),
                            "jarFailedAntlrFailed", new ArrayList<>(),
                            "timeout", new ArrayList<>()));

    private void tryParseAlloy(CharStream input, Path filePath) {
        boolean jarPassed = AlloyInterface.canParse(input.toString());
        if (!jarPassed) {
            return;
        }
        System.out.println(filePath);

        try {
            AlloyFile af = ParserUtil.parse(filePath);
            String s = af.toString();
            System.out.println(s);
            if (jarPassed) {
                boolean toStringCanPass = AlloyInterface.canParse(s);
                if (toStringCanPass) {
                    alloyResults.get("jarPassedAntlrPassed").add(filePath);
                    System.out.println(
                            "Successfully parsed "
                                    + alloyResults.get("jarPassedAntlrPassed").size()
                                    + " files. ");
                } else {
                    System.out.println("toString did not parse again. ");
                    alloyResults.get("jarPassedAntlrFailed").add(filePath);
                    if (stopOnFirstFail) {
                        throw new ParseCancellationException("toString did not parse again. ");
                    }
                }
            } else {
                alloyResults.get("jarFailedAntlrPassed").add(filePath);
                // if (stopOnFirstFail) {
                // throw new UnexpectedParsePassException();
                // }
            }
        } catch (ParseCancellationException pe) {
            if (jarPassed) {
                alloyResults.get("jarPassedAntlrFailed").add(filePath);
                if (stopOnFirstFail) {
                    throw pe;
                }
            } else {
                try {
                    System.out.println("Both Jar and Antlr failed. ");
                    boolean deleted = Files.deleteIfExists(filePath);
                    if (deleted) {
                        System.out.println("File deleted successfully.");
                    } else {
                        System.out.println("File did not exist.");
                    }
                } catch (IOException e) {
                    System.err.println("Failed to delete the file: " + e.getMessage());
                }
                alloyResults.get("jarFailedAntlrFailed").add(filePath);
            }
        } catch (Exception e) {
            System.err.println(e);
            this.printResults(alloyResults);
            System.exit(1);
        }
    }

    // ====================================================================================
    // For Dash Tests
    // ====================================================================================
    Map<String, List<Path>> dashResults =
            new LinkedHashMap<>(
                    Map.of(
                            "dashPassed", new ArrayList<>(),
                            "dashFailed", new ArrayList<>(),
                            "timeout", new ArrayList<>()));

    private void tryParseDash(CharStream input, Path filePath) {
        System.out.println(filePath);
        try {
            ParserUtil.parse(filePath);
            System.out.println(
                    "Successfully parsed " + dashResults.get("dashPassed").size() + " files. ");
            this.dashResults.get("dashPassed").add(filePath);
        } catch (ParseCancellationException pe) {
            this.dashResults.get("dashFailed").add(filePath);
            if (stopOnFirstFail) {
                throw pe;
            }
        } catch (Exception e) {
            System.err.println(e);
            this.printResults(dashResults);
            System.exit(1);
        }
    }

    // ====================================================================================

    private void tryParseWithTimeout(CharStream input, Path filePath, String extension) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = null;
        if (extension == ".dsh") {
            future =
                    executor.submit(
                            () -> {
                                tryParseDash(input, filePath);
                            });
        } else if (extension == ".als") {
            future =
                    executor.submit(
                            () -> {
                                tryParseAlloy(input, filePath);
                            });
        }

        try {
            future.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (TimeoutException te) {
            System.out.println(
                    "Parsing took longer than " + timeoutMs / 1000 + " seconds, file: " + filePath);
            if (extension == ".dsh") {
                dashResults.get("timeout").add(filePath);
            } else if (extension == ".als") {
                alloyResults.get("timeout").add(filePath);
            }
            future.cancel(true); // interrupt the parsing thread
        } catch (ExecutionException ee) {
            Throwable cause = ee.getCause();
            if (cause instanceof ParseCancellationException) {
                throw (ParseCancellationException) cause;
            } else {
                throw new RuntimeException(cause);
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdownNow();
        }
    }

    public void recurParseDir(Path dir, long timeoutMs, String extension) throws Exception {
        if (extension != ".dsh" && extension != ".als") {
            throw new ImplementationError("File extension must be .dsh or .als");
        }
        this.clearResults();
        this.timeoutMs = timeoutMs;
        List<Path> paths = ParserUtil.recurGetFiles(dir, extension);
        for (Path filePath : paths) {
            try {
                CharStream input = CharStreams.fromPath(filePath);
                this.tryParseWithTimeout(input, filePath, extension);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read file: " + filePath, e);
            }
        }
    }
}
