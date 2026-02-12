package com.veeva.testhealth;

import java.util.List;

public class TestHealthSummary {
  private final int totalTests;
  private final int passedTests;
  private final int failedTests;
  private final List<String> failedTestNames;

  public TestHealthSummary(int totalTests, int passedTests, int failedTests, List<String> failedTestNames) {
    this.totalTests = totalTests;
    this.passedTests = passedTests;
    this.failedTests = failedTests;
    this.failedTestNames = List.copyOf(failedTestNames);
  }

  public int getTotalTests() {
    return totalTests;
  }

  public int getPassedTests() {
    return passedTests;
  }

  public int getFailedTests() {
    return failedTests;
  }

  public List<String> getFailedTestNames() {
    return failedTestNames;
  }

  public double getPassPercentage() {
    if (totalTests == 0) {
      return 0.0;
    }
    return (passedTests * 100.0) / totalTests;
  }

  public double getFailPercentage() {
    if (totalTests == 0) {
      return 0.0;
    }
    return (failedTests * 100.0) / totalTests;
  }
}
