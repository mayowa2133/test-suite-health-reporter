package com.veeva.testhealth;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TestHealthAnalyzer {
  public TestHealthSummary summarize(List<TestResult> testResults) {
    int passed = 0;
    int failed = 0;
    Set<String> failedNames = new LinkedHashSet<>();

    for (TestResult result : testResults) {
      TestStatus status = TestStatus.fromRaw(result.getStatus());
      if (status == TestStatus.PASS) {
        passed++;
      } else {
        failed++;
        if (result.getTestName() != null && !result.getTestName().isBlank()) {
          failedNames.add(result.getTestName());
        }
      }
    }

    return new TestHealthSummary(testResults.size(), passed, failed, List.copyOf(failedNames));
  }
}
