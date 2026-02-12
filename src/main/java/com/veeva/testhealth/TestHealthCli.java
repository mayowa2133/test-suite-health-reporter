package com.veeva.testhealth;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class TestHealthCli {
  private final TestResultReader reader;
  private final TestHealthAnalyzer analyzer;

  public TestHealthCli() {
    this(new TestResultReader(), new TestHealthAnalyzer());
  }

  public TestHealthCli(TestResultReader reader, TestHealthAnalyzer analyzer) {
    this.reader = reader;
    this.analyzer = analyzer;
  }

  public static void main(String[] args) {
    int exitCode = new TestHealthCli().run(args, System.out, System.err);
    if (exitCode != 0) {
      System.exit(exitCode);
    }
  }

  int run(String[] args, PrintStream out, PrintStream err) {
    if (args.length != 1) {
      err.println("Usage: java -jar <jar-file> <result-file.json>");
      return 1;
    }

    Path resultFile = Path.of(args[0]);
    if (!Files.exists(resultFile) || !Files.isRegularFile(resultFile)) {
      err.println("Error: result file does not exist or is not a regular file: " + resultFile);
      return 2;
    }

    try {
      List<TestResult> results = reader.readResults(resultFile);
      TestHealthSummary summary = analyzer.summarize(results);
      printReport(resultFile, summary, out);
      return 0;
    } catch (Exception ex) {
      err.println("Error: unable to process results file. " + ex.getMessage());
      return 3;
    }
  }

  private void printReport(Path resultFile, TestHealthSummary summary, PrintStream out) {
    out.println("Test Health Report");
    out.println("==================");
    out.println("Input File: " + resultFile);
    out.println("Total Tests Run: " + summary.getTotalTests());
    out.printf(Locale.US, "Pass Percentage: %.2f%%%n", summary.getPassPercentage());
    out.printf(Locale.US, "Fail Percentage: %.2f%%%n", summary.getFailPercentage());
    out.println("Failed Tests:");

    List<String> failedTestNames = summary.getFailedTestNames();
    if (failedTestNames.isEmpty()) {
      out.println("- None");
      return;
    }

    for (String failedTestName : failedTestNames) {
      out.println("- " + failedTestName);
    }
  }
}
