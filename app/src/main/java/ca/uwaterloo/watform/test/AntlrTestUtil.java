package ca.uwaterloo.watform.test;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyinterface.AlloyInterface;
import ca.uwaterloo.watform.antlr.*;
import ca.uwaterloo.watform.utils.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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

    private List<Path> jarPassedAntlrPassed = new ArrayList<>();
    private List<Path> jarPassedAntlrFailed = new ArrayList<>();
    private List<Path> jarFailedAntlrPassed = new ArrayList<>();
    private List<Path> jarFailedAntlrFailed = new ArrayList<>();
    private List<Path> timeout = new ArrayList<>();

    private void clearAllLists() {
        jarPassedAntlrPassed.clear();
        jarPassedAntlrFailed.clear();
        jarFailedAntlrPassed.clear();
        jarFailedAntlrFailed.clear();
        timeout.clear();
    }

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

    public void printResults() {
        System.out.println("=== Parsing Results ===");
        printList("Jar Passed, ANTLR Passed", jarPassedAntlrPassed);
        printList("Jar Passed, ANTLR Failed", jarPassedAntlrFailed);
        printList("Jar Failed, ANTLR Passed (ignored for now)", jarFailedAntlrPassed);
        printList("Jar Failed, ANTLR Failed (ignored for now)", jarFailedAntlrFailed);
        printList("Timeout Files", timeout);
        System.out.println("=======================");
    }

    private void tryParse(CharStream input, Path filePath) {
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
                    jarPassedAntlrPassed.add(filePath);
                    System.out.println(
                            "Successfully parsed " + jarPassedAntlrPassed.size() + " files. ");
                } else {
                    System.out.println("toString did not parse again. ");
                    jarPassedAntlrFailed.add(filePath);
                    if (stopOnFirstFail) {
                        throw new ParseCancellationException("toString did not parse again. ");
                    }
                }
            } else {
                jarFailedAntlrPassed.add(filePath);
                // if (stopOnFirstFail) {
                // throw new UnexpectedParsePassException();
                // }
            }
        } catch (ParseCancellationException pe) {
            if (jarPassed) {
                jarPassedAntlrFailed.add(filePath);
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
                jarFailedAntlrFailed.add(filePath);
            }
        } catch (Exception e) {
            System.err.println(e);
            this.printResults();
            System.exit(1);
        }
    }

    private void tryParseWithTimeout(CharStream input, Path filePath) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future =
                executor.submit(
                        () -> {
                            tryParse(input, filePath);
                        });

        try {
            future.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (TimeoutException te) {
            System.out.println(
                    "Parsing took longer than " + timeoutMs / 1000 + " seconds, file: " + filePath);
            timeout.add(filePath);
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

    public void recurParseDir(Path dir, long timeoutMs) throws Exception {
        this.clearAllLists();
        this.timeoutMs = timeoutMs;
        try {
            List<Path> paths = ParserUtil.recurGetFiles(dir, ".als");
            for (Path filePath : paths) {
                try {
                    CharStream input = CharStreams.fromPath(filePath);
                    this.tryParseWithTimeout(input, filePath);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read file: " + filePath, e);
                }
            }
        } finally {
            this.printResults();
        }
    }
}
