package com.veeva.testhealth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class TestHealthAnalyzerTest {
  private final TestHealthAnalyzer analyzer = new TestHealthAnalyzer();

  @Test
  void summarize_handlesMixedStatusesAndDeduplicatesFailedTestNames() {
    List<TestResult> input = List.of(
        testResult("A", "Login", "PASS"),
        testResult("B", "Upload", "FAIL"),
        testResult("C", "Sync", "ERROR"),
        testResult("B", "Upload", "FAIL"));

    TestHealthSummary summary = analyzer.summarize(input);

    assertEquals(4, summary.getTotalTests());
    assertEquals(1, summary.getPassedTests());
    assertEquals(3, summary.getFailedTests());
    assertEquals(25.0, summary.getPassPercentage(), 0.0001);
    assertEquals(75.0, summary.getFailPercentage(), 0.0001);
    assertIterableEquals(List.of("Upload", "Sync"), summary.getFailedTestNames());
  }

  @Test
  void summarize_handlesEmptyResults() {
    TestHealthSummary summary = analyzer.summarize(List.of());

    assertEquals(0, summary.getTotalTests());
    assertEquals(0.0, summary.getPassPercentage(), 0.0001);
    assertEquals(0.0, summary.getFailPercentage(), 0.0001);
    assertIterableEquals(List.of(), summary.getFailedTestNames());
  }

  @Test
  void summarize_throwsForUnsupportedStatus() {
    List<TestResult> input = List.of(testResult("A", "Login", "UNKNOWN"));

    assertThrows(IllegalArgumentException.class, () -> analyzer.summarize(input));
  }

  private TestResult testResult(String id, String name, String status) {
    TestResult result = new TestResult();
    result.setTestId(id);
    result.setTestName(name);
    result.setStatus(status);
    return result;
  }
}
